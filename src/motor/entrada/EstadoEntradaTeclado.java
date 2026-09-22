package motor.entrada;
import java.security.Key;
import java.util.Set;
import javafx.scene.input.KeyCode;

public record EstadoEntradaTeclado(
    Set<KeyCode> presionadas,
    Set<KeyCode> mantenidas,
    Set<KeyCode> soltadas) {

    public EstadoEntradaTeclado(
        Set<KeyCode> presionadas,
        Set<KeyCode> mantenidas,
        Set<KeyCode> soltadas
    ) { 
        this.presionadas = presionadas;
        this.mantenidas = mantenidas;
        this.soltadas = soltadas;
    }

    public boolean esPresionada(KeyCode tecla) { return this.presionadas.contains(tecla); }
    public boolean esMantenida(KeyCode tecla) { return this.mantenidas.contains(tecla); }
    public boolean esSoltada(KeyCode tecla) { return this.soltadas.contains(tecla); }
}
