package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.components.PaginatedGUI;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AccountListToOffers extends PaginatedGUI<Offer> {
    private final AcceptOfferUseCase acceptOfferUseCase;

    public AccountListToOffers(SearchOfferUseCase searchOfferUseCase, AcceptOfferUseCase acceptOfferUseCase,
                               org.bukkit.entity.Player sender, IGUI parent) {
        super("players", 5, sender, parent, 21);
        this.acceptOfferUseCase = acceptOfferUseCase;

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
                        "§7Click to accept this offer"));
    }

    @Override
    protected void handleItemClick(Offer offer) {
        acceptOfferUseCase.execute(offer.getComprador(),offer.getVendedor());
    }

}
