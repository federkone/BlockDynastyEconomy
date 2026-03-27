package lib.placeholder;

import lib.commands.abstractions.IEntityCommands;
import net.blockdynasty.providers.services.Service;

import java.util.UUID;

public interface IPlaceHolderDynastyEconomy extends Service<UUID> {
    String onRequest(IEntityCommands player, String s);
}
