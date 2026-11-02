package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Direccion;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;

public class Jugador extends Personaje {

    public static final int ID_GLOBAL_JUGADOR_DEFAULT = 1;
    public static final double VELOCIDAD_ATAQUE_DEFAULT = 1.0;
    public static final double DANIO_ATAQUE_DEFAULT = 10.0;
    public static final double CRITICO_DEFAULT = 5.0;
    public static final double MULTIDISPARO_DEFAULT = 0.0;
    public static final double MULTIPLICADOR_CRITICO_DEFAULT = 2.0;
    public static final int NIVEL_INICIAL_DEFAULT = 1;
    public static final int EXP_INICIAL_DEFAULT = 30;
    public static final int EXP_INCREMENTO_POR_NIVEL_DEFAULT = 25;

    public static final double ACELERACION_DEFAULT = 800.0;
    public static final double FRICCION_DEFAULT = 600.0;
    public static final double VELOCIDAD_MAXIMA_DEFAULT = 200.0;
    public static final double VIDA_MAXIMA_DEFAULT = 100.0;

    public static final double OFFSET_ORIGEN_DISPARO_X_DEFAULT = 17.0;
    public static final double OFFSET_ORIGEN_DISPARO_Y_DEFAULT = 17.0;
    public static final double DESVIACION_MULTIDISPARO_GRADOS_DEFAULT = 15.0;

    public static final double DIRECCION_POSITIVA = 1.0;
    public static final double DIRECCION_NEGATIVA = -1.0;

    private int experiencia;
    private int nivel;
    private int experienciaSiguiente;
    private int nivelesPendientes;
    private double danioAtaque;
    private double velocidadAtaque;
    private double tiempoEnfriamientoAtaque;
    private double probabilidadCritico;
    private double probabilidadMultidisparo;
    private double multiplicadorCritico;
    private double offsetDisparoX;
    private double offsetDisparoY;
    private double desviacionMultidisparoGrados;

    private Direccion direccion;
    private final VecDouble2D vectorDireccion;
    private final VecDouble2D acumuladorDireccionTick;
    private final Notificador<Jugador> notificadorVida;
    private final Integer idSonidoSubirNivel;

    public Jugador(
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
        int nivelInicial,
        int experienciaSiguienteInicial,
        double danioAtaque,
        double velocidadAtaque,
        double probabilidadCritico,
        double probabilidadMultidisparo,
        double multiplicadorCritico,
        double offsetDisparoX,
        double offsetDisparoY,
        double desviacionMultidisparoGrados,
        Integer idSonidoSubirNivel,
        Notificador<Jugador> notificadorVida
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
        if (nivelInicial <= 0) {
            throw new IllegalArgumentException("El nivel inicial debe ser positivo.");
        }
        if (experienciaSiguienteInicial <= 0) {
            throw new IllegalArgumentException("La experiencia siguiente inicial debe ser positiva.");
        }
        if (!Double.isFinite(danioAtaque) || danioAtaque < 0) {
            throw new IllegalArgumentException("El daño de ataque no puede ser negativo.");
        }
        if (!Double.isFinite(velocidadAtaque) || velocidadAtaque <= 0) {
            throw new IllegalArgumentException("La velocidad de ataque debe ser positiva.");
        }
        if (!Double.isFinite(probabilidadCritico) || probabilidadCritico < 0 || probabilidadCritico > FACTOR_PORCENTUAL) {
            throw new IllegalArgumentException("La probabilidad de crítico debe estar entre 0 y " + FACTOR_PORCENTUAL + ".");
        }
        if (!Double.isFinite(probabilidadMultidisparo) || probabilidadMultidisparo < 0 || probabilidadMultidisparo > FACTOR_PORCENTUAL) {
            throw new IllegalArgumentException("La probabilidad de multidisparo debe estar entre 0 y " + FACTOR_PORCENTUAL + ".");
        }
        if (!Double.isFinite(multiplicadorCritico) || multiplicadorCritico <= 0) {
            throw new IllegalArgumentException("El multiplicador de daño crítico debe ser positivo.");
        }
        this.experiencia = 0;
        this.nivel = nivelInicial;
        this.experienciaSiguiente = experienciaSiguienteInicial;
        this.nivelesPendientes = 0;
        this.danioAtaque = danioAtaque;
        this.velocidadAtaque = velocidadAtaque;
        this.tiempoEnfriamientoAtaque = 0.0;
        this.probabilidadCritico = probabilidadCritico;
        this.probabilidadMultidisparo = probabilidadMultidisparo;
        this.multiplicadorCritico = multiplicadorCritico;
        this.offsetDisparoX = offsetDisparoX;
        this.offsetDisparoY = offsetDisparoY;
        this.desviacionMultidisparoGrados = desviacionMultidisparoGrados;

        this.direccion = Direccion.ABAJO;
        this.vectorDireccion = new VecDouble2D(0.0, DIRECCION_POSITIVA);
        this.acumuladorDireccionTick = new VecDouble2D(0.0, 0.0);
        this.notificadorVida = notificadorVida != null ? notificadorVida : new NotificadorFuerte<>();
        this.idSonidoSubirNivel = idSonidoSubirNivel;
    }

