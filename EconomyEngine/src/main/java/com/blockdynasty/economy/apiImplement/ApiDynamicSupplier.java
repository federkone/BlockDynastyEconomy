package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.log.Log;
import com.BlockDynasty.api.DynastyEconomy;
import com.BlockDynasty.api.entity.Currency;
import services.Console;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

class ApiDynamicSupplier implements Supplier<DynastyEconomy>, InternalProvider {
    private final UUID id;
    private final DynastyEconomy proxy;
    private boolean bypass = true;

    private volatile Log logger;
    private volatile UseCaseFactory useCaseFactory;
    private volatile DynastyEconomy defaultEconomy;

    private final Map<String, DynastyEconomy> specificProviders = new ConcurrentHashMap<>();
    private final Map<Class<?>, DynastyEconomy> callerCache = new ConcurrentHashMap<>();

    private final List<String> ignoredPackages = List.of(
            "BlockDynasty",
            "com.blockdynasty",
            "org.bukkit",
            "net.milkbowl.vault",
            "spigotmc"
    );

    public ApiDynamicSupplier() {
        this.id = UUID.randomUUID();
        this.defaultEconomy = new DynastyEconomyApiNull(id);
        this.proxy = new DynastyEconomyProxy(this);
    }

    public void updateDependencies(UseCaseFactory useCaseFactory, Log log,List<String> pluginsPath,boolean itemEcoEnabled,
                                   boolean consumerUseCurrency, List<Map<String,String>> plugins) {
        this.useCaseFactory = useCaseFactory;
        this.bypass = !itemEcoEnabled && !consumerUseCurrency;
        this.logger = log;
        this.defaultEconomy = new DynastyEconomyApi(useCaseFactory,log, id);
        this.callerCache.clear();
        this.specificProviders.clear();
        if (consumerUseCurrency){
            this.registerSpecialProvider(pluginsPath,plugins);
        }else {
            this.registerSpecialProvider(pluginsPath);
        }
    }

    private void registerSpecialProvider(List<String> pluginsPath){
        pluginsPath.forEach(plugin -> {
            specificProviders.put(plugin, new DynastyEconomyApiHardCash(this.useCaseFactory,logger, id));
        });
    }

    public void registerSpecialProvider(List<String> pluginsPath, List<Map<String,String>> plugins) {
        for (Map<String, String> pluginConfig : plugins) {
            String pluginPackage = pluginConfig.get("plugin");
            String currencyName = pluginConfig.get("currency");

            Currency currency= this.defaultEconomy.getCurrency(currencyName);
            if (currency == null){
                Console.logError("Failed to register specific currency for plugin: " + pluginPackage + " - Currency not found: " + currencyName);
                return;
            }

            if (pluginsPath.contains(pluginPackage)) {
                specificProviders.put(pluginPackage, new DynastyEconomyApiHardCashHardcoded(this.useCaseFactory, logger, id, currencyName));
            } else {
                specificProviders.put(pluginPackage, new DynastyEconomyApiHardCoded(this.useCaseFactory, logger, id, currencyName));
            }
            Console.debug("Registered specific currency for plugin: " + pluginPackage + " with currency: " + currencyName);
        }

        for (String pluginPath : pluginsPath) {
            if (!specificProviders.containsKey(pluginPath)) {
                specificProviders.put(pluginPath, new DynastyEconomyApiHardCash(this.useCaseFactory, logger, id));
            }
            Console.debug("Registered ItemsBasedEconomy for plugin: " + pluginPath);
        }
    }

    public void disable() {
        this.specificProviders.clear();
        this.callerCache.clear();
        this.defaultEconomy = new DynastyEconomyApiNull(id);
    }

    @Override
    public DynastyEconomy get() {
        return this.proxy;
    }

    @Override
    public DynastyEconomy getInternal() {
        return this.resolveProvider();
    }

    public UUID getId() {
        return id;
    }

    private DynastyEconomy resolveProvider() {
        if (this.bypass) return defaultEconomy;
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames
                        .map(StackWalker.StackFrame::getDeclaringClass)
                        .filter(this::isExternalClass)
                        .findFirst()
                        .map(clazz ->
                                callerCache.computeIfAbsent(clazz, c ->{
                                    Console.debug("Resolving economy for caller: " + c.getName());
                                    return findMatchingEconomy(c);
                                }))
                        .orElse(defaultEconomy));
    }

    private boolean isExternalClass(Class<?> clazz) {
        String className = clazz.getName();
        for (String prefix : ignoredPackages) {
            if (className.startsWith(prefix)) {
                return false;
            }
        }
        return true;
    }

    private DynastyEconomy findMatchingEconomy(Class<?> clazz) {
        String className = clazz.getName();
        for (Map.Entry<String, DynastyEconomy> entry : specificProviders.entrySet()) {
            if (className.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return defaultEconomy;
    }
}
