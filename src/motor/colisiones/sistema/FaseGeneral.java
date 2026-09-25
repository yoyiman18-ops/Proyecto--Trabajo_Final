package motor.colisiones.sistema;
import java.util.List;
import modelo.Entidad;
import motor.colisiones.ParColision;

@FunctionalInterface 
public interface FaseGeneral {
    public List<ParColision> calcularPares(List<Entidad> entidades);
}
