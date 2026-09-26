package motor.entrada;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.Node;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.Observador;

public class GestorEntradaTeclado {
    private final CapturadorEntradaTeclado capturador;
    private final List<MapeoTeclado> mapeos;
    private final EnumSet<Accion> acciones;
    private final NotificadorDebil<EstadoAcciones> notificador;

    public GestorEntradaTeclado(Node root) {
        this.mapeos = new CopyOnWriteArrayList<>();
        this.acciones = EnumSet.noneOf(Accion.class);
        this.capturador = new CapturadorEntradaTeclado();
        capturador.registrar(root);
        this.notificador = new NotificadorDebil<EstadoAcciones>();
    }

    public void añadirMapeo(MapeoTeclado mapeo) { if (!this.mapeos.contains(mapeo)) { this.mapeos.add(mapeo); }}
    public void eliminarMapeo(MapeoTeclado mapeo) { this.mapeos.remove(mapeo); }
    public void suscribir(Observador<EstadoAcciones> o) { this.notificador.suscribirObservador(o); }
    public void desuscribir(Observador<EstadoAcciones> o) { this.notificador.desuscribirObservador(o); }
    public void tick() {
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
