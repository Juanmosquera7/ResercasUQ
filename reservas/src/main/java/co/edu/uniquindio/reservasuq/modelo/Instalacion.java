package co.edu.uniquindio.reservasuq.modelo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
public class Instalacion {
    private String id;
    private String nombre;
    private int aforo;
    private float costo;
    private List<Horario> horarios;


    public boolean isDisponible(LocalDate dia, LocalTime hora) {

        for(Horario horario : horarios){

            if( horario.getDia().equals(dia.getDayOfWeek().name()) && horario.estaDentroDelHorario(hora) ){
                return true;
            }

        }

        return false;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

