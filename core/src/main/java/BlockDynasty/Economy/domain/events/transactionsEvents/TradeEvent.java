package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.SerializableEvent;

import java.math.BigDecimal;

//accountFrom, accountTo., currencyFrom, currencyTo, amountFrom, amountTo
public class TradeEvent extends Event implements SerializableEvent {
    private final Player fromPlayer;
    private final Player toPlayer;
    private final Currency currencyFrom;
    private final Currency currencyTo;
    private final BigDecimal amountFrom;
    private final BigDecimal amountTo;

    public TradeEvent(Player fromPlayer, Player toPlayer, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo) {
        this.fromPlayer = new Player(fromPlayer);
        this.toPlayer = new Player(toPlayer);
        this.currencyFrom = new Currency( currencyFrom );
        this.currencyTo = new Currency( currencyTo );
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

    @Override
    public String getEventType() {
        return "TradeEvent";
    }

    public static TradeEvent fromJson(String jsonString) {
        return SerializableEvent.fromJson(jsonString, TradeEvent.class);
    }

    @Override
    public String toString() {
        return "TradeEvent{" +
                "fromPlayer=" + fromPlayer.getNickname() +
                ", toPlayer=" + toPlayer.getNickname() +
                ", currencyFrom=" + currencyFrom.getSingular() +
                ", currencyTo=" + currencyTo.getSingular() +
                ", amountFrom=" + amountFrom.toString() +
                ", amountTo=" + amountTo.toString() +
                '}';
    }
}
