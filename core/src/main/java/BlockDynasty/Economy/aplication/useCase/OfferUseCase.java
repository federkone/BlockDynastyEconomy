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

package BlockDynasty.Economy.aplication.useCase;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.TradeCurrenciesUseCase;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.Courier;

public class OfferUseCase {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CreateOfferUseCase createOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;

    public OfferUseCase(IOfferService offerService, Courier courier, EventManager eventManager, SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase, TradeCurrenciesUseCase tradeCurrenciesUseCase) {
        this.createOfferUseCase = new CreateOfferUseCase(offerService, courier,eventManager,searchCurrencyUseCase, searchAccountUseCase);
        this.acceptOfferUseCase = new AcceptOfferUseCase(offerService, courier,eventManager,tradeCurrenciesUseCase);
        this.cancelOfferUseCase = new CancelOfferUseCase(offerService,courier, eventManager);
        this.searchOfferUseCase = new SearchOfferUseCase(offerService);
    }

    public AcceptOfferUseCase getAcceptOfferUseCase() {
        return acceptOfferUseCase;
    }
    public CreateOfferUseCase getCreateOfferUseCase() {
        return createOfferUseCase;
    }
    public CancelOfferUseCase getCancelOfferUseCase() {
        return cancelOfferUseCase;
    }
    public SearchOfferUseCase getSearchOfferUseCase() {
        return searchOfferUseCase;
    }
}
