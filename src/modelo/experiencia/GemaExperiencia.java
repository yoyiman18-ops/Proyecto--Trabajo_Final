package modelo.experiencia;

import java.util.List;

import controlador.SonidoJuego;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class GemaExperiencia extends Experiencia {

    public GemaExperiencia(
        VecDouble2D posicion,
        Hitbox hitbox,
        double aceleracion,
        double friccion,
        double velocidadMax,
        double cantidadExp,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            50,
            "Gema Experiencia",
            hitbox,
            posicion,
            new VecDouble2D(0, 0),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            cantidadExp,
            "idle",
            "expirar",
            SonidoJuego.RECOGER_EXPERIENCIA.getId()
        );
    }
}
