package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.ChatColor;
import lib.gui.templates.abstractions.PaginatedGUI;
import lib.gui.GUIFactory;

import java.util.List;

public class AccountListFromOffers extends PaginatedGUI<Offer> {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IEntityGUI player;

    public AccountListFromOffers(SearchOfferUseCase searchOfferUseCase,
                                 CancelOfferUseCase cancelOfferUseCase,
                                 IEntityGUI player, IGUI parent) {
        super("My Active Offers", 5, player, parent, 21);
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.searchOfferUseCase = searchOfferUseCase;
        this.player = player;

        List<Offer> offers = searchOfferUseCase.getOffersSeller(player.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    public void refresh(){
        //List<Offer> offers = searchOfferUseCase.getOffersBuyer(player.getUniqueId());
        //System.out.println("Offers found for: "+player.getName()+" " + offers.size());
        //showItemsPage(offers);
        GUIFactory.seeMyOffersPanel(player).open();
    }

    @Override
    public void openParent() {
        GUIFactory.bankPanel(player).open();
    }

    @Override
    protected IItemStack createItemFor(Offer offer) {
        Player comprador = offer.getComprador();

        Currency tipoCantidad = offer.getTipoCantidad();
        Currency tipoMonto = offer.getTipoMonto();

        return createItem(
                Materials.PLAYER_HEAD,
                "§a" + comprador.getNickname(),
                "",
                "§fAmount: " + ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                "§fPrice: " + ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                "",
                "§cLeft Click to Cancel"
            );
    }
    @Override
    protected void functionLeftItemClick(Offer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.seeMyOffersPanel(player).open();
            player.sendMessage("§aOffer cancelled");
        } else {
            this.close();
            player.sendMessage("§cFailed to cancel offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "§7Offer List",
                        "Click to refresh"),
                unused -> {
                    refresh();
                });
    }
}