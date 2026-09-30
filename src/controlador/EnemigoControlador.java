package controlador;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.Personaje;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;

public final class EnemigoControlador implements Controlador {
    private static final double VELOCIDAD = 75;
    private static final double DISTANCIA_ATAQUE = 34;
    private static final double DANIO = 10;
    private static final double INTERVALO_ATAQUE = 1;

    private final Personaje objetivo;
    private final Entidad modelo;
    private final Circle vista = new Circle(16, Color.web("#c94b3c"));
    private double tiempoHastaAtaque;

    public EnemigoControlador(Personaje objetivo) {
        this.objetivo = objetivo;
        VecDouble2D posicion = new VecDouble2D(360, 240);
        modelo = new Entidad(
            "enemigo",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 32, 32),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ENEMIGO),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            posicion,
            new VecDouble2D(),
            0,
            0,
            VELOCIDAD
        );
        vista.setStroke(Color.web("#55251f"));
        vista.setStrokeWidth(3);
        actualizarVista();
    }

    @Override
    public Entidad getModelo() {
        return modelo;
    }

    @Override
    public Node getVista() {
        return vista;
    }

    @Override
    public void tick(Double dt) {
        if (objetivo.getVida() <= 0) {
            return;
        }

        VecDouble2D posicion = modelo.getPosicion();
        VecDouble2D posicionObjetivo = objetivo.getPosicion();
        double diferenciaX = posicionObjetivo.getX() + 25 - posicion.getX();
        double diferenciaY = posicionObjetivo.getY() + 25 - posicion.getY();
        double distancia = Math.hypot(diferenciaX, diferenciaY);

        if (distancia <= DISTANCIA_ATAQUE) {
            modelo.getVelocidad().setX(0);
            modelo.getVelocidad().setY(0);
            tiempoHastaAtaque -= dt;
            if (tiempoHastaAtaque <= 0) {
                objetivo.recibirDanio(DANIO);
                tiempoHastaAtaque = INTERVALO_ATAQUE;
            }
            return;
        }

        tiempoHastaAtaque = 0;
        modelo.getVelocidad().setX(diferenciaX / distancia * VELOCIDAD);
        modelo.getVelocidad().setY(diferenciaY / distancia * VELOCIDAD);
        modelo.mover(dt);
        actualizarVista();
    }

    private void actualizarVista() {
        vista.setLayoutX(modelo.getPosicion().getX());
        vista.setLayoutY(modelo.getPosicion().getY());
    }
}