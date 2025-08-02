package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts.EditAccountGUI;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Player;

import java.util.List;

public class AccountPanelGUI extends AccountsList {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final SearchAccountUseCase searchAccountUseCase;
    private final TransactionsUseCase transactionsUseCase;
    private final GetBalanceUseCase getBalanceUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final org.bukkit.entity.Player sender;
    //private EditAccountUseCase edit...

    public AccountPanelGUI(org.bukkit.entity.Player sender, GetBalanceUseCase getBalanceUseCase, SearchAccountUseCase searchAccountUseCase, DeleteAccountUseCase deleteAccountUseCase,
                           TransactionsUseCase transactionsUseCase, SearchCurrencyUseCase searchCurrencyUseCase, AbstractGUI parent) {
        super("Seleccionar Jugador", 5,parent);
        this.sender = sender;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
        this.transactionsUseCase = transactionsUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.getBalanceUseCase = getBalanceUseCase;


        Result<List<Account>> result = searchAccountUseCase.getOfflineAccounts();
        if(result.isSuccess()) {
            List<Player> players = result.getValue().stream().map(Account::getPlayer).toList();
            showPlayersPage(players,sender);
        }
    }

    @Override
    public Player findPlayerByName(String playerName) {
        Result<Account> result = searchAccountUseCase.getAccount(playerName);
        if(result.isSuccess()){
            return result.getValue().getPlayer();
        }else {return null;}
    }

    @Override
    public void openNextSection(Player target) {
        EditAccountGUI editAccountGUI = new EditAccountGUI(searchCurrencyUseCase,deleteAccountUseCase,transactionsUseCase,getBalanceUseCase,sender,target,this);
        editAccountGUI.open(sender);
    }
}
