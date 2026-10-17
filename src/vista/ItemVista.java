package vista;

import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class ItemVista extends Circle implements VistaItem {
    public ItemVista(double radio, Color relleno, Color borde, double anchoBorde) {
        super();
        if (!Double.isFinite(radio) || radio <= 0) {
            throw new IllegalArgumentException("El radio debe ser positivo y finito.");
        }
        Objects.requireNonNull(relleno, "El color de relleno no puede ser nulo");
        Objects.requireNonNull(borde, "El color de borde no puede ser nulo");
        if (!Double.isFinite(anchoBorde) || anchoBorde < 0) {
            throw new IllegalArgumentException("El ancho del borde debe ser finito y no negativo.");
        }
        setRadius(radio);
        setFill(relleno);
        setStroke(borde);
        setStrokeWidth(anchoBorde);
    }

    @Override
    public Node getNodo() {
        return this;
    }

    @Override
    public void actualizarPosicion(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}
