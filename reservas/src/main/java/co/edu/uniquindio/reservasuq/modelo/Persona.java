package co.edu.uniquindio.reservasuq.modelo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Persona {
    private String cedula;
    private String nombre;
    private TipoPersona tipoPersona; // Enum para tipo de persona (e.g., CLIENTE, ADMIN)
    private String email;
    private String password;
}

