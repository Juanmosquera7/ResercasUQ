package co.edu.uniquindio.reservasuq.modelo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class Reserva {
    private String id;
    private Instalacion instalacion;
    private LocalTime horaReserva;
    private LocalDate diaReserva;
    private Persona persona;
    private Horario horarioSeleccionado;
}



