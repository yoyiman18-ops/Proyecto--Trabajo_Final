package motor.entrada;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.Observador;

public class GestorEntradaTeclado {
    private static final Logger logger = Logger.getLogger(GestorEntradaTeclado.class.getName());
    private final CapturadorEntradaTeclado capturador;
    private final List<MapeoTeclado> mapeos;
    private final EnumSet<Accion> acciones;
    private final NotificadorDebil<EstadoAcciones> notificador;
    private Scene escenaRoot;

    public GestorEntradaTeclado() {
        this.mapeos = new CopyOnWriteArrayList<>();
        this.acciones = EnumSet.noneOf(Accion.class);
        this.capturador = new CapturadorEntradaTeclado();
        this.notificador = new NotificadorDebil<EstadoAcciones>();
    }
    public void setEscenaRoot(Scene escenaRoot) { this.escenaRoot = escenaRoot; capturador.registrar(escenaRoot); }
    public void añadirMapeo(MapeoTeclado mapeo) { if (!this.mapeos.contains(mapeo)) { this.mapeos.add(mapeo); }}
    public void eliminarMapeo(MapeoTeclado mapeo) { this.mapeos.remove(mapeo); }
    public void suscribir(Observador<EstadoAcciones> o) { this.notificador.suscribirObservador(o); }
    public void desuscribir(Observador<EstadoAcciones> o) { this.notificador.desuscribirObservador(o); }
    public void tick() {
        if (this.escenaRoot == null) { return; }
        capturador.iniciarFrame();
        EstadoEntradaTeclado estado = capturador.getEstado();
        mapear(estado);
        notificador.notificar(new EstadoAcciones(acciones));
    }

    private void mapear(EstadoEntradaTeclado estadoEntrada) {
        this.acciones.clear();
        for (MapeoTeclado m : this.mapeos) {
            switch (m.getTipoEntrada()) {
                case TipoEntrada.MANTENER -> { if (estadoEntrada.esMantenida(m.getTecla())) { acciones.add(m.getAccion()); }}
                case TipoEntrada.PRESIONAR -> { if (estadoEntrada.esPresionada(m.getTecla())) { acciones.add(m.getAccion()); }}
                case TipoEntrada.SOLTAR -> { if (estadoEntrada.esSoltada(m.getTecla())) { acciones.add(m.getAccion()); }}
            }

        }
    }

}
