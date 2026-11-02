package motor.modelo;

import java.util.List;

public interface Interactuable {
    List<Interaccion> getInteracciones();
    boolean interactuar(Interaccion i);
}
