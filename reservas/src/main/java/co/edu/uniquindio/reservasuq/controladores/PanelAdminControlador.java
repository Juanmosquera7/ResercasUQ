package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.servicio.ServiciosReservasUQ;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PanelAdminControlador {

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, String> colIdInstalacion;

    @FXML
    private TableColumn<Reserva, String> colCedulaCliente;

    @FXML
    private TableColumn<Reserva, String> colFecha;

    @FXML
    private TableColumn<Reserva, String> colHora;

    @FXML
    private Button btnGestionarReservas;

    @FXML
    private Button btnGestionarInstalaciones;

    @FXML
    private Button btnVerReportes;

    private final ServiciosReservasUQ servicioReservas;

    public PanelAdminControlador() {
        this.servicioReservas = ControladorPrincipal.getInstancia(); // O obtener la instancia de servicios según tu diseño
    }

    @FXML
    public void initialize() {
        // Inicializar columnas de la tabla
        colIdInstalacion.setCellValueFactory(new PropertyValueFactory<>("idInstalacion"));
        colCedulaCliente.setCellValueFactory(new PropertyValueFactory<>("cedulaCliente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        cargarReservas();
    }

    private void cargarReservas() {
        List<Reserva> reservas = servicioReservas.listarTodasReservas();
        tablaReservas.getItems().clear();
        tablaReservas.getItems().addAll(reservas);
    }

    @FXML
    public void gestionarReservas() {
        // Lógica para gestionar reservas
        // Aquí puedes abrir un nuevo diálogo o ventana para manejar las reservas
        System.out.println("Gestionar Reservas");
    }

    @FXML
    public void gestionarInstalaciones() {
        // Lógica para gestionar instalaciones
        // Aquí puedes abrir un nuevo diálogo o ventana para manejar las instalaciones
        System.out.println("Gestionar Instalaciones");
    }

    @FXML
    public void verReportes() {
        // Lógica para ver reportes
        // Aquí puedes abrir un nuevo diálogo o ventana para mostrar reportes
        System.out.println("Ver Reportes");
    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.show();
    }
}

