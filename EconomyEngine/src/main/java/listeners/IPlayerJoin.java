package listeners;

import lib.commands.abstractions.IEntityCommands;

public interface IPlayerJoin {

    void loadOnlinePlayerAccount(IEntityCommands player);
    void loadOfflinePlayerAccount(IEntityCommands player);
    void offLoadPlayerAccount(IEntityCommands player);
}
