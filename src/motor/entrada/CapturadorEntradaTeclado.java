package motor.entrada;
import java.util.EnumSet;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CapturadorEntradaTeclado {
    
    private final Set<KeyCode> teclasSoltadas,teclasPresionadas,teclasSostenidas;

    public CapturadorEntradaTeclado() {
        this.teclasSoltadas= EnumSet.noneOf(KeyCode.class);
        this.teclasSostenidas = EnumSet.noneOf(KeyCode.class);
        this.teclasPresionadas = EnumSet.noneOf(KeyCode.class);
    }

    public EstadoEntradaTeclado getEstado() {
        return new EstadoEntradaTeclado(
            teclasPresionadas,
            teclasPresionadas,
            teclasPresionadas
        );}

    public void iniciarFrame() { 
        this.teclasPresionadas.clear(); 
        this.teclasSoltadas.clear(); 
    }

    public void registrar(Scene escena) {
        escena.addEventHandler(
            KeyEvent.KEY_PRESSED, 
            this::registrarTecla
        );
        escena.addEventHandler(
            KeyEvent.KEY_RELEASED, 
            this::soltarTecla
        );
    }

    public void deregistrar(Scene escena) {
        escena.removeEventHandler(
            KeyEvent.KEY_PRESSED, 
            this::registrarTecla
        );
        escena.removeEventHandler(
            KeyEvent.KEY_RELEASED, 
            this::soltarTecla
        );
    }

    private void registrarTecla(KeyEvent e) { 
        this.teclasSostenidas.add(e.getCode());
        this.teclasPresionadas.add(e.getCode()); 
    }

    private void soltarTecla(KeyEvent e) { 
        this.teclasSostenidas.remove(e.getCode());
        this.teclasSoltadas.add(e.getCode());
    }

}

