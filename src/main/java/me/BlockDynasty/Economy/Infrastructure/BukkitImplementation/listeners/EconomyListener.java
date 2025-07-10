package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.listeners;


import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import me.BlockDynasty.Economy.domain.services.IAccountService;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EconomyListener implements Listener {
    private final ICurrencyService currencyService;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountsUseCase getAccountsUseCase;
    private final IAccountService accountService;

    public EconomyListener(CreateAccountUseCase createAccountUseCase, GetAccountsUseCase getAccountsUseCase, IAccountService accountService, ICurrencyService currencyService) {
        this.currencyService = currencyService;
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
        this.accountService = accountService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        SchedulerUtils.run(() -> {
            Result<Account> result = this.getAccountsUseCase.getAccount(player.getUniqueId());
            if (!result.isSuccess() && result.getErrorCode() == ErrorCode.ACCOUNT_NOT_FOUND) {
                Result <Void> resultCreation = this.createAccountUseCase.execute(player.getUniqueId(), player.getName());
                if (!resultCreation.isSuccess()){
                    player.kick(Component.text("Error al crear tu cuenta de economía, vuelve a ingresar al server, o contacta a un administrador"));
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

        SchedulerUtils.run(() -> {
            Result<Account> result = this.getAccountsUseCase.getAccount(player.getUniqueId());
            if (!result.isSuccess() && result.getErrorCode() == ErrorCode.ACCOUNT_NOT_FOUND) {
                Result <Void> resultCreation = this.createAccountUseCase.execute(player.getUniqueId(), player.getName());
                if (!resultCreation.isSuccess()){
                    player.kick(Component.text("Error al cargar tu cuenta de economía vuelve a ingresar al server, o contacta a un administrador"));
                }
            }
        });

        SchedulerUtils.runLater(40L, () -> {
            if (currencyService.existsDefaultCurrency() && (player.isOp() || player.hasPermission("gemseconomy.command.currency"))) {
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

