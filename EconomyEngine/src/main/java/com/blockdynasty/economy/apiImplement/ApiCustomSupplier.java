package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.log.Log;
import com.BlockDynasty.api.DynastyEconomy;

import java.util.UUID;
import java.util.function.Supplier;

public class ApiCustomSupplier implements Supplier<DynastyEconomy> {
    private final UUID id= UUID.randomUUID();
    private UseCaseFactory useCaseFactory;
    private IAccountService accountService;
    private Log logger;

    public ApiCustomSupplier(UseCaseFactory useCaseFactory, IAccountService accountService, Log logger) {
        this.useCaseFactory = useCaseFactory;
        this.accountService = accountService;
        this.logger = logger;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public DynastyEconomy get() {
        return new DynastyEconomyApi(useCaseFactory,accountService,logger,id);
    }
}
