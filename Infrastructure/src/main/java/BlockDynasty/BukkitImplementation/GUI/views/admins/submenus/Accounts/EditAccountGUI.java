package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.BalanceGUI;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.CurrencyListToDeposit;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.CurrencyListToSet;
import BlockDynasty.BukkitImplementation.GUI.views.users.userPanels.CurrencyListToWithdraw;
import BlockDynasty.Economy.aplication.useCase.TransactionsUseCase;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.UUID;

public class EditAccountGUI extends AbstractGUI {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final TransactionsUseCase transactionsUseCase;
    private final GetBalanceUseCase getBalanceUseCase;

    public EditAccountGUI(SearchCurrencyUseCase searchCurrencyUseCase,
                          DeleteAccountUseCase deleteAccountUseCase,TransactionsUseCase transactionsUseCase,
                          GetBalanceUseCase getBalanceUseCase,
                          org.bukkit.entity.Player sender, Player target,AbstractGUI parent) {
        super("Edit account", 3);
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.transactionsUseCase = transactionsUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.getBalanceUseCase = getBalanceUseCase;

        buttons(sender,target,parent);
    }

    private void buttons(org.bukkit.entity.Player sender, Player target, AbstractGUI parent) {
        setItem(17,createItem(Material.BARRIER,"Borrar Cuenta","se borra la cuenta del jugador"),
                f -> {
                    AnvilMenu.open(sender, "Delete: "+target.getNickname(), "Confirm yes/no", s ->{
                        if(s.equals("yes")){
                            Result<Void> result = deleteAccountUseCase.execute(target.getNickname());
                            if(result.isSuccess()){
                                sender.sendMessage("Se ha eliminado el jugador "+ target.getNickname());
                                org.bukkit.entity.Player p = Bukkit.getPlayer(target.getNickname());
                                if(p != null){
                                    p.kickPlayer("Su cuenta de economia se ha eliminado.");
                                }
                                return null;
                            }else{
                                sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                                return null;
                            }
                        }else {
                            this.open(sender);
                            return null;
                        }
                    });
                });

        setItem(18,createItem(Material.BARRIER, "atras",""),f->{parent.open(sender);});

        setItem(11,createItem(Material.PAPER,"depositar moneda",""),
                f -> {
                    IGUI currencyListToDeposit = new CurrencyListToDeposit(sender,target,searchCurrencyUseCase,transactionsUseCase.getDepositUseCase(),this);
                    currencyListToDeposit.open(sender);
                });

        setItem(13,createItem(Material.PAPER,"establecer moneda",""),
                f -> {
                    IGUI currencyListToSet = new CurrencyListToSet(sender,target,searchCurrencyUseCase,transactionsUseCase.getSetBalanceUseCase(),this);
                    currencyListToSet.open(sender);
                });

        setItem(15,createItem(Material.PAPER,"retirar moneda",""),
                f -> {
                    IGUI currencyListToWithdraw = new CurrencyListToWithdraw(sender,target,searchCurrencyUseCase,transactionsUseCase.getWithdrawUseCase(),this);
                    currencyListToWithdraw.open(sender);
                });

        setItem(10,createItem(Material.PAPER, "ver balance",""),
                f -> {
                    IGUI balanceGUI = new BalanceGUI(sender, UUID.fromString(target.getUuid()),getBalanceUseCase , this);
                    balanceGUI.open(sender);
                });
    }
}
