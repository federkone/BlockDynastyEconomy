package BlockDynasty.BukkitImplementation.GUI.views.admins.submenus.Accounts;

import BlockDynasty.BukkitImplementation.GUI.components.AbstractGUI;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class EditAccountGUI extends AbstractGUI {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final GUIService guiService;

    public EditAccountGUI(DeleteAccountUseCase deleteAccountUseCase,org.bukkit.entity.Player sender, Player target,GUIService guiService,AbstractGUI parent) {
        super("edit account", 5);
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.guiService = guiService;
        buttons(sender,target,parent);
    }

    //boton para eliminar y ejecutar deleteAcountUseCase.exec(target.getName)
    private void buttons(org.bukkit.entity.Player sender, Player target, AbstractGUI parent) {
        setItem(15,createItem(Material.BARRIER,"borrar jugador","se borra la cuenta del jugador"),
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
    }
}
