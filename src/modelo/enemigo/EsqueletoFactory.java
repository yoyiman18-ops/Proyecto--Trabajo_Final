package modelo.enemigo;

import java.util.ArrayList;
import java.util.List;

import modelo.EstrategiaExpiracionSpawnear;
import modelo.experiencia.GemaExperienciaFactory;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.EstrategiaMovimientoPersecucion;
import motor.modelo.Interaccion;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;

public class EsqueletoFactory extends EnemigoFactory {

    public EsqueletoFactory() {
        this(
            MascaraColision.of(CategoriaColision.ENEMIGO),
            MascaraColision.of(
                CategoriaColision.JUGADOR,
                CategoriaColision.ALIADO,
                CategoriaColision.ENEMIGO,
                CategoriaColision.MURO
            ),
            new VecDouble2D(0.0, 0.0),
            600.0,
            600.0,
            75.0,
            40.0,
            10.0,
            10,
            null,
            1.0,
            new EstrategiaMovimientoPersecucion(null),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public EsqueletoFactory(
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        double intervaloAtaque,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            new HitboxRectangularFactory(
                28.0,
                36.0,
                TipoHitbox.SOLIDA,
                categoriasColision,
                capasColision
            ),
            posicion,
            aceleracion,
            friccion,
            velocidadMax,
            vidaMaxima,
            daño,
            experienciaRecompensa,
            objetivo,
            intervaloAtaque,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
    }

    @Override
    public Enemigo get() {
        List<EstrategiaExpiracion> estrategias = new ArrayList<>();
        if (getExperienciaRecompensa() > 0) {
            estrategias.add(new EstrategiaExpiracionSpawnear(
                List.of(new GemaExperienciaFactory(getPosicion(), getExperienciaRecompensa()).get())
            ));
        }
        estrategias.addAll(getEstrategiasExpiracion());

        return new Esqueleto(
            getPosicion(),
            getHitboxFactory().getHitbox(),
            getAceleracion(),
            getFriccion(),
            getVelocidadMax(),
            getVidaMaxima(),
            getDanio(),
            getIntervaloAtaque(),
            getEstrategiaMovimiento(),
            estrategias,
            getInteraccionesColision()
        );
    }
}
