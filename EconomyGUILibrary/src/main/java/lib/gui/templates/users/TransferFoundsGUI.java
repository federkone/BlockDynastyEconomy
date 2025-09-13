package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AccountsList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransferFoundsGUI extends AccountsList {
    private IEntityGUI sender;
    private final SearchAccountUseCase searchAccountUseCase;
    public TransferFoundsGUI(IEntityGUI sender, IGUI parent, SearchAccountUseCase searchAccountUseCase , ITextInput textInput) {
        super("Select Player", 5, sender, parent, textInput);
        this.sender = sender;
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

    @Override
    public void openNextSection(Player target) {
        GUIFactory.currencyListToPayPanel(sender,target,this.getParent()).open();
    }

    @Override
    public void addCustomButtons(){
        super.addCustomButtons(); // Call the parent method to add the default buttons accountList
        setItem(4, createItem(Materials.PAPER, "§aSelect Player to Transfer", "§7Click to select the player you want to transfer","#Ordered by name"), null);
    }
}