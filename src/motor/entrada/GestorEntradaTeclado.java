package motor.entrada;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import motor.modelo.Ticking;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.Observador;

public class GestorEntradaTeclado implements Ticking {
    private static final Logger logger = Logger.getLogger(GestorEntradaTeclado.class.getName());
    private final CapturadorEntradaTeclado capturador;
    private final List<MapeoTeclado> mapeos;
    private final EnumSet<Accion> acciones;
    private final NotificadorDebil<EstadoAcciones> notificador;
    private Scene escenaRoot;

    public GestorEntradaTeclado() {
        this(new CapturadorEntradaTeclado());
    }

    public GestorEntradaTeclado(CapturadorEntradaTeclado capturador) {
        this.capturador = Objects.requireNonNull(capturador, "El capturador de entrada de teclado no puede ser nulo");
        this.mapeos = new CopyOnWriteArrayList<>();
        this.acciones = EnumSet.noneOf(Accion.class);
        this.notificador = new NotificadorDebil<EstadoAcciones>();
    }
    public void setEscenaRoot(Scene escenaRoot) { this.escenaRoot = escenaRoot; capturador.registrar(escenaRoot); }
    public void añadirMapeo(MapeoTeclado mapeo) { if (!this.mapeos.contains(mapeo)) { this.mapeos.add(mapeo); }}
    public void eliminarMapeo(MapeoTeclado mapeo) { this.mapeos.remove(mapeo); }
    public void suscribir(Observador<EstadoAcciones> o) { this.notificador.suscribirObservador(o); }
    public void desuscribir(Observador<EstadoAcciones> o) { this.notificador.desuscribirObservador(o); }
    @Override public void tick(double dt) {
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
