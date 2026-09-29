package controlador;

import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.modelo.Entidad;
import motor.recursos.GestorRecursos;
import motor.util.observer.Observador;
import motor.recursos.Extension.Imagen;
import vista.SpriteVista;

public class JugadorControlador implements Observador<EstadoAcciones> {
    private final String nombre;
    private final SpriteVista vista;
    private final Entidad modelo;

    public JugadorControlador(String nombre, GestorRecursos recursos) {
        this.nombre = nombre;
        this.vista = new SpriteVista(recursos.getImagen(nombre, Imagen.PNG));
        this.modelo = new Entidad.Builder(nombre).build(recursos);
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
