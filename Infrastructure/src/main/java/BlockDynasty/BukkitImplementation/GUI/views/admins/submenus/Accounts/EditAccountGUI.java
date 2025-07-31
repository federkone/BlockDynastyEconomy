package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.CurrencyListToDeposit;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.CurrencyListToSet;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.CurrencyListToWithdraw;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class EditAccountGUI extends AbstractGUI {
    private final JavaPlugin plugin = BlockDynastyEconomy.getInstance();
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final TransactionsUseCase transactionsUseCase;
    private final GUIService guiService;
    //edit account unse case

    public EditAccountGUI(SearchCurrencyUseCase searchCurrencyUseCase,
                          DeleteAccountUseCase deleteAccountUseCase,TransactionsUseCase transactionsUseCase,
                          org.bukkit.entity.Player sender, Player target,GUIService guiService,AbstractGUI parent) {
        super("Edit account", 3);
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.transactionsUseCase = transactionsUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.guiService = guiService;
        buttons(sender,target,parent);
    }

    //boton para eliminar y ejecutar deleteAcountUseCase.exec(target.getName)
    private void buttons(org.bukkit.entity.Player sender, Player target, AbstractGUI parent) {
        setItem(13,createItem(Material.BARRIER,"borrar jugador","se borra la cuenta del jugador"),
                unused -> {
                    Result<Void> result = deleteAccountUseCase.execute(target.getNickname());
                    //sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                    if(result.isSuccess()){
                        sender.sendMessage(ChatColor.GREEN + "Se ha eliminado el jugador "+ target.getNickname());
                        Bukkit.getPlayer(target.getNickname()).kickPlayer("Su cuenta de economia se ha eliminado.");
                    }
                    sender.closeInventory();
                });

        setItem(18,createItem(Material.BARRIER, "atras",""),a->{
            sender.openInventory(parent.getInventory());
            guiService.registerGUI(sender, parent);
        });

        setItem(11,createItem(Material.PAPER,"depositar dinero",""),
                a -> {
                    IGUI currencyListToDeposit = new CurrencyListToDeposit(plugin,guiService,sender,target,searchCurrencyUseCase,transactionsUseCase.getDepositUseCase(),this);
                    sender.openInventory(currencyListToDeposit.getInventory());
                    guiService.registerGUI(sender, currencyListToDeposit);
                });

        setItem(13,createItem(Material.PAPER,"establecer dinero",""),
                a -> {
                    IGUI currencyListToSet = new CurrencyListToSet(plugin,guiService,sender,target,searchCurrencyUseCase,transactionsUseCase.getSetBalanceUseCase(),this);
                    sender.openInventory(currencyListToSet.getInventory());
                    guiService.registerGUI(sender, currencyListToSet);
                });

        setItem(15,createItem(Material.PAPER,"retirar dinero",""),
                a -> {
                    IGUI currencyListToWithdraw = new CurrencyListToWithdraw(plugin,guiService,sender,target,searchCurrencyUseCase,transactionsUseCase.getWithdrawUseCase(),this);
                    sender.openInventory(currencyListToWithdraw.getInventory());
                    guiService.registerGUI(sender, currencyListToWithdraw);
                });

//features similar to PayGUI
        //deposit
            //submenu list currencies, and anvil input to deposit amount
        //set
            //submenu list currencies, and anvil input to set amount
        //withdraw
            //submenu list currencies, and anvil input to withdraw amount
    }
}
