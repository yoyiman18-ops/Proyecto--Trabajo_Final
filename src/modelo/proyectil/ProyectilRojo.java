package modelo.proyectil;

import java.util.ArrayList;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class ProyectilRojo extends Proyectil {

    public ProyectilRojo(
        VecDouble2D posicion,
        VecDouble2D velocidad,
        Hitbox hitbox,
        ArrayList<EstrategiaExpiracion> estrategiasExpiracion,
        ArrayList<Interaccion> interaccionesColision
    ) {
        super(
            4,
            "Proyectil Rojo",
            hitbox,
            posicion,
            velocidad,
            3,
            2,
            1,
            estrategiasExpiracion,
            interaccionesColision
        );
    }
}
