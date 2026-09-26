package motor.entrada;
import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CapturadorEntradaTeclado {
    
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

    public void registrar(Node nodo) {
        nodo.addEventHandler(
            KeyEvent.KEY_PRESSED, 
            this::presionarTecla
        );
        nodo.addEventHandler(
            KeyEvent.KEY_RELEASED, 
            this::soltarTecla
        );
    }

    public void deregistrar(Node nodo) {
        nodo.removeEventHandler(
            KeyEvent.KEY_PRESSED,
            this::presionarTecla
        );
        nodo.removeEventHandler(
            KeyEvent.KEY_RELEASED,
            this::soltarTecla
        );
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

