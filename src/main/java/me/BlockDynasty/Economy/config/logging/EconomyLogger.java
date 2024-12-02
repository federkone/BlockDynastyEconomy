package me.BlockDynasty.Economy.config.logging;

import me.BlockDynasty.Economy.BlockDynastyEconomy;

public class EconomyLogger extends AbstractLogger {

    private final BlockDynastyEconomy plugin;

    public EconomyLogger(BlockDynastyEconomy plugin) {
        super(plugin);
        this.plugin = plugin;
    }


}
