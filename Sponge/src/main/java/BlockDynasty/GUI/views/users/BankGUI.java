package BlockDynasty.GUI.views.users;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.GUI.GUIFactory;
import BlockDynasty.GUI.components.AbstractGUI;
import BlockDynasty.GUI.components.AnvilMenu;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;

public class BankGUI extends AbstractGUI {
    private final ServerPlayer player;
    private final SearchAccountUseCase searchAccountUseCase;

    public BankGUI(ServerPlayer ServerPlayer , SearchAccountUseCase SearchAccountUseCase) {
        super("Bank ", 4, ServerPlayer);
        System.out.println(" CREANDO BANK GUI");
        this.player = ServerPlayer;
        this.searchAccountUseCase = SearchAccountUseCase;

        setupGUI();
        //setup icons
    }

    private void setupGUI() {
        boolean isBlocked =  true;//searchAccountUseCase.getAccount(player.uniqueId()).getValue().isBlocked();
        setItem(4, isBlocked
                        ? createItem( ItemTypes.BOOK.get(), "Account is blocked", "Your account is temporary blocked", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op")
                        : createItem( ItemTypes.BOOK.get(), "Account is enabled", "Your account is allowed to do transactions", "This affects:", "Withdraw", "Deposit", "Transfer", "Pay", "Trade", "Exchange", "All economy op"),
                null);
        setItem(11,createItem( ItemTypes.DIAMOND.get(), "§6Exchange currencies","§7Click to exchange currencies"),
                unused -> {
                    AnvilMenu.open(unused,"ingrese monto","" ,f->{return "";});
                    //GUIFactory.exchangePanel(player,this).open();
                });
        setItem(24, createItem( ItemTypes.WRITABLE_BOOK.get(),"Create Offer","Click to create an offer for trade currencies","with other players Online"),
                f -> {
                    //new OfferGUI(player,this).open();
                });
        setItem(22,createItem( ItemTypes.ENDER_CHEST.get(),"My Active Offers","These are the offers you have created and are currently active."),
                f -> {
                    //GUIFactory.seeMyOffersPanel(player,this).open();
                });;
        setItem(13, createItem( ItemTypes.BOOK.get(), "§6See Balance", "§7Click to see your balance"),
                unused -> {
                    GUIFactory.balancePanel(player,this).open();
                });
        setItem(20,createItem( ItemTypes.CHEST.get(),"Received Offers","Here you can view all the offers sent to you by other players."),
                f -> {
                    //GUIFactory.seeOffersPanel(player,this).open();
                });
        setItem(15, createItem( ItemTypes.PLAYER_HEAD.get(), "§aPay a Player Online", "§7Click to pay another player Online in this server"),
                unused -> {
                    GUIFactory.payPanel(player,this).open();
                });
        setItem(16,createItem( ItemTypes.PLAYER_HEAD.get(),"§aTransfer a Player from the Network", "§7Click to Transfer founds to another Player from the network"),
                f->{
                    //GUIFactory.transferFoundsPanel(player,this).open();
                });
        setItem(31, createItem( ItemTypes.BARRIER.get(), "§cExit", "§7Click to exit"), unused -> this.close());
    }
}
