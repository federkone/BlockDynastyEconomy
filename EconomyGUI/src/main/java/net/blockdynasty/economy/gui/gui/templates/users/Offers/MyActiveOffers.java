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

import net.blockdynasty.economy.core.aplication.useCase.offer.CancelOfferUseCase;
import net.blockdynasty.economy.core.aplication.useCase.offer.SearchOfferUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.entities.offers.IOffer;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;
import net.blockdynasty.economy.libs.services.messages.Message;

import java.util.List;
import java.util.Map;

public class MyActiveOffers extends PaginatedPanel<IOffer> {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IEntityGUI player;

    public MyActiveOffers(SearchOfferUseCase searchOfferUseCase,
                          CancelOfferUseCase cancelOfferUseCase,
                          IEntityGUI player, IGUI parent) {
        super("My Active Offers", 5, player, parent, 21);
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.searchOfferUseCase = searchOfferUseCase;
        this.player = player;

        //can be Player
        List<IOffer> offers = searchOfferUseCase.getOffersSeller(player.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    public void refresh(){
        GUIFactory.myActiveOffers(player).open();
    }

    @Override
    public void openParent() {
        GUIFactory.bankPanel(player).open();
    }

    @Override
    protected IItemStack createItemFor(IOffer offer) {
        Player comprador = offer.getComprador();

        ICurrency tipoCantidad = offer.getTipoCantidad();
        ICurrency tipoMonto = offer.getTipoMonto();

        return Item.of(
                RecipeItem.builder()
                        .setMaterial(Materials.PLAYER_HEAD)
                        .setName(Message.process(Map.of("playerName", comprador.getNickname()), "MyActiveOffers.button1.nameItem"))
                        .setLore(Message.processLines(Map.of(
                                "amount", ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                                "price", ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                                "color2", ChatColor.stringValueOf(Colors.RED)), "MyActiveOffers.button1.lore"))
                        .build()
            );
    }
    @Override
    protected void functionLeftItemClick(IOffer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.myActiveOffers(player).open();
            player.sendMessage("Offer cancelled");
        } else {
            this.close();
            player.sendMessage("Failed to cancel offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void addCustomButtons() {
        setButton(4, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(Message.process("MyActiveOffers.button2.nameItem"))
                        .setLore(Message.processLines("MyActiveOffers.button2.lore"))
                        .build()))
                .setLeftClickAction(unused -> {refresh();})
                .build());
    }
}