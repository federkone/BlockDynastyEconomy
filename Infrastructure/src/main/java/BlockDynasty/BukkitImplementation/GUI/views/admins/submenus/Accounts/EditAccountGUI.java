package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.EditAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.UUID;

public class EditAccountGUI extends AbstractGUI {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final EditAccountUseCase editAccountUseCase;
    private final SearchAccountUseCase searchAccountUseCase;

    public EditAccountGUI(
            DeleteAccountUseCase deleteAccountUseCase,
            EditAccountUseCase editAccountUseCase,
            SearchAccountUseCase searchAccountUseCase,
            org.bukkit.entity.Player sender, Player target, IGUI parent) {
        super("Edit Account: "+target.getNickname(), 5,sender,parent);
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.editAccountUseCase = editAccountUseCase;
        this.searchAccountUseCase = searchAccountUseCase;

        buttons(sender,target);
    }

    private void buttons(org.bukkit.entity.Player sender, Player target) {
        setItem(15,createItem(Material.REDSTONE,"Delete account","The player's account is deleted,before a confirmation"),
                f -> {
                    AnvilMenu.open(this,sender, "Delete: "+target.getNickname(), "Confirm yes/no", s ->{
                        if(s.equals("yes")){
                            Result<Void> result = deleteAccountUseCase.execute(target.getNickname());
                            if(result.isSuccess()){
                                sender.sendMessage("The player has been eliminated "+ target.getNickname());
                                org.bukkit.entity.Player p = Bukkit.getPlayer(target.getNickname());
                                if(p != null){
                                    p.kickPlayer("Your economy account has been deleted.");
                                }
                                return null;
                            }else{
                                sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                                return null;
                            }
                        }else {
                            this.open();
                            return null;
                        }
                    });
                });

        setItem(40,createItem(Material.BARRIER, "Back","go back"),f->{this.openParent();});

        setItem(29,createItem(Material.PAPER,"Deposit Currency","Deposit currency into the player's account"),
                f -> {
                    GUIFactory.depositPanel(sender , target, this).open();
                });

        setItem(31,createItem(Material.PAPER,"Set balance currency","Set the balance of a currency in the player's account"),
                f -> {
                    GUIFactory.setPanel(sender , target, this).open();
                });

        setItem(33,createItem(Material.PAPER,"Withdraw Currency","Withdraw currency from the player's account"),
                f -> {
                    GUIFactory.withdrawPanel(sender , target, this).open();
                });

        setItem(11,createItem(Material.BOOK, "See Balance","See the balance of the player's account"),
                f -> {
                    GUIFactory.balancePanel( sender, UUID.fromString(target.getUuid()), this).open();
                });

        boolean isBlocked = searchAccountUseCase.getAccount(UUID.fromString(target.getUuid())).getValue().isBlocked();
        if (isBlocked) {
            setItem(13, createItem(MaterialAdapter.getRedConcrete(), "Account is blocked", "Click to unblock transactions"),
                    f -> {
                        Result<Void>result= editAccountUseCase.unblockAccount(UUID.fromString(target.getUuid()));
                        if (result.isSuccess()){
                            GUIFactory.editAccountPanel( sender, target, this.getParent()).open();
                        }
                        else {
                            sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                        }
                    });
        } else {
            setItem(13, createItem(MaterialAdapter.getLimeConcrete(), "Account is enabled", "Click to block transactions Account"),
                    f -> {
                        Result<Void>result= editAccountUseCase.blockAccount(UUID.fromString(target.getUuid()));
                        if (result.isSuccess()){
                            GUIFactory.editAccountPanel( sender, target, this.getParent()).open();
                        }
                        else {
                            sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                        }
                    });
        }
    }
}
