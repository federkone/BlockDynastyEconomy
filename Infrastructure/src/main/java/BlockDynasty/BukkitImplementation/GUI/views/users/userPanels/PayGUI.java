package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;

import BlockDynasty.Economy.domain.entities.account.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

//la ui para pagar, va a mostrar primero: las personas Online indexadas, luego elegir la moneda "monedas disponibles del sistema" indexada, luego abrir AnvilGUI para escribir monto.Fin
public class PayGUI extends AccountsList {
    private final JavaPlugin plugin;
    private final org.bukkit.entity.Player sender;
    private final GUIService guiService;
    private final PayUseCase payUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final MessageService messageService;
    private final AbstractGUI parent;

    public PayGUI(JavaPlugin plugin, PayUseCase payUseCase, org.bukkit.entity.Player sender, GUIService guiService, SearchCurrencyUseCase searchCurrencyUseCase, MessageService messageService, AbstractGUI parent) {
        super("Seleccionar Jugador", 5,guiService,parent);
        this.plugin = plugin;
        this.guiService = guiService;
        this.sender = sender;
        this.parent = parent;
        this.payUseCase = payUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.messageService = messageService;

        List<Player> players = Bukkit.getOnlinePlayers().stream().map(p-> new Player(p.getUniqueId().toString(),p.getName())).toList();
        showPlayersPage(players,sender);
    }

    @Override
    public Player findPlayerByName(String playerName) {
        Player player;
        org.bukkit.entity.Player target = Bukkit.getPlayer(playerName);
        if (target != null) {
            return new Player(target.getUniqueId().toString(), target.getName());
        } else {
            return null;
        }
    }

    @Override
    public void openNextSection(Player target) {
        CurrencyListToPay gui = new CurrencyListToPay(plugin,guiService, sender, target, searchCurrencyUseCase,payUseCase,messageService,this);
        sender.openInventory(gui.getInventory());
        guiService.registerGUI(sender, gui);

    }
}