package motor.colisiones;
import java.util.List;

import modelo.Entidad;

public class GestorColisiones {
    private final FaseGeneral faseGeneral;
    private final FaseEspecifica faseEspecifica;

    public GestorColisiones(FaseGeneral faseGeneral) {
        this.faseGeneral = faseGeneral;
        this.faseEspecifica = new FaseEspecifica();
    }

    public void resolverColisiones(List<Entidad> entidades) {
        faseEspecifica.procesarColisiones(
            faseGeneral.calcularPares(entidades)
        );

    }
}