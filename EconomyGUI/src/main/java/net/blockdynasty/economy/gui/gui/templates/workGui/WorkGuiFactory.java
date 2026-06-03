package net.blockdynasty.economy.gui.gui.templates.workGui;

import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.libs.services.TrabajosService.TrabajosService;

public class WorkGuiFactory {
    private static TrabajosService trabajosService;
    private static PlatformGUI  platformGUI;

    public static void init(TrabajosService trabajosService, PlatformGUI platformGUI) {
        WorkGuiFactory.trabajosService = trabajosService;
        WorkGuiFactory.platformGUI = platformGUI;
    }

    public static IGUI getPlayersFromCategory(IEntityGUI owner, String category,IGUI parent){
        return new PlayersFromCategory(owner,parent,category, trabajosService);
    }

    public static IGUI inscribirseAcategoria(IEntityGUI owner,IGUI parent){
        return new InscribirseAcategoriaPag1(owner,parent,trabajosService,platformGUI);
    }

    public static IGUI inscribirseAcategoriaPag2(IEntityGUI owner,IGUI parent){
        return new InscribirseAcategoriaPag2(owner,parent,platformGUI,trabajosService);
    }

    public static IGUI quitarCategorias(IEntityGUI owner,IGUI parent){
        return new MisPostulacionesMenu(owner,parent,trabajosService);
    }
}