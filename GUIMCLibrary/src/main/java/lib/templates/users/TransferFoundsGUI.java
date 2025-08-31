package lib.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.IPlayerManager;
import lib.components.ITextInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransferFoundsGUI extends PayGUI{
    private final SearchAccountUseCase searchAccountUseCase;
    public TransferFoundsGUI(IPlayer sender, IGUI parent, SearchAccountUseCase searchAccountUseCase , ITextInput textInput) {
        super(sender, parent , textInput );
        this.searchAccountUseCase = searchAccountUseCase;
        Result<List<Account>> result = searchAccountUseCase.getOfflineAccounts();
        if(result.isSuccess()) {
            List<BlockDynasty.Economy.domain.entities.account.Player> players = result.getValue().stream()
                    .map(Account::getPlayer)
                    .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname())).collect(Collectors.toList());

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