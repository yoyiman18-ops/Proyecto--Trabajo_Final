public class App {

    private static void imprimirPrueba(EntidadMovil entidad) {
        System.out.printf("Posicion: x: %f ; y: %f | Velocidad: %f | Acel: %f %n",
            entidad.getPosicion().x,entidad.getPosicion().y,
            entidad.getVelocidad(),entidad.getAceleracion());
    }

    public static void main(String[] args) throws Exception {
        
        EntidadViva e1 = new EntidadViva.Builder()
                        .velocidad(5)
                        .direccion(1, 0)
                        .build();

        imprimirPrueba(e1);
        e1.mover();
        imprimirPrueba(e1);
                    
    }
}
