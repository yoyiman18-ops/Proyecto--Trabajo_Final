package modelo.enemigo;

import java.util.ArrayList;
import java.util.List;

import controlador.SonidoJuego;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class Esqueleto extends Enemigo {

    public static final int ID_GLOBAL_ESQUELETO = 11;
    public static final String NOMBRE_ESQUELETO = "Esqueleto";
    public static final String ANIMACION_CAMINAR_ABAJO = "caminar_abajo";
    public static final double VELOCIDAD_MAXIMA_DEFAULT = 75.0;
    public static final double VIDA_MAXIMA_DEFAULT = 40.0;
    public static final double DANIO_DEFAULT = 10.0;
    public static final int EXPERIENCIA_DEFAULT = 10;
    public static final double INTERVALO_ATAQUE_DEFAULT = 1.0;
    public static final double DEFENSA_DEFAULT = 0.0;

    public Esqueleto(
        VecDouble2D posicion,
        Hitbox hitbox,
        double velocidadMax,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this(
            posicion,
            hitbox,
            velocidadMax,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            vidaMaxima,
            daño,
            experienciaRecompensa,
            objetivo,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
    }

    public Esqueleto(
        VecDouble2D posicion,
        Hitbox hitbox,
        double velocidadMax,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<Integer> notificadorSonido,
        Notificador<String> notificadorAnimacion,
        Notificador<Double> notificadorDaño,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            ID_GLOBAL_ESQUELETO,
            NOMBRE_ESQUELETO,
            hitbox,
            posicion,
            new VecDouble2D(0.0, 0.0),
            ACELERACION_DEFAULT,
            FRICCION_DEFAULT,
            velocidadMax,
            notificadorPosicion,
            notificadorInstanciacion,
            notificadorExpiracion,
            notificadorSonido,
            notificadorAnimacion,
            notificadorDaño,
            estrategiaMovimiento,
            estrategiasExpiracion != null ? estrategiasExpiracion : new ArrayList<>(),
            interaccionesColision != null ? interaccionesColision : new ArrayList<>(),
            ANIMACION_CAMINAR_ABAJO,
            null,
            SonidoJuego.EXPIRAR_ENEMIGO.getId(),
            vidaMaxima,
            DEFENSA_DEFAULT,
            SonidoJuego.DAÑO_ENEMIGO.getId(),
            daño,
            experienciaRecompensa,
            objetivo,
            INTERVALO_ATAQUE_DEFAULT
        );
    }
}
