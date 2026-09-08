package motor.colisiones;

import java.util.Collection;

public class FaseEspecifica {
    
    public void procesarColisiones(Collection<ParEntidades> pares) {
        for (ParEntidades par : pares) {
            if (par.getPrimera().intersecta(par.getSegunda())) { par.colisionar(); }
        }
    }
}
