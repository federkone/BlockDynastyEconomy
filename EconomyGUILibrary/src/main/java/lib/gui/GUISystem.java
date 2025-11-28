package lib.gui;

import BlockDynasty.Economy.aplication.useCase.UseCaseFactory;
import lib.abstractions.IMessages;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.*;
import lib.gui.components.factory.Inventory;
import lib.gui.components.factory.Item;
import lib.util.colors.Message;

import java.util.UUID;

public class GUISystem {
    private static final IGUIService guiService = new GUIService();

    public static void init(UseCaseFactory useCaseFactory,PlatformAdapter adapter, IMessages messages) {
        Item.init(adapter);
        Inventory.init(adapter);
        Message.addLang(messages);
        GUIFactory.init(useCaseFactory, adapter);
    }

    public static void registerGUI(IEntityGUI entity,IGUI gui){
        GUISystem.guiService.registerGUI(entity,gui);
    }

    public static void unregisterGUI(IEntityGUI entity){
        GUISystem.guiService.unregisterGUI(entity);
    }

    public static void refresh(UUID playerUUID){
        GUISystem.guiService.refresh(playerUUID);
    }

    public static void handleClick(IEntityGUI player, ClickType clickType, int slot){
        GUISystem.guiService.handleClick(player, clickType, slot);
    }

    public static boolean hasOpenedGUI(IEntityGUI entity){
        return GUISystem.guiService.hasOpenedGUI(entity);
    }
}
