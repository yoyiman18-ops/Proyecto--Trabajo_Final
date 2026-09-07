package controlador;

import modelo.Proyectil;
import modelo.Vec2;
import vista.SpriteVista;

/** Mueve un proyectil y comprueba su impacto contra los enemigos. */
public class ProyectilControlador {

    private static final double VELOCIDAD = 4.0;
    private static final double RADIO_IMPACTO = 18.0;
    private static final double LIMITE_ESCENARIO = 360.0;

    private final Proyectil modelo;
    private final SpriteVista vista;
    private final double direccionX;
    private final double direccionY;
    private final int daño;
    private boolean activo = true;

    public ProyectilControlador(double x, double y, double direccionX,
                                double direccionY, int daño, SpriteVista vista) {
        this.modelo = new Proyectil(x, y);
        this.vista = vista;
        this.daño = daño;
        double magnitud = Math.sqrt(direccionX * direccionX + direccionY * direccionY);
        this.direccionX = direccionX / magnitud;
        this.direccionY = direccionY / magnitud;
        vista.actualizar(modelo);
    }

    public void actualizar(Iterable<EnemigoControlador> enemigos) {
        if (!activo) {
            return;
        }

        Vec2 posicion = modelo.getPosicion();
        modelo.setPosicion(
                posicion.getX() + direccionX * VELOCIDAD,
                posicion.getY() + direccionY * VELOCIDAD
        );

        if (modelo.getPosicion().getX() < -LIMITE_ESCENARIO
                || modelo.getPosicion().getX() > LIMITE_ESCENARIO
                || modelo.getPosicion().getY() < -LIMITE_ESCENARIO
                || modelo.getPosicion().getY() > LIMITE_ESCENARIO) {
            desactivar();
            return;
        }

        for (EnemigoControlador enemigo : enemigos) {
            if (enemigo.estaVivo() && distanciaA(enemigo) <= RADIO_IMPACTO) {
                enemigo.recibirAtaque(daño);
                desactivar();
                return;
            }
        }
        vista.actualizar(modelo);
    }

    private double distanciaA(EnemigoControlador enemigo) {
        Vec2 proyectil = modelo.getPosicion();
        Vec2 enemigoPosicion = enemigo.getModelo().getPosicion();
        double dx = proyectil.getX() - enemigoPosicion.getX();
        double dy = proyectil.getY() - enemigoPosicion.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean estaActivo() {
        return activo;
    }

    public SpriteVista getVista() {
        return vista;
    }

    private void desactivar() {
        activo = false;
        vista.setVisible(false);
    }
}
