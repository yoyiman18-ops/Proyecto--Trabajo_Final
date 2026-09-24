package motor.entrada;
import java.util.EnumSet;
import java.util.Set;
import javafx.scene.input.KeyCode;

/*
una snapshot de los 3 tipos de entrada que puede tener cada tecla
*/
public record EstadoEntradaTeclado (
    Set<KeyCode> presionadas,
    Set<KeyCode> mantenidas,
    Set<KeyCode> soltadas)  {

    public EstadoEntradaTeclado(
        Set<KeyCode> presionadas,
        Set<KeyCode> mantenidas,
        Set<KeyCode> soltadas
    ) { 
        this.presionadas = EnumSet.copyOf(presionadas);
        this.mantenidas = EnumSet.copyOf(mantenidas);
        this.soltadas = EnumSet.copyOf(soltadas);
    }

    public boolean esPresionada(KeyCode tecla) { return this.presionadas.contains(tecla); }
    public boolean esMantenida(KeyCode tecla) { return this.mantenidas.contains(tecla); }
    public boolean esSoltada(KeyCode tecla) { return this.soltadas.contains(tecla); }
    public String getInfo(KeyCode tecla) { return String.format(
        "TECLA: %5s | PRESIONA: %5b | MANTIENE: %5b | SUELTA: %5b",
        tecla.toString(),
        esPresionada(tecla),
        esMantenida(tecla),
        esSoltada(tecla));}
}
