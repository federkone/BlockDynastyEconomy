package BlockDynasty.BukkitImplementation.listeners;

import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.config.file.Message;
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
    protected final SearchAccountUseCase searchAccountUseCase;
    protected final IAccountService accountService;

    public EconomyListenerOnline(CreateAccountUseCase createAccountUseCase, SearchAccountUseCase searchAccountUseCase, IAccountService accountService, ICurrencyService currencyService) {
        this.currencyService = currencyService;
        this.createAccountUseCase = createAccountUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
        this.accountService = accountService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Scheduler.run(ContextualTask.build(() -> loadPlayerAccount(player)));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Scheduler.run(ContextualTask.build(() -> loadPlayerAccount(player)));
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
        accountService.removeAccountOnline(player.getUniqueId());
    }

    private void checkDefaultCurrency(Player player) {
        Scheduler.runLater(40L, ContextualTask.build(() -> {
            if (!currencyService.existsDefaultCurrency() &&
                    (player.isOp() || player.hasPermission("blockdynastyeconomy.command.currency"))) {
                player.sendMessage(Message.getPrefix() + "§cNo has creado una moneda predeterminada. Hazlo con \"§e/currency§c\".");
            }
        }));
    }

    //si se comienza a trabajar en online se van a buscar las cuentas por uuid y se va a preguntar si cambio el nombre para actualizar en sistema.
    protected void loadPlayerAccount(Player player) {
        Result<Account> result = searchAccountUseCase.getAccount(player.getUniqueId());
        if (result.isSuccess()) {
            Result<Void> resultChangeName = accountService.checkNameChange(result.getValue(), player.getName());
            if(!resultChangeName.isSuccess()){
                //player.kick(Component.text("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.")); //paper
                player.kickPlayer("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
                return;
            }
            accountService.addAccountToOnline(result.getValue());
            return;
        }

        Result<Account> creationResult = createAccountUseCase.execute(player.getUniqueId(), player.getName());
        if (!creationResult.isSuccess()) {
            //player.kick(Component.text("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.")); //paper
            player.kickPlayer("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador.");
        }
    }
}