    public Jugador(
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
        int nivelInicial,
        int experienciaSiguienteInicial,
        double danioAtaque,
        double velocidadAtaque,
        double probabilidadCritico,
        double probabilidadMultidisparo,
        Integer idSonidoSubirNivel,
        Notificador<Jugador> notificadorVida
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
            nivelInicial,
            experienciaSiguienteInicial,
            danioAtaque,
            velocidadAtaque,
            probabilidadCritico,
            probabilidadMultidisparo,
            MULTIPLICADOR_CRITICO_DEFAULT,
            OFFSET_ORIGEN_DISPARO_X_DEFAULT,
            OFFSET_ORIGEN_DISPARO_Y_DEFAULT,
            DESVIACION_MULTIDISPARO_GRADOS_DEFAULT,
            idSonidoSubirNivel,
            notificadorVida
        );
    }

    private Jugador(Builder b) {
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
            b.vidaMax,
            b.defensaMaximaPorcentual,
            b.idSonidoDaño,
            b.nivelInicial,
            b.experienciaSiguienteInicial,
            b.danioAtaque,
            b.velocidadAtaque,
            b.probabilidadCritico,
            b.probabilidadMultidisparo,
            b.multiplicadorCritico,
            b.offsetDisparoX,
            b.offsetDisparoY,
            b.desviacionMultidisparoGrados,
            b.idSonidoSubirNivel,
            b.notificadorVida
        );
        if (b.vida < b.vidaMax) {
            setVidaDirecta(b.vida);
        }
    }

    @Override
    public void acelerar(Direccion direccion, double dt) {
        this.direccion = Objects.requireNonNull(direccion, "La dirección no puede ser nula");
        switch (direccion) {
            case ARRIBA -> acumuladorDireccionTick.setY(DIRECCION_NEGATIVA);
            case ABAJO -> acumuladorDireccionTick.setY(DIRECCION_POSITIVA);
            case IZQUIERDA -> acumuladorDireccionTick.setX(DIRECCION_NEGATIVA);
            case DERECHA -> acumuladorDireccionTick.setX(DIRECCION_POSITIVA);
        }
        VecDouble2D normalizada = acumuladorDireccionTick.clone().normalizado();
        vectorDireccion.setX(normalizada.getX());
        vectorDireccion.setY(normalizada.getY());
        super.acelerar(direccion, dt);
    }

    @Override
    public void tick(double dt) {
        if (!activo()) {
            return;
        }
        super.tick(dt);
        acumuladorDireccionTick.setX(0.0);
        acumuladorDireccionTick.setY(0.0);
        if (tiempoEnfriamientoAtaque > 0) {
            tiempoEnfriamientoAtaque = Math.max(0.0, tiempoEnfriamientoAtaque - dt);
        }
    }

    public boolean intentarAtacar() {
        if (!activo() || tiempoEnfriamientoAtaque > 0) {
            return false;
        }
        tiempoEnfriamientoAtaque = 1.0 / velocidadAtaque;
        return true;
    }

    public List<DatosDisparo> realizarAtaque() {
        if (!intentarAtacar()) {
            return List.of();
        }
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        boolean esCritico = aleatorio.nextDouble(FACTOR_PORCENTUAL) < probabilidadCritico;
        double danioFinal = esCritico ? danioAtaque * multiplicadorCritico : danioAtaque;
        VecDouble2D origen = new VecDouble2D(getPosicion().getX() + offsetDisparoX, getPosicion().getY() + offsetDisparoY);
        VecDouble2D dirPrincipal = getVectorDireccion();

        List<DatosDisparo> disparos = new ArrayList<>();
        disparos.add(new DatosDisparo(origen.clone(), dirPrincipal.clone(), danioFinal, esCritico));

        if (aleatorio.nextDouble(FACTOR_PORCENTUAL) < probabilidadMultidisparo) {
            double anguloBase = Math.atan2(dirPrincipal.getY(), dirPrincipal.getX());
            double desviacion = Math.toRadians(desviacionMultidisparoGrados);
            VecDouble2D dirExtra = new VecDouble2D(
                Math.cos(anguloBase + desviacion),
                Math.sin(anguloBase + desviacion)
            );
            disparos.add(new DatosDisparo(origen.clone(), dirExtra, danioFinal, esCritico));
        }
        return List.copyOf(disparos);
    }

    public int ganarExperiencia(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }
        long experienciaAcumulada = (long) experiencia + cantidad;
        int nivelesGanados = 0;
        while (experienciaAcumulada >= experienciaSiguiente) {
            experienciaAcumulada -= experienciaSiguiente;
            nivel++;
            experienciaSiguiente = EXP_INICIAL_DEFAULT + (nivel - 1) * EXP_INCREMENTO_POR_NIVEL_DEFAULT;
            nivelesGanados++;
        }
        experiencia = (int) experienciaAcumulada;
        nivelesPendientes += nivelesGanados;
        if (nivelesGanados > 0 && idSonidoSubirNivel != null) {
            emitirSonido(idSonidoSubirNivel);
        }
        return nivelesGanados;
    }

    public boolean consumirNivelPendiente() {
        if (nivelesPendientes == 0) {
            return false;
        }
        nivelesPendientes--;
        return true;
    }

    public void aumentarDanioAtaque(double cantidad) {
        validarCantidadNoNegativa(cantidad);
        double nuevoDanio = danioAtaque + cantidad;
        if (!Double.isFinite(nuevoDanio)) {
            throw new IllegalArgumentException("El daño de ataque resultante debe ser finito.");
        }
        danioAtaque = nuevoDanio;
    }

    public void aumentarVelocidadAtaque(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de velocidad de ataque debe ser finito y no negativo.");
        }
        double nuevaVelocidadAtaque = velocidadAtaque * (1.0 + porcentaje / FACTOR_PORCENTUAL);
        if (!Double.isFinite(nuevaVelocidadAtaque)) {
            throw new IllegalArgumentException("La velocidad de ataque resultante debe ser finita.");
        }
        velocidadAtaque = nuevaVelocidadAtaque;
    }

    public void aumentarVelocidadMaxima(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de velocidad debe ser finito y no negativo.");
        }
        double nuevaVelocidadMaxima = getVelocidadMax() * (1.0 + porcentaje / FACTOR_PORCENTUAL);
        if (!Double.isFinite(nuevaVelocidadMaxima)) {
            throw new IllegalArgumentException("La velocidad máxima resultante debe ser finita.");
        }
        setVelocidadMax(nuevaVelocidadMaxima);
    }

    public void aumentarProbabilidadCritico(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de crítico debe ser finito y no negativo.");
        }
        probabilidadCritico = Math.min(FACTOR_PORCENTUAL, probabilidadCritico + porcentaje);
    }

    public void aumentarProbabilidadMultidisparo(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de multidisparo debe ser finito y no negativo.");
        }
        probabilidadMultidisparo = Math.min(FACTOR_PORCENTUAL, probabilidadMultidisparo + porcentaje);
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) {
            return false;
        }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == TipoInteraccion.GANAREXP || interaccion == TipoInteraccion.AÑADIR_EXP) {
            ganarExperiencia((int) i.valor());
            return true;
        }
        return super.interactuar(i);
    }

    @Override
    protected void alModificarVida() {
        if (notificadorVida != null) {
            notificadorVida.notificar(this);
        }
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperienciaSiguiente() {
        return experienciaSiguiente;
    }

    public int getNivelesPendientes() {
        return nivelesPendientes;
    }

    public double getDanioAtaque() {
        return danioAtaque;
    }

    public double getVelocidadAtaque() {
        return velocidadAtaque;
    }

    public double getProbabilidadCritico() {
        return probabilidadCritico;
    }

    public double getProbabilidadMultidisparo() {
        return probabilidadMultidisparo;
    }

    public double getMultiplicadorCritico() {
        return multiplicadorCritico;
    }

    public double getOffsetDisparoX() {
        return offsetDisparoX;
    }

    public double getOffsetDisparoY() {
        return offsetDisparoY;
    }

    public double getDesviacionMultidisparoGrados() {
        return desviacionMultidisparoGrados;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public VecDouble2D getVectorDireccion() {
        return vectorDireccion.clone();
    }

    public Notificador<Jugador> getNotificadorVida() {
        return notificadorVida;
    }

    public Integer getIdSonidoSubirNivel() {
        return idSonidoSubirNivel;
    }

    public static class Builder {
        private int idGlobal = ID_GLOBAL_JUGADOR_DEFAULT;
        private final String nombre;
        private final Hitbox hitbox;
        private VecDouble2D posicion = new VecDouble2D(0.0, 0.0);
        private VecDouble2D velocidad = new VecDouble2D(0.0, 0.0);
        private double aceleracion = ACELERACION_DEFAULT;
        private double friccion = FRICCION_DEFAULT;
        private double velocidadMax = VELOCIDAD_MAXIMA_DEFAULT;
        private double vidaMax = VIDA_MAXIMA_DEFAULT;
        private double vida = VIDA_MAXIMA_DEFAULT;
        private double defensaMaximaPorcentual = DEFENSA_MAXIMA_DEFAULT;
        private double danioAtaque = DANIO_ATAQUE_DEFAULT;
        private double velocidadAtaque = VELOCIDAD_ATAQUE_DEFAULT;
        private double probabilidadCritico = CRITICO_DEFAULT;
        private double probabilidadMultidisparo = MULTIDISPARO_DEFAULT;
        private double multiplicadorCritico = MULTIPLICADOR_CRITICO_DEFAULT;
        private double offsetDisparoX = OFFSET_ORIGEN_DISPARO_X_DEFAULT;
        private double offsetDisparoY = OFFSET_ORIGEN_DISPARO_Y_DEFAULT;
        private double desviacionMultidisparoGrados = DESVIACION_MULTIDISPARO_GRADOS_DEFAULT;
        private int nivelInicial = NIVEL_INICIAL_DEFAULT;
        private int experienciaSiguienteInicial = EXP_INICIAL_DEFAULT;
        private List<EstrategiaExpiracion> estrategiasExpiracion = new ArrayList<>();
        private List<Interaccion> interaccionesColision = new ArrayList<>();
        private String animacionDefault = ANIMACION_IDLE_DEFAULT;
        private String animacionExpirar = ANIMACION_EXPIRAR_DEFAULT;
        private Notificador<VecDouble2D> notificadorPosicion;
        private Notificador<Jugador> notificadorVida = new NotificadorFuerte<>();
        private Integer idSonidoDaño;
        private Integer idSonidoMuerte;
        private Integer idSonidoSubirNivel;

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

        public Builder vidaMax(double vidaMax) {
            if (!Double.isFinite(vidaMax) || vidaMax <= 0) {
                throw new IllegalArgumentException("La vida máxima debe ser positiva y finita.");
            }
            this.vidaMax = vidaMax;
            return this;
        }

        public Builder vida(double vida) {
            if (!Double.isFinite(vida) || vida < 0) {
                throw new IllegalArgumentException("La vida debe ser finita y no negativa.");
            }
            this.vida = vida;
            return this;
        }

        public Builder defensaMaximaPorcentual(double defensaMaximaPorcentual) {
            if (!Double.isFinite(defensaMaximaPorcentual) || defensaMaximaPorcentual < 0 || defensaMaximaPorcentual > FACTOR_PORCENTUAL) {
                throw new IllegalArgumentException("La defensa máxima porcentual debe estar entre 0 y " + FACTOR_PORCENTUAL + ".");
            }
            this.defensaMaximaPorcentual = defensaMaximaPorcentual;
            return this;
        }

        public Builder danioAtaque(double danioAtaque) {
            if (!Double.isFinite(danioAtaque) || danioAtaque < 0) {
                throw new IllegalArgumentException("El daño de ataque no puede ser negativo.");
            }
            this.danioAtaque = danioAtaque;
            return this;
        }

        public Builder velocidadAtaque(double velocidadAtaque) {
            if (!Double.isFinite(velocidadAtaque) || velocidadAtaque <= 0) {
                throw new IllegalArgumentException("La velocidad de ataque debe ser positiva.");
            }
            this.velocidadAtaque = velocidadAtaque;
            return this;
        }

        public Builder probabilidadCritico(double probabilidadCritico) {
            if (!Double.isFinite(probabilidadCritico) || probabilidadCritico < 0 || probabilidadCritico > FACTOR_PORCENTUAL) {
                throw new IllegalArgumentException("La probabilidad de crítico debe estar entre 0 y " + FACTOR_PORCENTUAL + ".");
            }
            this.probabilidadCritico = probabilidadCritico;
            return this;
        }

        public Builder probabilidadMultidisparo(double probabilidadMultidisparo) {
            if (!Double.isFinite(probabilidadMultidisparo) || probabilidadMultidisparo < 0 || probabilidadMultidisparo > FACTOR_PORCENTUAL) {
                throw new IllegalArgumentException("La probabilidad de multidisparo debe estar entre 0 y " + FACTOR_PORCENTUAL + ".");
            }
            this.probabilidadMultidisparo = probabilidadMultidisparo;
            return this;
        }

        public Builder multiplicadorCritico(double multiplicadorCritico) {
            if (!Double.isFinite(multiplicadorCritico) || multiplicadorCritico <= 0) {
                throw new IllegalArgumentException("El multiplicador de daño crítico debe ser positivo.");
            }
            this.multiplicadorCritico = multiplicadorCritico;
            return this;
        }

        public Builder offsetDisparo(double offsetX, double offsetY) {
            this.offsetDisparoX = offsetX;
            this.offsetDisparoY = offsetY;
            return this;
        }

        public Builder desviacionMultidisparoGrados(double grados) {
            this.desviacionMultidisparoGrados = grados;
            return this;
        }

        public Builder nivelInicial(int nivelInicial) {
            if (nivelInicial <= 0) {
                throw new IllegalArgumentException("El nivel inicial debe ser positivo.");
            }
            this.nivelInicial = nivelInicial;
            return this;
        }

        public Builder experienciaSiguienteInicial(int exp) {
            if (exp <= 0) {
                throw new IllegalArgumentException("La experiencia debe ser positiva.");
            }
            this.experienciaSiguienteInicial = exp;
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

        public Builder notificadorVida(Notificador<Jugador> notificadorVida) {
            this.notificadorVida = Objects.requireNonNull(notificadorVida, "El notificador de vida no puede ser nulo");
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

        public Builder idSonidoSubirNivel(Integer idSonidoSubirNivel) {
            this.idSonidoSubirNivel = idSonidoSubirNivel;
            return this;
        }

        public Jugador build() {
            if (this.vida > this.vidaMax) {
                this.vida = this.vidaMax;
            }
            if (this.vida < 0) {
                this.vida = 0;
            }
            return new Jugador(this);
        }
    }
}
