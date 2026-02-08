package com.blockdynasty.economy.apiImplement;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import BlockDynasty.Economy.domain.services.IAccountService;
import com.BlockDynasty.api.DynastyEconomy;

import java.util.UUID;
import java.util.function.Supplier;

public class ApiDefaultSupplier implements Supplier<DynastyEconomy> {
    private UseCaseFactory useCaseFactory;
    private IAccountService accountService;
    private final UUID id= UUID.randomUUID();

    public ApiDefaultSupplier(UseCaseFactory useCaseFactory, IAccountService accountService) {
        this.useCaseFactory = useCaseFactory;
        this.accountService = accountService;
    }

    @Override
    public DynastyEconomy get() {
        return new DynastyEconomyApi(useCaseFactory,accountService,id);
    }

    public UUID getId() {
        return id;
    }
}
