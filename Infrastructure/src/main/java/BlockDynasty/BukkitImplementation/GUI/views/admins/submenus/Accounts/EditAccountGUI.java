package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
import BlockDynasty.BukkitImplementation.GUI.components.IGUI;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.UUID;

public class EditAccountGUI extends AbstractGUI {
    private final DeleteAccountUseCase deleteAccountUseCase;

    public EditAccountGUI(
            DeleteAccountUseCase deleteAccountUseCase,
            org.bukkit.entity.Player sender, Player target, IGUI parent) {
        super("Edit Account: "+target.getNickname(), 3,sender,parent);
        this.deleteAccountUseCase = deleteAccountUseCase;

        buttons(sender,target);
    }

    private void buttons(org.bukkit.entity.Player sender, Player target) {
        setItem(5,createItem(Material.BARRIER,"Delete account","the player's account is deleted"),
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

        setItem(18,createItem(Material.BARRIER, "Back","go back"),f->{this.openParent();});

        setItem(11,createItem(Material.PAPER,"Deposit Currency","Deposit currency into the player's account"),
                f -> {
                    GUIFactory.depositPanel(sender , target, this).open();
                });

        setItem(13,createItem(Material.PAPER,"Set balance currency","Set the balance of a currency in the player's account"),
                f -> {
                    GUIFactory.setPanel(sender , target, this).open();
                });

        setItem(15,createItem(Material.PAPER,"Withdraw Currency","Withdraw currency from the player's account"),
                f -> {
                    GUIFactory.withdrawPanel(sender , target, this).open();
                });

        setItem(3,createItem(Material.BOOK, "See Balance","See the balance of the player's account"),
                f -> {
                    GUIFactory.balancePanel( sender, UUID.fromString(target.getUuid()), this).open();
                });
        setItem(22, createItem(Material.PAPER, "Block transactions", "Block transactions for the player"),
                f -> {
                    sender.sendMessage("This feature is not implemented yet.");
                });
    }
}
