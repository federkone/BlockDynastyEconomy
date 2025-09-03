package lib.commands.commands.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class ViewCommand extends AbstractCommand {
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public ViewCommand(SearchCurrencyUseCase searchCurrencyUseCase) {
        super("view","BlockDynastyEconomy.command.currency", List.of("currency"));
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(" usage: /eco currency view <currency>");
            return false;
        }

        Result<Currency> resultCurrency = searchCurrencyUseCase.getCurrency(args[0]);
        if (!resultCurrency.isSuccess()) {
            sender.sendMessage(" Unknown currency.");
                return false;
        }

        Currency currency = resultCurrency.getValue();
        sender.sendMessage("§7ID: §c" + currency.getUuid().toString());
        sender.sendMessage("§7Singular: "+ currency.getSingular() + "§7, Plural: " + currency.getPlural());
        sender.sendMessage("§7Color: " + currency.getColor());
        sender.sendMessage("§7Symbol: "+ currency.getSymbol());
        sender.sendMessage("§7Start Balance: "  + currency.format(currency.getDefaultBalance()) + "§7.");
        sender.sendMessage("§7Decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
        sender.sendMessage("§7Default: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"));
        sender.sendMessage("§7Payable: " + (currency.isTransferable() ? "§aYes" : "§cNo"));
        sender.sendMessage("§7Rate: "+ currency.getExchangeRate());

        return true;
    }
}
