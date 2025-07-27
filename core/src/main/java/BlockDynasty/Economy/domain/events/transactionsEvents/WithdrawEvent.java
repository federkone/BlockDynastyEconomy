package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Event;

import java.math.BigDecimal;

public class WithdrawEvent extends Event {
    private final Currency currency;
    private final Player player;
    private final BigDecimal amount;

    public WithdrawEvent(Player player, Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.player = player;
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
}
