
import java.util.ArrayList;
import java.util.Random;

public class App {

    public static void main(String[] args) throws Exception {

        Vec2 vector = new Vec2().normalizado();

        vector.normalizar();
        
        Random rng = new Random();

        EntidadViva e1 = new EntidadViva.Builder()
                        .nombre("Brotato")
                        .pathsprite("recursos/personaje.jpg")
                        .posicion(-12,53)
                        .direccion(0.45, 0.55)
                        .VELOCIDAD_MAX(1)
                        .aceleracion(10)
                        .vidaMax(rng.nextInt(100,201))
                        .defensa(rng.nextInt(0,1))
                        .build();

        EntidadViva e2 = new EntidadViva.Builder()
                        .posicion(284, 12)
                        .pathsprite("recursos/personaje.jpg")
                        .build();

        System.out.println(e1.toString());

        while (e1.estaVivo()) {
            e1.recibirDaño(rng.nextInt(20,50));
        }
        System.out.println(e1.toString());
        // e1.mover();
        // System.out.println(e1.toString());
                    
        e1.acelerar();
        e1.mover();
        System.out.println(e1.toString());

        Motor.dibujar(new ArrayList<>(java.util.List.of(e1,e2)));
    }
}
