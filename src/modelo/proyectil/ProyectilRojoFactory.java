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
        VecDouble2D velocidad,
        double dañoAdicional,
        double tiempoVidaAdicional,
        int penetracionAdicional
    ) {
        super(
            new HitboxRectangularFactory(16, 16, TipoHitbox.FANTASMA, categoriasColision, capasColision),
            posicion,
            velocidad,
            dañoAdicional,
            tiempoVidaAdicional,
            penetracionAdicional,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    @Override public Proyectil get() {
        return new ProyectilRojo(
            getPosicion(),
            getVelocidad(),
            getHitboxFactory().getHitbox(),
            getDañoAdicional(),
            getTiempoVidaAdicional(),
            getPenetracionAdicional(),
            getEstrategiasExpiracion(),
            getInteraccionesColision()
        );

    }
}
