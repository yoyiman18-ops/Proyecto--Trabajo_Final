package modelo.proyectil;

import java.util.ArrayList;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.EstrategiaMovimiento;
import motor.util.VecDouble2D;

public class ProyectilRojoFactory extends ProyectilFactory {

    public ProyectilRojoFactory(
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        double tiempoVidaAdicional,
        int penetracionAdicional,
        double dañoAdicional
    ) {
        super(
            new HitboxRectangularFactory(24, 24, 16, 16, TipoHitbox.SOLIDA, categoriasColision, capasColision, true),
            posicion,
            aceleracion,
            friccion,
            velocidadMax,
            estrategiaMovimiento,
            tiempoVidaAdicional,
            penetracionAdicional,
            dañoAdicional,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    @Override
    public Proyectil get() {
        return new ProyectilRojo(
            getPosicion(),
            getHitboxFactory().getHitbox(),
            getAceleracion(),
            getFriccion(),
            getVelocidadMax(),
            getEstrategiaMovimiento(),
            getDañoAdicional(),
            getTiempoVidaAdicional(),
            getPenetracionAdicional(),
            getEstrategiasExpiracion(),
            getInteraccionesColision()
        );
    }
}
