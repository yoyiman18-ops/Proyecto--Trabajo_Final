public class App {

    private static void imprimirPrueba(EntidadMovil entidad) {
        System.out.printf("Posicion: x: %f ; y: %f | Velocidad: %f | Acel: %f %n",
            entidad.getPosicion().getX(),entidad.getPosicion().getY(),
            entidad.getVelocidad(),entidad.getAceleracion());
    }

    public static void main(String[] args) throws Exception {
        EntidadMovil algo = new EntidadMovil(
            new Vec2(1,1),
            new Vec2(2,2),
            5.0,
            0.0
        );


        imprimirPrueba(algo);

        algo.acelerar();
        algo.mover();

        imprimirPrueba(algo);

        algo.frenar();
        algo.mover();

        imprimirPrueba(algo);

        algo.setDireccion(0, 0);

        algo.acelerar();
        algo.mover();

        imprimirPrueba(algo);

        algo.frenar();
        algo.setDireccion(-1,-1 );
        algo.acelerar();
        algo.mover();

        imprimirPrueba(algo);

    }
}
