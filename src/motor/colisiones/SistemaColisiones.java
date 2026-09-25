package motor.colisiones;

import java.util.Collection;
import motor.colisiones.sistema.FaseEspecifica;
import motor.colisiones.sistema.FaseGeneral;

public class SistemaColisiones {
    private final FaseGeneral faseGeneral;
    private final FaseEspecifica faseEspecifica;

    public SistemaColisiones(FaseGeneral faseGeneral, FaseEspecifica faseEspecifica) {
        if (faseGeneral == null || faseEspecifica == null) {
            throw new IllegalArgumentException("faseGeneral y faseEspecifica son obligatorias");
        }
        this.faseGeneral = faseGeneral;
        this.faseEspecifica = faseEspecifica;
    };

    public void resolverColisiones(Collection<Colisionable> colisionables) {
        faseEspecifica.procesarColisiones(
            faseGeneral.calcularPares(colisionables)
        );
    }
}