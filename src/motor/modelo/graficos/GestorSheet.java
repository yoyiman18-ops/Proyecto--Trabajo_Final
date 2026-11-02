package motor.modelo.graficos;
import javafx.geometry.Rectangle2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.Observador;

public abstract class GestorSheet {
    private final Sheet spriteSheet;
    private final Notificador<Rectangle2D> notificadorRectangulo;
    private final Observador<String> observadorAnimacion;

    public GestorSheet(Sheet spriteSheet) {
        if (spriteSheet ==  null) { throw new IllegalArgumentException("SpriteSheet nula no valida."); }
        this.spriteSheet = spriteSheet;
        this.notificadorRectangulo = new NotificadorDebil<>();
        this.observadorAnimacion = this::setAnimacion;
    }

    public void suscribir(Observador<Rectangle2D> o) { this.notificadorRectangulo.suscribirObservador(o); o.cambio(getRectangulo()); }
    public void desuscribir(Observador<Rectangle2D> o) { this.notificadorRectangulo.desuscribirObservador(o); }
    public void notificar() { this.notificadorRectangulo.notificar(getRectangulo()); }
    public Observador<String> getObservadorAnimacion() { return this.observadorAnimacion; }

    protected Sheet getSheet() { return this.spriteSheet; }

    protected abstract Rectangle2D getRectangulo();
    public abstract boolean setAnimacion(String nombre);
}
