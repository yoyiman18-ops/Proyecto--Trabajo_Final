package motor.colisiones;

import java.util.Collection;
import modelo.Entidad;

public class ControladorColisiones {

    public ControladorColisiones(int tamañoCelda) {
        if (tamañoCelda < 1) { throw new IllegalArgumentException("Tamaño celda no puede ser < 1"); }
        this.TAMAÑO_CELDA = tamañoCelda;
        this.faseGeneral = new FaseGeneralSpatialHashGrid(TAMAÑO_CELDA);
        this.faseEspecifica = new FaseEspecifica();
    }

    private final int TAMAÑO_CELDA;
    private final FaseGeneral faseGeneral;
    private final FaseEspecifica faseEspecifica;

    public void resolverColisiones(Collection<Entidad> entidades) {
        faseEspecifica.procesarColisiones(
            faseGeneral.calcularPares(entidades)
        );

    }
}