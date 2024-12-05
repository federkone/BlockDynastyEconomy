package me.BlockDynasty.Economy.aplication.listeners;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EconomyListener implements Listener {

    private final BlockDynastyEconomy plugin ;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountsUseCase getAccountsUseCase;
    private final AccountManager accountManager;

    public EconomyListener(BlockDynastyEconomy plugin, CreateAccountUseCase createAccountUseCase, GetAccountsUseCase getAccountsUseCase, AccountManager accountManager) {
        this.plugin = plugin;
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
        this.accountManager = accountManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runAsync(()->{
            try{
               Account account = getAccountsUseCase.getAccount(player.getUniqueId()); //traer y cargar en cache
                accountManager.addAccountToCache(account); //se agrega a cache por que se conecta
            }catch(AccountNotFoundException e){
                createAccountUseCase.execute(player.getUniqueId(),player.getName());  //sino crear
            }catch (TransactionException e){
                player.kickPlayer("Error al crear tu cuenta de economia, vuelve a ingresar al server, o contacta a un administrador"); //todo: si para el server es critico el plugin de economia, se debe impedir que juegue sin tener una cuenta, esto se podria hacer configurable
            }



           /* acc = getAccountsUseCase.getAccount(player.getUniqueId());
            if(!acc.getNickname().equals(player.getName()))
                acc.setNickname(player.getName());
            UtilServer.consoleLog("Account name changes detected, updating: " + player.getName());
            plugin.getDataStore().saveAccount(acc);  */ //TODO: PENSAR EN ESTA LOGICA, DONDED SE DETECTA UN CAMBIO DE NOMBRE SEGUN EL UUID
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        accountManager.removeAccount(player.getUniqueId());  //se quita cache por que se desconecta
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        accountManager.removeAccount(player.getUniqueId());  //se quita cache por que se kikea
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Caching
        SchedulerUtils.run(() -> {
            getAccountsUseCase.getAccount(player.getUniqueId());
        });

        SchedulerUtils.runLater(40L, () -> {
            if (plugin.getCurrencyManager().getDefaultCurrency() == null && (player.isOp() || player.hasPermission("gemseconomy.command.currency"))) {
                player.sendMessage(F.getPrefix() + "§cYou have not made a currency yet. Please do so by \"§e/currency§c\".");
            }
        });
    }

}

