package motor.colisiones.sistema;

import java.util.Collection;

import motor.colisiones.ParColision;

public class FaseEspecificaSimple implements FaseEspecifica {
    
    @Override
    public void procesarColisiones(Collection<ParColision> pares) {
        for (ParColision par : pares) {
            { 
                if (par.getPrimera().getHitbox().intersecta(par.getSegunda().getHitbox())) {
                    par.colisionar();
                }
            }
        }
    }
}
