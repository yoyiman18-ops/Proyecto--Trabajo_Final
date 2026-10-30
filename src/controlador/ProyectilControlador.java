package controlador;

import java.util.Objects;
import java.util.function.Consumer;

import modelo.ModeloProyectil;
import motor.Controlador.ControladorEntidad;
import motor.colisiones.Colisionable;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.util.observer.Observador;
import vista.VistaImagen;;

public final class ProyectilControlador implements ControladorEntidad {
    private final ModeloProyectil modelo;
    private final VistaImagen vista;
    private final Observador<Integer> observadorExpiracion;
    private final GestorSheetAnimada gestorSheet;
    private boolean activo;

    public ProyectilControlador(
        ModeloProyectil modelo,
        VistaImagen vista,
        GestorSheetAnimada gestorSheet,
        Consumer<Colisionable> accionImpacto
    ) {
        this.gestorSheet = gestorSheet;
        this.modelo = Objects.requireNonNull(modelo, "El modelo del proyectil no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del proyectil no puede ser nula");
        this.observadorExpiracion = e -> this.alExpirar(e);
        this.modelo.getNotificadorExpiracion().suscribirObservador(observadorExpiracion);
        this.gestorSheet.setAnimacion("idle", false);
        this.gestorSheet.suscribir(this.vista.getObservadorRectangulo());
        this.modelo.getNotificadorPosicion().suscribirObservador(this.vista.getObservadorPosicion());
    }

    @Override public boolean activo() { return this.activo; };
    @Override public void setActivo(boolean activo) { this.activo = activo; }
    @Override public ModeloEntidad getEntidad() { return this.modelo; }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        modelo.tick(dt);
    }

    private void alExpirar(int e) {
        gestorSheet.setAnimacion("expirar", false);
        gestorSheet.getNotificadorFinAnimacion().suscribirObservador(
            f -> { vista.setVisible(false); setActivo(false); }
        );
    }
}


