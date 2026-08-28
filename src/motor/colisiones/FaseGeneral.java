package motor.colisiones;

import java.util.Collection;
import modelo.Entidad;

public interface FaseGeneral {
    
    public Collection<ParEntidades> calcularPares(Collection<Entidad> entidades);

}
