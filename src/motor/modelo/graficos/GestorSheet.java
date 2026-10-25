package motor.modelo.graficos;
import javafx.geometry.Rectangle2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.Observador;

public abstract class GestorSheet {
    private final Sheet spriteSheet;
    private final Notificador<Rectangle2D> notificador;

    public GestorSheet(Sheet spriteSheet) {
        if (spriteSheet ==  null) { throw new IllegalArgumentException("SpriteSheet nula no valida."); }
        this.spriteSheet = spriteSheet;
        this.notificador = new NotificadorDebil<>();
    }

    public void suscribir(Observador<Rectangle2D> o) { this.notificador.suscribirObservador(o); }
    public void desuscribir(Observador<Rectangle2D> o) { this.notificador.desuscribirObservador(o); }

    protected void notificar() { this.notificador.notificar(getRectangulo()); }
    protected abstract Rectangle2D getRectangulo();
    protected Sheet getSheet() { return this.spriteSheet; }
}
