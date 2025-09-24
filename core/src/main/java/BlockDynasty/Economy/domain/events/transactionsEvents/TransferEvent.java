package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.SerializableEvent;

import java.math.BigDecimal;

public class TransferEvent extends SerializableEvent {
    private final Player fromPlayer;
    private final Player toPlayer;
    private final Currency currency;
    private final BigDecimal amount;

    public TransferEvent(Player fromPlayer, Player toPlayer, Currency currency, BigDecimal amount) {
        this.fromPlayer = new Player(fromPlayer);
        this.toPlayer = new Player(toPlayer);
        this.currency = new Currency( currency );
        this.amount = amount;
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }

    public Player getToPlayer() {
        return toPlayer;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransferEvent{" +
                "player sender=" + fromPlayer.getNickname() +
                ", player receiver=" + toPlayer.getNickname() +
                ", currency=" + currency.getSingular() +
                ", amount=" + amount.toString() +
                '}';
    }

}
