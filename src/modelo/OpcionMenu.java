package modelo;

import java.util.Objects;

/**
 * Modelo de datos puro que representa una opción seleccionable en un menú.
 * No depende de ninguna biblioteca gráfica (agnóstico de la UI).
 */
public class OpcionMenu {

    private final String texto;
    private final Runnable accion;
    private boolean habilitada;

    public OpcionMenu(String texto, Runnable accion) {
        this(texto, accion, true);
    }

    public OpcionMenu(String texto, Runnable accion, boolean habilitada) {
        this.texto = Objects.requireNonNull(texto, "El texto de la opción no puede ser nulo");
        this.accion = Objects.requireNonNull(accion, "La acción de la opción no puede ser nula");
        this.habilitada = habilitada;
    }

    public String getTexto() {
        return texto;
    }

    public Runnable getAccion() {
        return accion;
    }

    public boolean isHabilitada() {
        return habilitada;
    }

    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }

    public void ejecutar() {
        if (habilitada && accion != null) {
            accion.run();
        }
    }
}
