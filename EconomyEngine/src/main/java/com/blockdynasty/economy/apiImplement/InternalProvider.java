package com.blockdynasty.economy.apiImplement;

import com.BlockDynasty.api.DynastyEconomy;

import java.util.UUID;

interface InternalProvider {
    DynastyEconomy getInternal();
    UUID getId();
}
