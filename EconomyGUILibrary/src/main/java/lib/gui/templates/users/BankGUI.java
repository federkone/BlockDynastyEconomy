package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AbstractGUI;


public class BankGUI extends AbstractGUI {
    private final IEntityGUI player;
    private final SearchAccountUseCase searchAccountUseCase;
    private final ITextInput textInput;

    public BankGUI(IEntityGUI player , SearchAccountUseCase SearchAccountUseCase, ITextInput textInput) {
        super("Bank "+"["+player.getName()+"]", 4, player);
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
                        ? createItem(Materials.RED_CONCRETE, "§cAccount is blocked", "Your account is temporary blocked", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op")
                        : createItem(Materials.LIME_CONCRETE, "§aAccount is enabled", "Your account is allowed to do transactions", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op"),
                null);
        setItem(11,createItem(Materials.DIAMOND, "§6Exchange currencies","§7Click to exchange currencies"),
                unused -> {
                    GUIFactory.exchangePanel(player,this).open();
                });
        setItem(24, createItem(Materials.WRITABLE_BOOK,"§6Create Offer","§7Click to create an offer for trade currencies","§7with other players Online"),
                f -> {
                    new OfferGUI(player,this,textInput).open();
                });
        setItem(22,createItem(Materials.ENDER_CHEST,"§6My Active Offers","§7These are the offers you have created","§7and are currently active."),
                f -> {
                    GUIFactory.seeMyOffersPanel(player,this).open();
                });;
        setItem(13, createItem(Materials.BOOK, "§6See Balance", "§7Click to see your balance"),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });
        setItem(20,createItem(Materials.CHEST,"§6Received Offers","§7Here you can view all the offers sent","§7to you by other players."),
                f -> {
                    GUIFactory.seeOffersPanel(player,this).open();
                });
        setItem(15, createItem(Materials.PLAYER_HEAD, "§6Pay a Player Online", "§7Click to pay another player Online in this server"),
                unused -> {
                    GUIFactory.payPanel(player,this).open();
                });
        setItem(16,createItem(Materials.PLAYER_HEAD,"§6Transfer a Player from the Network", "§7Click to Transfer founds to another","§7Player from the network"),
                f->{
                    GUIFactory.transferFoundsPanel(player,this).open();
                });
        setItem(31, createItem(Materials.BARRIER, "§cExit", "§7Click to exit"), unused -> this.close());
    }
}