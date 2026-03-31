package net.blockdynasty.economy.gui.placeholder;

import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.providers.services.Service;

import java.util.UUID;

public interface IPlaceHolderDynastyEconomy extends Service<UUID> {
    String onRequest(IEntityCommands player, String s);
}
