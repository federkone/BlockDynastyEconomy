package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.gui.templates.abstractions.ChatColor;

import java.util.List;

public class ViewCommand extends AbstractCommand {
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public ViewCommand(SearchCurrencyUseCase searchCurrencyUseCase) {
        super("view","BlockDynastyEconomy.command.currency", List.of("currency"));
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        Result<Currency> resultCurrency = searchCurrencyUseCase.getCurrency(args[0]);
        if (!resultCurrency.isSuccess()) {
            sender.sendMessage(resultCurrency.getErrorMessage()+ " "+resultCurrency.getErrorCode());
            return false;
        }

        Currency currency = resultCurrency.getValue();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("§a--- Currency Info ---")
                .append("\n")
                     .append("§7ID: §c" + currency.getUuid().toString())
                .append("\n")
                     .append("§7Singular: "+ currency.getSingular() + "§7, Plural: " + currency.getPlural())
                .append("\n")
                     .append("§7Color: " + ChatColor.stringValueOf(currency.getColor()) +currency.getColor())
                .append("\n")
                     .append("§7Symbol: "+ currency.getSymbol())
                .append("\n")
                     .append("§7Start Balance: "  + currency.format(currency.getDefaultBalance()) + "§7.")
                .append("\n")
                     .append("§7Decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"))
                .append("\n")
                     .append("§7Default: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"))
                .append("\n")
                     .append("§7Payable: " + (currency.isTransferable() ? "§aYes" : "§cNo"))
                .append("\n")
                     .append("§7Rate: "+ currency.getExchangeRate());

        sender.sendMessage(stringBuilder.toString());
        return true;
    }
}
