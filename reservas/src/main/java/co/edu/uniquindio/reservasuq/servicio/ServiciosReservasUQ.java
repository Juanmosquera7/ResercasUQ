package co.edu.uniquindio.reservasuq.servicio;


import co.edu.uniquindio.reservasuq.modelo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ServiciosReservasUQ {


    Persona login(String correo, String contrasena) throws Exception;


    void registrarPersona(String cedula, String nombre, TipoPersona tipoPersona, String email, String password) throws Exception;


    void crearInstalacion(String nombre, int aforo, float costo, List<Horario> horarios) throws Exception;


    Reserva crearReserva(String idInstalacion, String cedulaPersona, LocalDate diaReserva, String horaReserva) throws Exception;


    List<Reserva> listarTodasReservas();


    List<Reserva> listarReservasPorPersona(String cedulaPersona);

    List<Instalacion> listarTodasInstalaciones();

    boolean verificarDisponibilidad(Instalacion instalacion, LocalTime hora, LocalDate dia);

    void cancelarReserva(String idReserva) throws Exception;  // Método agregado para cancelar una reserva


}

