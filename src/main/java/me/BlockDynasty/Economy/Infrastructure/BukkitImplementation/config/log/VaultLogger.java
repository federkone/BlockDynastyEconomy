package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.log;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;

public class VaultLogger extends AbstractLogger {

    public VaultLogger(BlockDynastyEconomy plugin) {
        super(plugin);
    }

    @Override
    public void log(String message) {
        //void, not activate for vault
    }

}
