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

package BlockDynasty.Economy.aplication.useCase.offer;

import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.services.IOfferService;

import java.util.List;
import java.util.UUID;

public class SearchOfferUseCase {
    private final IOfferService offerService;

    public SearchOfferUseCase(IOfferService offerService) {
        this.offerService = offerService;
    }

    public List<Offer> getOffersBuyer(UUID playerId) {
        return offerService.getOffersBuyer(playerId);
    }
    public List<Offer> getOffersSeller(UUID playerId) {
        return offerService.getOffersSeller(playerId);
    }

}
