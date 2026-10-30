package motor.modelo.graficos;
import javafx.geometry.Rectangle2D;
import motor.modelo.Ticking;
import motor.util.ContadorSegundos;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;
import motor.util.observer.Observador;

public class GestorSheetAnimada
extends GestorSheet
implements Ticking
{
    private Animacion animacionActual;
    private int indiceActual,indiceFinal;
    private final ContadorSegundos cs;
    private final Observador<Double> observadorCs;
    private final Notificador<Boolean> notificadorFinAnimacion;

    public GestorSheetAnimada(Sheet spriteSheet) {
        super(spriteSheet);
        this.indiceActual = 0;
        this.cs = new ContadorSegundos();
        this.observadorCs = s -> avanzarFrame((s.intValue()));
        this.cs.suscribirObservador(observadorCs);
        this.notificadorFinAnimacion = new NotificadorFuerte<>();
    }
    
    public boolean setAnimacion(String nombre, boolean conservarFrame) { 
        Animacion a = getSheet().getAnimacion(nombre);
        if (a == null) { return false; }
        this.animacionActual = a;
        this.indiceFinal = a.getFrames().size() - 1;
        cs.setIntervaloSegundos(((double) animacionActual.getFrames().getFirst().getDuracion()) / 1000);
        if (conservarFrame) { if (indiceFinal < indiceActual) { indiceActual = indiceFinal; }}
        else { indiceActual = 0; cs.reset(); }
        notificar();
        notificadorFinAnimacion.notificar(true);
        notificadorFinAnimacion.limpiarObservadores();
        return true;
    }

    public Notificador<Boolean> getNotificadorFinAnimacion() { return this.notificadorFinAnimacion; }

    // nota: solo funcionará bien con frames de misma longitud
    private boolean avanzarFrame(int numeroFrames) {
        if (animacionActual.esBucle()) { 
            this.indiceActual = (indiceActual + numeroFrames) % (indiceFinal + 1);
            notificar();
            return true;
        }
        else {
            if (fin()) { return false; }
            this.indiceActual = Math.min(indiceActual + numeroFrames, indiceFinal);
            notificar();
            if (fin()) { notificadorFinAnimacion.notificar(true); }
            return true;
        }
    }
    
    private boolean fin() { return indiceActual == indiceFinal; }

    @Override
    public void tick(double dt) { 
        if (animacionActual == null) { return; }
        cs.tick(dt);
    }

    @Override
    protected Rectangle2D getRectangulo() {
        return animacionActual.getFrames().get(indiceActual).getRecorte().getRectangulo();
    }
}
