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
        VecDouble2D direccion,
        double velocidad,
        double dañoAdicional,
        double tiempoVidaAdicional,
        int penetracionAdicional
    ) {
        this(
            categoriasColision,
            capasColision,
            posicion,
            direccion,
            velocidad,
            dañoAdicional,
            tiempoVidaAdicional,
            penetracionAdicional,
            "expirar"
        );
    }

    public ProyectilRojoFactory(
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double dañoAdicional,
        double tiempoVidaAdicional,
        int penetracionAdicional,
        String animacionExpirar
    ) {
        super(
            new HitboxRectangularFactory(16, 16, TipoHitbox.FANTASMA, categoriasColision, capasColision),
            posicion,
            direccion,
            velocidad,
            dañoAdicional,
            tiempoVidaAdicional,
            penetracionAdicional,
            new ArrayList<>(),
            new ArrayList<>(),
            animacionExpirar
        );
    }

    @Override public Proyectil get() {
        return new ProyectilRojo(
            getPosicion(),
            getDireccion(),
            getVelocidadDouble(),
            getHitboxFactory().getHitbox(),
            getDañoAdicional(),
            getTiempoVidaAdicional(),
            getPenetracionAdicional(),
            getEstrategiasExpiracion(),
            getInteraccionesColision(),
            getAnimacionExpirar()
        );

    }
}
