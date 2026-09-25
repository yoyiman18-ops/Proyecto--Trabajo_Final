package motor.colisiones.sistema;
import java.util.List;
import modelo.Entidad;
import motor.colisiones.ParEntidades;

public interface FaseGeneral {
    public List<ParEntidades> calcularPares(List<Entidad> entidades);
}
