package net.blockdynasty.economy.libs.services.TrabajosService;

import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;

import java.util.UUID;

public class PlayerTemp implements IPlayer {
    private String nombre;

    public PlayerTemp(String nombre){
        this.nombre = nombre;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public String getName() {
        return this.nombre;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void kickPlayer(String message) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public Object getRoot() {
        return null;
    }
}
