package BlockDynasty.adapters.spongeEconomyApi;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;

public class CurrencyAdapter implements Currency {
    private BlockDynasty.Economy.domain.entities.currency.Currency currency;

    public CurrencyAdapter(BlockDynasty.Economy.domain.entities.currency.Currency currency
    ) {
        this.currency = currency;
    }

    @Override
    public Component displayName() {
        return Component.text(currency.getSingular());
    }

    @Override
    public Component pluralDisplayName() {
        return Component.text(currency.getPlural());
    }

    @Override
    public Component symbol() {
        return Component.text(currency.getSymbol());
    }

    @Override
    public Component format(BigDecimal amount) {
        return Component.text(currency.format(amount));
    }

    @Override
    public Component format(BigDecimal amount, int numFractionDigits) {
        return Component.text(currency.format(amount));
    }

    @Override
    public int defaultFractionDigits() {
        return 0;
    }

    @Override
    public boolean isDefault() {
        return currency.isDefaultCurrency();
    }
}
