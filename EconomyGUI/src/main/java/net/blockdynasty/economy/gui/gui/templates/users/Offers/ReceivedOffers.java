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

package net.blockdynasty.economy.gui.gui.templates.users.Offers;

import net.blockdynasty.economy.core.aplication.useCase.offer.AcceptOfferUseCase;
import net.blockdynasty.economy.core.aplication.useCase.offer.CancelOfferUseCase;
import net.blockdynasty.economy.core.aplication.useCase.offer.SearchOfferUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.entities.offers.IOffer;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.List;
import java.util.Map;

public class ReceivedOffers extends PaginatedPanel<IOffer> {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IEntityGUI sender;
    public ReceivedOffers(SearchOfferUseCase searchOfferUseCase, AcceptOfferUseCase acceptOfferUseCase,
                          CancelOfferUseCase cancelOfferUseCase,
                          IEntityGUI sender, IGUI parent) {
        super(Message.process("ReceivedOffers.title"), 5, sender, parent, 21);
        this.acceptOfferUseCase = acceptOfferUseCase;
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.searchOfferUseCase = searchOfferUseCase;
        this.sender = sender;

        //can be Player
        List<IOffer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    public void refresh(){
        //List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        //showItemsPage(offers);
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected IItemStack createItemFor(IOffer offer) {
        Player vendedor = offer.getVendedor();

        ICurrency tipoCantidad = offer.getTipoCantidad();
        ICurrency tipoMonto = offer.getTipoMonto();

        return Item.of(
                RecipeItem.builder()
                        .setMaterial(Materials.PLAYER_HEAD)
                        .setName(Message.process(Map.of("playerName", vendedor.getNickname()), "ReceivedOffers.button1.nameItem"))
                        .setLore( Message.processLines(Map.of(
                                "amount", ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                                "price", ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                                "color1", ChatColor.stringValueOf(Colors.GREEN),
                                "color2", ChatColor.stringValueOf(Colors.RED)), "ReceivedOffers.button1.lore"))
                        .build()
            );

    }

    @Override
    protected void functionLeftItemClick(IOffer offer) {
        //can be player
        Result<Void> result =acceptOfferUseCase.execute(offer.getComprador().getUuid(),offer.getVendedor().getUuid());
        if (result.isSuccess()) {
            sender.sendMessage("Offer accepted successfully!");
        } else {
            sender.sendMessage("Failed to accept offer: " + result.getErrorMessage());
        }
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected void functionRightItemClick(IOffer offer) {
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

        setButton(4, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(Message.process("ReceivedOffers.button2.nameItem"))
                        .setLore( Message.processLines("ReceivedOffers.button2.lore"))
                        .build()))
                .setLeftClickAction(unused -> {refresh();})
                .build());
    }

}