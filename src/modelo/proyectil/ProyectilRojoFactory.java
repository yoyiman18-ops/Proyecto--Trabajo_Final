package modelo.proyectil;
import java.util.ArrayList;

import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.util.VecDouble2D;

public class ProyectilRojoFactory extends ProyectilFactory {

    public ProyectilRojoFactory(
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        VecDouble2D posicion,
        VecDouble2D velocidad
    ) {
        super(
            4,
            "Proyectil Rojo",
            new HitboxRectangularFactory(16, 16, TipoHitbox.FANTASMA, categoriasColision, capasColision),
            posicion,
            velocidad,
            3,
            2,
            1,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    @Override public Proyectil get() {
        return new Proyectil(
            getIdGlobal(),
            getNombre(),
            getHitboxFactory().getHitbox(),
            getPosicion(),
            getVelocidad(),
            getTiempoVida(),
            getPenetracion(),
            getDaño(),
            getEstrategiasExpiracion(),
            getInteraccionesColision()
        );

    }
}
