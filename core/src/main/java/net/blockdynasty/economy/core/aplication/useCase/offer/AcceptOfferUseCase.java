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

package net.blockdynasty.economy.core.aplication.useCase.offer;

import net.blockdynasty.economy.core.aplication.events.EventManager;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.ITradeUseCase;
import net.blockdynasty.economy.core.domain.entities.offers.IOffer;
import net.blockdynasty.economy.core.domain.events.offersEvents.OfferAccepted;
import net.blockdynasty.economy.core.domain.events.offersEvents.OfferCanceled;
import net.blockdynasty.economy.core.domain.result.ErrorCode;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.core.domain.services.IOfferService;
import net.blockdynasty.economy.core.domain.services.courier.Courier;
import net.blockdynasty.economy.core.domain.services.courier.Message;

import java.util.UUID;

public class AcceptOfferUseCase {
    private final IOfferService offerService;
    private final ITradeUseCase tradeCurrenciesUseCase;
    private final Courier courier;
    private final EventManager eventManager;

    public AcceptOfferUseCase(IOfferService offerService, Courier courier, EventManager eventmanager, ITradeUseCase tradeCurrenciesUseCase) {
        this.offerService = offerService;
        this.tradeCurrenciesUseCase = tradeCurrenciesUseCase;
        this.courier = courier;
        this.eventManager = eventmanager;
    }

    public Result<Void> execute (UUID playerAccept, UUID playerOffer) {
        IOffer offer = offerService.getOffer(playerAccept,playerOffer);
        if(offer == null) {
            return Result.failure("This user has not offered you anything", ErrorCode.OFFER_NOT_FOUND);
        }
        Result<Void> tradeResult = tradeCurrenciesUseCase.execute(offer.getVendedor(), offer.getComprador(),  offer.getTipoCantidad().getSingular(), offer.getTipoMonto().getSingular(),offer.getCantidad(), offer.getMonto());
        if (!tradeResult.isSuccess()) {
            offerService.cancelOffer(playerAccept);
            eventManager.emit(new OfferCanceled(offer));
            courier.sendUpdateMessage(Message.builder()
                    .setType(Message.Type.EVENT)
                    .setData(new OfferCanceled(offer).toJson())
                    .setTarget(playerOffer)
                    .build());
            return Result.failure(tradeResult.getErrorMessage(), tradeResult.getErrorCode());
        }
        offerService.acceptOffer(playerAccept,playerOffer);
        eventManager.emit(new OfferAccepted(offer));
        courier.sendUpdateMessage(Message.builder()
                .setType(Message.Type.EVENT)
                .setData(new OfferAccepted(offer).toJson())
                .setTarget(playerOffer)
                .build());
        return Result.success();
    }
}
