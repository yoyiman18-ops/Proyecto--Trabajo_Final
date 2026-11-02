package modelo;

import java.util.Objects;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;

/**
 * Modelo de datos puro que representa una opción seleccionable en un menú.
 * Es agnóstico de la interfaz gráfica y notifica cambios de estado reactivamente
 * a través de notificadores observables del motor.
 */
public class OpcionMenu {

    private final String id;
    private String texto;
    private Runnable accion;
    private boolean habilitada;

    private final Notificador<Boolean> notificadorHabilitada;
    private final Notificador<String> notificadorTexto;

    public OpcionMenu(String texto, Runnable accion) {
        this(texto, texto, accion, true);
    }

    public OpcionMenu(String texto, Runnable accion, boolean habilitada) {
        this(texto, texto, accion, habilitada);
    }

    public OpcionMenu(String id, String texto, Runnable accion) {
        this(id, texto, accion, true);
    }

    public OpcionMenu(String id, String texto, Runnable accion, boolean habilitada) {
        this.id = Objects.requireNonNull(id, "El identificador de la opción no puede ser nulo").trim().toLowerCase();
        this.texto = Objects.requireNonNull(texto, "El texto de la opción no puede ser nulo");
        this.accion = Objects.requireNonNull(accion, "La acción de la opción no puede ser nula");
        this.habilitada = habilitada;
        this.notificadorHabilitada = new NotificadorFuerte<>();
        this.notificadorTexto = new NotificadorFuerte<>();
    }

    public String getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String nuevoTexto) {
        Objects.requireNonNull(nuevoTexto, "El nuevo texto no puede ser nulo");
        if (!this.texto.equals(nuevoTexto)) {
            this.texto = nuevoTexto;
            this.notificadorTexto.notificar(this.texto);
        }
    }

    public Runnable getAccion() {
        return accion;
    }

    public void setAccion(Runnable nuevaAccion) {
        this.accion = Objects.requireNonNull(nuevaAccion, "La nueva acción no puede ser nula");
    }

    public boolean isHabilitada() {
        return habilitada;
    }

    public void setHabilitada(boolean habilitada) {
        if (this.habilitada != habilitada) {
            this.habilitada = habilitada;
            this.notificadorHabilitada.notificar(this.habilitada);
        }
    }

    public Notificador<Boolean> getNotificadorHabilitada() {
        return notificadorHabilitada;
    }

    public Notificador<String> getNotificadorTexto() {
        return notificadorTexto;
    }

    public void ejecutar() {
        if (habilitada && accion != null) {
            accion.run();
        }
    }
}
