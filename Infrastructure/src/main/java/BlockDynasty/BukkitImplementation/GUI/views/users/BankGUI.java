package BlockDynasty.BukkitImplementation.GUI.views.users;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.OfferGUI;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BankGUI extends AbstractGUI {
    private final Player player;
    private final SearchAccountUseCase searchAccountUseCase;

    public BankGUI(Player player, SearchAccountUseCase searchAccountUseCase) {
        super("Bank "+ "("+player.getName()+")", 4,player);
        this.player = player;
        this.searchAccountUseCase = searchAccountUseCase;
        setupGUI();
    }

    private void setupGUI() {
        boolean isBlocked = searchAccountUseCase.getAccount(player.getUniqueId()).getValue().isBlocked();
        setItem(4, isBlocked
                        ? createItem(MaterialAdapter.getRedConcrete(), "Account is blocked", "Your account is temporary blocked", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op")
                        : createItem(MaterialAdapter.getLimeConcrete(), "Account is enabled", "Your account is allowed to do transactions", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op"),
                null);
        setItem(11,createItem(Material.DIAMOND, "§6Exchange currencies","§7Click to exchange currencies"),
                unused -> {
                    GUIFactory.exchangePanel(player, this).open();
                });
        setItem(24, createItem(MaterialAdapter.getWritableBook(),"Create Offer","Click to create an offer for trade currencies","with other players Online"),
                f -> {
                    new OfferGUI(player,this).open();
                });
        setItem(22,createItem(Material.ENDER_CHEST,"My Active Offers","These are the offers you have created and are currently active."),
                f -> {
                    GUIFactory.seeMyOffersPanel(player,this).open();
                });;
        setItem(13, createItem(Material.BOOK, "§6See Balance", "§7Click to see your balance"),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });
        setItem(20,createItem(Material.CHEST,"Received Offers","Here you can view all the offers sent to you by other players."),
                f -> {
                    GUIFactory.seeOffersPanel(player,this).open();
                });
        setItem(15, createItem(MaterialAdapter.getPlayerHead(), "§aPay a Player Online", "§7Click to pay another player Online in this server"),
                unused -> {
                    GUIFactory.payPanel(player,this).open();
                });
        setItem(16,createItem(MaterialAdapter.getPlayerHead(),"§aTransfer a Player from the Network", "§7Click to Transfer founds to another Player from the network"),
                f->{
                    GUIFactory.transferFoundsPanel(player,this).open();
                });
        setItem(31, createItem(Material.BARRIER, "§cExit", "§7Click to exit"), unused -> this.close());
    }
}