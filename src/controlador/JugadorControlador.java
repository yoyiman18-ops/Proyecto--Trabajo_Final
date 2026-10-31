package controlador;

import java.util.Objects;

import modelo.ModeloJugador;
import motor.controlador.ControladorEntidad;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.entrada.ReceptorEstadoAcciones;
import motor.modelo.Direccion;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.recursos.GestorAudio;
import motor.util.Eje;
import motor.util.observer.Observador;
import vista.VistaImagen;

public final class JugadorControlador implements ControladorEntidad,ReceptorEstadoAcciones {
    private final ModeloJugador modelo;
    private final VistaImagen vista;
    private final GestorSheetAnimada gestorSheet;
    private final GestorAudio gestorAudio;
    private final Observador<ModeloJugador> observadorVida;
    private final Observador<Double> observadorDaño;
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
    ) {
        this(modelo, vista, gestorSheet, observadorVida, null);
    }

    public JugadorControlador(
        ModeloJugador modelo,
        VistaImagen vista,
        GestorSheetAnimada gestorSheet,
        Observador<ModeloJugador> observadorVida,
        GestorAudio gestorAudio
    ) {
        this.gestorSheet = Objects.requireNonNull(gestorSheet, "El gestor de sheet no puede ser nulo");
        this.modelo = Objects.requireNonNull(modelo, "El modelo del jugador no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del jugador no puede ser nula");
        this.observadorVida = Objects.requireNonNull(observadorVida, "El observador de vida no puede ser nulo");
        this.accionAtaque = Objects.requireNonNull(accionAtaque, "La acción de ataque no puede ser nula");
        this.gestorAudio = gestorAudio;
        this.observadorAcciones = estado -> this.estadoAcciones = estado;
        this.observadorDaño = this::alRecibirDaño;
        this.observadorExpiracion = this::alExpirar;
        this.activo = true;

        this.modelo.getNotificadorVida().suscribirObservador(this.observadorVida);
        this.modelo.getNotificadorDaño().suscribirObservador(this.observadorDaño);
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
    @Override public VistaImagen getVista() { return this.vista; }

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

    private void alRecibirDaño(double daño) {
        if (!modelo.activo() || modelo.getVida() <= 0) { return; }
        SonidoJuego.DAÑO_JUGADOR.reproducirEn(gestorAudio);
    }

    private void alExpirar(int e) {
        SonidoJuego.MUERTE_JUGADOR.reproducirEn(gestorAudio);
        if (gestorSheet.setAnimacion("expirar", false)) {
            gestorSheet.getNotificadorFinAnimacion().suscribirObservador(
                f -> { vista.setVisible(false); setActivo(false); }
            );
        } else {
            vista.setVisible(false);
            setActivo(false);
        }
    }
}
