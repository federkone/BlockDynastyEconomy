package me.BlockDynasty.Economy.config.logging;

import me.BlockDynasty.Economy.BlockDynastyEconomy;

public class VaultLogger extends AbstractLogger {

    public VaultLogger(BlockDynastyEconomy plugin) {
        super(plugin);
    }

    @Override
    public void log(String message) {
        //void, not activate for vault
    }

}
