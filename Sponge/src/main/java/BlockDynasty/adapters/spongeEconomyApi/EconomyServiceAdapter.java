package BlockDynasty.adapters.spongeEconomyApi;

import api.EconomyResponse;
import api.IApi;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class EconomyServiceAdapter implements MultiCurrencyService {
    private IApi api;

    public EconomyServiceAdapter(IApi api) {
        this.api = api;
    }

    @Override
    public Currency defaultCurrency() {
        return new CurrencyAdapter(api.getDefaultCurrency());
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return api.existAccount(uuid);
    }

    @Override
    public boolean hasAccount(String identifier) {
        return api.existAccount(identifier);
    }

    @Override
    public Optional<UniqueAccount> findOrCreateAccount(UUID uuid) {
        BlockDynasty.Economy.domain.entities.account.Account response = api.getAccount(uuid);
        if (response != null) {
            return Optional.of(new UniqueAccountAdapter(response, api));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findOrCreateAccount(String identifier) {
        BlockDynasty.Economy.domain.entities.account.Account response = api.getAccount(identifier);
        if (response != null) {
            return Optional.of(new UniqueAccountAdapter(response, api));
        }
        return Optional.empty();
    }

    @Override
    public Stream<UniqueAccount> streamUniqueAccounts() {
        return Stream.empty();
    }

    @Override
    public Collection<UniqueAccount> uniqueAccounts() {
        return List.of();
    }

    @Override
    public Stream<VirtualAccount> streamVirtualAccounts() {
        return Stream.empty();
    }

    @Override
    public Collection<VirtualAccount> virtualAccounts() {
        return List.of();
    }

    @Override
    public AccountDeletionResultType deleteAccount(UUID uuid) {
        return null;
    }

    @Override
    public AccountDeletionResultType deleteAccount(String identifier) {
        return null;
    }

    @Override
    public List<Currency> getCurrencies() {
        return List.of();
    }

    @Override
    public Optional<Currency> getCurrency(String currencyName) {
        BlockDynasty.Economy.domain.entities.currency.Currency response = api.getCurrency(currencyName);
        if (response != null) {
            return Optional.of(new CurrencyAdapter(response));
        }
        return Optional.empty();
    }
}
