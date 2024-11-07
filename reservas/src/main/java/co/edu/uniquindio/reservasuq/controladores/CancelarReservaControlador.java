package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.servicio.ServiciosReservasUQ;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;

public class CancelarReservaControlador {

    @FXML
    private ComboBox<Reserva> comboReservas; // ComboBox para seleccionar la reserva

    private final ServiciosReservasUQ servicioReservas; // Instancia del servicio de reservas

    // Agrega esta variable para mantener la referencia al controlador del panel
    private PanelClienteControlador panelClienteControlador;

    public CancelarReservaControlador() {
        this.servicioReservas = ControladorPrincipal.getInstancia(); // Obtener la instancia de servicios
    }

    // Método para establecer el controlador del panel cliente
    public void setPanelClienteControlador(PanelClienteControlador panelClienteControlador) {
        this.panelClienteControlador = panelClienteControlador; // Guardar la referencia
    }

    // Método para cargar las reservas en el ComboBox.
    public void cargarReservas(List<Reserva> reservas) {
        comboReservas.getItems().clear(); // Limpiar el ComboBox
        comboReservas.getItems().addAll(reservas); // Agregar todas las reservas
    }

    // Método para cancelar la reserva seleccionada.
    @FXML
    public void cancelarReserva() {
        Reserva reservaSeleccionada = comboReservas.getValue(); // Obtener la reserva seleccionada

        if (reservaSeleccionada != null) {
            try {
                servicioReservas.cancelarReserva(reservaSeleccionada.getId()); // Llamada al método de cancelación
                mostrarAlerta("Reserva cancelada con éxito.", "Éxito", Alert.AlertType.INFORMATION);

                // Aquí podrías comunicarte con el panelClienteControlador si es necesario.
                // panelClienteControlador.cargarReservas(); // Actualizar reservas si fuera necesario

                // Cerrar la ventana después de cancelar
                cerrarVentana();

            } catch (Exception e) {
                mostrarAlerta("Error al cancelar la reserva: " + e.getMessage(), "Error", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Seleccione una reserva para cancelar.", "Advertencia", Alert.AlertType.WARNING);
        }
    }

    // Método para cerrar la ventana de cancelación de reserva.
    @FXML
    public void regresar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) comboReservas.getScene().getWindow();
        stage.close();
    }





    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}




