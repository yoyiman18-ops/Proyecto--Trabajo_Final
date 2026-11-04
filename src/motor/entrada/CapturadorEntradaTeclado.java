package motor.entrada;
import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CapturadorEntradaTeclado {
    private final static Logger logger = Logger.getLogger(CapturadorEntradaTeclado.class.getName());
    private final EventHandler<KeyEvent> capturaPresionar = e -> presionarTecla(e);
    private final EventHandler<KeyEvent> capturaSoltar = e -> soltarTecla(e);

    private final Set<KeyCode> soltadas,presionadas,mantenidas;
    private final Queue<EstadoEntradaTeclado> bufferEntrada;

    public CapturadorEntradaTeclado() {
        this.soltadas= EnumSet.noneOf(KeyCode.class);
        this.mantenidas = EnumSet.noneOf(KeyCode.class);
        this.presionadas = EnumSet.noneOf(KeyCode.class);
        this.bufferEntrada = new ArrayDeque<>();
    }

    public EstadoEntradaTeclado getEstado() { return bufferEntrada.poll(); }

    public void iniciarFrame() { 
        encolarEstado();
        this.presionadas.clear(); 
        this.soltadas.clear(); 
    }

    public void registrar(Scene escena) {
        escena.addEventFilter(KeyEvent.KEY_PRESSED, capturaPresionar);
        escena.addEventFilter(KeyEvent.KEY_RELEASED, capturaSoltar);
    }

    public void deregistrar(Scene escena) {
        escena.removeEventFilter(KeyEvent.KEY_PRESSED, capturaPresionar);
        escena.removeEventFilter(KeyEvent.KEY_RELEASED, capturaSoltar);
    }

    private void encolarEstado() {
        this.bufferEntrada.add(new EstadoEntradaTeclado(presionadas, mantenidas, soltadas));
    }

    private void presionarTecla(KeyEvent e) { 
        if (!(this.mantenidas.contains(e.getCode()))) { this.presionadas.add(e.getCode()); }
        this.mantenidas.add(e.getCode());
    }

    private void soltarTecla(KeyEvent e) { 
        this.soltadas.add(e.getCode());
        this.mantenidas.remove(e.getCode());
    }

}

