package lib.gui;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.commands.abstractions.PlatformAdapter;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IGUIService;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.ITextInput;
import lib.gui.templates.abstractions.AbstractGUI;
import lib.gui.templates.administrators.mainMenus.AccountPanelGUI;
import lib.gui.templates.administrators.mainMenus.AdminPanelGUI;
import lib.gui.templates.administrators.mainMenus.CurrencyPanelGUI;
import lib.gui.templates.administrators.subMenus.accounts.CurrencyListToDeposit;
import lib.gui.templates.administrators.subMenus.accounts.CurrencyListToSet;
import lib.gui.templates.administrators.subMenus.accounts.CurrencyListToWithdraw;
import lib.gui.templates.administrators.subMenus.accounts.EditAccountGUI;
import lib.gui.templates.administrators.subMenus.currencies.*;
import lib.gui.templates.users.*;

import java.util.UUID;

public class GUIFactory {
    private static CurrencyUseCase currencyUseCase;
    private static AccountsUseCase accountsUseCase;
    private static TransactionsUseCase transactionsUseCase;
    private static OfferUseCase offerUseCase;
    private static ITextInput textInput;
    private static final IGUIService guiService = new GUIService();

    public static void init(CurrencyUseCase currencyUseCase, AccountsUseCase accountsUseCase, TransactionsUseCase transactionsUseCase,
                            OfferUseCase offerUseCase, ITextInput textInput, PlatformAdapter adapter) {
        AbstractGUI.setPlatformAdapter(adapter,guiService);
        GUIFactory.currencyUseCase = currencyUseCase;
        GUIFactory.accountsUseCase = accountsUseCase;
        GUIFactory.transactionsUseCase = transactionsUseCase;
        GUIFactory.offerUseCase = offerUseCase;
        GUIFactory.textInput = textInput;

    }
    public static IGUIService getGuiService() {
        return guiService;
    }
    //_-------------------------------------------------------------------------------

    //main admin panel
    public static IGUI adminPanel(IPlayer sender){
        return new AdminPanelGUI(sender);
    }
    //account admin panel
        public static IGUI accountPanel(IPlayer sender, IGUI parent){
            return new AccountPanelGUI(sender,accountsUseCase.getGetAccountsUseCase(),parent,textInput);
        }
        //submenus for accountPanel
            public static IGUI editAccountPanel(IPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
                return new EditAccountGUI(accountsUseCase.getDeleteAccountUseCase(), accountsUseCase.getEditAccountUseCase(),accountsUseCase.getGetAccountsUseCase(),sender, target, parent,textInput);
            }
            //submenus for editAccountPanel
                public static IGUI balancePanel(IPlayer sender, UUID target, IGUI parent) {
                    return new BalanceGUI(sender, target, accountsUseCase.getGetBalanceUseCase(), parent);
                }
                public static IGUI depositPanel(IPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return new CurrencyListToDeposit( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getDepositUseCase(),parent,textInput);
                }
                public static IGUI setPanel(IPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return  new CurrencyListToSet( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getSetBalanceUseCase(),parent,textInput);
                }
                public static IGUI withdrawPanel(IPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return new CurrencyListToWithdraw( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getWithdrawUseCase(),parent,textInput);
                }

        //currency admin panel
        public static IGUI currencyPanel(IPlayer player, IGUI parent) {
            return new CurrencyPanelGUI(player, parent);
        }
        //submenus for currencyPanel
            public static void createCurrencyPanel(IPlayer sender,IGUI parent) {
                new CreateCurrencyGUI(sender, currencyUseCase.getCreateCurrencyUseCase(),currencyUseCase.getGetCurrencyUseCase(),parent,textInput);
            }
            public static IGUI currencyListToDeletePanel(IPlayer player, IGUI parent) {
                return new CurrencyListDelete(player, currencyUseCase.getGetCurrencyUseCase(), currencyUseCase.getDeleteCurrencyUseCase(), parent,textInput);
            }
            public static IGUI currencyListToEditPanel(IPlayer player, IGUI parent) {
                return new CurrencyListEdit(player, currencyUseCase.getGetCurrencyUseCase(), parent,textInput);
            }
            //submenus for currencyListToEditPanel
                public static IGUI editCurrencyPanel(IPlayer sender, Currency currency, IGUI parent) {
                    return new EditCurrencyGUI(sender, currency, currencyUseCase.getEditCurrencyUseCase(), parent,textInput);
                }
                //submenus for editCurrencyPanel
                    public static IGUI colorSelectorPanel(IPlayer sender,Currency currency, EditCurrencyGUI parent) {
                        return new ColorSelectionGUI( sender,currency, currencyUseCase.getEditCurrencyUseCase(), parent);
                    }
    //_-------------------------------------------------------------------------------
    //main bank user panel
    public static IGUI bankPanel(IPlayer sender) {
        return new BankGUI(sender, accountsUseCase.getGetAccountsUseCase(),textInput);
    }
    //submenus for bankPanel
        public static IGUI currencyListToOffer(IPlayer sender,BlockDynasty.Economy.domain.entities.account.Player target,IGUI parent ){
            return new CurrencyListToOfferFirst(sender,target,currencyUseCase.getGetCurrencyUseCase(),offerUseCase.getCreateOfferUseCase(), parent, textInput);
        }
        public static IGUI balancePanel(IPlayer sender, IGUI parent) {
            return new BalanceGUI(sender, accountsUseCase.getGetBalanceUseCase(), parent);
        }
        public static IGUI transferFoundsPanel(IPlayer sender,IGUI parent) {
            return new TransferFoundsGUI(sender, parent, accountsUseCase.getGetAccountsUseCase(),textInput);
        }
        public static IGUI exchangePanel(IPlayer sender, IGUI parent) {
            return new CurrencyListToExchangeFirst(sender, currencyUseCase.getGetCurrencyUseCase(), transactionsUseCase.getExchangeUseCase(), parent , textInput);
        }
        public static IGUI seeOffersPanel(IPlayer sender, IGUI parent) {
            return new AccountListToOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getAcceptOfferUseCase(),offerUseCase.getCancelOfferUseCase(), sender, parent );
        }
        public static IGUI seeMyOffersPanel(IPlayer sender, IGUI parent) {
            return new AccountListFromOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getCancelOfferUseCase(), sender, parent);
        }
        public static IGUI seeMyOffersPanel(IPlayer sender) {
            return new AccountListFromOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getCancelOfferUseCase(), sender, null);
        }
        public static IGUI payPanel(IPlayer sender,IGUI parent) {
        return new PayGUI(sender, parent, textInput);
    }
            //submenus for payPanel
            public static IGUI currencyListToPayPanel(IPlayer sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
                return new CurrencyListToPay(
                        sender,
                        target,
                        currencyUseCase.getGetCurrencyUseCase(),
                        transactionsUseCase.getPayUseCase(),
                        parent,
                        textInput
                );
            }
}
