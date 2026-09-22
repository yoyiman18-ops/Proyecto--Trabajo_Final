package motor.entrada;
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
}
