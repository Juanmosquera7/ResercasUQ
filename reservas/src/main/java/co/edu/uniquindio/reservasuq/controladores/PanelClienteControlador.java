package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Persona;
import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.servicio.ServiciosReservasUQ;
import co.edu.uniquindio.reservasuq.modelo.Sesion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelClienteControlador {

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblCedula;

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, String> colIdInstalacion;

    @FXML
    private TableColumn<Reserva, String> colFecha;

    @FXML
    private TableColumn<Reserva, String> colHora;

    @FXML
    private Button btnCrearReserva;

    @FXML
    private Button btnCancelarReserva;

    private final ServiciosReservasUQ servicioReservas;

    public PanelClienteControlador() {
        this.servicioReservas = ControladorPrincipal.getInstancia(); // O obtener la instancia de servicios según tu diseño
    }

    @FXML
    public void initialize() {
        Persona persona = Sesion.getInstancia().getPersona();
        lblNombre.setText(persona.getNombre());
        lblCedula.setText("Cédula: " + persona.getCedula());

        // Inicializar columnas de la tabla
        colIdInstalacion.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getInstalacion().getId());
        });
        colFecha.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDiaReserva().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
        colHora.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getHoraReserva().toString());
        });

        cargarReservas(persona.getCedula()); // Cargar reservas al iniciar
    }

    public void cargarReservas(String cedula) {
        List<Reserva> reservas = servicioReservas.listarReservasPorPersona(cedula);
        tablaReservas.getItems().clear(); // Limpiar los elementos actuales de la tabla
        tablaReservas.getItems().addAll(reservas); // Añadir las reservas actualizadas
    }

    @FXML
    public void crearReserva() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crearReserva.fxml")); // Ruta del FXML para crear reserva
            Parent root = loader.load();

            // Obtener el controlador de la vista de creación de reserva
            CrearReservaControlador crearReservaControlador = loader.getController();
            crearReservaControlador.setPanelClienteControlador(this); // Pasar la referencia del panel de cliente

            Stage stage = new Stage();
            stage.setTitle("Crear Reserva");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Espera a que se cierre el diálogo

            // La actualización de la tabla se hace dentro del método confirmarReserva
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), "Error", Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void cancelarReserva() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cancelar_reserva.fxml")); // Ruta del FXML para cancelar reserva
            Parent root = loader.load();

            // Obtener el controlador de la vista de cancelación de reserva
            CancelarReservaControlador cancelarReservaControlador = loader.getController();

            // Pasar la referencia del controlador del panel cliente si es necesario
            cancelarReservaControlador.setPanelClienteControlador(this);

            Stage stage = new Stage();
            stage.setTitle("Cancelar Reserva");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Espera a que se cierre el diálogo

            // Opcionalmente recargar las reservas después de cancelar
            cargarReservas(Sesion.getInstancia().getPersona().getCedula()); // Cargar reservas nuevamente
        } catch (Exception e) {
            mostrarAlerta(e.getMessage(), "Error", Alert.AlertType.ERROR);
        }
    }






    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.show();
    }

    public void actualizarTablaReservas(String cedula) {
        cargarReservas(cedula);
    }

}



