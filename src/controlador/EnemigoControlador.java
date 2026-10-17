package controlador;

import java.util.Objects;
import javafx.scene.Node;
import modelo.Enemigo;
import modelo.ObjetivoEnemigo;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;
import vista.VistaEnemigo;

public final class EnemigoControlador implements Controlador {
    private final Enemigo modelo;
    private final VistaEnemigo vista;
    private final ObjetivoEnemigo objetivo;
    private final double distanciaAtaque;
    private final double intervaloAtaque;

    private double tiempoHastaAtaque;

    public EnemigoControlador(
        Enemigo modelo,
        VistaEnemigo vista,
        ObjetivoEnemigo objetivo
    ) {
        this(modelo, vista, objetivo, 34, 1);
    }

    public EnemigoControlador(
        Enemigo modelo,
        VistaEnemigo vista,
        ObjetivoEnemigo objetivo,
        double distanciaAtaque,
        double intervaloAtaque
    ) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista no puede ser nula");
        this.objetivo = Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
        if (!Double.isFinite(distanciaAtaque) || distanciaAtaque <= 0
            || !Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("La distancia y el intervalo de ataque deben ser positivos y finitos.");
        }
        this.distanciaAtaque = distanciaAtaque;
        this.intervaloAtaque = intervaloAtaque;

        actualizarVista();
    }

    @Override
    public Enemigo getModelo() {
        return modelo;
    }

    @Override
    public Node getVista() {
        return vista.getNodo();
    }

    public boolean recibirDanio(double cantidad) {
        boolean derrotado = modelo.recibirDanio(cantidad);
        double proporcionVida = modelo.getVida() / modelo.getVidaMaxima();
        vista.mostrarReaccionDanio(proporcionVida);
        return derrotado;
    }

    @Override
    public void tick(Double dt) {
        if (objetivo.getVida() <= 0) {
            return;
        }
        vista.actualizarAnimacion(dt);

        VecDouble2D posicion = modelo.getPosicion();
        VecDouble2D posicionObjetivo = objetivo.getPosicion();
        double diferenciaX = posicionObjetivo.getX() + 25 - posicion.getX();
        double diferenciaY = posicionObjetivo.getY() + 25 - posicion.getY();
        double distancia = Math.hypot(diferenciaX, diferenciaY);

        if (distancia <= distanciaAtaque) {
            modelo.getVelocidad().setX(0);
            modelo.getVelocidad().setY(0);
            tiempoHastaAtaque -= dt;
            if (tiempoHastaAtaque <= 0) {
                objetivo.recibirDanio(modelo.getDanio());
                tiempoHastaAtaque = intervaloAtaque;
            }
            return;
        }

        tiempoHastaAtaque = 0;
        double velocidadMax = modelo.getVelocidadMaxima();
        modelo.getVelocidad().setX(diferenciaX / distancia * velocidadMax);
        modelo.getVelocidad().setY(diferenciaY / distancia * velocidadMax);
        modelo.mover(dt);
        actualizarVista();
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}
