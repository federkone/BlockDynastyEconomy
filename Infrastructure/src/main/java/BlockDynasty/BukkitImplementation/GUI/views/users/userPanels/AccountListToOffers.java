package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.components.PaginatedGUI;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountListToOffers extends PaginatedGUI<Offer> {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final org.bukkit.entity.Player sender;

    public AccountListToOffers(SearchOfferUseCase searchOfferUseCase, AcceptOfferUseCase acceptOfferUseCase,
                               CancelOfferUseCase cancelOfferUseCase,
                               org.bukkit.entity.Player sender, IGUI parent) {
        super("Offers", 5, sender, parent, 21);
        this.acceptOfferUseCase = acceptOfferUseCase;
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.sender = sender;

        List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    protected ItemStack createItemFor(Offer offer) {
        Player vendedor = Bukkit.getPlayer(offer.getVendedor());
        if (vendedor == null) {
            return createItem(Material.BARRIER, "§cPlayer not found",
                    List.of("§7This player is not online or does not exist"));
        }
        return MaterialAdapter.createPlayerHead(vendedor.getName(),
                List.of("§7Amount: "+ offer.getTipoCantidad().format(offer.getCantidad()) ,
                        "§7Price: " + offer.getTipoMonto().format(offer.getMonto()),
                        "§aLeft Click to accept",
                        "§cRight Click to Cancel"));
    }

    @Override
    protected void handleLeftItemClick(Offer offer) {
        Result<Void> result =acceptOfferUseCase.execute(offer.getComprador(),offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.seeOffersPanel(sender,getParent()).open();
            sender.sendMessage("§aOffer accepted successfully!");
        } else {
            sender.closeInventory();
            sender.sendMessage("§cFailed to accept offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void handleRightItemClick(Offer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.seeOffersPanel(sender,getParent()).open();
            sender.sendMessage("§aOffer cancelled");
        } else {
            sender.closeInventory();
            sender.sendMessage("§cFailed to cancel offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Material.PAPER, "§7Offer List",
                        List.of("§aClick to Refresh")),
                unused -> { GUIFactory.seeOffersPanel(sender,getParent()).open();});
    }

}
