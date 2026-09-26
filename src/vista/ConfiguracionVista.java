package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ConfiguracionVista extends VBox {
	private static final double ANCHO = 360;
	private static final double ALTO = 460;

	private final Label titulo = new Label("Configuración");
	private final VBox opciones = new VBox();
	private final Button botonVolver = new Button("VOLVER");

	public ConfiguracionVista() {
		setPrefSize(ANCHO, ALTO);
		setMaxSize(ANCHO, ALTO);
		setSpacing(18);
		setPadding(new Insets(24));
		setAlignment(Pos.TOP_LEFT);
		setStyle("-fx-background-color: rgba(24,33,43,0.96);"
				+ "-fx-background-radius: 12;"
				+ "-fx-border-color: #526779;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-radius: 12;");

		titulo.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

		opciones.setSpacing(12);
		opciones.setFillWidth(true);

		getChildren().addAll(titulo, opciones, botonVolver);
	}

	public VBox getOpciones() {
		return opciones;
	}

	public Button getBotonVolver() {
		return botonVolver;
	}
}
