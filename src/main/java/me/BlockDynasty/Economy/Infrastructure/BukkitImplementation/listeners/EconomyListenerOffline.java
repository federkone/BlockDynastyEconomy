package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.listeners;

import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.services.IAccountService;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class EconomyListenerOffline extends  EconomyListenerOnline {

    public EconomyListenerOffline(CreateAccountUseCase createAccountUseCase, GetAccountsUseCase getAccountsUseCase, IAccountService accountService, ICurrencyService currencyService) {
        super(createAccountUseCase, getAccountsUseCase, accountService, currencyService);
    }

    //si se comienza a trabajar en offline se van a buscar las cuentas por nombre y se va a preguntar si cambio la uuid para actualizar en sistema.
    @Override
    protected void loadPlayerAccount(Player player){
        Result<Account> result = getAccountsUseCase.getAccount(player.getName());
        if (result.isSuccess()) {
            Result<Void> resultChangeUuid = getAccountsUseCase.checkUuidChange(result.getValue(), player.getUniqueId());
            if(!resultChangeUuid.isSuccess()){
                player.kick(Component.text("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador."));
                return;
            }
            accountService.addAccountToCache(result.getValue());
            return;
        }

        Result<Account> creationResult = createAccountUseCase.executeOffline(player.getUniqueId(), player.getName());
        if (!creationResult.isSuccess()) {
            player.kick(Component.text("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador."));
        }
    }
}
