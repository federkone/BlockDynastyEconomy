/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lib.gui.templates.users.Offers;

import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IItemStack;
import lib.gui.components.IEntityGUI;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.PaginatedPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.util.List;

public class ReceivedOffers extends PaginatedPanel<Offer> {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IEntityGUI sender;
    public ReceivedOffers(SearchOfferUseCase searchOfferUseCase, AcceptOfferUseCase acceptOfferUseCase,
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
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected IItemStack createItemFor(Offer offer) {
        Player vendedor = offer.getVendedor();

        Currency tipoCantidad = offer.getTipoCantidad();
        Currency tipoMonto = offer.getTipoMonto();

        return createItem(
                Materials.PLAYER_HEAD,
                ChatColor.stringValueOf(Colors.GREEN) + vendedor.getNickname(),
                "",
                "Amount: " + ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                "Price: " + ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                "",
                ChatColor.stringValueOf(Colors.GREEN)+"Left Click to Accept",
                ChatColor.stringValueOf(Colors.RED)+"Right Click to Cancel"
            );

    }

    @Override
    protected void functionLeftItemClick(Offer offer) {
        Result<Void> result =acceptOfferUseCase.execute(offer.getComprador().getUuid(),offer.getVendedor().getUuid());
        if (result.isSuccess()) {
            sender.sendMessage("Offer accepted successfully!");
        } else {
            sender.sendMessage("Failed to accept offer: " + result.getErrorMessage());
        }
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected void functionRightItemClick(Offer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            sender.sendMessage("Offer cancelled");
        } else {
            sender.sendMessage("Failed to cancel offer: " + result.getErrorMessage());
        }
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, "Received Offers List",
                        "Click to Refresh"),
                unused -> {
                    refresh();
                });
    }

}