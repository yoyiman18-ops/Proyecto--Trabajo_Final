import java.util.Random;

public class App {

    public static void main(String[] args) throws Exception {

        Vec2 vector = new Vec2().normalizado();

        vector.normalizar();
        
        Random rng = new Random();

        EntidadViva e1 = new EntidadViva.Builder()
                        .nombre("Geralt")
                        .direccion(0.45, 0.55)
                        .VELOCIDAD_MAX(1)
                        .aceleracion(10)
                        .vidaMax(rng.nextInt(20,201))
                        .defensa(rng.nextInt(1,11))
                        .build();

        System.out.println(e1.toString());

        while (e1.estaVivo()) {
            e1.recibirDaño(rng.nextInt(10,40));
        }
        System.out.println(e1.toString());
        // e1.mover();
        // System.out.println(e1.toString());
                    
        e1.acelerar();
        e1.mover();
        System.out.println(e1.toString());
    }
}
