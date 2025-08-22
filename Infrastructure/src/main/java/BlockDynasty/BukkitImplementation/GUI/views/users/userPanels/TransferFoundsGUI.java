package BlockDynasty.BukkitImplementation.GUI.views.users.userPanels;

import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TransferFoundsGUI extends PayGUI{
    private final SearchAccountUseCase searchAccountUseCase;
    public TransferFoundsGUI(Player sender, IGUI parent, SearchAccountUseCase searchAccountUseCase) {
        super(sender, parent);
        this.searchAccountUseCase = searchAccountUseCase;
        Result<List<Account>> result = searchAccountUseCase.getOfflineAccounts();
        if(result.isSuccess()) {
            List<BlockDynasty.Economy.domain.entities.account.Player> players = new ArrayList<>(result.getValue().stream()
                    .map(Account::getPlayer)
                    .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                    .toList());

            //test
            /*for (int i=0; i < 45 ; i++) {
               players.add(new Player("empty", "empty"));
            }
            players.add(new Player("empty", "Cristian"));
            players.add(new Player("empty", "Daniel"));
            players.add(new Player("empty", "Alberto"));
            */
            showPlayers(players);
        }else {showPlayers(new ArrayList<>());}
    }

    @Override
    public BlockDynasty.Economy.domain.entities.account.Player findPlayerByName(String playerName) {
        Result<Account> result = searchAccountUseCase.getAccount(playerName);
        if(result.isSuccess()){
            return result.getValue().getPlayer();
        }else {return null;}
    }
}
