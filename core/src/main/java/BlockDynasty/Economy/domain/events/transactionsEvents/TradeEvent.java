package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Event;

import java.math.BigDecimal;

//accountFrom, accountTo., currencyFrom, currencyTo, amountFrom, amountTo
public class TradeEvent extends Event {
    private final Player fromPlayer;
    private final Player toPlayer;
    private final Currency currencyFrom;
    private final Currency currencyTo;
    private final BigDecimal amountFrom;
    private final BigDecimal amountTo;

    public TradeEvent(Player fromPlayer, Player toPlayer, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }
    public Player getToPlayer() {
        return toPlayer;
    }
    public Currency getCurrencyFrom() {
        return currencyFrom;
    }
    public Currency getCurrencyTo() {
        return currencyTo;
    }
    public BigDecimal getAmountFrom() {
        return amountFrom;
    }
    public BigDecimal getAmountTo() {
        return amountTo;
    }


}
