package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Instalacion;
import co.edu.uniquindio.reservasuq.modelo.Horario;
import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.servicio.ServiciosReservasUQ;
import co.edu.uniquindio.reservasuq.modelo.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CrearReservaControlador {

    @FXML
    private ComboBox<Instalacion> comboInstalaciones;

    @FXML
    private ComboBox<Horario> comboHorarios;

    @FXML
    private DatePicker datePickerDia;

    @FXML
    private Label lblEstadoDisponibilidad;

    @FXML
    private Button btnConfirmarReserva;

    private final ServiciosReservasUQ servicioReservas;

    public CrearReservaControlador() {
        this.servicioReservas = ControladorPrincipal.getInstancia(); // O obtener la instancia de servicios según tu diseño
    }

    private PanelClienteControlador panelClienteControlador;

    public void setPanelClienteControlador(PanelClienteControlador panelClienteControlador) {
        this.panelClienteControlador = panelClienteControlador;
    }


    @FXML
    public void initialize() {
        cargarInstalaciones(); // Carga las instalaciones al iniciar

        // Configura el evento para cargar horarios al seleccionar una instalación
        comboInstalaciones.setOnAction(e -> {
            cargarHorarios();  // Cargar horarios de la instalación seleccionada
            verificarDisponibilidad(); // Verifica la disponibilidad de la instalación y horario seleccionados
        });

        comboHorarios.setOnAction(e -> verificarDisponibilidad()); // Verifica la disponibilidad al seleccionar un horario
    }


    private void cargarInstalaciones() {
        List<Instalacion> instalaciones = new ArrayList<>();

        // Biblioteca con horarios
        List<Horario> horariosBiblioteca = new ArrayList<>();
        horariosBiblioteca.add(Horario.builder().dia("Lunes").horaInicio("08:00").horaFin("18:00").build());
        horariosBiblioteca.add(Horario.builder().dia("Martes").horaInicio("08:00").horaFin("18:00").build());
        // Agrega más horarios si es necesario
        instalaciones.add(Instalacion.builder().id("1").nombre("Biblioteca").aforo(100).costo(0.0f).horarios(horariosBiblioteca).build());

        // Cancha de Fútbol con horarios
        List<Horario> horariosCancha = new ArrayList<>();
        horariosCancha.add(Horario.builder().dia("Lunes").horaInicio("10:00").horaFin("16:00").build());
        horariosCancha.add(Horario.builder().dia("Miércoles").horaInicio("10:00").horaFin("16:00").build());
        instalaciones.add(Instalacion.builder().id("2").nombre("Cancha de Fútbol").aforo(22).costo(0.0f).horarios(horariosCancha).build());

        // Salón de Clases con horarios
        List<Horario> horariosSalon = new ArrayList<>();
        horariosSalon.add(Horario.builder().dia("Lunes").horaInicio("09:00").horaFin("17:00").build());
        horariosSalon.add(Horario.builder().dia("Jueves").horaInicio("09:00").horaFin("17:00").build());
        instalaciones.add(Instalacion.builder().id("3").nombre("Salón de Clases").aforo(30).costo(0.0f).horarios(horariosSalon).build());

        // Laboratorio de Computación con horarios
        List<Horario> horariosLaboratorio = new ArrayList<>();
        horariosLaboratorio.add(Horario.builder().dia("Martes").horaInicio("08:00").horaFin("14:00").build());
        horariosLaboratorio.add(Horario.builder().dia("Viernes").horaInicio("08:00").horaFin("14:00").build());
        instalaciones.add(Instalacion.builder().id("4").nombre("Laboratorio de Computación").aforo(15).costo(0.0f).horarios(horariosLaboratorio).build());

        comboInstalaciones.getItems().addAll(instalaciones);
    }

    private void cargarHorarios() {
        // Limpia los horarios existentes en el ComboBox
        comboHorarios.getItems().clear();

        // Obtén la instalación seleccionada
        Instalacion instalacion = comboInstalaciones.getValue();

        // Verifica si hay una instalación seleccionada
        if (instalacion != null) {
            // Obtiene los horarios de la instalación seleccionada
            List<Horario> horarios = instalacion.getHorarios();

            // Verifica si hay horarios disponibles
            if (horarios != null && !horarios.isEmpty()) {
                // Agrega los horarios al ComboBox
                comboHorarios.getItems().addAll(horarios);
            } else {
                // Muestra un mensaje si no hay horarios
                mostrarAlerta("No hay horarios disponibles para la instalación seleccionada.", "Información", Alert.AlertType.INFORMATION);
            }
        } else {
            // Muestra un mensaje si no se seleccionó ninguna instalación
            mostrarAlerta("Por favor, seleccione una instalación.", "Error", Alert.AlertType.ERROR);
        }
    }










    private void verificarDisponibilidad() {
        Instalacion instalacion = comboInstalaciones.getValue();
        Horario horario = comboHorarios.getValue();
        LocalDate dia = datePickerDia.getValue();

        if (instalacion != null && horario != null && dia != null) {
            try {
                if (horario.getHoraInicio() != null && !horario.getHoraInicio().isEmpty()) {
                    LocalTime hora = LocalTime.parse(horario.getHoraInicio());

                    boolean disponible = servicioReservas.verificarDisponibilidad(instalacion, hora, dia);
                    lblEstadoDisponibilidad.setText(disponible ? "Disponible" : "No Disponible");
                } else {
                    lblEstadoDisponibilidad.setText("Horario no válido");
                }
            } catch (Exception e) {
                lblEstadoDisponibilidad.setText("Error al procesar el horario");
            }
        } else {
            lblEstadoDisponibilidad.setText("");
        }
    }

    @FXML
    public void confirmarReserva() {
        if (!validarSeleccion()) return; // Validación de selección de campos

        Instalacion instalacion = comboInstalaciones.getValue();
        Horario horario = comboHorarios.getValue();
        String cedulaPersona = Sesion.getInstancia().getPersona().getCedula(); // Obtener la cédula de la persona desde la sesión

        // Usamos el día del horario seleccionado
        LocalDate dia = obtenerDiaDeHorario(horario);

        try {
            Reserva reserva = servicioReservas.crearReserva(instalacion.getId(), cedulaPersona, dia, horario.getHoraInicio());
            mostrarAlerta("Reserva confirmada con éxito. ID: " + reserva.getId(), "Éxito", Alert.AlertType.INFORMATION);

            // Actualiza la tabla de reservas en el panel de cliente
            if (panelClienteControlador != null) {
                panelClienteControlador.actualizarTablaReservas(cedulaPersona);

                // Cargar las reservas en el ComboBox directamente
                List<Reserva> reservas = servicioReservas.listarReservasPorPersona(cedulaPersona); // Obtener las reservas de la persona
                panelClienteControlador.cargarReservas(reservas.toString()); // Cargar las reservas en el ComboBox
            }
        } catch (Exception e) {
            mostrarAlerta("Error al crear la reserva: " + e.getMessage(), "Error", Alert.AlertType.ERROR);
        }
    }



    private LocalDate obtenerDiaDeHorario(Horario horario) {
        // Aquí se implementa la lógica para convertir el día del horario a un LocalDate
        // Suponiendo que ya tienes un mapeo de días a LocalDate, por ejemplo:
        LocalDate diaActual = LocalDate.now(); // Puedes ajustar esto según tu lógica
        switch (horario.getDia()) {
            case "Lunes":
                return diaActual.with(DayOfWeek.MONDAY);
            case "Martes":
                return diaActual.with(DayOfWeek.TUESDAY);
            case "Miércoles":
                return diaActual.with(DayOfWeek.WEDNESDAY);
            case "Jueves":
                return diaActual.with(DayOfWeek.THURSDAY);
            case "Viernes":
                return diaActual.with(DayOfWeek.FRIDAY);
            case "Sábado":
                return diaActual.with(DayOfWeek.SATURDAY);
            case "Domingo":
                return diaActual.with(DayOfWeek.SUNDAY);
            default:
                return diaActual; // Default al día actual si no coincide
        }
    }

    private boolean validarSeleccion() {
        if (comboInstalaciones.getValue() == null) {
            mostrarAlerta("Por favor, seleccione una instalación.", "Error", Alert.AlertType.ERROR);
            return false;
        }
        if (comboInstalaciones.getValue() == null) {
            mostrarAlerta("Por favor, seleccione un horario.", "Error", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }





    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.show();
    }
}



