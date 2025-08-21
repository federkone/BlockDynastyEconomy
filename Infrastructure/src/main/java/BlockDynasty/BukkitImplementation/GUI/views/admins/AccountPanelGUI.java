package BlockDynasty.BukkitImplementation.GUI.views.admins;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AccountsList;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.account.Player;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountPanelGUI extends AccountsList {
    private final org.bukkit.entity.Player sender;
    private final SearchAccountUseCase searchAccountUseCase;

    public AccountPanelGUI(org.bukkit.entity.Player sender, SearchAccountUseCase searchAccountUseCase, IGUI parent) {
        super("Select player", 5,sender,parent);
        this.searchAccountUseCase = searchAccountUseCase;
        this.sender = sender;

        Result<List<Account>> result = searchAccountUseCase.getOfflineAccounts();
        if(result.isSuccess()) {
            List<Player> players = new ArrayList<>(result.getValue().stream().map(Account::getPlayer).toList());

            //test
            //for (int i=0; i < 45 ; i++) {
            //   players.add(new Player("empty", "empty"));
            //}

            showPlayers(players);
        }
    }

    @Override
    protected void addCustomButtons(){
        super.addCustomButtons();
        setItem(4, createItem(Material.PAPER, "§aSelect Account to edit",
                        Arrays.asList("§7Click to select an account, or search by name")),
                unused -> {});
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
