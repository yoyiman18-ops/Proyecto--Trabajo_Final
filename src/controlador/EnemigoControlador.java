package controlador;

import java.util.Objects;
import javafx.scene.Node;
import modelo.ModeloEnemigo;
import modelo.ObjetivoEnemigo;
import motor.colisiones.Colisionable;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import vista.VistaEnemigo;

public final class EnemigoControlador implements Controlador {
    private final ModeloEnemigo modelo;
    private final VistaEnemigo vista;
    private final ObjetivoEnemigo objetivo;
    private final double intervaloAtaque;
    private final Observador<Colisionable> observadorColision;

    private double tiempoHastaAtaque;
    private boolean contactoConObjetivo;

    public EnemigoControlador(
        ModeloEnemigo modelo,
        VistaEnemigo vista,
        ObjetivoEnemigo objetivo
    ) {
        this(modelo, vista, objetivo, 1);
    }

    public EnemigoControlador(
        ModeloEnemigo modelo,
        VistaEnemigo vista,
        ObjetivoEnemigo objetivo,
        double intervaloAtaque
    ) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista no puede ser nula");
        this.objetivo = Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
        if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
        }
        this.intervaloAtaque = intervaloAtaque;
        this.observadorColision = this::alColisionar;
        this.modelo.getEntidad().getNotificadorColision().suscribirObservador(observadorColision);

        actualizarVista();
    }

    @Override
    public Entidad getModelo() {
        return modelo.getEntidad();
    }

    public int getExperienciaRecompensa() {
        return modelo.getExperienciaRecompensa();
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
        tiempoHastaAtaque = Math.max(0, tiempoHastaAtaque - dt);

        if (contactoConObjetivo) {
            modelo.getVelocidad().setX(0);
            modelo.getVelocidad().setY(0);
            contactoConObjetivo = false;
            return;
        }

        VecDouble2D posicion = modelo.getPosicion();
        VecDouble2D posicionObjetivo = objetivo.getPosicion();
        double diferenciaX = posicionObjetivo.getX() + 25 - posicion.getX();
        double diferenciaY = posicionObjetivo.getY() + 25 - posicion.getY();
        double distancia = Math.hypot(diferenciaX, diferenciaY);

        double velocidadMax = modelo.getVelocidadMaxima();
        if (distancia == 0) {
            modelo.getVelocidad().setX(0);
            modelo.getVelocidad().setY(0);
            return;
        }
        modelo.getVelocidad().setX(diferenciaX / distancia * velocidadMax);
        modelo.getVelocidad().setY(diferenciaY / distancia * velocidadMax);
        modelo.mover(dt);
        actualizarVista();
    }

    private void alColisionar(Colisionable otro) {
        if (otro instanceof ObjetivoEnemigo objetivoColisionado && objetivoColisionado == objetivo) {
            contactoConObjetivo = true;
            if (tiempoHastaAtaque <= 0 && objetivo.getVida() > 0) {
                objetivo.recibirDanio(modelo.getDanio());
                tiempoHastaAtaque = intervaloAtaque;
            }
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}
