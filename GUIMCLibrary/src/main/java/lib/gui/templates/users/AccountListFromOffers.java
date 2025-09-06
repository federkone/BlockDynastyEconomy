package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.ChatColor;
import lib.gui.templates.abstractions.PaginatedGUI;
import lib.gui.GUIFactory;

import java.util.List;
import java.util.Optional;

public class AccountListFromOffers extends PaginatedGUI<Offer> {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IPlayer player;

    public AccountListFromOffers(SearchOfferUseCase searchOfferUseCase,
                                 CancelOfferUseCase cancelOfferUseCase,
                                 IPlayer player, IGUI parent) {
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
        //showItemsPage(offers);
        GUIFactory.seeMyOffersPanel(player).open();
    }


    @Override
    protected IItemStack createItemFor(Offer offer) {
        Optional<IPlayer> optionalPlayer = platformAdapter.getPlayerOnlineByUUID(offer.getComprador());
        IPlayer comprador = optionalPlayer.orElse(null);

        Currency tipoCantidad = offer.getTipoCantidad();
        Currency tipoMonto = offer.getTipoMonto();

        if (comprador != null) {
            return createItem(
                    Materials.PLAYER_HEAD,
                    "§a" + comprador.getName(),
                    "",
                    "§fAmount: §" + ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                    "§fPrice: §" + ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                    "",
                    "§cLeft Click to Cancel"
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
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.seeOffersPanel(player,getParent()).open();
            player.sendMessage("§aOffer cancelled");
        } else {
            this.close();
            player.sendMessage("§cFailed to cancel offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "§7Offer List",
                        ""),
                null);
    }
}