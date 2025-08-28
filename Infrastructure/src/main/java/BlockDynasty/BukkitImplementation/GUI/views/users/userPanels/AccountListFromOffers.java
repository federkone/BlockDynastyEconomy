package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.components.PaginatedGUI;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AccountListFromOffers extends PaginatedGUI<Offer> {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final org.bukkit.entity.Player sender;

    public AccountListFromOffers(SearchOfferUseCase searchOfferUseCase,
                                 CancelOfferUseCase cancelOfferUseCase,
                                 org.bukkit.entity.Player sender, IGUI parent) {
        super("My Active Offers", 5, sender, parent, 21);
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.searchOfferUseCase = searchOfferUseCase;
        this.sender = sender;

        List<Offer> offers = searchOfferUseCase.getOffersSeller(sender.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    public void refresh(){
        List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        showItemsPage(offers);
    }


    @Override
    protected ItemStack createItemFor(Offer offer) {
        Player comprador = Bukkit.getPlayer(offer.getComprador());
        if (comprador == null) {
            return createItem(Material.BARRIER, "§cPlayer not found",
                    List.of("§7This player is not online or does not exist"));
        }
        Currency tipoCantidad = offer.getTipoCantidad();
        Currency tipoMonto = offer.getTipoMonto();
        return MaterialAdapter.createPlayerHead(comprador.getName(),
                List.of("",
                        "§fAmount: "+ ChatColor.valueOf(tipoCantidad.getColor())+tipoCantidad.format(offer.getCantidad()) ,
                        "§fPrice: " + ChatColor.valueOf(tipoMonto.getColor())+tipoMonto.format(offer.getMonto()),
                        "",
                        "§cLeft Click to Cancel"));
    }

    @Override
    protected void handleLeftItemClick(Offer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.seeOffersPanel(sender,getParent()).open();
            sender.sendMessage("§aOffer cancelled");
        } else {
            this.close();
            sender.sendMessage("§cFailed to cancel offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Material.PAPER, "§7Offer List",
                        List.of("")),
                null);
    }
}
