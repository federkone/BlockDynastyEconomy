package lib.gui.abstractions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPlayerManager {
    Optional<IPlayer> getPlayerOnlineByUUID(UUID uuid);
    List<IPlayer> getOnlinePlayers();
}