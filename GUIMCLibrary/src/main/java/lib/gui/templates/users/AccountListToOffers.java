package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.ChatColor;
import lib.gui.templates.abstractions.PaginatedGUI;

import java.util.List;
import java.util.Optional;

public class AccountListToOffers extends PaginatedGUI<Offer> {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IPlayer sender;
    public AccountListToOffers(SearchOfferUseCase searchOfferUseCase, AcceptOfferUseCase acceptOfferUseCase,
                               CancelOfferUseCase cancelOfferUseCase,
                               IPlayer sender, IGUI parent) {
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
        List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    protected IItemStack createItemFor(Offer offer) {
        Optional<IPlayer> optionalPlayer = platformAdapter.getPlayerOnlineByUUID(offer.getVendedor());
        IPlayer vendedor = optionalPlayer.orElse(null);

        Currency tipoCantidad = offer.getTipoCantidad();
        Currency tipoMonto = offer.getTipoMonto();

        if (vendedor != null) {
            return createItem(
                    Materials.PLAYER_HEAD,
                    "§a" + vendedor.getName(),
                    "",
                    "§fAmount: §" + ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                    "§fPrice: §" + ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                    "",
                    "§aLeft Click to Accept",
                    "§cRight Click to Cancel"
            );
        } else {
            return createItem(
                    Materials.BARRIER,
                    "§cPlayer not found",
                    "§7This player is not online or does not exist"
            );
        }
    }

    @Override
    protected void functionLeftItemClick(Offer offer) {
        Result<Void> result =acceptOfferUseCase.execute(offer.getComprador(),offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.seeOffersPanel(sender,getParent()).open();
            sender.sendMessage("§aOffer accepted successfully!");
        } else {
            this.close();
            sender.sendMessage("§cFailed to accept offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void functionRightItemClick(Offer offer) {
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
        setItem(4, createItem(Materials.PAPER, "§7Received Offers List",
                        "§aClick to Refresh"),
                unused -> {
                    refresh();
                });
    }

}