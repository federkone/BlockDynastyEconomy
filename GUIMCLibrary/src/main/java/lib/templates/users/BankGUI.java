package lib.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.GUIFactory;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.components.Materials;
import lib.templates.abstractions.AbstractGUI;


public class BankGUI extends AbstractGUI {
    private final IPlayer player;
    private final SearchAccountUseCase searchAccountUseCase;
    private final ITextInput textInput;

    public BankGUI(IPlayer player , SearchAccountUseCase SearchAccountUseCase, ITextInput textInput) {
        super("Bank ", 4, player);
        this.player = player;
        this.searchAccountUseCase = SearchAccountUseCase;
        this.textInput = textInput;

        setupGUI();
    }

    private void setupGUI() {
        Result<Account> account = searchAccountUseCase.getAccount(player.getUniqueId());
        boolean isBlocked = false;
        if (account.isSuccess()){
            isBlocked = account.getValue().isBlocked();
        }
        setItem(4, isBlocked
                        ? createItem(Materials.EMERALD, "Account is blocked", "Your account is temporary blocked", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op")
                        : createItem(Materials.EMERALD, "Account is enabled", "Your account is allowed to do transactions", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op"),
                null);
        setItem(11,createItem(Materials.DIAMOND, "§6Exchange currencies","§7Click to exchange currencies"),
                unused -> {
                    GUIFactory.exchangePanel(player,this).open();
                });
        setItem(24, createItem(Materials.WRITABLE_BOOK,"Create Offer","Click to create an offer for trade currencies","with other players Online"),
                f -> {
                    new OfferGUI(player,this,textInput).open();
                });
        setItem(22,createItem(Materials.ENDER_CHEST,"My Active Offers","These are the offers you have created and are currently active."),
                f -> {
                    GUIFactory.seeMyOffersPanel(player,this).open();
                });;
        setItem(13, createItem(Materials.BOOK, "§6See Balance", "§7Click to see your balance"),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });
        setItem(20,createItem(Materials.CHEST,"Received Offers","Here you can view all the offers sent to you by other players."),
                f -> {
                    GUIFactory.seeOffersPanel(player,this).open();
                });
        setItem(15, createItem(Materials.PLAYER_HEAD, "§aPay a Player Online", "§7Click to pay another player Online in this server"),
                unused -> {
                    GUIFactory.payPanel(player,this).open();
                });
        setItem(16,createItem(Materials.PLAYER_HEAD,"§aTransfer a Player from the Network", "§7Click to Transfer founds to another Player from the network"),
                f->{
                    GUIFactory.transferFoundsPanel(player,this).open();
                });
        setItem(31, createItem(Materials.BARRIER, "§cExit", "§7Click to exit"), unused -> this.close());
    }
}