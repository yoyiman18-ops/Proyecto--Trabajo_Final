package controlador;

import java.awt.geom.Rectangle2D;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.Moneda;
import modelo.Personaje;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;

public final class MonedaControlador implements Controlador {
    private static final double RADIO = 9;
    private static final double DIAMETRO = RADIO * 2;

    private final Personaje jugador;
    private final Moneda modelo;
    private final Circle vista = new Circle(RADIO, Color.GOLD);
    private boolean recogida;

    public MonedaControlador(
        Personaje jugador,
        double x,
        double y,
        int valor
    ) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("La posición de la moneda debe ser finita.");
        }

        this.jugador = jugador;
        this.modelo = new Moneda(
            "moneda",
            new HitboxRectangular(
                new Rectangle2D.Double(0, 0, DIAMETRO, DIAMETRO),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ITEM),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            valor
        );
        modelo.getHitbox().actualizarTransformacion(modelo.getPosicion());
        vista.setStroke(Color.DARKGOLDENROD);
        vista.setStrokeWidth(2);
        actualizarVista();
    }

    @Override
    public Moneda getModelo() {
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
        if (dt == null || !Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException("El tiempo de actualización debe ser finito y no negativo.");
        }
        if (recogida || jugador.getVida() <= 0) {
            return;
        }

        if (modelo.getHitbox().intersecta(jugador.getHitbox())) {
            jugador.sumarMonedas(modelo.getValor());
            recogida = true;
            modelo.desactivar();
            if (vista.getParent() instanceof Pane escenario) {
                escenario.getChildren().remove(vista);
            }
        }
    }

    private void actualizarVista() {
        vista.setLayoutX(modelo.getPosicion().getX());
        vista.setLayoutY(modelo.getPosicion().getY());
    }
}
