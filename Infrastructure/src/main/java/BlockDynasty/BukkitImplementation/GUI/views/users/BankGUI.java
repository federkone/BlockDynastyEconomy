package BlockDynasty.BukkitImplementation.GUI.views.users;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.BalanceGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.PayGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BankGUI extends AbstractGUI {
    private final GUIService guiService;
    private final MessageService messageService;
    private final GetBalanceUseCase getBalanceUseCase;
    private final Player player;
    private final PayUseCase payUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public BankGUI( Player player, GUIService guiService, PayUseCase payUseCase, SearchCurrencyUseCase searchCurrencyUseCase, GetBalanceUseCase getBalanceUseCase, MessageService messageService) {
        super("Banco", 3);
        this.guiService = guiService;
        this.messageService = messageService;
        this.player = player;
        this.payUseCase = payUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.getBalanceUseCase = getBalanceUseCase;

        setupGUI();
    }

    private void setupGUI() {
        // Balance option
        setItem(11, createItem(Material.GOLD_INGOT, "§6Ver Balance",
                "§7Click para ver tu balance"), unused -> {
            openBalanceGUI();
        });

        setItem(15, createItem(MaterialAdapter.getPlayerHead(), "§aPagar a un Jugador",
                "§7Click para pagar a otro jugador"), unused -> {
            openPayGUI();
        });

        setItem(22, createItem(Material.BARRIER, "§cSalir",
                "§7Click para salir"), unused -> player.closeInventory());
    }

    private void openBalanceGUI() {
        BalanceGUI balanceGUI = new BalanceGUI(guiService, player,getBalanceUseCase,this);
        player.openInventory(balanceGUI.getInventory());
        guiService.registerGUI(player, balanceGUI);
    }

    private void openPayGUI() {
        PayGUI payGUI = new PayGUI( payUseCase, player, guiService, searchCurrencyUseCase,messageService,this);
        player.openInventory(payGUI.getInventory());
        guiService.registerGUI(player, payGUI);
    }
}