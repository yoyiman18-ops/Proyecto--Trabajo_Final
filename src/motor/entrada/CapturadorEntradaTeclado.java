package motor.entrada;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class CapturadorEntradaTeclado {
    private final EventHandler<KeyEvent> observadorPresionar;
    private final EventHandler<KeyEvent> observadorSoltar;

    private final Queue<EntradaTeclado> bufferEntrada;

    public CapturadorEntradaTeclado() {
        this.bufferEntrada = new ConcurrentLinkedDeque<>();
        this.observadorPresionar = this::presionarTecla;
        this.observadorSoltar = this::soltarTecla;
    }

    public EntradaTeclado getEntrada() { return bufferEntrada.poll(); }

    public void registrar(Scene escena) {
        escena.addEventFilter(KeyEvent.KEY_PRESSED, observadorPresionar);
        escena.addEventFilter(KeyEvent.KEY_RELEASED, observadorSoltar);
    }

    public void deregistrar(Scene escena) {
        escena.removeEventFilter(KeyEvent.KEY_PRESSED, observadorPresionar);
        escena.removeEventFilter(KeyEvent.KEY_RELEASED, observadorSoltar);
    }

    private void presionarTecla(KeyEvent e) {
        bufferEntrada.add(new EntradaTeclado(e.getCode(), TipoEntrada.PRESIONAR));
        System.out.println("Presionada: " + e.getCode());    
    }

    private void soltarTecla(KeyEvent e) { 
        bufferEntrada.add(new EntradaTeclado(e.getCode(), TipoEntrada.SOLTAR));
    }

}

