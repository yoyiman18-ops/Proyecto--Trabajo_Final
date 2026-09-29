package controlador;

import modelo.Personaje;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.recursos.GestorRecursos;
import motor.util.observer.Observador;
import motor.recursos.Extension.Imagen;
import vista.SpriteVista;
import java.awt.geom.Rectangle2D;

public class JugadorControlador implements Observador<EstadoAcciones> {
    private final String nombre;
    private final SpriteVista vista;
    private final Personaje modelo;

    public JugadorControlador(String nombre, GestorRecursos recursos) {
        this.nombre = nombre;
        this.vista = new SpriteVista(recursos.getImagen(nombre, Imagen.PNG));
        this.modelo = new Personaje.Builder(
            nombre,
            new HitboxRectangular(
                new Rectangle2D.Double(0,0,30,20),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(CategoriaColision.ENEMIGO, CategoriaColision.EXP, CategoriaColision.ITEM, CategoriaColision.MURO)
            ))
            .build();
    }

    @Override
    public void cambio(EstadoAcciones e) {
        if (!(
            e.activa(Accion.MOVER_ARRIBA) || 
            e.activa(Accion.MOVER_ABAJO) || 
            e.activa(Accion.MOVER_DERECHA) || 
            e.activa(Accion.MOVER_IZQUIERDA)
        )) {} else {
            if (e.activa(Accion.MOVER_ARRIBA)) {}
            else if (e.activa(Accion.MOVER_ABAJO)) {}
            if (e.activa(Accion.MOVER_DERECHA)) {}
            else if (e.activa((Accion.MOVER_IZQUIERDA))) {}
        }
    }
}
