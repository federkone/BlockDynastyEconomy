package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Event;

import java.math.BigDecimal;

public class PayEvent  extends Event {
    private final Currency currency;
    private final Player payer;
    private final Player received;
    private final BigDecimal amount;

    public PayEvent(Player player, Player received,Currency currency,  BigDecimal amount ) {
            this.currency = new Currency( currency );
            this.payer = new Player(player);
            this.received = new Player(received);
            this.amount = amount;
        }

    public Currency getCurrency() {
        return currency;
    }

    public Player getPayer() {
        return payer;
    }

    public Player getReceived() {
        return received;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "PayEvent{" +
                "currency=" + currency.getSingular() +
                ", payer sender=" + payer.getNickname() +
                ", player receiver=" + received.getNickname() +
                ", amount=" + amount.toString() +
                '}';
    }

}
