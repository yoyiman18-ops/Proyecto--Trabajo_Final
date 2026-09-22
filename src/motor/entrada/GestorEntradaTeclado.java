package motor.entrada;

import java.util.EnumSet;

import javafx.scene.input.KeyCode;
import motor.util.Notificador;

public class GestorEntradaTeclado {
    private final CapturadorEntradaTeclado capturador;
    private final EnumSet<Accion> acciones;

    private final Notificador<EnumSet<Accion>> notificador;

    public GestorEntradaTeclado() {
        this.capturador = new CapturadorEntradaTeclado();
        this.acciones = EnumSet.noneOf(Accion.class);
        this.notificador = new Notificador<EnumSet<Accion>>();
    }

    public void tick() {
        acciones.clear();
        mapearAcciones(capturador.getEstado());
        notificador.notificar(acciones);
        capturador.iniciarFrame();
    }

    private void mapearAcciones(EstadoEntradaTeclado entrada) {
        if (entrada.esPresionada(KeyCode.SPACE)) { acciones.add(Accion.TEST); 
        if (entrada.esMantenida(KeyCode.A)) { acciones.add(Accion.MOVER_IZQUIERDA); }
        else if (entrada.esMantenida(KeyCode.D)) { acciones.add(Accion.MOVER_DERECHA); }

        }
    }

}
