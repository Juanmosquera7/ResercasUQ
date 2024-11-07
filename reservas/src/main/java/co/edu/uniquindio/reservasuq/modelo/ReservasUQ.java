package co.edu.uniquindio.reservasuq.modelo;

import co.edu.uniquindio.reservasuq.servicio.ServiciosReservasUQ;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservasUQ implements ServiciosReservasUQ {

    private List<Persona> personasRegistradas; // Lista para manejar personas
    private List<Instalacion> instalaciones; // Lista para manejar instalaciones
    private List<Reserva> reservas; // Lista para manejar reservas

    public ReservasUQ() {
        personasRegistradas = new ArrayList<>(); // Inicializa la lista de personas registradas
        instalaciones = new ArrayList<>(); // Inicializa la lista de instalaciones
        reservas = new ArrayList<>(); // Inicializa la lista de reservas
        cargarInstalaciones();
    }

    private void cargarInstalaciones() {
        instalaciones.add(Instalacion.builder()
                .id("1")
                .nombre("Biblioteca")
                .aforo(100)
                .costo(0.0f)
                .horarios(new ArrayList<>())
                .build());
        instalaciones.add(Instalacion.builder()
                .id("2")
                .nombre("Cancha de Fútbol")
                .aforo(22)
                .costo(0.0f)
                .horarios(new ArrayList<>())
                .build());
        instalaciones.add(Instalacion.builder()
                .id("3")
                .nombre("Salón de Clases")
                .aforo(30)
                .costo(0.0f)
                .horarios(new ArrayList<>())
                .build());
        instalaciones.add(Instalacion.builder()
                .id("4")
                .nombre("Laboratorio de Computación")
                .aforo(15)
                .costo(0.0f)
                .horarios(new ArrayList<>())
                .build());
    }

    @Override
    public Persona login(String correo, String contrasena) throws Exception {
        for (Persona persona : personasRegistradas) {
            if (persona.getEmail().equals(correo) && persona.getPassword().equals(contrasena)) {
                return persona; // Retorna la persona si el correo y la contraseña son correctos
            }
        }
        throw new Exception("Correo o contraseña incorrectos.");
    }

    @Override
    public void registrarPersona(String cedula, String nombre, TipoPersona tipoPersona, String email, String password) throws Exception {
        // Validación de cédula
        for (Persona persona : personasRegistradas) {
            if (persona.getCedula().equals(cedula)) {
                throw new Exception("La cédula ya está registrada.");
            }
        }

        if (cedula.length() < 5 || !cedula.matches("\\d+")) {
            throw new Exception("La cédula debe tener al menos 5 caracteres y contener solo números.");
        }

        // Crear y registrar la nueva persona
        Persona persona = Persona.builder()
                .cedula(cedula)
                .nombre(nombre)
                .tipoPersona(tipoPersona)
                .email(email) // Se puede mantener, ya que no se valida
                .password(password) // Se puede mantener, ya que no se valida
                .build();

        personasRegistradas.add(persona);
    }




    @Override
    public void crearInstalacion(String nombre, int aforo, float costo, List<Horario> horarios) throws Exception {
        if (nombre == null || nombre.isEmpty()) {
            throw new Exception("El nombre de la instalación es obligatorio.");
        }
        if (aforo <= 0) {
            throw new Exception("El aforo debe ser mayor que cero.");
        }
        if (costo < 0) {
            throw new Exception("El costo no puede ser negativo.");
        }

        Instalacion instalacion = Instalacion.builder()
                .nombre(nombre)
                .aforo(aforo)
                .costo(costo)
                .horarios(horarios)
                .build();

        instalaciones.add(instalacion); // Agrega la instalación a la lista
    }

    @Override
    public Reserva crearReserva(String idInstalacion, String cedulaPersona, LocalDate diaReserva, String horaReserva) throws Exception {
        // Validar si la instalación existe
        Instalacion instalacion = obtenerInstalacion(idInstalacion);
        if (instalacion == null) {
            throw new Exception("La instalación especificada no existe.");
        }

        // Validar si la persona existe
        Persona persona = obtenerPersona(cedulaPersona);
        if (persona == null) {
            throw new Exception("La persona con la cédula proporcionada no está registrada.");
        }

        // Validar que el día de la reserva coincide con el día del horario seleccionado





        // Verificar si ya existe una reserva para la misma instalación, fecha y hora
        for (Reserva reserva : reservas) {
            if (reserva.getInstalacion().getId().equals(idInstalacion) &&
                    reserva.getDiaReserva().isEqual(diaReserva)){

                throw new Exception("Ya existe una reserva para esta instalación en la fecha y hora solicitada.");
            }
        }

        // Crear la nueva reserva
        Reserva reserva = Reserva.builder()
                .id(generarIdReserva()) // Llama a un método para generar un ID único
                .instalacion(instalacion)
                .persona(persona)
                .diaReserva(diaReserva)

                .build();

        // Guardar la reserva en la lista de reservas
        reservas.add(reserva);
        return reserva;
    }

    // Método auxiliar para obtener el horario seleccionado por su hora
    private Horario obtenerHorarioPorHora(String hora, List<Horario> horarios) {
        for (Horario horario : horarios) {
            if (horario.getHoraInicio().equals(hora)) {
                return horario;
            }
        }
        return null; // Si no se encuentra el horario, devuelve null
    }



    // Método para generar un ID único de reserva (puede ser basado en la fecha, hora o un contador).
    private String generarIdReserva() {
        return "RES-" + (reservas.size() + 1);
    }



    public Instalacion obtenerInstalacion(String id){
        for(Instalacion i:instalaciones){
            if(i.getId().equals(id)){
                return i;
            }
        }
        return null;
    }

    public Persona obtenerPersona(String cedula) {
        for (Persona p : personasRegistradas) {
            if (p.getCedula().equals(cedula)) {
                return p;
            }
        }
        return null;
    }

    public Horario obtenerHorario(Instalacion instalacion, String horaInicio) {
        for (Horario h : instalacion.getHorarios()) {
            if (h.getHoraInicio().equals(horaInicio)) {
                return h;
            }
        }
        return null;
    }

    @Override
    public List<Reserva> listarTodasReservas() {
        return new ArrayList<>(reservas); // Retorna una copia de la lista de reservas
    }

    @Override
    public List<Reserva> listarReservasPorPersona(String cedulaPersona) {
        List<Reserva> reservasPorPersona = new ArrayList<>(); // Crear una lista para almacenar las reservas filtradas

        // Iterar a través de todas las reservas
        for (Reserva reserva : reservas) {
            // Comprobar si la cédula de la persona asociada a la reserva coincide con la cédula proporcionada
            if (reserva.getPersona().getCedula().equals(cedulaPersona)) {
                // Aquí podrías agregar lógica adicional si necesitas filtrar o modificar las reservas antes de añadirlas
                reservasPorPersona.add(reserva); // Agregar la reserva a la lista filtrada
            }
        }

        // Retorna las reservas correspondientes a la persona
        return reservasPorPersona;
    }



    @Override
    public List<Instalacion> listarTodasInstalaciones() {
        return instalaciones; // Devuelve la lista de instalaciones
    }

    public boolean verificarDisponibilidad(Instalacion instalacion, LocalTime hora, LocalDate dia) {

        for (Reserva reserva : reservas) {
            // Verifica si la instalación y el día coinciden y si la hora solicitada es igual a la hora de inicio de la reserva
            if (reserva.getInstalacion().equals(instalacion) &&
                    reserva.getDiaReserva().isEqual(dia) && instalacion.isDisponible(dia, hora)) {
                return false; // No hay disponibilidad
            }
        }
        return true;
    }


    @Override
    public void cancelarReserva(String idReserva) throws Exception {
        Reserva reserva = reservas.stream()
                .filter(r -> r.getId().equals(idReserva))
                .findFirst()
                .orElseThrow(() -> new Exception("Reserva no encontrada"));

        // Aquí se podría realizar lógica adicional para validar la cancelación
        reservas.remove(reserva); // Elimina la reserva de la lista
    }


}


