import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Torneo {
    private String nombre;
    private List<Jugador> equipo1 = new ArrayList<>();
    private List<Jugador> equipo2 = new ArrayList<>();
    private List<Emparejamiento> emparejamientos = new ArrayList<>();

    public Torneo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean agregarJugadores(List<Jugador> jugadores) {
        if (jugadores.size() == 8) {
            equipo1.addAll(jugadores.subList(0, 4));
            equipo2.addAll(jugadores.subList(4, 8));
            return true;
        }
        return false;
    }

    public void generarEmparejamientos() {
        emparejamientos.clear();
        for (int i = 0; i < 4; i++) {
            emparejamientos.add(new Emparejamiento(equipo1.get(i), equipo2.get(i)));
        }
    }

    public List<Emparejamiento> getEmparejamientos() {
        return emparejamientos;
    }

    public List<Jugador> getEquipo1() {
        return equipo1;
    }

    public List<Jugador> getEquipo2() {
        return equipo2;
    }
}
