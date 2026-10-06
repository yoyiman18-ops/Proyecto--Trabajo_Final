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
import motor.mvc.ControladorUsuario;
import motor.recursos.GestorRecursos;
import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import motor.recursos.Extension.Imagen;
import vista.SpriteVista;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class JugadorControlador implements ControladorUsuario {
    private final String nombre;
    private final SpriteVista vista;
    private final Personaje modelo;
    private final Observador<Personaje> observadorVida;
    private final Runnable accionAtaque;
    private EstadoAcciones estadoAcciones;
    private final Observador<EstadoAcciones> observadorAcciones;
    private boolean posicionInicializada;

    public JugadorControlador(String nombre, GestorRecursos recursos) {
        this(nombre, recursos, personaje -> {}, () -> {});
    }

    public JugadorControlador(
        String nombre,
        GestorRecursos recursos,
        Observador<Personaje> observadorVida
    ) {
        this(nombre, recursos, observadorVida, () -> {});
    }

    public JugadorControlador(
        String nombre,
        GestorRecursos recursos,
        Observador<Personaje> observadorVida,
        Runnable accionAtaque
    ) {
        this.nombre = nombre;
        this.observadorVida = observadorVida;
        this.accionAtaque = accionAtaque;
        this.observadorAcciones = e -> this.estadoAcciones = e;

        this.vista = new SpriteVista(
            recursos.getImagen("zorro_sheet", Imagen.PNG),
            4,
            4,
            50,
            50
        );

        this.modelo = new Personaje.Builder(
            nombre,
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0,0,30,20),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(CategoriaColision.ENEMIGO, CategoriaColision.EXP, CategoriaColision.ITEM, CategoriaColision.MURO)
            ))
            .friccion(2500)
            .aceleracion(3000)
            .velocidadMax(240)
            .build();
        
        modelo.getNotificador().suscribirObservador(vista);
        modelo.getNotificadorVida().suscribirObservador(this.observadorVida);
        this.observadorVida.cambio(modelo);
    }

    public void tick(Double dt) {
        if (estadoAcciones.activa(Accion.MOVER_ARRIBA)) { modelo.acelerar(Direccion.ARRIBA, dt); }
        else if (estadoAcciones.activa(Accion.MOVER_ABAJO)) { modelo.acelerar(Direccion.ABAJO, dt); }
        else { modelo.frenar(Eje.Y, dt); }

        if (estadoAcciones.activa(Accion.MOVER_DERECHA)) { modelo.acelerar(Direccion.DERECHA, dt); }
        else if (estadoAcciones.activa(Accion.MOVER_IZQUIERDA)) { modelo.acelerar(Direccion.IZQUIERDA, dt); }
        else { modelo.frenar(Eje.X, dt); }
        modelo.mover(dt);
        mantenerDentroDelArea();
        double vx = modelo.getVelocidad().getX();
        double vy = modelo.getVelocidad().getY();
        // implementación preliminar para lógica de animación, queda poder volverla
        // más genérica y abstracta 
        int fila;
        if (Math.abs(vx) > Math.abs(vy)) {
            if (vx > 0) {
                fila = 2;
            } else {
                fila = 3;
            }
        } else {
            if (vy > 0) {
                fila = 0;
            } else {
                fila = 1;
            }
        }
        vista.actualizarAnimacion(fila, (vx != 0 || vy != 0), dt);

        if (estadoAcciones.activa(Accion.ATACAR)) {
            accionAtaque.run();
        }
    }

    private void mantenerDentroDelArea() {
        if (!(vista.getParent() instanceof Pane area)) {
            return;
        }

        double anchoArea = area.getWidth();
        double altoArea = area.getHeight();
        if (anchoArea <= 0 || altoArea <= 0) {
            return;
        }

        VecDouble2D posicion = modelo.getPosicion();
        double anchoVista = vista.getLayoutBounds().getWidth();
        double altoVista = vista.getLayoutBounds().getHeight();
        boolean centrar = !posicionInicializada;
        if (centrar) {
            posicion.setX((anchoArea - anchoVista) / 2);
            posicion.setY((altoArea - altoVista) / 2);
            posicionInicializada = true;
        }

        double xAnterior = posicion.getX();
        double yAnterior = posicion.getY();
        double x = Math.max(0, Math.min(xAnterior, anchoArea - anchoVista));
        double y = Math.max(0, Math.min(yAnterior, altoArea - altoVista));
        boolean fueraEnX = x != xAnterior;
        boolean fueraEnY = y != yAnterior;

        if (centrar || fueraEnX || fueraEnY) {
            posicion.setX(x);
            posicion.setY(y);
            if (fueraEnX) { modelo.getVelocidad().setX(0); }
            if (fueraEnY) { modelo.getVelocidad().setY(0); }
            modelo.getHitbox().actualizarTransformacion(posicion);
            vista.cambio(modelo);
        }
    }

    @Override 
    public Observador<EstadoAcciones> getObservadorAcciones() { return this.observadorAcciones; }

    public Personaje getPersonaje() { return this.modelo; }

    @Override public Entidad getModelo() { return this.modelo; }
    @Override public Node getVista() { return this.vista; }

}
