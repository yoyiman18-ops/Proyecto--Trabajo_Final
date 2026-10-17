package controlador;

import java.util.function.DoubleSupplier;

import modelo.ItemMejoraAtributo;
import modelo.ModeloJugador;
import modelo.ObjetivoEnemigo;
import motor.mvc.ControladorUsuario;
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

    MejoraAtributoControlador crearMejoraAtributo(
        ModeloJugador jugador,
        ItemMejoraAtributo mejora,
        double x,
        double y
    );
}
