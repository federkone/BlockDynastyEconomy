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

package BlockDynasty.Economy.aplication.services;

import BlockDynasty.Economy.domain.entities.bank.Bank;
import BlockDynasty.Economy.domain.services.IBankService;

import java.util.ArrayList;
import java.util.List;

public class BankService implements IBankService {
        private List<Bank> banks;

        public BankService(){
            this.banks = new ArrayList<>();
        }

        public void addBank(Bank bank){
            this.banks.add(bank);
        }

        public List<Bank> getBanks() {
            return banks;
        }

        public boolean bankExists(String name) {
            return banks.stream()
                    .anyMatch(bank -> bank.getName().equalsIgnoreCase(name));
        }

        public Bank getBank(String name) {
            return banks.stream()
                    .filter(bank -> bank.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
        }

    @Override
    public void removeBank(String name) {
        banks.removeIf(bank -> bank.getName().equalsIgnoreCase(name));
    }

    //update balances de cuenta
    @Override
    public void updateBank(Bank bank) {

    }
}
