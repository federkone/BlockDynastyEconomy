package net.blockdynasty.economy.engine.platform;

import net.blockdynasty.economy.libs.abstractions.platform.PlatformAdapter;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.gui.commands.PlatformCommand;
import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.libs.services.configuration.IConfiguration;

public interface IPlatform extends PlatformGUI, HardCashCreator, PlatformCommand ,PlatformAdapter{
    void startServer(IConfiguration configuration);
    void reloadServer();
}
