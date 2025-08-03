package BlockDynasty.BukkitImplementation.GUI;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.AccountPanelGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.AdminPanelGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.CurrencyPanelGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts.EditAccountGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies.CreateCurrencyGUI;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies.CurrencyListDelete;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies.CurrencyListEdit;
import BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Currencies.EditCurrencyGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.BankGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.*;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.Economy.aplication.useCase.AccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.CurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import org.bukkit.entity.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import java.util.UUID;

public class GUIFactory {
    private static CurrencyUseCase currencyUseCase;
    private static AccountsUseCase accountsUseCase;
    private static TransactionsUseCase transactionsUseCase;
    private static MessageService messageService;

    public static void init(CurrencyUseCase currencyUseCase, AccountsUseCase accountsUseCase, TransactionsUseCase transactionsUseCase, MessageService messageService) {
        GUIFactory.messageService = messageService;
        GUIFactory.currencyUseCase = currencyUseCase;
        GUIFactory.accountsUseCase = accountsUseCase;
        GUIFactory.transactionsUseCase = transactionsUseCase;
    }

    public static IGUI adminPanel(Player sender){
        return new AdminPanelGUI(sender);
    }
    public static IGUI accountPanel(Player sender, AbstractGUI parent){
        return new AccountPanelGUI(sender,accountsUseCase.getGetAccountsUseCase(),parent);
    }
    public static IGUI currencyPanel(Player player, AbstractGUI parent) {
        return new CurrencyPanelGUI(player, parent);
    }
    public static void createCurrencyPanel(Player sender) {
        new CreateCurrencyGUI(sender, currencyUseCase.getCreateCurrencyUseCase());
    }
    public static IGUI editAccountPanel(Player sender,BlockDynasty.Economy.domain.entities.account.Player target, AbstractGUI parent) {
        return new EditAccountGUI(accountsUseCase.getDeleteAccountUseCase(), sender, target, parent);
    }
    public static IGUI editCurrencyPanel(Player sender, Currency currency, AbstractGUI parent) {
        return new EditCurrencyGUI(sender, currency, currencyUseCase.getEditCurrencyUseCase(), parent);
    }
    public static IGUI currencyListEditPanel(Player player, AbstractGUI parent) {
        return new CurrencyListEdit(player, currencyUseCase.getGetCurrencyUseCase(), parent);
    }
    public static IGUI currencyListDeletePanel(Player player, AbstractGUI parent) {
        return new CurrencyListDelete(player, currencyUseCase.getGetCurrencyUseCase(), currencyUseCase.getDeleteCurrencyUseCase(), parent);
    }
    public static IGUI currencyListToPayPanel(Player sender, BlockDynasty.Economy.domain.entities.account.Player target, AbstractGUI parent) {
        return new BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.CurrencyListToPay(
                sender,
                target,
                currencyUseCase.getGetCurrencyUseCase(),
                transactionsUseCase.getPayUseCase(),
                messageService,
                parent
        );
    }
    public static IGUI depositPanel(Player sender,BlockDynasty.Economy.domain.entities.account.Player target, AbstractGUI parent){
        return new CurrencyListToDeposit( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getDepositUseCase(),parent);
    }
    public static IGUI setPanel(Player sender,BlockDynasty.Economy.domain.entities.account.Player target, AbstractGUI parent){
        return  new CurrencyListToSet( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getSetBalanceUseCase(),parent);
    }
    public static IGUI withdrawPanel(Player sender,BlockDynasty.Economy.domain.entities.account.Player target, AbstractGUI parent){
        return new CurrencyListToWithdraw( sender,target,currencyUseCase.getGetCurrencyUseCase(),transactionsUseCase.getWithdrawUseCase(),parent);
    }
    public static IGUI balancePanel(Player sender, UUID target, AbstractGUI parent) {
        return new BalanceGUI(sender, target, accountsUseCase.getGetBalanceUseCase(), parent);
    }
    public static IGUI balancePanel(Player sender, AbstractGUI parent) {
        return new BalanceGUI(sender, accountsUseCase.getGetBalanceUseCase(), parent);
    }
    public static IGUI payPanel(Player sender,AbstractGUI parent) {
        return new PayGUI(sender, parent);
    }
    public static IGUI bankPanel(Player sender) {
        return new BankGUI(sender);
    }
}
