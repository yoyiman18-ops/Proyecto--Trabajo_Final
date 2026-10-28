package controlador;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

import modelo.ModeloJugador;
import modelo.ObjetivoEnemigo;
import motor.colisiones.Colisionable;
import motor.modelo.Entidad;
import motor.mvc.ControladorUsuario;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;

public interface FabricaEntidadesPartida {
    ModeloJugador crearModeloJugador();

    ControladorUsuario crearControladorJugador(
        ModeloJugador modelo,
        DoubleSupplier anchoArea,
        DoubleSupplier altoArea,
        JugadorControlador.LimitesArea limites,
        Observador<ModeloJugador> observadorVida,
        Runnable accionAtaque
    );

    List<Entidad> crearMurosPerimetrales(
        double anchoArea,
        double altoArea,
        JugadorControlador.LimitesArea limites
    );

    EnemigoControlador crearEnemigo(
        double x,
        double y,
        double velocidad,
        ObjetivoEnemigo objetivo
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
        boolean esCritico,
        Consumer<Colisionable> accionImpacto
    );
}
