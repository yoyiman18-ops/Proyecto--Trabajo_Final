package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;

public class Enemigo extends Personaje {

    public static final int ID_GLOBAL_ENEMIGO_DEFAULT = 3;
    public static final double INTERVALO_ATAQUE_DEFAULT = 1.0;
    public static final double DANIO_DEFAULT = 10.0;
    public static final int EXP_RECOMPENSA_DEFAULT = 10;

    public static final double VELOCIDAD_MAXIMA_DEFAULT = 75.0;
    public static final double VIDA_MAXIMA_DEFAULT = 50.0;
    public static final double DEFENSA_MAXIMA_DEFAULT = 0.0;
    public static final double ACELERACION_DEFAULT = 0.0;
    public static final double FRICCION_DEFAULT = 0.0;

    private double daño;
    private int experienciaRecompensa;
    private Posicionable objetivo;
    private double intervaloAtaque;
    private double tiempoHastaAtaque;
    private boolean contactoConObjetivo;

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

        if (daño > 0) {
            añadirInteraccionColision(new Interaccion(TipoInteraccion.DAÑAR.clase(), daño));
        }
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
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
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

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double velocidadMaxima,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        double intervaloAtaque,
        Integer idSonidoDaño,
        Integer idSonidoExpiracion
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(0.0, 0.0),
            ACELERACION_DEFAULT,
            FRICCION_DEFAULT,
            velocidadMaxima,
            new ArrayList<>(),
            new ArrayList<>(),
            ANIMACION_IDLE_DEFAULT,
            ANIMACION_EXPIRAR_DEFAULT,
            idSonidoExpiracion,
            vidaMaxima,
            DEFENSA_MAXIMA_DEFAULT,
            idSonidoDaño,
            daño,
            experienciaRecompensa,
            objetivo,
            intervaloAtaque
        );
    }

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double velocidadMaxima,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidadMaxima,
            vidaMaxima,
            daño,
            experienciaRecompensa,
            objetivo,
            INTERVALO_ATAQUE_DEFAULT,
            null,
            null
        );
    }

    private Enemigo(Builder b) {
        this(
            b.idGlobal,
            b.nombre,
            b.hitbox,
            b.posicion,
            b.velocidad,
            b.aceleracion,
            b.friccion,
            b.velocidadMax,
            b.notificadorPosicion != null ? b.notificadorPosicion : new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            b.estrategiasExpiracion,
            b.interaccionesColision,
            b.animacionDefault,
            b.animacionExpirar,
            b.idSonidoMuerte,
            b.vidaMaxima,
            b.defensaMaximaPorcentual,
            b.idSonidoDaño,
            b.danio,
            b.experienciaRecompensa,
            b.objetivo,
            b.intervaloAtaque
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

        if (objetivo != null && objetivo.getPosicion() != null) {
            VecDouble2D posicion = getPosicion();
            VecDouble2D posicionObjetivo = objetivo.getPosicion();
            double diferenciaX = posicionObjetivo.getX() - posicion.getX();
            double diferenciaY = posicionObjetivo.getY() - posicion.getY();
            double distancia = Math.hypot(diferenciaX, diferenciaY);

            double velocidadMax = getVelocidadMax();
            if (distancia == 0.0) {
                getVelocidad().setX(0.0);
                getVelocidad().setY(0.0);
            } else {
                getVelocidad().setX(diferenciaX / distancia * velocidadMax);
                getVelocidad().setY(diferenciaY / distancia * velocidadMax);
            }
        }
        super.tick(dt);
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
    }

    public double getIntervaloAtaque() {
        return intervaloAtaque;
    }

    public double getTiempoHastaAtaque() {
        return tiempoHastaAtaque;
    }

    public static class Builder {
        private int idGlobal = ID_GLOBAL_ENEMIGO_DEFAULT;
        private final String nombre;
        private final Hitbox hitbox;
        private VecDouble2D posicion = new VecDouble2D(0.0, 0.0);
        private VecDouble2D velocidad = new VecDouble2D(0.0, 0.0);
        private double aceleracion = ACELERACION_DEFAULT;
        private double friccion = FRICCION_DEFAULT;
        private double velocidadMax = VELOCIDAD_MAXIMA_DEFAULT;
        private double vidaMaxima = VIDA_MAXIMA_DEFAULT;
        private double defensaMaximaPorcentual = DEFENSA_MAXIMA_DEFAULT;
        private double danio = DANIO_DEFAULT;
        private int experienciaRecompensa = EXP_RECOMPENSA_DEFAULT;
        private double intervaloAtaque = INTERVALO_ATAQUE_DEFAULT;
        private Posicionable objetivo;
        private List<EstrategiaExpiracion> estrategiasExpiracion = new ArrayList<>();
        private List<Interaccion> interaccionesColision = new ArrayList<>();
        private String animacionDefault = ANIMACION_IDLE_DEFAULT;
        private String animacionExpirar = ANIMACION_EXPIRAR_DEFAULT;
        private Notificador<VecDouble2D> notificadorPosicion;
        private Integer idSonidoDaño;
        private Integer idSonidoMuerte;

        public Builder(String nombre, Hitbox hitbox) {
            this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
            this.hitbox = Objects.requireNonNull(hitbox, "La hitbox no puede ser nula");
        }

        public Builder idGlobal(int idGlobal) {
            this.idGlobal = idGlobal;
            return this;
        }

        public Builder posicion(VecDouble2D posicion) {
            Objects.requireNonNull(posicion, "La posición no puede ser nula");
            this.posicion = posicion.clone();
            return this;
        }

        public Builder posicion(double x, double y) {
            this.posicion = new VecDouble2D(x, y);
            return this;
        }

        public Builder velocidad(VecDouble2D velocidad) {
            Objects.requireNonNull(velocidad, "La velocidad no puede ser nula");
            this.velocidad = velocidad.clone();
            return this;
        }

        public Builder aceleracion(double aceleracion) {
            if (!Double.isFinite(aceleracion) || aceleracion < 0) {
                throw new IllegalArgumentException("La aceleración debe ser finita y no negativa.");
            }
            this.aceleracion = aceleracion;
            return this;
        }

        public Builder friccion(double friccion) {
            if (!Double.isFinite(friccion) || friccion < 0) {
                throw new IllegalArgumentException("La fricción debe ser finita y no negativa.");
            }
            this.friccion = friccion;
            return this;
        }

        public Builder velocidadMax(double velocidadMax) {
            if (!Double.isFinite(velocidadMax) || velocidadMax < 0) {
                throw new IllegalArgumentException("La velocidad máxima debe ser finita y no negativa.");
            }
            this.velocidadMax = velocidadMax;
            return this;
        }

        public Builder vidaMaxima(double vidaMaxima) {
            if (!Double.isFinite(vidaMaxima) || vidaMaxima <= 0) {
                throw new IllegalArgumentException("La vida máxima debe ser positiva y finita.");
            }
            this.vidaMaxima = vidaMaxima;
            return this;
        }

        public Builder defensaMaximaPorcentual(double defensaMaximaPorcentual) {
            if (!Double.isFinite(defensaMaximaPorcentual) || defensaMaximaPorcentual < 0 || defensaMaximaPorcentual > FACTOR_PORCENTUAL) {
                throw new IllegalArgumentException("La defensa máxima porcentual debe estar entre 0 y " + FACTOR_PORCENTUAL + ".");
            }
            this.defensaMaximaPorcentual = defensaMaximaPorcentual;
            return this;
        }

        public Builder danio(double danio) {
            if (!Double.isFinite(danio) || danio < 0) {
                throw new IllegalArgumentException("El daño no puede ser negativo.");
            }
            this.danio = danio;
            return this;
        }

        public Builder experienciaRecompensa(int experienciaRecompensa) {
            if (experienciaRecompensa < 0) {
                throw new IllegalArgumentException("La experiencia de recompensa no puede ser negativa.");
            }
            this.experienciaRecompensa = experienciaRecompensa;
            return this;
        }

        public Builder intervaloAtaque(double intervaloAtaque) {
            if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
                throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
            }
            this.intervaloAtaque = intervaloAtaque;
            return this;
        }

        public Builder objetivo(Posicionable objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public Builder estrategiasExpiracion(List<EstrategiaExpiracion> estrategias) {
            this.estrategiasExpiracion = estrategias != null ? new ArrayList<>(estrategias) : new ArrayList<>();
            return this;
        }

        public Builder interaccionesColision(List<Interaccion> interacciones) {
            this.interaccionesColision = interacciones != null ? new ArrayList<>(interacciones) : new ArrayList<>();
            return this;
        }

        public Builder animacionDefault(String animacion) {
            this.animacionDefault = animacion;
            return this;
        }

        public Builder animacionExpirar(String animacion) {
            this.animacionExpirar = animacion;
            return this;
        }

        public Builder notificadorPosicion(Notificador<VecDouble2D> notificadorPosicion) {
            this.notificadorPosicion = notificadorPosicion;
            return this;
        }

        public Builder idSonidoDaño(Integer idSonidoDaño) {
            this.idSonidoDaño = idSonidoDaño;
            return this;
        }

        public Builder idSonidoMuerte(Integer idSonidoMuerte) {
            this.idSonidoMuerte = idSonidoMuerte;
            return this;
        }

        public Enemigo build() {
            return new Enemigo(this);
        }
    }
}
