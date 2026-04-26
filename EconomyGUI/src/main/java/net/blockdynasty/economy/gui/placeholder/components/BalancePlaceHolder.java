package net.blockdynasty.economy.gui.placeholder.components;

import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.util.colors.ChatColor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BalancePlaceHolder {
    private final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    private static final Map<String, Locale> LOCALE_MAP = Map.ofEntries(
            Map.entry("english", Locale.ENGLISH), Map.entry("en", Locale.ENGLISH),
            Map.entry("french", Locale.FRENCH), Map.entry("fr", Locale.FRENCH),
            Map.entry("german", Locale.GERMAN), Map.entry("de", Locale.GERMAN),
            Map.entry("italian", Locale.ITALIAN), Map.entry("it", Locale.ITALIAN),
            Map.entry("japanese", Locale.JAPANESE), Map.entry("ja", Locale.JAPANESE),
            Map.entry("korean", Locale.KOREAN), Map.entry("ko", Locale.KOREAN),
            Map.entry("chinese", Locale.CHINESE), Map.entry("zh", Locale.CHINESE),
            Map.entry("simplified_chinese", Locale.SIMPLIFIED_CHINESE), Map.entry("zh_cn", Locale.SIMPLIFIED_CHINESE),
            Map.entry("traditional_chinese", Locale.TRADITIONAL_CHINESE), Map.entry("zh_tw", Locale.TRADITIONAL_CHINESE),
            Map.entry("france", Locale.FRANCE), Map.entry("germany", Locale.GERMANY),
            Map.entry("italy", Locale.ITALY), Map.entry("japan", Locale.JAPAN),
            Map.entry("korea", Locale.KOREA), Map.entry("uk", Locale.UK),
            Map.entry("canada", Locale.CANADA), Map.entry("canada_french", Locale.CANADA_FRENCH),
            Map.entry("us", Locale.US)
    );

    public BalancePlaceHolder(GetAccountByUUIDUseCase getAccountByUUIDUseCase, SearchCurrencyUseCase searchCurrencyUseCase) {
        this.getAccountByUUIDUseCase = getAccountByUUIDUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    public String handle(UUID playerId, String placeholder) {
        Result<Account> accountResult = getAccountByUUIDUseCase.execute(playerId);
        Result<ICurrency> defaultCurrencyResult = searchCurrencyUseCase.getDefaultCurrency();

        if (!accountResult.isSuccess()) return "Player data not found";
        if (!defaultCurrencyResult.isSuccess()) return "Default currency not found";

        return processInternal(placeholder, accountResult.getValue(), defaultCurrencyResult.getValue());
    }

    private String processInternal(String placeholder, Account account, ICurrency defaultCurrency) {
        if (placeholder.equals("balance_default")) {
            return String.valueOf(Math.round(account.getMoney(defaultCurrency).getAmount().doubleValue()));
        }

        if (placeholder.equals("balance_default_formatted")) {
            return defaultCurrency.format(account.getMoney(defaultCurrency).getAmount());
        }

        String[] parts = placeholder.split("_");
        if (parts.length < 2) return "Invalid placeholder format";

        String currencyName = parts[1];
        Result<ICurrency> result = searchCurrencyUseCase.getCurrency(currencyName);
        if (!result.isSuccess()) return "Currency not found";

        ICurrency currency = result.getValue();
        BigDecimal amount = account.getMoney(currency).getAmount();

        boolean isFormattedPlaceholder = placeholder.startsWith("balance_" + currencyName + "_formatted");
        Locale locale = getLocaleFromPlaceholder(parts);

        return formatBalance(currency, amount, isFormattedPlaceholder, locale != null ? locale : Locale.US, locale != null);
    }

    private Locale getLocaleFromPlaceholder(String[] parts) {
        if (parts.length >= 3) {
            String possibleLocale = parts[parts.length - 1].toLowerCase();
            return LOCALE_MAP.get(possibleLocale);
        }
        return null;
    }

    private String formatBalance(ICurrency currency, BigDecimal amountBD, boolean isFormatted, Locale locale, boolean hasLocale) {
        double amount = amountBD.doubleValue();
        String prefix = isFormatted ? ChatColor.formatColorToPlaceholder(currency.getColor()) : "";

        if (hasLocale) {
            NumberFormat formatter = NumberFormat.getNumberInstance(locale);
            int fractionDigits = currency.isDecimalSupported() ? 2 : 0;
            formatter.setMaximumFractionDigits(fractionDigits);
            formatter.setMinimumFractionDigits(fractionDigits);

            double amountToFormat = currency.isDecimalSupported() ? amount : Math.floor(amount);
            return prefix + formatter.format(amountToFormat);
        }

        if (isFormatted) {
            return prefix + currency.format(amountBD);
        }

        return currency.isDecimalSupported() ? String.valueOf(amount) : String.valueOf((int) Math.floor(amount));
    }
}