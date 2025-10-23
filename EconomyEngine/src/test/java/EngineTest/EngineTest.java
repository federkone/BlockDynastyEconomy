package EngineTest;

import EngineTest.mocks.MinecraftServer;
import EngineTest.mocks.Player;
import EngineTest.mocks.TextInput;
import lib.abstractions.IPlayer;
import lib.commands.CommandsFactory;
import lib.commands.abstractions.Command;
import lib.gui.GUIFactory;
import lib.gui.GUIService;
import lib.gui.components.ClickType;
import lib.gui.components.IGUI;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EngineTest {
    private static MinecraftServer server;
    private static Player player= new Player(UUID.fromString("55e72bac-6481-3abe-9c9b-94cefed85271"), "Nullplague");
    private static Player player2= new Player(UUID.fromString("51a888b7-5a59-3f3d-9922-08746bcd8cd6"), "Fede");

    @BeforeAll
    public static void setup(){
        player.addPermission("BlockDynastyEconomy.economy");
        MinecraftServer.start();
        MinecraftServer.connectPlayer(player);
        MinecraftServer.connectPlayer(player2);
    }

    @Test
    public void testInitialization() {
        assert(MinecraftServer.getEconomy() != null);
    }

    @Test
    public void CommandExecutions(){
        Command command= CommandsFactory.Commands.getCommand("eco");
        command.execute(MinecraftServer.getPlayer("Nullplague"), new String[]{"give","Fede","5000","Money"});
        command.execute(MinecraftServer.getPlayer("Nullplague"), new String[]{"give","Nullplague","3000","Money"});
    }

    @Test
    public void testGUIWorkFlowDepositBalance(){
        //player.setOp(true);
        player.addPermission("BlockDynastyEconomy.economy.superUser");
        Command command= CommandsFactory.Commands.getCommand("eco");
        command.execute(MinecraftServer.getPlayer("Nullplague"), new String[]{"menu"});

        System.out.println("Clicked on slot 24 ->");
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,24);
        System.out.println("Clicked on slot 10 ->");
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,10);
        System.out.println("Clicked on slot 29 ->");
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,29);
        TextInput.setInput("1400");
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,10);
    }

    @Test
    public void testGUIWorkFlowTransferOnline(){
        player.addPermission("BlockDynastyEconomy.players.bank");
        Command command= CommandsFactory.Commands.getCommand("bank");
        command.execute(MinecraftServer.getPlayer("Nullplague"), new String[]{});

        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,15);
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,10);
        TextInput.setInput("2000");
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,10);
    }

    @Test
    public void testGUIWorkFlowTransferOffline(){
        IGUI bankPanel= GUIFactory.bankPanel(player);
        GUIFactory.getGuiService().registerGUI(player,bankPanel);
        bankPanel.open();

        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,16);
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,10);
        TextInput.setInput("1400");
        GUIFactory.getGuiService().handleClick(player, ClickType.LEFT,10);
    }

    @Test
    public void hasPermissions(){
        player.setOp(false);
        player.addPermission("BlockDynastyEconomy.players.bank");
        Command command= CommandsFactory.Commands.getCommand("bank");
        command.execute(MinecraftServer.getPlayer("Nullplague"), new String[]{});
        assertEquals(true,player.hasPermission("BlockDynastyEconomy.players.bank"));
    }

    @AfterAll
    public static void afterAll() {
        MinecraftServer.stop();

    }

}
