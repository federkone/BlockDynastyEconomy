package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts.EditAccountGUI;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Player;

import java.util.List;

public class AccountPanelGUI extends AccountsList {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final SearchAccountUseCase searchAccountUseCase;
    private final GUIService guiService;
    private final org.bukkit.entity.Player sender;
    //private EditAccountUseCase edit...

    public AccountPanelGUI(org.bukkit.entity.Player sender, GUIService guiService, SearchAccountUseCase searchAccountUseCase, DeleteAccountUseCase deleteAccountUseCase) {
        super("Seleccionar Jugador", 5,guiService);
        this.guiService = guiService;
        this.sender = sender;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.searchAccountUseCase = searchAccountUseCase;

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
        EditAccountGUI editAccountGUI = new EditAccountGUI(deleteAccountUseCase,sender,target,guiService,this);//sender tambien
        sender.openInventory(editAccountGUI.getInventory());
        guiService.registerGUI(sender, editAccountGUI);
    }
}
