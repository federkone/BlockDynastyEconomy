package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.util.colors.ChatColor;
import lib.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BalanceTopCommand extends AbstractCommand {
    private final SearchAccountUseCase searchAccountUseCase;

    public BalanceTopCommand(SearchAccountUseCase searchAccountUseCase) {
        super("baltop" ,"BlockDynastyEconomy.command.baltop", List.of("currency" ,"limit"));
        this.searchAccountUseCase = searchAccountUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String nameCurrency =  args[0];
        int limit = 10;
        if (args.length>1){
            try {
                limit = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("El segundo argumento debe ser un numero");
                return false;
            }
        }
        int finalLimit = limit;

        Result<List<Account>> resultAccounts = searchAccountUseCase.getTopAccounts(nameCurrency, finalLimit,0);
        if (!resultAccounts.isSuccess()){
            sender.sendMessage(resultAccounts.getErrorMessage());

        }else{
            List<Account> accounts = resultAccounts.getValue();

            StringBuilder aux = new StringBuilder();
            aux.append("Top ").append(finalLimit).append(" ").append(nameCurrency).append(" : ").append("\n");
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                Money money = account.getMoney(nameCurrency);
                Currency currency = money.getCurrency();
                BigDecimal balanceValue = money.getAmount();
                String message = MessageService.getMessage("balance_top.balance",
                        Map.of(
                                "number", String.valueOf(i+1),
                                "currencycolor", ChatColor.stringValueOf(currency.getColor()),
                                "player",account.getNickname(),
                                "balance",currency.format(balanceValue))
                        );
                aux.append(message).append("\n");
            }
            sender.sendMessage(aux.toString());
        }

        return true;
    }

}
