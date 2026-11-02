package motor.colisiones;

import java.util.ArrayList;
import java.util.List;
import motor.modelo.Interactuable;
import motor.modelo.Interaccion;

/**
 * Implementación simple de {@link ResolvedorInteraccion} que clona/copia las listas
 * de interacciones de ambos interactuables al momento de la resolución para evitar
 * condiciones de carrera (*race conditions*), y las itera de forma respectiva sobre el otro interactuable.
 */
public class ResolvedorInteraccionSimple implements ResolvedorInteraccion {

    @Override
    public void resolverInteracciones(Interactuable i1, Interactuable i2) {
        if (i1 == null || i2 == null) {
            return;
        }

        List<Interaccion> interacciones1 = i1.getInteracciones();
        List<Interaccion> interacciones2 = i2.getInteracciones();

        // Se clonan las listas para capturar el estado exacto del momento del choque
        // y evitar modificaciones concurrentes durante la ejecución de efectos.
        List<Interaccion> copia1 = (interacciones1 != null) ? new ArrayList<>(interacciones1) : List.of();
        List<Interaccion> copia2 = (interacciones2 != null) ? new ArrayList<>(interacciones2) : List.of();

        // Se iteran de forma respectiva:
        // i2 reacciona a cada interacción de i1
        for (Interaccion inter : copia1) {
            if (inter != null) {
                i2.interactuar(inter);
            }
        }

        // i1 reacciona a cada interacción de i2
        for (Interaccion inter : copia2) {
            if (inter != null) {
                i1.interactuar(inter);
            }
        }
    }
}
