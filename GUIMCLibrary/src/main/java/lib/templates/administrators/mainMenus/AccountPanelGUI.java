package lib.templates.administrators.mainMenus;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.GUIFactory;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.components.Materials;
import lib.templates.abstractions.AccountsList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AccountPanelGUI extends AccountsList {
    private final IPlayer sender;
    private final SearchAccountUseCase searchAccountUseCase;

    public AccountPanelGUI(IPlayer sender, SearchAccountUseCase searchAccountUseCase, IGUI parent, ITextInput textInput) {
        super("Select player", 5,sender,parent,textInput);
        this.searchAccountUseCase = searchAccountUseCase;
        this.sender = sender;

        Result<List<Account>> result = searchAccountUseCase.getOfflineAccounts();
        if(result.isSuccess()) {
            List<Player> players = new ArrayList<>(result.getValue().stream()
                    .map(Account::getPlayer)
                    .sorted((a, b) -> a.getNickname().compareToIgnoreCase(b.getNickname()))
                    .collect(Collectors.toList()));

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
    protected void addCustomButtons(){
        super.addCustomButtons();
        setItem(4, createItem(Materials.PAPER, "§aSelect Account to edit",
                        "§7Click to select an account, or search by name","#Ordered by name, CaseSensitive"),
                null);
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
