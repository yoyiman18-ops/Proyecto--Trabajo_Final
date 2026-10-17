package controlador;

import java.awt.geom.Rectangle2D;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.Proyectil;
import modelo.ProyectilAliado;
import modelo.ProyectilEnemigo;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;

public final class ProyectilControlador implements Controlador {
    private static final double RADIO_VISTA = 5;
    private static final double TAMANIO_HITBOX = RADIO_VISTA * 2;
    private static final Color COLOR_ALIADO = Color.ORANGERED;
    private static final Color COLOR_ENEMIGO = Color.MEDIUMPURPLE;

    private final Proyectil modelo;
    private final Circle vista;

    private ProyectilControlador(Proyectil modelo, Color color) {
        if (modelo == null) {
            throw new IllegalArgumentException("El proyectil no puede ser nulo.");
        }
        this.modelo = modelo;
        this.vista = new Circle(RADIO_VISTA, color);
        this.vista.setStroke(Color.WHITE);
        this.modelo.getHitbox().actualizarTransformacion(modelo.getPosicion());
        actualizarVista();
    }

    public static ProyectilControlador aliado(
        String nombre,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double danio,
        double tiempoVida,
        int penetracion
    ) {
        validarNombreYPosicion(nombre, posicion);
        HitboxRectangular hitbox = crearHitbox(
            CategoriaColision.ALIADO,
            CategoriaColision.ENEMIGO,
            CategoriaColision.MURO
        );
        return new ProyectilControlador(
            new ProyectilAliado(
                nombre,
                hitbox,
                posicion,
                direccion,
                velocidad,
                danio,
                tiempoVida,
                penetracion
            ),
            COLOR_ALIADO
        );
    }

    public static ProyectilControlador enemigo(
        String nombre,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double danio,
        double tiempoVida,
        int penetracion
    ) {
        validarNombreYPosicion(nombre, posicion);
        HitboxRectangular hitbox = crearHitbox(
            CategoriaColision.ENEMIGO,
            CategoriaColision.JUGADOR,
            CategoriaColision.ALIADO,
            CategoriaColision.MURO
        );
        return new ProyectilControlador(
            new ProyectilEnemigo(
                nombre,
                hitbox,
                posicion,
                direccion,
                velocidad,
                danio,
                tiempoVida,
                penetracion
            ),
            COLOR_ENEMIGO
        );
    }

    private static HitboxRectangular crearHitbox(
        CategoriaColision categoria,
        CategoriaColision... capas
    ) {
        return new HitboxRectangular(
            new Rectangle2D.Double(0, 0, TAMANIO_HITBOX, TAMANIO_HITBOX),
            TipoHitbox.SOLIDA,
            MascaraColision.of(categoria),
            MascaraColision.of(capas)
        );
    }

    private static void validarNombreYPosicion(
        String nombre,
        VecDouble2D posicion
    ) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (posicion == null) {
            throw new IllegalArgumentException("La posición no puede ser nula.");
        }
    }

    @Override
    public Proyectil getModelo() {
        return modelo;
    }

    public Proyectil getProyectil() {
        return modelo;
    }

    @Override
    public Node getVista() {
        return vista;
    }

    @Override
    public void tick(Double dt) {
        if (dt == null) {
            throw new IllegalArgumentException("El tiempo de actualización no puede ser nulo.");
        }
        modelo.actualizar(dt);
        actualizarVista();
    }

    public boolean estaActivo() {
        return modelo.estaActivo();
    }

    private void actualizarVista() {
        vista.setLayoutX(modelo.getPosicion().getX());
        vista.setLayoutY(modelo.getPosicion().getY());
    }
}
