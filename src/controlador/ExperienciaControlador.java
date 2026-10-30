package controlador;

import java.util.Objects;

import modelo.ModeloExperiencia;
import motor.controlador.ControladorEntidad;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.util.observer.Observador;
import vista.VistaImagen;

public final class ExperienciaControlador implements ControladorEntidad {
    private final ModeloExperiencia modelo;
    private final VistaImagen vista;
    private final GestorSheetAnimada gestorSheet;
    private final Observador<Integer> observadorExpiracion;
    private boolean activo;

    public ExperienciaControlador(
        ModeloExperiencia modelo,
        VistaImagen vista,
        GestorSheetAnimada gestorSheet
    ) {
        this.gestorSheet = Objects.requireNonNull(gestorSheet, "El gestor de sheet no puede ser nulo");
        this.modelo = Objects.requireNonNull(modelo, "El modelo de experiencia no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista de experiencia no puede ser nula");
        this.observadorExpiracion = this::alExpirar;
        this.activo = true;
        this.modelo.getNotificadorExpiracion().suscribirObservador(observadorExpiracion);
        this.gestorSheet.setAnimacion("idle", false);
        this.gestorSheet.suscribir(this.vista.getObservadorRectangulo());
        this.modelo.getNotificadorPosicion().suscribirObservador(this.vista.getObservadorPosicion());
        this.vista.setPosicion(this.modelo.getPosicion());
    }

    @Override public boolean activo() { return this.activo; }
    @Override public void setActivo(boolean activo) { this.activo = activo; }
    @Override public ModeloEntidad getEntidad() { return this.modelo; }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        modelo.tick(dt);
        gestorSheet.tick(dt);
    }

    private void alExpirar(int e) {
        gestorSheet.setAnimacion("expirar", false);
        gestorSheet.getNotificadorFinAnimacion().suscribirObservador(
            f -> { vista.setVisible(false); setActivo(false); }
        );
    }
}
