package BlockDynasty.GUI;

import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.GUI.components.IGUI;
import BlockDynasty.GUI.views.users.BalanceGUI;
import BlockDynasty.GUI.views.users.BankGUI;
import BlockDynasty.GUI.views.users.CurrencyListToPay;
import BlockDynasty.GUI.views.users.PayGUI;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.UUID;

public class GUIFactory {
    private static CurrencyUseCase currencyUseCase;
    private static AccountsUseCase accountsUseCase;
    private static TransactionsUseCase transactionsUseCase;
    private static OfferUseCase offerUseCase;

    public static void init(CurrencyUseCase currencyUseCase, AccountsUseCase accountsUseCase, TransactionsUseCase transactionsUseCase, OfferUseCase offerUseCase) {
        GUIFactory.currencyUseCase = currencyUseCase;
        GUIFactory.accountsUseCase = accountsUseCase;
        GUIFactory.transactionsUseCase = transactionsUseCase;
        GUIFactory.offerUseCase = offerUseCase;
    }

    //main admin panel
    public static IGUI adminPanel(ServerPlayer sender){
        //return new AdminPanelGUI(sender);
        return null;
    }
    //account admin panel
    public static IGUI accountPanel(ServerPlayer sender, IGUI parent){
        //return new AccountPanelGUI(sender,accountsUseCase.getGetAccountsUseCase(),parent);
        return null;
    }
    //submenus for accountPanel
    public static IGUI editAccountPanel(ServerPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
        //return new EditAccountGUI(accountsUseCase.getDeleteAccountUseCase(), accountsUseCase.getEditAccountUseCase(),accountsUseCase.getGetAccountsUseCase(),sender, target, parent);
        return null;
    }
    //submenus for editAccountPanel
    public static IGUI balancePanel(ServerPlayer sender, UUID target, IGUI parent) {
        //return new BalanceGUI(sender, target, accountsUseCase.getGetBalanceUseCase(), parent);
        return null;
    }
    public static IGUI depositPanel(ServerPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
        //return new CurrencyListToDeposit( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getDepositUseCase(),parent);
        return null;
    }
    public static IGUI setPanel(ServerPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
        //return  new CurrencyListToSet( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getSetBalanceUseCase(),parent);
        return null;
    }
    public static IGUI withdrawPanel(ServerPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
        //return new CurrencyListToWithdraw( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getWithdrawUseCase(),parent);
        return null;
    }

    //currency admin panel
    public static IGUI currencyPanel(ServerPlayer player, IGUI parent) {
        //return new CurrencyPanelGUI(player, parent);
        return null;
    }
    //submenus for currencyPanel
    public static void createCurrencyPanel(ServerPlayer sender,IGUI parent) {
        //new CreateCurrencyGUI(sender, currencyUseCase.getCreateCurrencyUseCase(),currencyUseCase.getGetCurrencyUseCase(),parent);
    }
    public static IGUI currencyListToDeletePanel(ServerPlayer player, IGUI parent) {
        //return new CurrencyListDelete(player, currencyUseCase.getGetCurrencyUseCase(), currencyUseCase.getDeleteCurrencyUseCase(), parent);
        return null;
    }
    public static IGUI currencyListToEditPanel(ServerPlayer player, IGUI parent) {
        //return new CurrencyListEdit(player, currencyUseCase.getGetCurrencyUseCase(), parent);
        return null;
    }
    //submenus for currencyListToEditPanel
    public static IGUI editCurrencyPanel(ServerPlayer sender, Currency currency, IGUI parent) {
        //return new EditCurrencyGUI(sender, currency, currencyUseCase.getEditCurrencyUseCase(), parent);
        return null;
    }
    //submenus for editCurrencyPanel
    //public static IGUI colorSelectorPanel(ServerPlayer sender,Currency currency, EditCurrencyGUI parent) {
        //return new ColorSelectionGUI( sender,currency, currencyUseCase.getEditCurrencyUseCase(), parent);
    //    return null;
    //}
//_-------------------------------------------------------------------------------

    //main bank user panel
    public static IGUI bankPanel(ServerPlayer sender) {
        return new BankGUI(sender, accountsUseCase.getGetAccountsUseCase());
    }
        //submenus for bankPanel
        public static IGUI currencyListToOffer(ServerPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target,IGUI parent ){
            //return new CurrencyListToOfferFirst(sender,target,currencyUseCase.getGetCurrencyUseCase(),offerUseCase.getCreateOfferUseCase(), parent);
            return null;
        }
        public static IGUI balancePanel(ServerPlayer sender, IGUI parent) {
            return new BalanceGUI(sender, accountsUseCase.getGetBalanceUseCase(), parent);
        }
        public static IGUI transferFoundsPanel(ServerPlayer sender,IGUI parent) {
            //return new TransferFoundsGUI(sender, parent, accountsUseCase.getGetAccountsUseCase());
            return null;
        }
        public static IGUI exchangePanel(ServerPlayer sender, IGUI parent) {
            //return new CurrencyListToExchangeFirst(sender, currencyUseCase.getGetCurrencyUseCase(), transactionsUseCase.getExchangeUseCase(), parent);
            return null;
        }
        public static IGUI payPanel(ServerPlayer sender,IGUI parent) {
            return new PayGUI(sender, parent);
            //return null;
        }
        public static IGUI seeOffersPanel(ServerPlayer sender, IGUI parent) {
            //return new AccountListToOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getAcceptOfferUseCase(),offerUseCase.getCancelOfferUseCase(), sender, parent);
            return null;
        }
        public static IGUI seeMyOffersPanel(ServerPlayer sender, IGUI parent) {
            //return new AccountListFromOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getCancelOfferUseCase(), sender, parent);
            return null;
        }
        public static IGUI seeMyOffersPanel(ServerPlayer sender) {
            //return new AccountListFromOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getCancelOfferUseCase(), sender, null);
            return null;
        }
        //submenus for payPanel
            public static IGUI currencyListToPayPanel(ServerPlayer sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
                return new CurrencyListToPay(
                        sender,
                        target,
                        currencyUseCase.getGetCurrencyUseCase(),
                        transactionsUseCase.getPayUseCase(),

                        parent
                );
    }
}
