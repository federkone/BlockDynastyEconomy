package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.listeners;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.BlockDynastyEconomy;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.Infrastructure.services.AccountService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import me.BlockDynasty.Economy.domain.services.IAccountService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
//todo:refactorizar, se podria hacer uso solo de los casos de uso, sin los Servicios
public class EconomyListener implements Listener {

    private final BlockDynastyEconomy plugin ;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountsUseCase getAccountsUseCase;
    private final IAccountService accountService;

    public EconomyListener(BlockDynastyEconomy plugin, CreateAccountUseCase createAccountUseCase, GetAccountsUseCase getAccountsUseCase, IAccountService accountService) {
        this.plugin = plugin;
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
        this.accountService = accountService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        SchedulerUtils.run(() -> {
            Result<Account> result = this.getAccountsUseCase.getAccount(player.getUniqueId());
            if (result.isSuccess()) {
                this.accountService.addAccountToCache(result.getValue());
            } else {
                Result <Void> resultCreation = this.createAccountUseCase.execute(player.getUniqueId(), player.getName());
                if (!resultCreation.isSuccess()){
                        player.kickPlayer("Error al crear tu cuenta de economia, vuelve a ingresar al server, o contacta a un administrador");
                }
            }
        });
           /* acc = getAccountsUseCase.getAccount(player.getUniqueId());
            if(!acc.getNickname().equals(player.getName()))
                acc.setNickname(player.getName());
            UtilServer.consoleLog("Account name changes detected, updating: " + player.getName());
            plugin.getDataStore().saveAccount(acc);  */ //TODO: PENSAR EN ESTA LOGICA, DONDED SE DETECTA UN CAMBIO DE NOMBRE SEGUN EL UUID
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Caching
        SchedulerUtils.run(() -> {
            Result<Account> resultAccount = this.getAccountsUseCase.getAccount(player.getUniqueId());
            if (!resultAccount.isSuccess()) {
                player.kickPlayer("Error al cargar tu cuenta de economia vuelve a ingresar al server, o contacta a un administrador");
            }else { this.accountService.addAccountToCache(resultAccount.getValue());}
        });

        SchedulerUtils.runLater(40L, () -> {
            if (plugin.getCurrencyManager().getDefaultCurrency() == null && (player.isOp() || player.hasPermission("gemseconomy.command.currency"))) {
                player.sendMessage(F.getPrefix() + "§cYou have not made a default currency yet. Please do so by \"§e/currency§c\".");
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.accountService.removeAccountFromCache(player.getUniqueId());  //se quita cache por que se desconecta
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        this.accountService.removeAccountFromCache(player.getUniqueId());  //se quita cache por que se kikea
    }

}

