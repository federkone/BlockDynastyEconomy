package BlockDynasty.BukkitImplementation.GUI.views.users;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.OfferGUI;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BankGUI extends AbstractGUI {
    private final Player player;
    private final SearchAccountUseCase searchAccountUseCase;

    public BankGUI(Player player, SearchAccountUseCase searchAccountUseCase) {
        super("Bank", 3,player);
        this.player = player;
        this.searchAccountUseCase = searchAccountUseCase;
        setupGUI();
    }

    private void setupGUI() {
        // Balance option
        setItem(13, createItem(Material.BOOK, "§6See Balance",
                "§7Click to see your balance"), unused -> {
            GUIFactory.balancePanel(player,this).open();
        });

        setItem(10,createItem(Material.DIAMOND, "§6Exchange currencies","§7Click to exchange currencies"), unused -> {
            GUIFactory.exchangePanel(player, this).open();
        });

        setItem(15, createItem(MaterialAdapter.getPlayerHead(), "§aPay a Player Online",
                "§7Click to pay another player Online in this server"), unused -> {
            GUIFactory.payPanel(player,this).open();
        });

        setItem(16,createItem(MaterialAdapter.getPlayerHead(),"§aTransfer a Player from the Network",
                "§7Click to Transfer founds to another Player from the network"),f->{
            GUIFactory.transferFoundsPanel(player,this).open();
        });

        //transfer option to all account system network
        //exchange option to exchange currencies

        setItem(11, createItem(Material.EMERALD,"Create Offer","Click to create an offer for trade currencies","with other players Online"),
                f -> {
            new OfferGUI(player,this).open();
         });

        setItem(14,createItem(MaterialAdapter.getPlayerHead(),"Pending Offers","Click to see and manage your offers"),
                f -> {
            GUIFactory.seeOffersPanel(player,this).open();
        });

        setItem(22, createItem(Material.BARRIER, "§cExit",
                "§7Click to exit"), unused -> player.closeInventory());


        boolean isBlocked = searchAccountUseCase.getAccount(player.getUniqueId()).getValue().isBlocked();
        if (isBlocked) {
            setItem(4, createItem(MaterialAdapter.getRedConcrete(), "Account is blocked", "Your account is temporary blocked","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op"), f -> {});
        } else{
            setItem(4, createItem(MaterialAdapter.getLimeConcrete(), "Account is enabled", "Your account is allowed to do transactions","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op"), f -> {});
        }
    }
}