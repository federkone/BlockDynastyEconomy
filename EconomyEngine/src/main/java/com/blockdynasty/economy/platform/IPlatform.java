package com.blockdynasty.economy.platform;

import abstractions.platform.PlatformAdapter;
import domain.entity.platform.HardCashCreator;
import lib.commands.PlatformCommand;
import lib.gui.components.PlatformGUI;
import services.configuration.IConfiguration;

public interface IPlatform extends PlatformGUI, HardCashCreator, PlatformCommand ,PlatformAdapter{
    void startServer(IConfiguration configuration);
    void reloadServer();
}
