import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private GestorTorneos gestorTorneos = new GestorTorneos();

    public MainFrame() {
        setTitle("Gestión de Torneos de Tenis");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Gestión de Jugadores", crearPanelJugadores());
        tabbedPane.addTab("Gestión de Torneos", crearPanelTorneos());
        tabbedPane.addTab("Emparejamientos", crearPanelEmparejamientos());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelJugadores() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = {"Nombre", "Edad"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaJugadores = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaJugadores);

        // Cargar jugadores predefinidos
        for (Jugador jugador : gestorTorneos.getJugadores()) {
            modeloTabla.addRow(new Object[]{jugador.getNombre(), jugador.getEdad()});
        }

        JPanel formulario = new JPanel(new GridLayout(3, 2));
        formulario.add(new JLabel("Nombre:"));
        JTextField campoNombre = new JTextField();
        formulario.add(campoNombre);

        formulario.add(new JLabel("Edad:"));
        JTextField campoEdad = new JTextField();
        formulario.add(campoEdad);

        JButton botonAgregar = new JButton("Agregar Jugador");
        formulario.add(botonAgregar);

        botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                int edad = Integer.parseInt(campoEdad.getText());

                if (gestorTorneos.agregarJugador(new Jugador(nombre, edad))) {
                    modeloTabla.addRow(new Object[]{nombre, edad});
                    JOptionPane.showMessageDialog(panel, "Jugador agregado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(panel, "No se pueden agregar más jugadores.");
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formulario, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelTorneos() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = {"Nombre del Torneo", "Equipo 1", "Equipo 2"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaTorneos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaTorneos);

        JPanel formulario = new JPanel(new GridLayout(2, 2));
        formulario.add(new JLabel("Nombre del Torneo:"));
        JTextField campoNombreTorneo = new JTextField();
        formulario.add(campoNombreTorneo);

        JButton botonCrearTorneo = new JButton("Crear Torneo");
        formulario.add(botonCrearTorneo);

        botonCrearTorneo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreTorneo = campoNombreTorneo.getText();

                if (gestorTorneos.getJugadores().size() < 8) {
                    JOptionPane.showMessageDialog(panel, "No hay suficientes jugadores para crear un torneo.");
                    return;
                }

                if (gestorTorneos.crearTorneo(nombreTorneo)) {
                    Torneo torneo = gestorTorneos.getTorneos().get(gestorTorneos.getTorneos().size() - 1);
                    List<Jugador> jugadoresSeleccionados = gestorTorneos.getJugadores().subList(0, 8);
                    torneo.agregarJugadores(jugadoresSeleccionados);

                    modeloTabla.addRow(new Object[]{
                        nombreTorneo,
                        torneo.getEquipo1().stream().map(Jugador::getNombre).collect(Collectors.toList()),
                        torneo.getEquipo2().stream().map(Jugador::getNombre).collect(Collectors.toList())
                    });

                    JOptionPane.showMessageDialog(panel, "Torneo creado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(panel, "No se pueden crear más torneos.");
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formulario, BorderLayout.NORTH);

        return panel;
    }

    private JPanel crearPanelEmparejamientos() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea areaEmparejamientos = new JTextArea();
        areaEmparejamientos.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaEmparejamientos);

        JButton botonGenerar = new JButton("Generar Emparejamientos");
        botonGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gestorTorneos.getTorneos().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "No hay torneos disponibles.");
                    return;
                }

                Torneo torneo = gestorTorneos.getTorneos().get(0);

                if (torneo.getEquipo1().isEmpty() || torneo.getEquipo2().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "El torneo no tiene equipos completos.");
                    return;
                }

                torneo.generarEmparejamientos();

                StringBuilder emparejamientos = new StringBuilder();
                for (Emparejamiento emparejamiento : torneo.getEmparejamientos()) {
                    emparejamientos.append(emparejamiento.getJugador1().getNombre())
                            .append(" vs ")
                            .append(emparejamiento.getJugador2().getNombre())
                            .append("\n");
                }

                areaEmparejamientos.setText(emparejamientos.toString());
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(botonGenerar, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}