/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lib.gui;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.OfferUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.IGUI;
import lib.gui.components.IGUIService;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.abstractions.AbstractPanel;
import lib.gui.templates.administrators.mainMenus.AccountSelectorToEdit;
import lib.gui.templates.administrators.mainMenus.EconomyAdminPanel;
import lib.gui.templates.administrators.mainMenus.CurrencyAdminPanel;
import lib.gui.templates.administrators.subMenus.accounts.DepositPanel;
import lib.gui.templates.administrators.subMenus.accounts.SetBalancePanel;
import lib.gui.templates.administrators.subMenus.accounts.WithdrawPanel;
import lib.gui.templates.administrators.subMenus.accounts.EditAccountPanel;
import lib.gui.templates.administrators.subMenus.currencies.*;
import lib.gui.templates.users.*;
import lib.gui.templates.users.Exchange.ExchangeFirstPanel;
import lib.gui.templates.users.Offers.CreateOfferFirstPanel;
import lib.gui.templates.users.Offers.MyActiveOffers;
import lib.gui.templates.users.Offers.ReceivedOffers;

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
        AbstractPanel.setPlatformAdapter(adapter,guiService);
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
    public static IGUI economyAdminPanel(IEntityGUI sender){
        return new EconomyAdminPanel(sender);
    }
    //account admin panel
        public static IGUI accountSelectorToEdit(IEntityGUI sender, IGUI parent){
            return new AccountSelectorToEdit(sender,accountsUseCase.getGetAccountsUseCase(),parent,textInput);
        }
        //submenus for accountPanel
            public static IGUI editAccountPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
                return new EditAccountPanel(accountsUseCase.getDeleteAccountUseCase(), accountsUseCase.getEditAccountUseCase(),accountsUseCase.getGetAccountsUseCase(),sender, target, parent,textInput);
            }
            //submenus for editAccountPanel
                public static IGUI balancePanel(IEntityGUI sender, UUID target, IGUI parent) {
                    return new AccountBalance(sender, target, accountsUseCase.getGetBalanceUseCase(), parent);
                }
                public static IGUI depositPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return new DepositPanel( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getDepositUseCase(),parent,textInput);
                }
                public static IGUI setPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return  new SetBalancePanel( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getSetBalanceUseCase(),parent,textInput);
                }
                public static IGUI withdrawPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent){
                    return new WithdrawPanel( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getWithdrawUseCase(),parent,textInput);
                }

        //currency admin panel
        public static IGUI currencyPanel(IEntityGUI player, IGUI parent) {
            return new CurrencyAdminPanel(player, parent);
        }
        //submenus for currencyPanel
            public static void createCurrencyPanel(IEntityGUI sender, IGUI parent) {
                new CreateCurrencyGUI(sender, currencyUseCase.getCreateCurrencyUseCase(),currencyUseCase.getGetCurrencyUseCase(),parent,textInput);
            }
            public static IGUI currencyListToDeletePanel(IEntityGUI player, IGUI parent) {
                return new CurrencyListDelete(player, currencyUseCase.getGetCurrencyUseCase(), currencyUseCase.getDeleteCurrencyUseCase(), parent,textInput);
            }
            public static IGUI currencyListToEditPanel(IEntityGUI player, IGUI parent) {
                return new CurrencyListEdit(player, currencyUseCase.getGetCurrencyUseCase(), parent,textInput);
            }
            //submenus for currencyListToEditPanel
                public static IGUI editCurrencyPanel(IEntityGUI sender, Currency currency, IGUI parent) {
                    return new EditCurrencyPanel(sender, currency, currencyUseCase.getEditCurrencyUseCase(), parent,textInput);
                }
                //submenus for editCurrencyPanel
                    public static IGUI colorSelectorPanel(IEntityGUI sender, Currency currency, EditCurrencyPanel parent) {
                        return new ColorSelectionPanel( sender,currency, currencyUseCase.getEditCurrencyUseCase(), parent,textInput);
                    }
    //_-------------------------------------------------------------------------------
    //main bank user panel
    public static IGUI bankPanel(IEntityGUI sender) {
        return new BankPanel(sender, accountsUseCase.getGetAccountsUseCase(),textInput);
    }
    //submenus for bankPanel
        public static IGUI createOfferFirstPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent ){
            return new CreateOfferFirstPanel(sender,target,currencyUseCase.getGetCurrencyUseCase(),offerUseCase.getCreateOfferUseCase(), parent, textInput);
        }
        public static IGUI balancePanel(IEntityGUI sender, IGUI parent) {
            return new AccountBalance(sender, accountsUseCase.getGetBalanceUseCase(), parent);
        }
        public static IGUI listPlayersFromDb(IEntityGUI sender, IGUI parent) {
            return new ListPlayersFromDb(sender, parent, accountsUseCase.getGetAccountsUseCase(),textInput);
        }
        public static IGUI exchangeFirstPanel(IEntityGUI sender, IGUI parent) {
            return new ExchangeFirstPanel(sender, currencyUseCase.getGetCurrencyUseCase(), transactionsUseCase.getExchangeUseCase(), parent , textInput);
        }
        public static IGUI receivedOffers(IEntityGUI sender, IGUI parent) {
            return new ReceivedOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getAcceptOfferUseCase(),offerUseCase.getCancelOfferUseCase(), sender, parent );
        }
        public static IGUI myActiveOffers(IEntityGUI sender, IGUI parent) {
            return new MyActiveOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getCancelOfferUseCase(), sender, parent);
        }
        public static IGUI myActiveOffers(IEntityGUI sender) {
            return new MyActiveOffers(offerUseCase.getSearchOfferUseCase(), offerUseCase.getCancelOfferUseCase(), sender, null);
        }
        public static IGUI listPlayersOnline(IEntityGUI sender, IGUI parent) {
        return new ListPlayersOnline(sender, parent, textInput);
    }
            //submenus for payPanel
            public static IGUI currencyListToPayPanel(IEntityGUI sender, BlockDynasty.Economy.domain.entities.account.Player target, IGUI parent) {
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
