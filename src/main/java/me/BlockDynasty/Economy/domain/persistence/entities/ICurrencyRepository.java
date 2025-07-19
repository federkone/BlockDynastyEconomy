package me.BlockDynasty.Economy.domain.persistence.entities;

import me.BlockDynasty.Economy.domain.entities.currency.Currency;

import java.util.List;

public interface ICurrencyRepository {
    List<Currency> findAll();
    Currency findByName(String name);
    Currency findByUuid(String uid);
    Currency findDefaultCurrency();
    void save(Currency currency);
    void delete(Currency currency);
    void update(Currency currency);
    void create(Currency currency);
}
