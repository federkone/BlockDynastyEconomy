package lib.commands.commands.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class BalanceTopCommand extends AbstractCommand {
    private final SearchAccountUseCase searchAccountUseCase;

    public BalanceTopCommand(SearchAccountUseCase searchAccountUseCase) {
        super("baltop" ,"BlockDynastyEconomy.command.baltop", List.of("currency"));
        this.searchAccountUseCase = searchAccountUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /baltop <currency> [limit]");
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
        //if (!resultAccounts.isSuccess()){
            //messageService.sendErrorMessage(resultAccounts.getErrorCode(),sender, nameCurrency);

        //}else{
                //sender.sendMessage(messageService.getBalanceTopMessage(resultAccounts.getValue(),nameCurrency,finalLimit));
                //for (Account account : resultAccounts.getValue()) {
                //    sender.sendMessage(account.getNickname() + " " + account.getBalance(nameCurrency).getBalance());
                //}
        //}

        return true;
    }

}
