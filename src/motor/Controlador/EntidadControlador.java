package motor.controlador;

import java.util.Objects;

import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.util.observer.Observador;
import motor.vista.VistaImagen;

public class EntidadControlador implements ControladorEntidad {
    private final ModeloEntidad modelo;
    private final VistaImagen vista;
    private final Observador<Integer> observadorExpiracion;
    private final Observador<Integer> observadorSonido;
    private final GestorSheetAnimada gestorSheet;
    private boolean activo;

    public EntidadControlador(
        ModeloEntidad modelo,
        VistaImagen vista,
        GestorSheetAnimada gestorSheet
    ) {
        this(modelo, vista, gestorSheet, id -> {});
    }

    public EntidadControlador(
        ModeloEntidad modelo,
        VistaImagen vista,
        GestorSheetAnimada gestorSheet,
        Observador<Integer> observadorSonido
    ) {
        this.activo = true;
        this.gestorSheet = Objects.requireNonNull(gestorSheet, "El gestor de sheet no puede ser nulo");
        this.modelo = Objects.requireNonNull(modelo, "El modelo de la entidad no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista de la entidad no puede ser nula");
        this.observadorSonido = Objects.requireNonNull(observadorSonido, "El observador de sonido no puede ser nulo");
        this.observadorExpiracion = this::alExpirar;
        this.modelo.getNotificadorExpiracion().suscribirObservador(observadorExpiracion);
        this.gestorSheet.setAnimacion(modelo.getAnimacionDefault());
        this.gestorSheet.suscribir(this.vista.getObservadorRectangulo());
        this.modelo.getNotificadorAnimacion().suscribirObservador(this.gestorSheet.getObservadorAnimacion());
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
        gestorSheet.getNotificadorFinAnimacion().suscribirObservador(
            f -> { vista.setVisible(false); setActivo(false); }
        );
    }
}
