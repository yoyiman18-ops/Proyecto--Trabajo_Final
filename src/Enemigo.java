public class Enemigo extends EntidadViva {
    
    public Enemigo(float vida, float vidaMax, float daño) {
        super(vida, vidaMax, daño);
        
    }

    public void dañar(int cantidad) {
       recibirDaño(cantidad);
        if (!EstaVivo())    {
            morir();
        }
        public void morir() {
            // mas adelante se definita que pasa cuando muere un enemigo o como se maneja la muerte de un enemigo
    }

   
}
    