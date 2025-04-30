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

        // Crear el modelo de lista compartido
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        for (Jugador jugador : gestorTorneos.getJugadores()) {
            modeloLista.addElement(jugador.getNombre());
        }

        tabbedPane.addTab("Gestión de Jugadores", crearPanelJugadores(modeloLista));
        tabbedPane.addTab("Gestión de Torneos", crearPanelTorneos(modeloLista));
        tabbedPane.addTab("Emparejamientos", crearPanelEmparejamientos());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelJugadores(DefaultListModel<String> modeloLista) {
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
                int edad;

                try {
                    edad = Integer.parseInt(campoEdad.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "La edad debe ser un número válido.");
                    return;
                }

                if (gestorTorneos.agregarJugador(new Jugador(nombre, edad))) {
                    modeloTabla.addRow(new Object[]{nombre, edad});
                    modeloLista.addElement(nombre); // Actualizar la lista de jugadores
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

    private JPanel crearPanelTorneos(DefaultListModel<String> modeloLista) {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = {"Nombre del Torneo", "Jugadores"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaTorneos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaTorneos);

        JList<String> listaJugadores = new JList<>(modeloLista);
        listaJugadores.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollJugadores = new JScrollPane(listaJugadores);

        JPanel formulario = new JPanel(new GridLayout(3, 2));
        formulario.add(new JLabel("Nombre del Torneo:"));
        JTextField campoNombreTorneo = new JTextField();
        formulario.add(campoNombreTorneo);

        formulario.add(new JLabel("Seleccionar Jugadores (máx. 8):"));
        formulario.add(scrollJugadores);

        JButton botonCrearTorneo = new JButton("Crear Torneo");
        formulario.add(botonCrearTorneo);

        botonCrearTorneo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreTorneo = campoNombreTorneo.getText();
                List<String> jugadoresSeleccionados = listaJugadores.getSelectedValuesList();

                if (jugadoresSeleccionados.size() > 8) {
                    JOptionPane.showMessageDialog(panel, "No puedes seleccionar más de 8 jugadores.");
                    return;
                }

                if (jugadoresSeleccionados.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Debes seleccionar al menos un jugador.");
                    return;
                }

                if (gestorTorneos.crearTorneo(nombreTorneo)) {
                    Torneo torneo = gestorTorneos.getTorneos().get(gestorTorneos.getTorneos().size() - 1);

                    // Agregar jugadores seleccionados al torneo
                    List<Jugador> jugadoresTorneo = gestorTorneos.getJugadores().stream()
                            .filter(jugador -> jugadoresSeleccionados.contains(jugador.getNombre()))
                            .collect(Collectors.toList());
                    torneo.agregarJugadores(jugadoresTorneo);

                    modeloTabla.addRow(new Object[]{
                        nombreTorneo,
                        jugadoresSeleccionados
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

                StringBuilder emparejamientosTexto = new StringBuilder();

                for (Torneo torneo : gestorTorneos.getTorneos()) {
                    if (torneo.getJugadores().isEmpty()) {
                        emparejamientosTexto.append("El torneo '")
                                .append(torneo.getNombre())
                                .append("' no tiene jugadores.\n\n");
                        continue;
                    }

                    torneo.generarEmparejamientos();

                    emparejamientosTexto.append("Emparejamientos de ")
                            .append(torneo.getNombre())
                            .append(":\n");

                    for (Emparejamiento emparejamiento : torneo.getEmparejamientos()) {
                        emparejamientosTexto.append(emparejamiento.getJugador1().getNombre())
                                .append(" vs ")
                                .append(emparejamiento.getJugador2() != null ? emparejamiento.getJugador2().getNombre() : "Nadie")
                                .append("\n");
                    }

                    emparejamientosTexto.append("\n");
                }

                areaEmparejamientos.setText(emparejamientosTexto.toString());
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