package listeners;

import lib.commands.abstractions.Source;

public interface IPlayerJoin {

    void loadOnlinePlayerAccount(Source player);
    void loadOfflinePlayerAccount(Source player);
    void offLoadPlayerAccount(Source player);
}
