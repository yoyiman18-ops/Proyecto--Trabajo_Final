package controlador;

import java.util.Objects;

import modelo.ModeloJugador;
import motor.controlador.ControladorEntidad;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.modelo.Direccion;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.util.Eje;
import motor.util.observer.Observador;
import vista.VistaImagen;

public final class JugadorControlador implements ControladorEntidad {
    private final ModeloJugador modelo;
    private final VistaImagen vista;
    private final GestorSheetAnimada gestorSheet;
    private final Observador<ModeloJugador> observadorVida;
    private final Observador<Integer> observadorExpiracion;
    private final Runnable accionAtaque;
    private EstadoAcciones estadoAcciones;
    private final Observador<EstadoAcciones> observadorAcciones;
    private boolean activo;

    public JugadorControlador(
        ModeloJugador modelo,
        VistaImagen vista,
        GestorSheetAnimada gestorSheet,
        Observador<ModeloJugador> observadorVida,
        Runnable accionAtaque
    ) {
        this.gestorSheet = Objects.requireNonNull(gestorSheet, "El gestor de sheet no puede ser nulo");
        this.modelo = Objects.requireNonNull(modelo, "El modelo del jugador no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del jugador no puede ser nula");
        this.observadorVida = Objects.requireNonNull(observadorVida, "El observador de vida no puede ser nulo");
        this.accionAtaque = Objects.requireNonNull(accionAtaque, "La acción de ataque no puede ser nula");
        this.observadorAcciones = estado -> this.estadoAcciones = estado;
        this.observadorExpiracion = this::alExpirar;
        this.activo = true;

        this.modelo.getNotificadorVida().suscribirObservador(this.observadorVida);
        this.modelo.getNotificadorExpiracion().suscribirObservador(this.observadorExpiracion);
        this.gestorSheet.setAnimacion("idle", false);
        this.gestorSheet.suscribir(this.vista.getObservadorRectangulo());
        this.modelo.getNotificadorPosicion().suscribirObservador(this.vista.getObservadorPosicion());
        this.vista.setPosicion(this.modelo.getPosicion());
        this.observadorVida.cambio(modelo);
    }

    @Override public boolean activo() { return this.activo; }
    @Override public void setActivo(boolean activo) { this.activo = activo; }
    @Override public ModeloEntidad getEntidad() { return this.modelo; }

    public Observador<EstadoAcciones> getObservadorAcciones() {
        return observadorAcciones;
    }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }

        if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_ARRIBA)) {
            modelo.acelerar(Direccion.ARRIBA, dt);
        } else if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_ABAJO)) {
            modelo.acelerar(Direccion.ABAJO, dt);
        } else {
            modelo.frenar(Eje.Y, dt);
        }

        if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_DERECHA)) {
            modelo.acelerar(Direccion.DERECHA, dt);
        } else if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_IZQUIERDA)) {
            modelo.acelerar(Direccion.IZQUIERDA, dt);
        } else {
            modelo.frenar(Eje.X, dt);
        }

        modelo.tick(dt);
        gestorSheet.tick(dt);

        if (estadoAcciones != null && estadoAcciones.activa(Accion.ATACAR)) {
            accionAtaque.run();
        }
    }

    private void alExpirar(int e) {
        gestorSheet.setAnimacion("expirar", false);
        gestorSheet.getNotificadorFinAnimacion().suscribirObservador(
            f -> { vista.setVisible(false); setActivo(false); }
        );
    }
}
