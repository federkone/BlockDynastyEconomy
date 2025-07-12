package me.BlockDynasty.Economy.domain.services;

import me.BlockDynasty.Economy.domain.entities.bank.Bank;

import java.util.List;

public interface IBankService {

    void addBank(Bank bank);
    List<Bank> getBanks();
    Bank getBank(String name);
    void removeBank(String name);
    void updateBank(Bank bank);
    boolean bankExists(String name);
}
