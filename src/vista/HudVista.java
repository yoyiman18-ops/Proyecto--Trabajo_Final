package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HudVista extends VBox {
    private final Label vida = new Label();
    private final Label danio = new Label();
    private final Label puntos = new Label();
    private final Label oleada = new Label();


    public HudVista() {
        setSpacing(6);
        setPrefWidth(220);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_LEFT);
        setStyle("-fx-background-color: rgba(0,0,0,0.35); -fx-background-radius: 8;");

        getChildren().addAll(vida, danio, puntos, oleada);
    }

    public void actualizarVida(int actual, int maximo) {
        vida.setText("Vida: " + actual + "/" + maximo);
    }

    public void actualizarDanio(int valor) {
        danio.setText("Daño: " + valor);
    }

    public void actualizarPuntos(int actual, int siguiente) {
        puntos.setText("Puntos: " + actual + "/" + siguiente);
    }

    public void actualizarOleada(int numero) {
        oleada.setText("Oleada: " + numero);
    }


}