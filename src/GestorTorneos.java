import java.util.ArrayList;
import java.util.List;

public class GestorTorneos {
    private List<Jugador> jugadores = new ArrayList<>();
    private List<Torneo> torneos = new ArrayList<>();

    public GestorTorneos() {
        // Jugadores predefinidos (solo 5)
        jugadores.add(new Jugador("Jugador 1", 25));
        jugadores.add(new Jugador("Jugador 2", 28));
        jugadores.add(new Jugador("Jugador 3", 22));
        jugadores.add(new Jugador("Jugador 4", 30));
        jugadores.add(new Jugador("Jugador 5", 27));
    }

    public boolean agregarJugador(Jugador jugador) {
        if (jugadores.size() < 12) {
            jugadores.add(jugador);
            return true;
        }
        return false;
    }

    public boolean crearTorneo(String nombre) {
        torneos.add(new Torneo(nombre));
        return true;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Torneo> getTorneos() {
        return torneos;
    }
}