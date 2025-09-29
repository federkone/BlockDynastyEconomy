package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.ITextInput;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AbstractGUI;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;


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
                        ? createItem(Materials.RED_CONCRETE, ChatColor.stringValueOf(Colors.RED)+"Account is blocked", "Your account is temporary blocked", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op")
                        : createItem(Materials.LIME_CONCRETE, ChatColor.stringValueOf(Colors.GREEN)+"Account is enabled", "Your account is allowed to do transactions", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op"),
                null);
        setItem(11,createItem(Materials.DIAMOND, ChatColor.stringValueOf(Colors.GOLD)+"Exchange currencies",ChatColor.stringValueOf(Colors.WHITE)+"Click to exchange currencies"),
                unused -> {
                    GUIFactory.exchangePanel(player,this).open();
                });
        setItem(24, createItem(Materials.WRITABLE_BOOK,ChatColor.stringValueOf(Colors.GOLD)+"Create Offer to Player online",ChatColor.stringValueOf(Colors.WHITE)+"Click to create an offer for trade currencies",ChatColor.stringValueOf(Colors.WHITE)+"with other players Online"),
                f -> {
                    new OfferGUI(player,this,textInput).open();
                });
        setItem(25, createItem(Materials.WRITABLE_BOOK,ChatColor.stringValueOf(Colors.GOLD)+"Create Offer To Network Player",ChatColor.stringValueOf(Colors.WHITE)+"Click to create an offer for trade currencies",ChatColor.stringValueOf(Colors.WHITE)+"with other players from the network"),
                f -> {
                    new OfferOfflineGUI(player,this,searchAccountUseCase,textInput).open();
                });
        setItem(22,createItem(Materials.ENDER_CHEST,ChatColor.stringValueOf(Colors.GOLD)+"My Active Offers",ChatColor.stringValueOf(Colors.WHITE)+"These are the offers you have created",ChatColor.stringValueOf(Colors.WHITE)+"and are currently active."),
                f -> {
                    GUIFactory.seeMyOffersPanel(player,this).open();
                });;
        setItem(13, createItem(Materials.BOOK, ChatColor.stringValueOf(Colors.GOLD)+"See Balance", ChatColor.stringValueOf(Colors.WHITE)+"Click to see your balance"),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });
        setItem(20,createItem(Materials.CHEST,ChatColor.stringValueOf(Colors.GOLD)+"Received Offers",ChatColor.stringValueOf(Colors.WHITE)+"Here you can view all the offers sent",ChatColor.stringValueOf(Colors.WHITE)+"to you by other players."),
                f -> {
                    GUIFactory.seeOffersPanel(player,this).open();
                });
        setItem(15, createItem(Materials.PLAYER_HEAD, ChatColor.stringValueOf(Colors.GOLD)+"Pay a Player Online", ChatColor.stringValueOf(Colors.WHITE)+"Click to pay another player Online in this server"),
                unused -> {
                    GUIFactory.payPanel(player,this).open();
                });
        setItem(16,createItem(Materials.PLAYER_HEAD,ChatColor.stringValueOf(Colors.GOLD)+"Transfer a Player from the Network", ChatColor.stringValueOf(Colors.WHITE)+"Click to Transfer founds to another",ChatColor.stringValueOf(Colors.WHITE)+"Player from the network"),
                f->{
                    GUIFactory.transferFoundsPanel(player,this).open();
                });
        setItem(31, createItem(Materials.BARRIER, ChatColor.stringValueOf(Colors.RED)+"Exit", ChatColor.stringValueOf(Colors.WHITE)+"Click to exit"), unused -> this.close());
    }
}