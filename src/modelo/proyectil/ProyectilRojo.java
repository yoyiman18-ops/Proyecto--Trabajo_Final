package modelo.proyectil;

import java.util.List;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class ProyectilRojo extends Proyectil {

    public ProyectilRojo(
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        Hitbox hitbox,
        double dañoAdicional,
        double tiempoVidaAdicional,
        int penetracionAdicional,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            4,
            "Proyectil Rojo",
            hitbox,
            posicion,
            direccion,
            velocidad,
            0,
            0,
            1000,
            estrategiasExpiracion,
            interaccionesColision,
            1 + tiempoVidaAdicional,
            1 + penetracionAdicional,
            1 + dañoAdicional,
            "idle",
            "expirar"
        );

    }
}
