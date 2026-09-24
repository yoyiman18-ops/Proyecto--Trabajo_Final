package motor.colisiones;
import java.util.Collection;
import modelo.Entidad;

public class ControladorColisiones {
    private final int tamañoCelda;
    private final FaseGeneral faseGeneral;
    private final FaseEspecifica faseEspecifica;

    public ControladorColisiones(int tamañoCelda) {
        if (tamañoCelda < 1) { throw new IllegalArgumentException("Tamaño celda no puede ser < 1"); }
        this.tamañoCelda = tamañoCelda;
        this.faseGeneral = new FaseGeneralSpatialHashGrid(tamañoCelda);
        this.faseEspecifica = new FaseEspecifica();
    }

    public void resolverColisiones(Collection<Entidad> entidades) {
        faseEspecifica.procesarColisiones(
            faseGeneral.calcularPares(entidades)
        );

    }
}