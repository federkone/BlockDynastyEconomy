package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.log.Log;
import com.BlockDynasty.api.DynastyEconomy;
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

    public void updateDependencies(UseCaseFactory useCaseFactory, Log log,List<String> pluginsPath,boolean itemEcoEnabled) {
        this.useCaseFactory = useCaseFactory;
        this.bypass = !itemEcoEnabled;
        this.logger = log;
        this.defaultEconomy = new DynastyEconomyApi(useCaseFactory,log, id);
        this.callerCache.clear();
        this.specificProviders.clear();
        this.registerSpecialProvider(pluginsPath);
    }

    public void registerSpecialProvider(List<String> pluginsPath) {
        pluginsPath.forEach(plugin -> {
            specificProviders.put(plugin, new DynastyEconomyApiHardCash(this.useCaseFactory,logger, id));
        });
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
