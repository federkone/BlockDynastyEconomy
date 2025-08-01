package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts;

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
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class EditAccountGUI extends AbstractGUI {
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
        setItem(17,createItem(Material.BARRIER,"Borrar Cuenta","se borra la cuenta del jugador"),
                f -> {
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

        setItem(11,createItem(Material.PAPER,"depositar moneda",""),
                f -> {
                    IGUI currencyListToDeposit = new CurrencyListToDeposit(guiService,sender,target,searchCurrencyUseCase,transactionsUseCase.getDepositUseCase(),this);
                    sender.openInventory(currencyListToDeposit.getInventory());
                    guiService.registerGUI(sender, currencyListToDeposit);
                });

        setItem(13,createItem(Material.PAPER,"establecer moneda",""),
                f -> {
                    IGUI currencyListToSet = new CurrencyListToSet(guiService,sender,target,searchCurrencyUseCase,transactionsUseCase.getSetBalanceUseCase(),this);
                    sender.openInventory(currencyListToSet.getInventory());
                    guiService.registerGUI(sender, currencyListToSet);
                });

        setItem(15,createItem(Material.PAPER,"retirar moneda",""),
                f -> {
                    IGUI currencyListToWithdraw = new CurrencyListToWithdraw(guiService,sender,target,searchCurrencyUseCase,transactionsUseCase.getWithdrawUseCase(),this);
                    sender.openInventory(currencyListToWithdraw.getInventory());
                    guiService.registerGUI(sender, currencyListToWithdraw);
                });

    }
}
