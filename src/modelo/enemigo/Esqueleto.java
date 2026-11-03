package modelo.enemigo;

import java.util.ArrayList;
import java.util.List;

import controlador.SonidoJuego;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;

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
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            ID_GLOBAL_ESQUELETO,
            NOMBRE_ESQUELETO,
            hitbox,
            posicion,
            new VecDouble2D(0.0, 0.0),
            0.0,
            0.0,
            velocidadMax,
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
