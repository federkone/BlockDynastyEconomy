package net.blockdynasty.economy.libs.services.TrabajosService;

import net.blockdynasty.economy.libs.abstractions.platform.PlatformAdapter;
import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrabajosService {
    private List<Jugador> jugadores;
    private PlatformAdapter platformAdapter;

    public TrabajosService(PlatformAdapter platformAdapter) {
        this.jugadores = new ArrayList<>();
        this.platformAdapter = platformAdapter;

        //load from yaml.....
    }

    public List<IPlayer> getJugadoresEnLineaDeCategoria(String categoria){
        List<Jugador> jugadoresEncontrados = this.jugadores.stream().filter( jugador -> jugador.getPostulaciones().contains(categoria)).collect(Collectors.toList());
        List<Jugador> jugadoresEncontradosEnLinea = jugadoresEncontrados.stream().filter( jugador -> platformAdapter.getPlayer(jugador.getNombre()) != null).collect(Collectors.toList());
        return jugadoresEncontradosEnLinea.stream().map(Jugador::getPlayer).collect(Collectors.toList());
    }


    public void inscribirJugadorACategoria(IPlayer jugador, String categoria){
        Optional<Jugador> finded = this.jugadores.stream().filter( j -> j.getNombre().equals(jugador.getName())).findFirst();

        if (finded.isPresent()){
            finded.get().addPostulacion(categoria);
        }else{
            Jugador j =  new Jugador(jugador,new ArrayList<>());
            j.addPostulacion(categoria);
            this.jugadores.add(j);
            //save to yaml file...
        }
    }

    public void removerInscripcionJugadorACategoria(IPlayer jugador, String categoria){
        Optional<Jugador> finded = this.jugadores.stream().filter( j -> j.getNombre().equals(jugador.getName())).findFirst();
        finded.ifPresent(value -> value.removePostulacion(categoria));


        //save yaml?
    }

    public List<String> getPostulacionesDe(String player){
        List<String> postulaciones = new ArrayList<>();

        Optional<Jugador> j = this.jugadores.stream().filter(p -> p.getNombre().equals(player)).findFirst();
        if (j.isPresent()){
            return j.get().getPostulaciones();
        }
        return postulaciones;
    }


    public boolean puedePostularse(IPlayer jugador){
        Optional<Jugador> jEncontrado= this.jugadores.stream().filter(j -> j.getNombre().equals(jugador.getName())).findFirst();
        return jEncontrado.map(Jugador::puedePostularse).orElse(true);
    }
}
