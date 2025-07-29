package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Event;

import java.math.BigDecimal;

public class SetEvent extends Event {
    private final Currency currency;
    private final Player player;
    private final BigDecimal amount;

    public SetEvent(Player player, Currency currency, BigDecimal amount) {
        this.currency = new Currency( currency );
        this.player = new Player(player);
        this.amount = amount;
    }
    public Currency getCurrency() {
        return currency;
    }
    public Player getPlayer() {
        return player;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    @Override
    public String toString() {
        return "SetEvent{" +
                "currency=" + currency.getSingular() +
                ", player=" + player.getNickname() +
                ", amount=" + amount.toString() +
                '}';
    }
}
