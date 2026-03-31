package net.blockdynasty.economy.engine.platform;

import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;

public interface IPlayer extends IEntityCommands, IEntityHardCash, IEntityGUI {
}
