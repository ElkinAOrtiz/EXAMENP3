import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Torneo {
    private String nombre;
    private List<Jugador> jugadores = new ArrayList<>();
    private List<Emparejamiento> emparejamientos = new ArrayList<>();

    public Torneo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean agregarJugadores(List<Jugador> nuevosJugadores) {
        jugadores.addAll(nuevosJugadores);
        return true;
    }

    public void generarEmparejamientos() {
        emparejamientos.clear();
        Collections.shuffle(jugadores); // Mezclar jugadores aleatoriamente

        for (int i = 0; i < jugadores.size() - 1; i += 2) {
            emparejamientos.add(new Emparejamiento(jugadores.get(i), jugadores.get(i + 1)));
        }

        // Si hay un jugador sin pareja, se empareja con "Nadie"
        if (jugadores.size() % 2 != 0) {
            emparejamientos.add(new Emparejamiento(jugadores.get(jugadores.size() - 1), null));
        }
    }

    public List<Emparejamiento> getEmparejamientos() {
        return emparejamientos;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
}