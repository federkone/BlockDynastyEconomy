package net.blockdynasty.economy.gui.commands;

import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.hardcash.domain.entity.platform.HardCashCreator;
import net.blockdynasty.economy.gui.gui.components.ITextInput;

public interface PlatformCommand {
    IPlayer getPlayer(String name);
    void dispatchCommand(String command) throws Exception;
    ITextInput getTextInput();
    HardCashCreator asPlatformHardCash();
}
