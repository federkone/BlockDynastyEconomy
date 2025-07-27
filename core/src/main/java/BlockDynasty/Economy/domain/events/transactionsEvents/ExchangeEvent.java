package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Event;

import java.math.BigDecimal;

public class ExchangeEvent extends Event {
    private final Player player;
    private final Currency fromCurrency;
    private final Currency toCurrency;
    private final BigDecimal amount;
    private final double exchangeRate;
    private final BigDecimal exchangedAmount;

    public ExchangeEvent(Player player, Currency fromCurrency, Currency toCurrency, BigDecimal amount, double exchangeRate, BigDecimal exchangedAmount) {
        this.player = player;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.exchangeRate = exchangeRate;
        this.exchangedAmount = exchangedAmount;
    }

    public Player getPlayer() {
        return player;
    }
    public Currency getFromCurrency() {
        return fromCurrency;
    }
    public Currency getToCurrency() {
        return toCurrency;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public double getExchangeRate() {
        return exchangeRate;
    }
    public BigDecimal getExchangedAmount() {
        return exchangedAmount;
    }
}
