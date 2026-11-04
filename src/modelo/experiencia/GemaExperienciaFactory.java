package modelo.experiencia;

import java.util.ArrayList;
import java.util.List;

import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class GemaExperienciaFactory extends ExperienciaFactory {

    public GemaExperienciaFactory(
        VecDouble2D posicion,
        double cantidadExp
    ) {
        this(
            MascaraColision.of(CategoriaColision.EXP),
            MascaraColision.of(CategoriaColision.JUGADOR),
            posicion,
            0.0,
            0.0,
            0.0,
            null,
            cantidadExp,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public GemaExperienciaFactory(
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        double cantidadExp,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            new HitboxRectangularFactory(24, 24, 16, 16, TipoHitbox.FANTASMA, categoriasColision, capasColision, true),
            posicion,
            new VecDouble2D(),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            cantidadExp
        );
    }

    @Override
    public Experiencia get() {
        return new GemaExperiencia(
            getPosicion(),
            getHitboxFactory().getHitbox(),
            getAceleracion(),
            getFriccion(),
            getVelocidadMax(),
            getCantidadExp(),
            getEstrategiaMovimiento(),
            getEstrategiasExpiracion(),
            getInteraccionesColision()
        );
    }
}
