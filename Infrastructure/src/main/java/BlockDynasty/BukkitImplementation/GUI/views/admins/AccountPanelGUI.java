package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Player;

import java.util.List;

public class AccountPanelGUI extends AccountsList {
    private final org.bukkit.entity.Player sender;
    private final SearchAccountUseCase searchAccountUseCase;

    public AccountPanelGUI(org.bukkit.entity.Player sender, SearchAccountUseCase searchAccountUseCase, IGUI parent) {
        super("Seleccionar Jugador", 5,parent,sender);
        this.searchAccountUseCase = searchAccountUseCase;
        this.sender = sender;

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
        GUIFactory.editAccountPanel( sender, target, this).open();
    }
}
