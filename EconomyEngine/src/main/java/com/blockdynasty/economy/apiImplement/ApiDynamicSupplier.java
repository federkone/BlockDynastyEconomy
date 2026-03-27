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

//si esta habilitado itemsbasedEconomy a lo mejor deberia registrar este
public class ApiDynamicSupplier implements Supplier<DynastyEconomy>, InternalProvider {
    private final UUID id;
    private final DynastyEconomy proxy;

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

    public void updateDependencies(UseCaseFactory useCaseFactory, Log log) {
        this.useCaseFactory = useCaseFactory;
        this.logger = log;
        this.defaultEconomy = new DynastyEconomyApi(useCaseFactory,log, id);
        this.callerCache.clear();
        this.specificProviders.clear();
        this.registerSpecialProvider();
    }

    public void registerSpecialProvider() {
        //specificProviders.put("com.gamingmesh.jobs", new DynastyEconomyApiHardCash(this.useCaseFactory, id));
        specificProviders.put("me.angeschossen.lands", new DynastyEconomyApiHardCash(this.useCaseFactory,logger, id));
        //ahora puedo extender la posibilidad de agregar mas proveedores especificos para plugins conocidos
        //objetivo: que consuman la posibilidad de indicar que plugin usa items o no.
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
