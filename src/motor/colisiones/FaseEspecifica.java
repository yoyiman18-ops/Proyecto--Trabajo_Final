package motor.colisiones;

import java.util.HashSet;

public class FaseEspecifica {
    
    public void procesarColisiones(HashSet<ParColision> pares) {
        for (ParColision par : pares) {
            if (par.getPrimera().intersecta(par.getSegunda())) { par.colisionar(); }
        }
    }
}
