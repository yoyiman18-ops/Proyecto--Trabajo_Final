package motor.entrada;
import javafx.scene.input.KeyCode;

public record MapeoTeclado(
    KeyCode tecla,
    TipoEntrada tipoEntrada,
    Accion accion
) {
    public MapeoTeclado(KeyCode tecla, TipoEntrada tipoEntrada, Accion accion) {
        if (tecla == null || tipoEntrada == null || accion == null) {
            throw new IllegalArgumentException("Mapeo con nulls inválido.");
        }
        this.tecla = tecla;
        this.tipoEntrada = tipoEntrada;
        this.accion = accion;
    }

    public KeyCode getTecla() { return this.tecla; }
    public TipoEntrada getTipoEntrada() { return this.tipoEntrada; }
    public Accion getAccion() { return this.accion; }
}
