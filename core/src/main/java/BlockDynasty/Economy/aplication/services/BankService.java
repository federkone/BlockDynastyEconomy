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
