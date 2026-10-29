package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Direccion;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public class Personaje extends Entidad implements ModeloJugador {
    private static final double PROBABILIDAD_MAXIMA = 100.0;
    private static final double MULTIPLICADOR_CRITICO = 2.0;
    private static final double[] ANGULOS_UNICO_DISPARO = { 0.0 };
    private static final double[] ANGULOS_MULTIDISPARO = { -18.0, 0.0, 18.0 };
    private static final double OFFSET_CENTRO_DISPARO = 17.0;
    private static final double DISTANCIA_SALIDA_DISPARO = 16.0;

    private final double defensaMaximaPorcentual;
    private final int experienciaBaseNivel;
    private final int incrementoExperienciaNivel;
    private double vida;
    private double vidaMax;
    private double defensaPorcentual;
    private final Notificador<ModeloJugador> notificadorVida;
    private int experiencia;
    private int nivel;
    private int experienciaSiguiente;
    private int nivelesPendientes;
    private double danioAtaque;
    private double velocidadAtaque;
    private double tiempoEnfriamientoAtaque;
    private double probabilidadCritico;
    private double probabilidadMultidisparo;
    private Direccion direccion;
    private final VecDouble2D vectorDireccion;
    private final VecDouble2D acumuladorDireccionTick;

    private Personaje(Builder b) {
        super(
            b.nombre,
            b.hitbox,
            b.posicion,
            b.velocidad,
            b.aceleracion,
            b.friccion,
            b.velocidadMax,
            b.notificadorEntidad
        );
        this.vida = b.vida;
        this.vidaMax = b.vidaMax;
        this.defensaMaximaPorcentual = b.defensaMaximaPorcentual;
        this.defensaPorcentual = 0;
        this.experiencia = 0;
        this.nivel = b.nivelInicial;
        this.experienciaBaseNivel = b.experienciaSiguienteInicial;
        this.incrementoExperienciaNivel = b.incrementoExperienciaNivel;
        this.experienciaSiguiente = b.experienciaSiguienteInicial;
        this.nivelesPendientes = 0;
        this.danioAtaque = b.danioAtaque;
        this.velocidadAtaque = b.velocidadAtaque;
        this.tiempoEnfriamientoAtaque = 0;
        this.probabilidadCritico = b.probabilidadCritico;
        this.probabilidadMultidisparo = b.probabilidadMultidisparo;
        this.direccion = Direccion.ABAJO;
        this.vectorDireccion = new VecDouble2D(0, 1);
        this.acumuladorDireccionTick = new VecDouble2D(0, 0);
        this.notificadorVida = Objects.requireNonNull(b.notificadorVida, "El notificador de vida no puede ser nulo");
    }

    @Override
    public void acelerar(Direccion direccion, double dt) {
        this.direccion = Objects.requireNonNull(direccion, "La dirección no puede ser nula");
        switch (direccion) {
            case ARRIBA -> acumuladorDireccionTick.setY(-1);
            case ABAJO -> acumuladorDireccionTick.setY(1);
            case IZQUIERDA -> acumuladorDireccionTick.setX(-1);
            case DERECHA -> acumuladorDireccionTick.setX(1);
        }
        VecDouble2D normalizada = acumuladorDireccionTick.clone().normalizado();
        vectorDireccion.setX(normalizada.getX());
        vectorDireccion.setY(normalizada.getY());
        super.acelerar(direccion, dt);
    }

    @Override
    public void mover(double dt) {
        super.mover(dt);
        acumuladorDireccionTick.setX(0);
        acumuladorDireccionTick.setY(0);
        if (tiempoEnfriamientoAtaque > 0) {
            tiempoEnfriamientoAtaque = Math.max(0, tiempoEnfriamientoAtaque - dt);
        }
    }

    @Override
    public boolean intentarAtacar() {
        if (tiempoEnfriamientoAtaque > 0) { return false; }
        tiempoEnfriamientoAtaque = 1.0 / velocidadAtaque;
        return true;
    }

    @Override
    public List<DatosDisparo> realizarAtaque() {
        if (!intentarAtacar()) {
            return List.of();
        }

        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        boolean esMultidisparo = aleatorio.nextDouble(PROBABILIDAD_MAXIMA) < probabilidadMultidisparo;
        double[] angulos = esMultidisparo ? ANGULOS_MULTIDISPARO : ANGULOS_UNICO_DISPARO;

        List<DatosDisparo> disparos = new ArrayList<>(angulos.length);
        VecDouble2D posicionActual = getPosicion();
        for (double angulo : angulos) {
            VecDouble2D direccionDisparo = rotarVector(vectorDireccion, angulo);
            boolean esCritico = aleatorio.nextDouble(PROBABILIDAD_MAXIMA) < probabilidadCritico;
            double danioFinal = esCritico ? danioAtaque * MULTIPLICADOR_CRITICO : danioAtaque;

            VecDouble2D origen = new VecDouble2D(
                posicionActual.getX() + OFFSET_CENTRO_DISPARO + direccionDisparo.getX() * DISTANCIA_SALIDA_DISPARO,
                posicionActual.getY() + OFFSET_CENTRO_DISPARO + direccionDisparo.getY() * DISTANCIA_SALIDA_DISPARO
            );
            disparos.add(new DatosDisparo(origen, direccionDisparo, danioFinal, esCritico));
        }
        return List.copyOf(disparos);
    }

    private static VecDouble2D rotarVector(VecDouble2D vector, double grados) {
        if (grados == 0) {
            return vector.clone();
        }
        double radianes = Math.toRadians(grados);
        double cos = Math.cos(radianes);
        double sin = Math.sin(radianes);
        double x = vector.getX() * cos - vector.getY() * sin;
        double y = vector.getX() * sin + vector.getY() * cos;
        return new VecDouble2D(x, y);
    }

    @Override
    public Direccion getDireccion() { return direccion; }
    @Override
    public VecDouble2D getVectorDireccion() { return vectorDireccion.clone(); }
    @Override
    public double getVida() { return vida; }
    @Override
    public double getVidaMax() { return vidaMax;}
    @Override
    public int getExperiencia() { return experiencia; }
    @Override
    public int getNivel() { return nivel; }
    @Override
    public int getExperienciaSiguiente() { return experienciaSiguiente;}
    @Override
    public double getDanioAtaque() { return danioAtaque; }
    @Override
    public double getProbabilidadCritico() { return probabilidadCritico; }
    @Override
    public double getProbabilidadMultidisparo() { return probabilidadMultidisparo; }

    @Override
    public void aumentarDefensa(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de defensa debe ser finito y no negativo.");
        }
        defensaPorcentual = Math.min(defensaMaximaPorcentual, defensaPorcentual + porcentaje);
    }

    @Override
    public void aumentarDanioAtaque(double cantidad) {
        validarCantidadNoNegativa(cantidad);
        double nuevoDanio = danioAtaque + cantidad;
        if (!Double.isFinite(nuevoDanio)) {
            throw new IllegalArgumentException("El daño de ataque resultante debe ser finito.");
        }
        danioAtaque = nuevoDanio;
    }

    @Override
    public void aumentarVelocidadAtaque(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de velocidad de ataque debe ser finito y no negativo.");
        }
        double nuevaVelocidadAtaque = velocidadAtaque * (1 + porcentaje / PROBABILIDAD_MAXIMA);
        if (!Double.isFinite(nuevaVelocidadAtaque)) {
            throw new IllegalArgumentException("La velocidad de ataque resultante debe ser finita.");
        }
        velocidadAtaque = nuevaVelocidadAtaque;
    }

    @Override
    public void aumentarVelocidadMaxima(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de velocidad debe ser finito y no negativo.");
        }
        double nuevaVelocidadMaxima = getVelocidadMax() * (1 + porcentaje / PROBABILIDAD_MAXIMA);
        if (!Double.isFinite(nuevaVelocidadMaxima)) {
            throw new IllegalArgumentException("La velocidad máxima resultante debe ser finita.");
        }
        setVelocidadMax(nuevaVelocidadMaxima);
    }

    @Override
    public void aumentarProbabilidadCritico(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de crítico debe ser finito y no negativo.");
        }
        probabilidadCritico = Math.min(PROBABILIDAD_MAXIMA, probabilidadCritico + porcentaje);
    }

    @Override
    public void aumentarProbabilidadMultidisparo(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de multidisparo debe ser finito y no negativo.");
        }
        probabilidadMultidisparo = Math.min(PROBABILIDAD_MAXIMA, probabilidadMultidisparo + porcentaje);
    }

    @Override
    public boolean consumirNivelPendiente() {
        if (nivelesPendientes == 0) {
            return false;
        }
        nivelesPendientes--;
        return true;
    }

    @Override
    public int ganarExperiencia(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }
        long experienciaAcumulada = (long) experiencia + cantidad;
        int nivelesGanados = 0;
        while (experienciaAcumulada >= experienciaSiguiente) {
            experienciaAcumulada -= experienciaSiguiente;
            nivel++;
            experienciaSiguiente = experienciaBaseNivel + (nivel - 1) * incrementoExperienciaNivel;
            nivelesGanados++;
        }
        experiencia = (int) experienciaAcumulada;
        nivelesPendientes += nivelesGanados;
        return nivelesGanados;
    }

    @Override
    public Notificador<ModeloJugador> getNotificadorVida() {
        return notificadorVida;
    }

    @Override
    public boolean recibirDanio(double cantidad) {
        validarCantidadNoNegativa(cantidad);
        if (cantidad == 0 || vida == 0) {
            return false;
        }
        double danioEfectivo = cantidad * (1 - defensaPorcentual / PROBABILIDAD_MAXIMA);
        vida = Math.max(0, vida - danioEfectivo);
        notificadorVida.notificar(this);
        return vida == 0;
    }

    @Override
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
        vidaMax = nuevaVidaMaxima;
        vida = nuevaVida;
        notificadorVida.notificar(this);
    }

    private void validarCantidadNoNegativa(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser finita y no negativa.");
        }
    }

    public static class Builder {
        private final String nombre;
        private final Hitbox hitbox;
        private VecDouble2D posicion;
        private VecDouble2D velocidad;
        private double aceleracion;
        private double friccion;
        private double velocidadMax;
        private double vida, vidaMax;
        private double defensaMaximaPorcentual;
        private int nivelInicial;
        private int experienciaSiguienteInicial;
        private int incrementoExperienciaNivel;
        private double danioAtaque;
        private double velocidadAtaque;
        private double probabilidadCritico;
        private double probabilidadMultidisparo;
        private Notificador<Entidad> notificadorEntidad;
        private Notificador<ModeloJugador> notificadorVida;

        public Builder(String nombre, Hitbox hitbox) {
            this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
            this.hitbox = Objects.requireNonNull(hitbox, "La hitbox no puede ser nula");
            this.posicion = new VecDouble2D(0, 0);
            this.velocidad = new VecDouble2D(0, 0);
            this.aceleracion = 1;
            this.friccion = 0.75;
            this.vidaMax = 100;
            this.velocidadMax = aceleracion * 2;
            this.vida = vidaMax;
            this.defensaMaximaPorcentual = 80;
            this.nivelInicial = 1;
            this.experienciaSiguienteInicial = 30;
            this.incrementoExperienciaNivel = 25;
            this.danioAtaque = 25;
            this.velocidadAtaque = 1.2;
            this.probabilidadCritico = 5.0;
            this.probabilidadMultidisparo = 5.0;
            this.notificadorEntidad = new NotificadorDebil<>();
            this.notificadorVida = new NotificadorDebil<>();
        }

        public Builder posicion(VecDouble2D posicion) {
            Objects.requireNonNull(posicion, "La posición no puede ser nula");
            if (!Double.isFinite(posicion.getX()) || !Double.isFinite(posicion.getY())) {
                throw new IllegalArgumentException("La posición debe tener coordenadas finitas.");
            }
            this.posicion = posicion.clone();
            return this;
        }

        public Builder velocidad(VecDouble2D velocidad) {
            Objects.requireNonNull(velocidad, "La velocidad no puede ser nula");
            if (!Double.isFinite(velocidad.getX()) || !Double.isFinite(velocidad.getY())) {
                throw new IllegalArgumentException("La velocidad debe tener componentes finitos.");
            }
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
            if (!Double.isFinite(defensaMaximaPorcentual) || defensaMaximaPorcentual < 0 || defensaMaximaPorcentual > PROBABILIDAD_MAXIMA) {
                throw new IllegalArgumentException("La defensa máxima porcentual debe estar entre 0 y 100");
            }
            this.defensaMaximaPorcentual = defensaMaximaPorcentual;
            return this;
        }

        public Builder danioAtaque(double danioAtaque) {
            if (!Double.isFinite(danioAtaque) || danioAtaque < 0) {
                throw new IllegalArgumentException("El daño de ataque no puede ser negativo");
            }
            this.danioAtaque = danioAtaque;
            return this;
        }

        public Builder velocidadAtaque(double velocidadAtaque) {
            if (!Double.isFinite(velocidadAtaque) || velocidadAtaque <= 0) {
                throw new IllegalArgumentException("La velocidad de ataque debe ser positiva");
            }
            this.velocidadAtaque = velocidadAtaque;
            return this;
        }

        public Builder probabilidadCritico(double probabilidadCritico) {
            if (!Double.isFinite(probabilidadCritico) || probabilidadCritico < 0 || probabilidadCritico > PROBABILIDAD_MAXIMA) {
                throw new IllegalArgumentException("La probabilidad de crítico debe estar entre 0 y 100");
            }
            this.probabilidadCritico = probabilidadCritico;
            return this;
        }

        public Builder probabilidadMultidisparo(double probabilidadMultidisparo) {
            if (!Double.isFinite(probabilidadMultidisparo) || probabilidadMultidisparo < 0 || probabilidadMultidisparo > PROBABILIDAD_MAXIMA) {
                throw new IllegalArgumentException("La probabilidad de multidisparo debe estar entre 0 y 100");
            }
            this.probabilidadMultidisparo = probabilidadMultidisparo;
            return this;
        }

        public Builder notificadorEntidad(Notificador<Entidad> notificadorEntidad) {
            this.notificadorEntidad = Objects.requireNonNull(
                notificadorEntidad,
                "El notificador de entidad no puede ser nulo"
            );
            return this;
        }

        public Builder notificadorVida(Notificador<ModeloJugador> notificadorVida) {
            this.notificadorVida = Objects.requireNonNull(
                notificadorVida,
                "El notificador de vida no puede ser nulo"
            );
            return this;
        }

        public Personaje build() {
            if (this.vida > this.vidaMax) { this.vida = this.vidaMax; }
            if (this.vida < 0) { this.vida = 0; }
            return new Personaje(this);
        }
    }
}
