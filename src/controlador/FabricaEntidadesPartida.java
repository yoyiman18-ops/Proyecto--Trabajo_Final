package controlador;

import modelo.ModeloJugador;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;

public interface FabricaEntidadesPartida {
    ModeloJugador crearModeloJugador();

    JugadorControlador crearControladorJugador(
        ModeloJugador modelo,
        Observador<ModeloJugador> observadorVida,
        Runnable accionAtaque
    );

    EnemigoControlador crearEnemigo(
        double x,
        double y,
        double velocidad,
        Posicionable objetivo
    );

    ExperienciaControlador crearExperiencia(
        ModeloJugador jugador,
        double x,
        double y,
        int cantidad
    );

    ProyectilControlador crearProyectil(
        double x,
        double y,
        VecDouble2D direccion,
        double danio,
        boolean esCritico
    );
}
