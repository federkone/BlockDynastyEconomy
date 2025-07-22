package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.listeners;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import me.BlockDynasty.Economy.domain.services.IAccountService;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;
//import net.kyori.adventure.text.Component; //paper messages
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EconomyListenerOnline implements Listener {
    protected final ICurrencyService currencyService;
    protected final CreateAccountUseCase createAccountUseCase;
    protected final GetAccountsUseCase getAccountsUseCase;
    protected final IAccountService accountService;

    public EconomyListenerOnline(CreateAccountUseCase createAccountUseCase, GetAccountsUseCase getAccountsUseCase, IAccountService accountService, ICurrencyService currencyService) {
        this.currencyService = currencyService;
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
        this.accountService = accountService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        SchedulerUtils.run(() -> loadPlayerAccount(player));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SchedulerUtils.run(() -> loadPlayerAccount(player));
        checkDefaultCurrency(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removePlayerCache(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removePlayerCache(event.getPlayer());
    }

    private void removePlayerCache(Player player) {
        accountService.removeAccountFromCache(player.getUniqueId());
    }

    private void checkDefaultCurrency(Player player) {
        SchedulerUtils.runLater(40L, () -> {
            if (!currencyService.existsDefaultCurrency() &&
                    (player.isOp() || player.hasPermission("gemseconomy.command.currency"))) {
                player.sendMessage(F.getPrefix() + "§cNo has creado una moneda predeterminada. Hazlo con \"§e/currency§c\".");
            }
        });
    }

    //si se comienza a trabajar en online se van a buscar las cuentas por uuid y se va a preguntar si cambio el nombre para actualizar en sistema.
    protected void loadPlayerAccount(Player player) {
        Result<Account> result = getAccountsUseCase.getAccount(player.getUniqueId());
        if (result.isSuccess()) {
            Result<Void> resultChangeName = getAccountsUseCase.checkNameChange(result.getValue(), player.getName());
            if(!resultChangeName.isSuccess()){
                //player.kick(Component.text("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.")); //paper
                player.kickPlayer("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
                return;
            }
            accountService.addAccountToCache(result.getValue());
            return;
        }

        Result<Account> creationResult = createAccountUseCase.execute(player.getUniqueId(), player.getName());
        if (!creationResult.isSuccess()) {
            //player.kick(Component.text("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.")); //paper
            player.kickPlayer("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
        }
    }
}


