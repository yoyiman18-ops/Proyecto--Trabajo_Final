package motor.colisiones.sistema;

import motor.colisiones.ParColision;

/**
 * 
 * La fase específica (narrow phase) más simple, únicamente verifica si las hitboxes de ambos colisionables
 * se intersectan y los colisiona.
 */
public class FaseEspecificaSimple implements FaseEspecifica {
    
    @Override
    public void procesarColisiones(Iterable<ParColision> pares) {
        for (ParColision par : pares) {
            if (par.getPrimera().getHitbox().intersecta(par.getSegunda().getHitbox())) { par.colisionar(); }
        }
    }
}
