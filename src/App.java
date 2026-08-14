public class App {

    public static void main(String[] args) throws Exception {
        
        EntidadViva e1 = new EntidadViva.Builder()
                        .velocidad(5)
                        .direccion(1, 0)
                        .build();

        System.out.println(e1.toString());
        e1.mover();
        System.out.println(e1.toString());
                    
    }
}
