package motor.colisiones;
import modelo.Entidad;

public record ParEntidades(Entidad primera, Entidad segunda) {

    public ParEntidades(Entidad primera, Entidad segunda) {
        if (primera == null || segunda == null) { throw new IllegalArgumentException("No puede haber entidad nula en par de colision");}
        if (primera.compareTo(segunda) >= 0) {
        this.primera = primera;
        this.segunda = segunda;
        } else {
            this.primera = segunda;
            this.segunda = primera;
        }
    }

    public Entidad getPrimera() { return primera; } 
    public Entidad getSegunda() { return segunda; }
    public void colisionar() { primera.colisionar(segunda); segunda.colisionar(primera);}
}
