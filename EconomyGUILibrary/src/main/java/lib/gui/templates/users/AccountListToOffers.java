package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.ChatColor;
import lib.gui.templates.abstractions.PaginatedGUI;

import java.util.List;
import java.util.UUID;

public class AccountListToOffers extends PaginatedGUI<Offer> {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IEntityGUI sender;
    public AccountListToOffers(SearchOfferUseCase searchOfferUseCase, AcceptOfferUseCase acceptOfferUseCase,
                               CancelOfferUseCase cancelOfferUseCase,
                               IEntityGUI sender, IGUI parent) {
        super("Received Offers", 5, sender, parent, 21);
        this.acceptOfferUseCase = acceptOfferUseCase;
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.searchOfferUseCase = searchOfferUseCase;
        this.sender = sender;

        List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    public void refresh(){
        //List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        //showItemsPage(offers);
        GUIFactory.seeOffersPanel(sender,getParent()).open();
    }

    @Override
    protected IItemStack createItemFor(Offer offer) {
        Player vendedor = offer.getVendedor();

        Currency tipoCantidad = offer.getTipoCantidad();
        Currency tipoMonto = offer.getTipoMonto();

        return createItem(
                Materials.PLAYER_HEAD,
                "§a" + vendedor.getNickname(),
                "",
                "§fAmount: " + ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                "§fPrice: " + ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                "",
                "§aLeft Click to Accept",
                "§cRight Click to Cancel"
            );

    }

    @Override
    protected void functionLeftItemClick(Offer offer) {
        Result<Void> result =acceptOfferUseCase.execute(offer.getComprador().getUuid(),offer.getVendedor().getUuid());
        if (result.isSuccess()) {
            sender.sendMessage("§aOffer accepted successfully!");
        } else {
            sender.sendMessage("§cFailed to accept offer: " + result.getErrorMessage());
        }
        GUIFactory.seeOffersPanel(sender,getParent()).open();
    }

    @Override
    protected void functionRightItemClick(Offer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            sender.sendMessage("§aOffer cancelled");
        } else {
            sender.sendMessage("§cFailed to cancel offer: " + result.getErrorMessage());
        }
        GUIFactory.seeOffersPanel(sender,getParent()).open();
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "§7Received Offers List",
                        "§aClick to Refresh"),
                unused -> {
                    refresh();
                });
    }

}