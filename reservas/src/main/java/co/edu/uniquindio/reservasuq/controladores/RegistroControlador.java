package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Persona;
import co.edu.uniquindio.reservasuq.modelo.TipoPersona;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;

public class RegistroControlador {

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> comboTipoPersona;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    private final ControladorPrincipal controladorPrincipal;

    public RegistroControlador() {
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    @FXML
    public void initialize() {
        // Inicializar el ComboBox con los valores de TipoPersona
        comboTipoPersona.setItems(FXCollections.observableArrayList(
                TipoPersona.DOCENTE.name(),
                TipoPersona.ESTUDIANTE.name(),
                TipoPersona.ADMIN.name(),
                TipoPersona.EXTERNA.name()
        ));
    }

    @FXML
    public void registrarPersona() {
        try {
            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            String tipoPersonaStr = comboTipoPersona.getValue();
            String email = txtEmail.getText();
            String password = txtPassword.getText();

            // Validación básica de los campos
            if (cedula.isEmpty() || nombre.isEmpty() || tipoPersonaStr == null || email.isEmpty() || password.isEmpty()) {
                mostrarAlerta("Todos los campos son obligatorios", "Error de Registro", Alert.AlertType.ERROR);
                return;
            }

            // Conversión del tipo de persona
            TipoPersona tipoPersona = TipoPersona.valueOf(tipoPersonaStr.toUpperCase());

            // Llamada al método de ControladorPrincipal para registrar
            controladorPrincipal.registrarPersona(cedula, nombre, tipoPersona, email, password);
            mostrarAlerta("Registro exitoso", "Persona registrada", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error al registrar la persona: " + e.getMessage(), "Error", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void limpiarCampos() {
        txtCedula.clear();
        txtNombre.clear();
        comboTipoPersona.getSelectionModel().clearSelection();
        txtEmail.clear();
        txtPassword.clear();
    }
}



