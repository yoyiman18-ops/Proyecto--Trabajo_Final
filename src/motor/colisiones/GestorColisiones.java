package motor.colisiones;
import java.util.List;

import modelo.Entidad;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneral;

public class GestorColisiones {
    private final FaseGeneral faseGeneral;
    private final FaseEspecificaSimple faseEspecifica;

    public GestorColisiones(FaseGeneral faseGeneral) {
        this.faseGeneral = faseGeneral;
        this.faseEspecifica = new FaseEspecificaSimple();
    }

    public void resolverColisiones(List<Entidad> entidades) {
        faseEspecifica.procesarColisiones(
            faseGeneral.calcularPares(entidades)
        );

    }
}