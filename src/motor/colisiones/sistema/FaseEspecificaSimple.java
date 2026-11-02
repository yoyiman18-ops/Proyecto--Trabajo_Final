package motor.colisiones.sistema;

import java.util.Objects;
import motor.colisiones.Colisionable;
import motor.colisiones.EstrategiaResolucionColision;
import motor.colisiones.ParColision;
import motor.colisiones.ResolvedorInteraccion;


public class FaseEspecificaSimple implements FaseEspecifica {

    private final ResolvedorInteraccion resolvedorInteraccion;
    private final EstrategiaResolucionColision estrategiaResolucion;

    public FaseEspecificaSimple(ResolvedorInteraccion resolvedorInteraccion, EstrategiaResolucionColision estrategiaResolucion) {
        this.resolvedorInteraccion = Objects.requireNonNull(resolvedorInteraccion, "resolvedorInteraccion no puede ser nulo");
        this.estrategiaResolucion = Objects.requireNonNull(estrategiaResolucion, "La estrategia para resolver las colisiones no puede ser nula");
    }

    @Override
    public void procesarColisiones(Iterable<ParColision> pares) {
        if (pares == null) { return; }
        for (ParColision par : pares) {
            Colisionable c1 = par.getPrimera();
            Colisionable c2 = par.getSegunda();
            if (c1 != null && c2 != null && c1.getHitbox() != null && c2.getHitbox() != null) {
                if (c1.getHitbox().intersecta(c2.getHitbox())) {
                    resolvedorInteraccion.resolverInteracciones(c1, c2);
                    estrategiaResolucion.resolverSeparacionFisica(c1, c2);
                }
            }
        }
    }

    public ResolvedorInteraccion getResolvedorInteraccion() {
        return resolvedorInteraccion;
    }

    public EstrategiaResolucionColision getEstrategiaResolucion() {
        return estrategiaResolucion;
    }
}
