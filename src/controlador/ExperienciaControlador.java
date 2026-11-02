package controlador;

import java.util.Objects;

import modelo.ModeloExperiencia;
import motor.controlador.ControladorEntidad;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.util.observer.Observador;
import motor.vista.VistaImagen;

public final class ExperienciaControlador implements ControladorEntidad {
    private final ModeloExperiencia modelo;
    private final VistaImagen vista;
    private final GestorSheetAnimada gestorSheet;
    private final Observador<Integer> observadorExpiracion;
    private final Observador<Integer> observadorSonido;
    private boolean activo;

    public ExperienciaControlador(
        ModeloExperiencia modelo,
        VistaImagen vista,
        GestorSheetAnimada gestorSheet,
        Observador<Integer> observadorSonido
    ) {
        this.gestorSheet = Objects.requireNonNull(gestorSheet, "El gestor de sheet no puede ser nulo");
        this.modelo = Objects.requireNonNull(modelo, "El modelo de experiencia no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista de experiencia no puede ser nula");
        this.observadorSonido = Objects.requireNonNull(observadorSonido, "El observador de sonido no puede ser nulo");
        this.observadorExpiracion = this::alExpirar;
        this.activo = true;
        this.modelo.getNotificadorExpiracion().suscribirObservador(observadorExpiracion);
        this.gestorSheet.setAnimacion("idle");
        this.gestorSheet.suscribir(this.vista.getObservadorRectangulo());
        this.modelo.getNotificadorPosicion().suscribirObservador(this.vista.getObservadorPosicion());
        this.modelo.getNotificadorSonido().suscribirObservador(this.observadorSonido);
        this.vista.setPosicion(this.modelo.getPosicion());
    }

    @Override public boolean activo() { return this.activo; }
    @Override public void setActivo(boolean activo) { this.activo = activo; }
    @Override public ModeloEntidad getEntidad() { return this.modelo; }
    @Override public VistaImagen getVista() { return this.vista; }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        modelo.tick(dt);
        gestorSheet.tick(dt);
    }

    private void alExpirar(int e) {
        if (gestorSheet.setAnimacion("expirar")) {
            gestorSheet.getNotificadorFinAnimacion().suscribirObservador(
                f -> { vista.setVisbilidad(false); setActivo(false); }
            );
        } else {
            vista.setVisbilidad(false);
            setActivo(false);
        }
    }
}
