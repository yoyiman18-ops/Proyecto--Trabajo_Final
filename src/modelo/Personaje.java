package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public abstract class Personaje extends Entidad {

    private double vidaMax;
    private double vida;
    private final double defensaMaximaPorcentual;
    private double defensaPorcentual;
    private final Notificador<Double> notificadorDaño;
    private final Integer idSonidoDaño;

    public Personaje(
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
        Integer idSonidoDaño
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
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion
        );
        if (!Double.isFinite(vidaMaxima) || vidaMaxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser positiva y finita.");
        }
        if (!Double.isFinite(defensaMaximaPorcentual) || defensaMaximaPorcentual < 0 || defensaMaximaPorcentual > 100.0) {
            throw new IllegalArgumentException("La defensa máxima porcentual debe estar entre 0 y 100.0.");
        }
        this.vidaMax = vidaMaxima;
        this.vida = vidaMaxima;
        this.defensaMaximaPorcentual = defensaMaximaPorcentual;
        this.defensaPorcentual = 0.0;
        this.notificadorDaño = Objects.requireNonNull(notificadorDaño, "El notificadorDaño no puede ser nulo");
        this.idSonidoDaño = idSonidoDaño;
    }

    public Personaje(
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
        Integer idSonidoDaño
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad != null ? velocidad : new VecDouble2D(),
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
            estrategiasExpiracion != null ? estrategiasExpiracion : new ArrayList<>(),
            interaccionesColision != null ? interaccionesColision : new ArrayList<>(),
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion,
            vidaMaxima,
            defensaMaximaPorcentual,
            idSonidoDaño
        );
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) {
            return false;
        }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == null) {
            return false;
        }
        return switch (interaccion) {
            case DAÑAR, SOLIDO -> {
                recibirDaño(i.valor());
                yield true;
            }
            default -> false;
        };
    }

    public boolean recibirDaño(double cantidad) {
        if (!activo()) {
            return false;
        }
        validarCantidadNoNegativa(cantidad);
        if (cantidad == 0 || vida == 0) {
            return false;
        }
        double danioEfectivo = cantidad * (1.0 - (defensaPorcentual / 100.0));
        vida = Math.max(0, vida - danioEfectivo);
        if (idSonidoDaño != null) {
            emitirSonido(idSonidoDaño);
        }
        notificadorDaño.notificar(danioEfectivo);
        alModificarVida();
        if (vida == 0) {
            expirar();
        }
        return vida == 0;
    }

    public void aumentarVidaMaxima(double cantidad) {
        validarCantidadNoNegativa(cantidad);
        if (cantidad == 0) {
            return;
        }
        double nuevaVidaMaxima = vidaMax + cantidad;
        double nuevaVida = vida + cantidad;
        if (!Double.isFinite(nuevaVidaMaxima) || !Double.isFinite(nuevaVida)) {
            throw new IllegalArgumentException("La vida resultante debe ser finita.");
        }
        this.vidaMax = nuevaVidaMaxima;
        this.vida = nuevaVida;
        alModificarVida();
    }

    public void aumentarDefensa(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de defensa debe ser finito y no negativo.");
        }
        this.defensaPorcentual = Math.min(defensaMaximaPorcentual, defensaPorcentual + porcentaje);
    }

    protected void alModificarVida() {
    }

    protected void validarCantidadNoNegativa(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser finita y no negativa.");
        }
    }

    public double getVida() {
        return vida;
    }

    public double getVidaMax() {
        return vidaMax;
    }

    public Notificador<Double> getNotificadorDaño() {
        return notificadorDaño;
    }
}
