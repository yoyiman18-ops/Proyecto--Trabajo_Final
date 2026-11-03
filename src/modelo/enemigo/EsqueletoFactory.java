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
import motor.modelo.EstrategiaMovimientoPersecucion;
import motor.modelo.Interaccion;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;

public class EsqueletoFactory extends EnemigoFactory {

    public static final double ANCHO_HITBOX_ESQUELETO = 28.0;
    public static final double ALTO_HITBOX_ESQUELETO = 36.0;
    public static final double ACELERACION_DEFAULT = 600.0;
    public static final double FRICCION_DEFAULT = 600.0;
    public static final double VELOCIDAD_MAXIMA_DEFAULT = 75.0;
    public static final double VIDA_MAXIMA_DEFAULT = 40.0;
    public static final double DANIO_DEFAULT = 10.0;
    public static final int EXPERIENCIA_DEFAULT = 10;
    public static final double INTERVALO_ATAQUE_DEFAULT = 1.0;

    public EsqueletoFactory() {
        this(
            new VecDouble2D(0.0, 0.0),
            VELOCIDAD_MAXIMA_DEFAULT,
            VIDA_MAXIMA_DEFAULT,
            DANIO_DEFAULT,
            EXPERIENCIA_DEFAULT,
            null
        );
    }

    public EsqueletoFactory(Posicionable objetivo) {
        this(
            new VecDouble2D(0.0, 0.0),
            VELOCIDAD_MAXIMA_DEFAULT,
            VIDA_MAXIMA_DEFAULT,
            DANIO_DEFAULT,
            EXPERIENCIA_DEFAULT,
            objetivo
        );
    }

    public EsqueletoFactory(
        VecDouble2D posicion,
        double velocidadMax,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo
    ) {
        this(
            MascaraColision.of(CategoriaColision.ENEMIGO),
            MascaraColision.of(
                CategoriaColision.JUGADOR,
                CategoriaColision.ALIADO,
                CategoriaColision.ENEMIGO,
                CategoriaColision.MURO
            ),
            posicion,
            ACELERACION_DEFAULT,
            FRICCION_DEFAULT,
            velocidadMax,
            vidaMaxima,
            daño,
            experienciaRecompensa,
            objetivo,
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
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            new HitboxRectangularFactory(
                ANCHO_HITBOX_ESQUELETO,
                ALTO_HITBOX_ESQUELETO,
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
            INTERVALO_ATAQUE_DEFAULT,
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
            getExperienciaRecompensa(),
            getObjetivo(),
            getIntervaloAtaque(),
            new EstrategiaMovimientoPersecucion(getObjetivo()),
            estrategias,
            getInteraccionesColision()
        );
    }
}
