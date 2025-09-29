package BlockDynasty.adapters.spongeEconomyApi;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.util.List;
import java.util.Optional;

public interface MultiCurrencyService extends EconomyService {
    List<Currency> getCurrencies();
    Optional<Currency> getCurrency(String currencyName);
}
