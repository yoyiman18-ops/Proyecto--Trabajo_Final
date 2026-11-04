package motor.entrada;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import motor.modelo.Ticking;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.Observador;

public class GestorEntradaTeclado implements Ticking {
    private Scene escenaRoot;
    private final CapturadorEntradaTeclado capturador;
    private final List<MapeoTeclado> mapeos;
    private final NotificadorDebil<EstadoAcciones> notificador;
    private final Set<KeyCode> presionadas,mantenidas,soltadas;

    public GestorEntradaTeclado() {
        this(new CapturadorEntradaTeclado());
    }

    public GestorEntradaTeclado(CapturadorEntradaTeclado capturador) {
        this.capturador = Objects.requireNonNull(capturador, "El capturador de entrada de teclado no puede ser nulo");
        this.mapeos = new CopyOnWriteArrayList<>();
        this.notificador = new NotificadorDebil<EstadoAcciones>();
        this.soltadas = EnumSet.noneOf(KeyCode.class);
        this.mantenidas = EnumSet.noneOf(KeyCode.class);
        this.presionadas = EnumSet.noneOf(KeyCode.class);
    }
    public boolean setEscenaRoot(Scene escenaRoot) { 
        if (escenaRoot == null) { return false; }
        if (this.escenaRoot != null) { capturador.deregistrar(this.escenaRoot); }
        capturador.registrar(escenaRoot); return true;
    
    }
    public void añadirMapeo(MapeoTeclado mapeo) { if (!this.mapeos.contains(mapeo)) { this.mapeos.add(mapeo); }}
    public void eliminarMapeo(MapeoTeclado mapeo) { this.mapeos.remove(mapeo); }
    public void suscribir(Observador<EstadoAcciones> o) { this.notificador.suscribirObservador(o); }
    public void desuscribir(Observador<EstadoAcciones> o) { this.notificador.desuscribirObservador(o); }

    @Override public void tick(double dt) {

        notificador.notificar(
            mapear(generarEstado())
        );
    }

    private EstadoEntradaTeclado generarEstado() {
        EntradaTeclado entrada;
        soltadas.clear();
        presionadas.clear();
        while ((entrada = capturador.getEntrada()) != null) {
            if (entrada.tipo() == TipoEntrada.PRESIONAR) {
                if (!mantenidas.contains(entrada.tecla())) { presionadas.add(entrada.tecla()); }
                else { presionadas.add(entrada.tecla()); mantenidas.add(entrada.tecla()); }
            }
            else if (entrada.tipo() == TipoEntrada.SOLTAR) {
                soltadas.add(entrada.tecla()); mantenidas.remove(entrada.tecla());
            }
        }
        return new EstadoEntradaTeclado(presionadas,mantenidas,soltadas);
    }

    private EstadoAcciones mapear(EstadoEntradaTeclado estadoEntrada) {
        Set<Accion> acciones = EnumSet.noneOf(Accion.class);
        for (MapeoTeclado m : this.mapeos) {
            switch (m.getTipoEntrada()) {
                case TipoEntrada.MANTENER -> { if (estadoEntrada.esMantenida(m.getTecla())) { acciones.add(m.getAccion()); }}
                case TipoEntrada.PRESIONAR -> { if (estadoEntrada.esPresionada(m.getTecla())) { acciones.add(m.getAccion()); }}
                case TipoEntrada.SOLTAR -> { if (estadoEntrada.esSoltada(m.getTecla())) { acciones.add(m.getAccion()); }}
            }
        }
        return new EstadoAcciones(acciones);
    }

}
