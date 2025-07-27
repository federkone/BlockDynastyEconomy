package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Event;

import java.math.BigDecimal;

public class TransferEvent extends Event {
    private final Player fromPlayer;
    private final Player toPlayer;
    private final Currency currency;
    private final BigDecimal amount;

    public TransferEvent(Player fromPlayer, Player toPlayer, Currency currency, BigDecimal amount) {
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.currency = currency;
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
}
