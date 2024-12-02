/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.BlockDynasty.Economy.aplication.vault;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.utils.UtilServer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHandler {

    private GEVaultHook economy = null;
    private BlockDynastyEconomy plugin;
    private CreateAccountUseCase createAccountUseCase;
    private GetAccountsUseCase getAccountsUseCase;
    private GetCurrencyUseCase getCurrencyUseCase;
    private DepositUseCase depositUseCase;
    private WithdrawUseCase withdrawUseCase;

    public VaultHandler(BlockDynastyEconomy plugin, CreateAccountUseCase createAccountUseCase, GetAccountsUseCase getAccountsUseCase, GetCurrencyUseCase getCurrencyUseCase, DepositUseCase depositUseCase, WithdrawUseCase withdrawUseCase) {
        this.plugin = plugin;
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
    }

    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new GEVaultHook(plugin, createAccountUseCase, getAccountsUseCase, getCurrencyUseCase, depositUseCase, withdrawUseCase);
            }

            if(plugin.getCurrencyManager().getDefaultCurrency() == null){
                UtilServer.consoleLog("No Default currency found. Vault linking disabled!");
                return;
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.economy, plugin, ServicePriority.Highest);

            UtilServer.consoleLog("Vault link enabled.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if(this.economy != null){
            sm.unregister(Economy.class, this.economy);
            this.economy = null;
        }
    }

}
