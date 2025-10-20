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

package platform.listeners;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.events.offersEvents.OfferAccepted;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCanceled;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCreated;
import BlockDynasty.Economy.domain.events.offersEvents.OfferExpired;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;
import lib.scheduler.ContextualTask;
import lib.util.colors.ChatColor;
import services.Message;

import java.math.BigDecimal;
import java.util.Map;

public class EventListener {

    public static void register(EventManager eventManager, PlatformAdapter platformAdapter) {

        eventManager.subscribe(PayEvent.class, event -> {
            IEntityCommands player = platformAdapter.getPlayer(event.getPayer().getNickname());
            IEntityCommands target = platformAdapter.getPlayer(event.getReceived().getNickname());

            Currency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String colorCode = ChatColor.stringValueOf(currency.getColor());
            String receiverName = event.getReceived().getNickname();
            String senderName = event.getPayer().getNickname();

            if (player != null){
                player.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(colorCode)+format, "playerName",receiverName),"pay1"));
                player.playNotificationSound();
            }
            if (target != null){
                target.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(colorCode)+format,"playerName",senderName),"pay2"));
                target.playNotificationSound();
            }
        });

        eventManager.subscribe(TransferEvent.class, event -> {
            IEntityCommands player = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            IEntityCommands target = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            Currency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String colorCode = ChatColor.stringValueOf(currency.getColor());
            String receiverName = event.getToPlayer().getNickname();
            String senderName = event.getFromPlayer().getNickname();


            if (player != null) {
                player.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(colorCode)+format, "playerName",receiverName),"transfer1"));
                player.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(player.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , player));
            }

            if (target != null) {
                target.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(colorCode)+format, "playerName",senderName),"transfer2"));
                target.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(target.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , target));
            }
        });

        eventManager.subscribe(ExchangeEvent.class, event -> {
            IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());

            Currency fromCurrency = event.getFromCurrency();
            String fromFormat = fromCurrency.format(event.getAmount());
            String fromColorCode = ChatColor.stringValueOf(fromCurrency.getColor());

            Currency toCurrency = event.getToCurrency();
            String toFormat = toCurrency.format(event.getExchangedAmount());
            String toColorCode = ChatColor.stringValueOf(toCurrency.getColor());


            if (player != null) {
                player.sendMessage(Message.process(Map.of("fromCurrency",ChatColor.stringValueOf(fromColorCode)+fromFormat,"toCurrency",ChatColor.stringValueOf(toColorCode)+toFormat),"exchange"));
                player.playNotificationSound();
            }
        });

        eventManager.subscribe(TradeEvent.class, event -> {
            IEntityCommands sender = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            IEntityCommands receiver = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            Currency fromCurrency = event.getCurrencyFrom();
            String fromFormat = fromCurrency.format(event.getAmountFrom());
            String fromColorCode = ChatColor.stringValueOf(fromCurrency.getColor());

            Currency toCurrency = event.getCurrencyTo();
            String toFormat = toCurrency.format(event.getAmountTo());
            String toColorCode = ChatColor.stringValueOf(toCurrency.getColor());


            if (sender != null ) {
                sender.sendMessage(Message.process(
                        Map.of("fromCurrency",ChatColor.stringValueOf(fromColorCode)+fromFormat,
                                "toCurrency",ChatColor.stringValueOf(toColorCode)+toFormat,
                                "playerName",event.getToPlayer().getNickname()
                        ),"trade1"));
                sender.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));

            }
            if (receiver != null){
                receiver.sendMessage(Message.process(
                        Map.of("fromCurrency",ChatColor.stringValueOf(fromColorCode)+fromFormat,
                                "toCurrency",ChatColor.stringValueOf(toColorCode)+toFormat,
                                "playerName",event.getFromPlayer().getNickname()
                ),"trade2"));
                receiver.playNotificationSound();

                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

        });

        eventManager.subscribe(DepositEvent.class, event -> {
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage(Message.process(Map.of(
                            "currency",ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount())
                            ),"deposit"));
                    player.playNotificationSound();
                }
            }

        });

        eventManager.subscribe(WithdrawEvent.class, event -> {
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage(Message.process(Map.of(
                            "currency",ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount())
                    ),"withdraw"));
                    player.playNotificationSound();
                }
            }
        });

        eventManager.subscribe(SetEvent.class, event -> {
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage(Message.process(Map.of(
                            "currency",ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount())
                    ),"set"));
                    player.playNotificationSound();
                }
            }
        });

        eventManager.subscribe(OfferCreated.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            Currency currencyOffered = offer.getTipoCantidad();
            BigDecimal amountOffered = offer.getCantidad();
            Currency currencyValue = offer.getTipoMonto();
            BigDecimal amountValue = offer.getMonto();

            String fromFormat = currencyOffered.format(amountOffered);
            String fromColorCode = ChatColor.stringValueOf(currencyOffered.getColor());
            String toFormat = currencyValue.format(amountValue);
            String toColorCode = ChatColor.stringValueOf(currencyValue.getColor());

            if (sender != null) {
                sender.sendMessage(Message.process(
                        Map.of("playerName", offer.getComprador().getNickname(),
                                "fromCurrency",ChatColor.stringValueOf(fromColorCode) + fromFormat,
                                "toCurrency",ChatColor.stringValueOf(toColorCode) + toFormat
                ),"offerCreated1"));
            }
            if (receiver != null){
                receiver.sendMessage(Message.process(
                        Map.of("playerName", offer.getVendedor().getNickname(),
                                "fromCurrency",ChatColor.stringValueOf(fromColorCode)+ fromFormat,
                                "toCurrency",ChatColor.stringValueOf(toColorCode) + toFormat
                        ),"offerCreated2"));
                receiver.playNotificationSound();

                Runnable task1=()->{
                    GUIFactory.getGuiService().refresh(offer.getComprador().getUuid());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task1 , receiver));
            }
        });

        eventManager.subscribe(OfferCanceled.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            if (receiver != null ) {
                receiver.sendMessage(Message.process(Map.of("playerName",offer.getVendedor().getNickname()),"offerCanceled1"));

                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(receiver.getUniqueId());
                };

                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

            if (sender != null){
                sender.sendMessage(Message.process(Map.of("playerName",offer.getComprador().getNickname()),"offerCanceled2"));

                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(sender.getUniqueId());
                };

                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
        });

        eventManager.subscribe(OfferExpired.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            if (receiver != null ) {
                receiver.sendMessage(Message.process(Map.of("playerName", offer.getVendedor().getNickname()),"offerExpired1"));
                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

            if (sender != null){
                sender.sendMessage(Message.process(Map.of("playerName", offer.getComprador().getNickname()),"offerExpired2"));
                Runnable task = () -> {
                    GUIFactory.getGuiService().refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
        });

        eventManager.subscribe(OfferAccepted.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            if (receiver != null ) {
                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

            if (sender != null){
                Runnable task=()->{
                    GUIFactory.getGuiService().refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
        });
    }
}
