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
        super("Banco", 3,player);
        this.player = player;
        this.searchAccountUseCase = searchAccountUseCase;
        setupGUI();
    }

    private void setupGUI() {
        // Balance option
        setItem(4, createItem(Material.BOOK, "§6See Balance",
                "§7Click to see your balance"), unused -> {
            GUIFactory.balancePanel(player,this).open();
        });

        setItem(15, createItem(MaterialAdapter.getPlayerHead(), "§aPay a Player",
                "§7Click to pay another player"), unused -> {
            GUIFactory.payPanel(player,this).open();
        });

        //transfer option to all account system network
        //exchange option to exchange currencies

        setItem(11, createItem(Material.EMERALD,"Create Offer","Click to create an offer"),
                f -> {
            new OfferGUI(player,this).open();
         });

        setItem(22, createItem(Material.BARRIER, "§cExit",
                "§7Click to exit"), unused -> player.closeInventory());


        boolean isBlocked = searchAccountUseCase.getAccount(player.getUniqueId()).getValue().isBlocked();
        if (isBlocked) {
            setItem(13, createItem(MaterialAdapter.getRedConcrete(), "Account is blocked", "Your account is temporary blocked"), f -> {});
        } else{
            setItem(13, createItem(MaterialAdapter.getLimeConcrete(), "Account is enabled", "Your account is allowed to do transactions"), f -> {});
        }
    }
}