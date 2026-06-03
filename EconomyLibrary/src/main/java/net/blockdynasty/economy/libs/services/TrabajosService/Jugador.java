package net.blockdynasty.economy.libs.services.TrabajosService;

import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;

import java.util.List;

public class Jugador {
    private IPlayer player;
    private List<String> postulaciones;
    private int maxPostulaciones = 3;

    public Jugador(IPlayer player, List<String> postulaciones) {
        this.player = player;
        this.postulaciones = postulaciones;
    }

    public boolean tienePostulacion(String postulacion) {
        return postulaciones.contains(postulacion);
    }

    public void addPostulacion(String postulacion) {
        postulaciones.add(postulacion);
    }

    public void removePostulacion(String postulacion) {
        postulaciones.remove(postulacion);
    }

    public boolean puedePostularse(){
        return postulaciones.size() < maxPostulaciones;
    }

    public List<String> getPostulaciones() {
        return postulaciones;
    }

    public String getNombre() {
        return this.player.getName();
    }

    public IPlayer getPlayer() {
        return this.player;
    }
}
