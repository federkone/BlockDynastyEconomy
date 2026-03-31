package net.blockdynasty.economy.engine.apiImplement;

import net.blockdynasty.economy.api.DynastyEconomy;

import java.util.UUID;

interface InternalProvider {
    DynastyEconomy getInternal();
    UUID getId();
}
