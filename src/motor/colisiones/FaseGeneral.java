package motor.colisiones;
import java.util.List;

import modelo.Entidad;

public interface FaseGeneral {
    public List<ParEntidades> calcularPares(List<Entidad> entidades);
}
