package co.edu.uniquindio.reservasuq.modelo;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Horario {
    private String dia;        // Día de la semana (e.g., "Lunes")
    private String horaInicio;  // Hora de inicio (e.g., "09:00")
    private String horaFin;     // Hora de fin (e.g., "18:00")

    public boolean estaDentroDelHorario(LocalTime hora) {
        LocalTime inicio = LocalTime.parse(horaInicio);
        LocalTime fin = LocalTime.parse(horaFin);
        return !hora.isBefore(inicio) && !hora.isAfter(fin);
    }

    @Override
    public String toString() {
        return dia + " " + horaInicio + " - " + horaFin;
    }

}

