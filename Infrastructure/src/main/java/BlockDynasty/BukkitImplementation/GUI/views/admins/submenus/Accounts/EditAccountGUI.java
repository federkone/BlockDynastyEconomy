package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts;

import BlockDynasty.BukkitImplementation.GUI.GUIFactory;
import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.components.AnvilMenu;
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
                          org.bukkit.entity.Player sender, Player target,AbstractGUI parent) {
        super("Edit account", 3,sender);
        this.deleteAccountUseCase = deleteAccountUseCase;

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
                            this.open();
                            return null;
                        }
                    });
                });

        setItem(18,createItem(Material.BARRIER, "atras",""),f->{parent.open();});

        setItem(11,createItem(Material.PAPER,"depositar moneda",""),
                f -> {
                    GUIFactory.depositPanel(sender , target, this).open();
                });

        setItem(13,createItem(Material.PAPER,"establecer moneda",""),
                f -> {
                    GUIFactory.setPanel(sender , target, this).open();
                });

        setItem(15,createItem(Material.PAPER,"retirar moneda",""),
                f -> {
                    GUIFactory.withdrawPanel(sender , target, this).open();
                });

        setItem(10,createItem(Material.PAPER, "ver balance",""),
                f -> {
                    GUIFactory.balancePanel( sender, UUID.fromString(target.getUuid()), this).open();
                });
    }
}
