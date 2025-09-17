package BlockDynasty.Economy.domain.events.transactionsEvents;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.events.Event;
import BlockDynasty.Economy.domain.events.SerializableEvent;

import java.math.BigDecimal;

public class SetEvent extends Event implements SerializableEvent {
    private final Context context;
    private final Currency currency;
    private final Player player;
    private final BigDecimal amount;

    public SetEvent(Player player, Currency currency, BigDecimal amount) {
        this.currency = new Currency( currency );
        this.player = new Player(player);
        this.amount = amount;
        this.context = Context.OTHER;
    }
    public SetEvent(Player player, Currency currency, BigDecimal amount,Context context) {
        this.currency = new Currency( currency );
        this.player = new Player(player);
        this.amount = amount;
        this.context = context;
    }

    public Context getContext() {
        return context;
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

    @Override
    public String getEventType() {
        return "SetEvent";
    }

    public static SetEvent fromJson(String jsonString) {
        return SerializableEvent.fromJson(jsonString, SetEvent.class);
    }
}
