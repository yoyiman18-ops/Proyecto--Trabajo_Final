package modelo.enemigo;

import java.util.List;

import modelo.Personaje;
import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Direccion;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.EstrategiaMovimientoPersecucion;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class Enemigo extends Personaje {

    private double daño;
    private int experienciaRecompensa;
    private Posicionable objetivo;
    private double intervaloAtaque;
    private double tiempoHastaAtaque;
    private boolean contactoConObjetivo;
    private Direccion direccion;
    private String animacionActual;

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<Integer> notificadorSonido,
        Notificador<String> notificadorAnimacion,
        Notificador<Double> notificadorDaño,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault,
        String animacionExpirar,
        Integer idSonidoExpiracion,
        double vidaMaxima,
        double defensaMaximaPorcentual,
        Integer idSonidoDaño,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        double intervaloAtaque
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad,
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
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion,
            vidaMaxima,
            defensaMaximaPorcentual,
            idSonidoDaño
        );
        if (!Double.isFinite(daño) || daño < 0) {
            throw new IllegalArgumentException("El daño debe ser finito y no negativo.");
        }
        if (experienciaRecompensa < 0) {
            throw new IllegalArgumentException("La experiencia de recompensa no puede ser negativa.");
        }
        if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
        }
        this.daño = daño;
        this.experienciaRecompensa = experienciaRecompensa;
        this.objetivo = objetivo;
        this.intervaloAtaque = intervaloAtaque;
        this.tiempoHastaAtaque = 0.0;
        this.contactoConObjetivo = false;
        this.direccion = Direccion.ABAJO;
        this.animacionActual = animacionDefault;

        if (daño > 0) {
            añadirInteraccionColision(new Interaccion(TipoInteraccion.ATAQUE_ENEMIGO.clase(), daño));
        }
        añadirInteraccionColision(new Interaccion(TipoInteraccion.SOLIDO.clase(), 0.0));
    }

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault,
        String animacionExpirar,
        Integer idSonidoExpiracion,
        double vidaMaxima,
        double defensaMaximaPorcentual,
        Integer idSonidoDaño,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        double intervaloAtaque
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion,
            vidaMaxima,
            defensaMaximaPorcentual,
            idSonidoDaño,
            daño,
            experienciaRecompensa,
            objetivo,
            intervaloAtaque
        );
    }

    @Override
    public void tick(double dt) {
        if (!activo()) {
            return;
        }
        tiempoHastaAtaque = Math.max(0.0, tiempoHastaAtaque - dt);

        if (contactoConObjetivo) {
            getVelocidad().setX(0.0);
            getVelocidad().setY(0.0);
            contactoConObjetivo = false;
            return;
        }

        super.tick(dt);

        VecDouble2D vel = getVelocidad();
        if (vel.getX() != 0.0 || vel.getY() != 0.0) {
            if (Math.abs(vel.getX()) >= Math.abs(vel.getY())) {
                direccion = vel.getX() >= 0.0 ? Direccion.DERECHA : Direccion.IZQUIERDA;
            } else {
                direccion = vel.getY() >= 0.0 ? Direccion.ABAJO : Direccion.ARRIBA;
            }
            if (puedeAtacar()) {
                String animacionMovimiento = obtenerAnimacionMovimiento(direccion);
                if (!animacionMovimiento.equals(animacionActual)) {
                    animacionActual = animacionMovimiento;
                    getNotificadorAnimacion().notificar(animacionMovimiento);
                }
            }
        }
    }

    private String obtenerAnimacionMovimiento(Direccion dir) {
        return switch (dir) {
            case ARRIBA -> "caminar_arriba";
            case ABAJO -> "caminar_abajo";
            case IZQUIERDA -> "caminar_izquierda";
            case DERECHA -> "caminar_derecha";
        };
    }

    private String obtenerAnimacionAtaque(Direccion dir) {
        return switch (dir) {
            case ARRIBA -> "atacar_arriba";
            case ABAJO -> "atacar_abajo";
            case IZQUIERDA -> "atacar_izquierda";
            case DERECHA -> "atacar_derecha";
        };
    }

    public boolean puedeAtacar() {
        return tiempoHastaAtaque <= 0.0;
    }

    public void reiniciarTiempoAtaque() {
        tiempoHastaAtaque = intervaloAtaque;
    }

    public void setContactoConObjetivo(boolean contacto) {
        this.contactoConObjetivo = contacto;
    }

    @Override
    public List<Interaccion> getInteracciones() {
        if (!activo()) {
            return List.of();
        }
        if (puedeAtacar() && daño > 0) {
            return super.getInteracciones();
        }
        return List.of(new Interaccion(TipoInteraccion.SOLIDO.clase(), 0.0));
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) {
            return false;
        }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == TipoInteraccion.AÑADIR_EXP) {
            setContactoConObjetivo(true);
            if (puedeAtacar()) {
                reiniciarTiempoAtaque();
                animacionActual = obtenerAnimacionAtaque(direccion);
                getNotificadorAnimacion().notificar(animacionActual);
            }
            return true;
        }
        return super.interactuar(i);
    }

    public double getDaño() {
        return daño;
    }

    public int getExperienciaRecompensa() {
        return experienciaRecompensa;
    }

    public Posicionable getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Posicionable objetivo) {
        this.objetivo = objetivo;
        if (getEstrategiaMovimiento() instanceof EstrategiaMovimientoPersecucion persecucion) {
            persecucion.setObjetivo(objetivo);
        }
    }

    public double getIntervaloAtaque() {
        return intervaloAtaque;
    }

    public double getTiempoHastaAtaque() {
        return tiempoHastaAtaque;
    }
}
