package modelo.enemigo;

import motor.modelo.Posicionable;

public abstract class EnemigoFactory {

    public abstract Enemigo get(Posicionable objetivo, double velocidadOleada);
}
