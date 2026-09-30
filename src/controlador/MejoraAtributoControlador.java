package controlador;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.ItemMejoraAtributo;
import modelo.Personaje;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;

public final class MejoraAtributoControlador implements Controlador {
    private static final double DISTANCIA_RECOLECCION = 38;

    private final Personaje jugador;
    private final ItemMejoraAtributo mejora;
    private final Entidad modelo;
    private final Circle vista = new Circle(11, Color.web("#e8bd45"));
    private boolean recogida;

    public MejoraAtributoControlador(
        Personaje jugador,
        ItemMejoraAtributo mejora,
        double x,
        double y
    ) {
        if (jugador == null || mejora == null) {
            throw new IllegalArgumentException("El jugador y la mejora son obligatorios.");
        }
        this.jugador = jugador;
        this.mejora = mejora;
        this.modelo = new Entidad(
            "mejora-atributo",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 22, 22),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ITEM),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            new VecDouble2D(),
            0,
            0,
            0
        );
        vista.setStroke(Color.web("#fff0a6"));
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
            mejora.aplicar(jugador);
            recogida = true;
        }
    }

    private void actualizarVista() {
        vista.setLayoutX(modelo.getPosicion().getX());
        vista.setLayoutY(modelo.getPosicion().getY());
    }
}