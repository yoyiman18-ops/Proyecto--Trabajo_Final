package vista;

import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class ItemVista extends Circle implements VistaItem {
    public ItemVista(double radio, Color relleno, Color borde, double anchoBorde) {
        super(
            validarPositivo(radio, "El radio debe ser positivo y finito."),
            Objects.requireNonNull(relleno, "El color de relleno no puede ser nulo")
        );
        Objects.requireNonNull(borde, "El color de borde no puede ser nulo");
        if (!Double.isFinite(anchoBorde) || anchoBorde < 0) {
            throw new IllegalArgumentException("El ancho del borde debe ser finito y no negativo.");
        }
        setStroke(borde);
        setStrokeWidth(anchoBorde);
    }

    public static ItemVista crearExperiencia() {
        return new ItemVista(9, Color.web("#83df4e"), Color.web("#e4ff9b"), 2);
    }

    public static ItemVista crearMejoraAtributo() {
        return new ItemVista(11, Color.web("#e8bd45"), Color.web("#fff0a6"), 2);
    }

    private static double validarPositivo(double valor, String mensaje) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor;
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
