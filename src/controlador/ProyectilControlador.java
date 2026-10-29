package controlador;

import java.util.Objects;
import java.util.function.Consumer;

import javafx.scene.Node;
import modelo.ModeloProyectil;
import motor.colisiones.Colisionable;
import motor.modelo.Activable;
import motor.modelo.Entidad;
import motor.modelo.Ticking;
import motor.util.observer.Observador;
import vista.VistaImagen;
import vista.VistaImagenFX;

public final class ProyectilControlador implements Ticking,Activable {
    private final ModeloProyectil modelo;
    private final VistaImagen vista;
    private final Observador<Integer> observadorExpiracion;
    private boolean activo;

    public ProyectilControlador(
        ModeloProyectil modelo,
        VistaImagen vista,
        Consumer<Colisionable> accionImpacto
    ) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo del proyectil no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del proyectil no puede ser nula");
        this.observadorExpiracion = e -> { System.out.println("Hola"); };
        this.modelo.getNotificadorExpiracion().suscribirObservador(observadorExpiracion);
    }

    @Override public boolean activo() { return modelo.activo(); };
    @Override public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        modelo.tick(dt);
    }
}


