package net.blockdynasty.economy.gui.commands.templates.users;

import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.templates.workGui.WorksMainMenu;

public class WorksMainMenuCommand  extends AbstractCommand {


    public WorksMainMenuCommand() {
        super("linkedin","");
    }


    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        IEntityGUI entityGUI = sender.asEntityGUI();
        if (entityGUI != null) {
            new WorksMainMenu(entityGUI).open();
        }else {
            sender.sendMessage("Works GUI is only available for players.");
        }

        return true;
    }
}
