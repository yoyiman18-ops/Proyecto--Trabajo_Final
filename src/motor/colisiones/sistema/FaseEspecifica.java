package motor.colisiones.sistema;

import java.util.Collection;

import motor.colisiones.ParColision;

@FunctionalInterface 
public interface FaseEspecifica {
    public void procesarColisiones(Collection<ParColision> pares);
}
