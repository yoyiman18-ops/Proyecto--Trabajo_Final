package modelo;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public class Personaje extends Entidad {
    private static final double DEFENSA_MAXIMA_PORCENTUAL = 80;

    private double vida;
    private double vidaMax;
    private double defensaPorcentual;
    private final Notificador<Personaje> notificadorVida = new NotificadorDebil<>();
    private int experiencia;
    private int nivel = 1;
    private int experienciaSiguiente = 100;
    private int nivelesPendientes;
    private double danioAtaque = 25;
    
    private Personaje(Builder b) {
        super(b.nombre, b.hitbox, b.posicion, b.velocidad, b.aceleracion, b.friccion, b.velocidadMax);
        this.vida = b.vida;
        this.vidaMax = b.vidaMax;
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

    public double getDefensa() {
        return defensaPorcentual;
    }

    public void aumentarDefensa(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de defensa debe ser finito y no negativo.");
        }
        defensaPorcentual = Math.min(DEFENSA_MAXIMA_PORCENTUAL, defensaPorcentual + porcentaje);
    }

    public void aumentarDanioAtaque(double cantidad) {
        validarCantidadVida(cantidad);
        danioAtaque += cantidad;
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
            experienciaSiguiente = nivel * 100;
            nivelesGanados++;
        }
        nivelesPendientes += nivelesGanados;
        return nivelesGanados;
    }

    public Notificador<Personaje> getNotificadorVida() {
        return notificadorVida;
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

    private static void validarCantidadVida(double cantidad) {
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
        private double vida,vidaMax;

        public Builder(String nombre, Hitbox hitbox) {
            this.nombre = nombre;
            this.hitbox = hitbox;
            this.posicion = new VecDouble2D(0, 0);
            this.velocidad = new VecDouble2D(0,0);
            this.aceleracion = 1;
            this.friccion = 0.75;
            this.vidaMax = 100;
            this.velocidadMax = aceleracion * 2;
            this.vida = vidaMax;
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

        public Personaje build() {
            if (this.vida > this.vidaMax) { this.vida = this.vidaMax; }
            if (this.vida < 0) { this.vida = 0; }
            return new Personaje(this);
        }
    }


}
