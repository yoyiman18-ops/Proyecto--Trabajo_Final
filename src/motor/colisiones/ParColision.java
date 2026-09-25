package motor.colisiones;

public record ParColision(Colisionable primera, Colisionable segunda) {

    public ParColision(Colisionable primera, Colisionable segunda) {
        if (primera == null || segunda == null) { throw new IllegalArgumentException("Los colisionables no pueden ser nulos."); }
        if (primera.getHitbox().getId() <= segunda.getHitbox().getId()) {
            this.primera = primera;
            this.segunda = segunda;
        } else {
            this.primera = segunda;
            this.segunda = primera;
        }
    }
    
    public Colisionable getPrimera() { return primera; } 
    public Colisionable getSegunda() { return segunda; }
    public void colisionar() { primera.colisionar(segunda); segunda.colisionar(primera);}
}
