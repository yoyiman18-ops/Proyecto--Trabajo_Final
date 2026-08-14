public class App {

    public static void main(String[] args) throws Exception {
        
        EntidadViva e1 = new EntidadViva.Builder()
                        .nombre("Geralt")
                        .vidaMax(50)
                        .build();

        System.out.println(e1.toString());
        e1.recibirDaño(40);
        System.out.println(e1.toString());
        // e1.mover();
        // System.out.println(e1.toString());
                    
    }
}
