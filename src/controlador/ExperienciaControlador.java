package controlador;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.Personaje;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;

public final class ExperienciaControlador implements Controlador {
    private static final double DISTANCIA_RECOLECCION = 38;

    private final Personaje jugador;
    private final int cantidad;
    private final Entidad modelo;
    private final Circle vista = new Circle(9, Color.web("#83df4e"));
    private boolean recogida;

    public ExperienciaControlador(Personaje jugador, double x, double y, int cantidad) {
        if (jugador == null || cantidad <= 0) {
            throw new IllegalArgumentException("El jugador y una cantidad positiva de XP son obligatorios.");
        }
        this.jugador = jugador;
        this.cantidad = cantidad;
        this.modelo = new Entidad(
            "experiencia",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 18, 18),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.EXP),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            new VecDouble2D(),
            0,
            0,
            0
        );
        vista.setStroke(Color.web("#e4ff9b"));
        vista.setStrokeWidth(2);
        actualizarVista();
    }

    @Override
    public Entidad getModelo() {
        return modelo;
    }

    @Override
    public Node getVista() {
        return vista;
    }

    public boolean fueRecogida() {
        return recogida;
    }

    @Override
    public void tick(Double dt) {
        if (recogida || jugador.getVida() <= 0) {
            return;
        }

        double diferenciaX = jugador.getPosicion().getX() + 25 - modelo.getPosicion().getX();
        double diferenciaY = jugador.getPosicion().getY() + 25 - modelo.getPosicion().getY();
        if (Math.hypot(diferenciaX, diferenciaY) <= DISTANCIA_RECOLECCION) {
            jugador.ganarExperiencia(cantidad);
            recogida = true;
        }
    }

    private void actualizarVista() {
        vista.setLayoutX(modelo.getPosicion().getX());
        vista.setLayoutY(modelo.getPosicion().getY());
    }
}