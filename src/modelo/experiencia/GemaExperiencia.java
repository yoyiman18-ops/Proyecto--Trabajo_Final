package modelo.experiencia;

import java.util.List;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;

public class GemaExperiencia extends ExperienciaGema {

    public GemaExperiencia(
        VecDouble2D posicion,
        Hitbox hitbox,
        double cantidadExp,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(posicion, hitbox, cantidadExp, estrategiaMovimiento, estrategiasExpiracion, interaccionesColision);
    }

    public GemaExperiencia(
        VecDouble2D posicion,
        Hitbox hitbox,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<Integer> notificadorSonido,
        Notificador<String> notificadorAnimacion,
        double cantidadExp,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            posicion,
            hitbox,
            notificadorPosicion,
            notificadorInstanciacion,
            notificadorExpiracion,
            notificadorSonido,
            notificadorAnimacion,
            cantidadExp,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
    }
}
