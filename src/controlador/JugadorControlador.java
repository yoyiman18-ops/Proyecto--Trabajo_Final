package controlador;

import modelo.Personaje;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.modelo.Direccion;
import motor.modelo.Entidad;
import motor.recursos.GestorRecursos;
import motor.util.Eje;
import motor.util.observer.Observador;
import motor.recursos.Extension.Imagen;
import vista.SpriteVista;

import javafx.scene.Node;

public class JugadorControlador 
implements Observador<EstadoAcciones>, Controlador {
    private final String nombre;
    private final SpriteVista vista;
    private final Personaje modelo;

    public JugadorControlador(String nombre, GestorRecursos recursos) {
        this.nombre = nombre;
        this.vista = new SpriteVista(
            recursos.getImagen(nombre, Imagen.PNG),
            new javafx.geometry.Rectangle2D(0, 0, 30, 30),
            30,
            30
        );

        this.modelo = new Personaje.Builder(
            nombre,
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0,0,30,20),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(CategoriaColision.ENEMIGO, CategoriaColision.EXP, CategoriaColision.ITEM, CategoriaColision.MURO)
            ))
            .build();
        
        modelo.getNotificador().suscribirObservador(vista);
    }

    @Override 
    public void tick(Double dt) {
        modelo.mover();
    }

    @Override public Entidad getModelo() { return this.modelo; }
    @Override public Node getVista() { return this.vista; }

    @Override
    public void cambio(EstadoAcciones e) {
        if (e.activa(Accion.MOVER_ARRIBA)) { modelo.acelerar(Direccion.ARRIBA); }
        else if (e.activa(Accion.MOVER_ABAJO)) { modelo.acelerar(Direccion.ABAJO); }
        else { modelo.frenar(Eje.Y); }
        if (e.activa(Accion.MOVER_DERECHA)) { modelo.acelerar(Direccion.DERECHA); }
        else if (e.activa((Accion.MOVER_IZQUIERDA))) { modelo.acelerar(Direccion.IZQUIERDA); }
        else { modelo.frenar(Eje.X); }
    }
}
