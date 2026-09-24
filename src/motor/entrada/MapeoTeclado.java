package motor.entrada;
import javafx.scene.input.KeyCode;

public record MapeoTeclado(
    KeyCode tecla,
    TipoEntrada tipoEntrada,
    Accion accion
) {
    public KeyCode getTecla() { return this.tecla; }
    public TipoEntrada getTipoEntrada() { return this.tipoEntrada; }
    public Accion getAccion() { return this.accion; }
}
