package net.blockdynasty.economy.minestom.adapters;

import net.minestom.server.entity.Player;

public interface PermissionsService {
    boolean hasPermission(Player player, String permission);
}
