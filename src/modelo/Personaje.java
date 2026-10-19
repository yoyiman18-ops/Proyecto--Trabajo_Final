package modelo;

import java.util.Objects;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Direccion;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public class Personaje extends Entidad implements ModeloJugador {
    private final double defensaMaximaPorcentual;
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
        if (tiempoEnfriamientoAtaque > 0) {
            return false;
        }
        tiempoEnfriamientoAtaque = 1.0 / velocidadAtaque;
        return true;
    }

    @Override
    public Direccion getDireccion() {
        return direccion;
    }

    @Override
    public VecDouble2D getVectorDireccion() {
        return vectorDireccion.clone();
    }

    public double getVida() {
        return vida;
    }

    public double getVidaMax() {
        return vidaMax;
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

    @Override
    public double getVelocidadAtaque() {
        return velocidadAtaque;
    }

    public double getDefensa() {
        return defensaPorcentual;
    }

    @Override
    public double getProbabilidadCritico() {
        return probabilidadCritico;
    }

    @Override
    public double getProbabilidadMultidisparo() {
        return probabilidadMultidisparo;
    }

    public void aumentarDefensa(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de defensa debe ser finito y no negativo.");
        }
        defensaPorcentual = Math.min(defensaMaximaPorcentual, defensaPorcentual + porcentaje);
    }

    public void aumentarDanioAtaque(double cantidad) {
        validarCantidadVida(cantidad);
        danioAtaque += cantidad;
    }

    @Override
    public void aumentarVelocidadAtaque(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de velocidad de ataque debe ser finito y no negativo.");
        }
        velocidadAtaque *= 1 + porcentaje / 100;
    }

    @Override
    public void aumentarProbabilidadCritico(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de crítico debe ser finito y no negativo.");
        }
        probabilidadCritico = Math.min(100, probabilidadCritico + porcentaje);
    }

    @Override
    public void aumentarProbabilidadMultidisparo(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de multidisparo debe ser finito y no negativo.");
        }
        probabilidadMultidisparo = Math.min(100, probabilidadMultidisparo + porcentaje);
    }

    public boolean consumirNivelPendiente() {
        if (nivelesPendientes == 0) {
            return false;
        }
        nivelesPendientes--;
        return true;
    }

    public int ganarExperiencia(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }
        experiencia += cantidad;
        int nivelesGanados = 0;
        while (experiencia >= experienciaSiguiente) {
            experiencia -= experienciaSiguiente;
            nivel++;
            experienciaSiguiente = 30 + (nivel - 1) * 25;
            nivelesGanados++;
        }
        nivelesPendientes += nivelesGanados;
        return nivelesGanados;
    }

    public Notificador<ModeloJugador> getNotificadorVida() {
        return notificadorVida;
    }

    @Override
    public Entidad getEntidad() {
        return this;
    }

    public void recibirDanio(double cantidad) {
        validarCantidadVida(cantidad);
        if (cantidad == 0 || vida == 0) {
            return;
        }
        double danioEfectivo = cantidad * (1 - defensaPorcentual / 100);
        vida = Math.max(0, vida - danioEfectivo);
        notificadorVida.notificar(this);
    }

    public void curar(double cantidad) {
        validarCantidadVida(cantidad);
        if (cantidad == 0 || vida == vidaMax) {
            return;
        }
        vida = Math.min(vidaMax, vida + cantidad);
        notificadorVida.notificar(this);
    }

    public void aumentarVidaMaxima(double cantidad) {
        validarCantidadVida(cantidad);
        if (cantidad == 0) {
            return;
        }
        vidaMax += cantidad;
        vida += cantidad;
        notificadorVida.notificar(this);
    }

    private void validarCantidadVida(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad de vida debe ser finita y no negativa.");
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
        private double danioAtaque;
        private double velocidadAtaque;
        private double probabilidadCritico;
        private double probabilidadMultidisparo;
        private Notificador<Entidad> notificadorEntidad;
        private Notificador<ModeloJugador> notificadorVida;

        public Builder(String nombre, Hitbox hitbox) {
            this.nombre = nombre;
            this.hitbox = hitbox;
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
            this.danioAtaque = 25;
            this.velocidadAtaque = 1.2;
            this.probabilidadCritico = 5.0;
            this.probabilidadMultidisparo = 5.0;
            this.notificadorEntidad = new NotificadorDebil<>();
            this.notificadorVida = new NotificadorDebil<>();
        }

        public Builder posicion(VecDouble2D posicion) {
            this.posicion = posicion;
            return this;
        }

        public Builder velocidad(VecDouble2D velocidad) {
            this.velocidad = velocidad;
            return this;
        }

        public Builder aceleracion(double aceleracion) {
            if (aceleracion < 0) { throw new IllegalArgumentException("Aceleracion negativa no es valida"); }
            this.aceleracion = aceleracion;
            return this;
        }

        public Builder friccion(double friccion) {
            if (friccion < 0) { throw new IllegalArgumentException("Friccion negativa no es valida"); }
            this.friccion = friccion;
            return this;
        }

        public Builder velocidadMax(double velocidadMax) {
            this.velocidadMax = velocidadMax;
            return this;
        }

        public Builder vidaMax(double vidaMax) {
            if (vidaMax <= 0) { throw new IllegalArgumentException("Vida maxima menor o igual a 0 no es valida"); }
            this.vidaMax = vidaMax;
            return this;
        }

        public Builder vida(double vida) {
            if (vida < 0) { throw new IllegalArgumentException("Vida negativa no es valida"); }
            this.vida = vida;
            return this;
        }

        public Builder defensaMaximaPorcentual(double defensaMaximaPorcentual) {
            if (!Double.isFinite(defensaMaximaPorcentual) || defensaMaximaPorcentual < 0 || defensaMaximaPorcentual > 100) {
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
            if (!Double.isFinite(probabilidadCritico) || probabilidadCritico < 0 || probabilidadCritico > 100) {
                throw new IllegalArgumentException("La probabilidad de crítico debe estar entre 0 y 100");
            }
            this.probabilidadCritico = probabilidadCritico;
            return this;
        }

        public Builder probabilidadMultidisparo(double probabilidadMultidisparo) {
            if (!Double.isFinite(probabilidadMultidisparo) || probabilidadMultidisparo < 0 || probabilidadMultidisparo > 100) {
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

