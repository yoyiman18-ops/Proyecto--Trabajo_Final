package modelo.enemigo;

import java.util.ArrayList;
import java.util.List;

import controlador.SonidoJuego;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class Esqueleto extends Enemigo {

    public Esqueleto(
        VecDouble2D posicion,
        Hitbox hitbox,
        double aceleracion,
        double friccion,
        double velocidadMax,
        double vidaMaxima,
        double daño,
        double intervaloAtaque,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this(
            posicion,
            hitbox,
            aceleracion,
            friccion,
            velocidadMax,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            vidaMaxima,
            daño,
            intervaloAtaque,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
    }

    public Esqueleto(
        VecDouble2D posicion,
        Hitbox hitbox,
        double aceleracion,
        double friccion,
        double velocidadMax,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<Integer> notificadorSonido,
        Notificador<String> notificadorAnimacion,
        Notificador<Double> notificadorDaño,
        double vidaMaxima,
        double daño,
        double intervaloAtaque,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            11,
            "Esqueleto",
            hitbox,
            posicion,
            new VecDouble2D(0.0, 0.0),
            aceleracion,
            friccion,
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
            "caminar_abajo",
            null,
            SonidoJuego.EXPIRAR_ENEMIGO.getId(),
            vidaMaxima,
            0.0,
            SonidoJuego.DAÑO_ENEMIGO.getId(),
            daño,
            intervaloAtaque
        );
    }
}
