package controlador;

import java.util.Objects;
import javafx.scene.Node;
import modelo.ModeloEnemigo;
import modelo.ObjetivoEnemigo;
import motor.colisiones.Colisionable;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.observer.Observador;
import vista.VistaEnemigo;

public final class EnemigoControlador implements Controlador {
    private final ModeloEnemigo modelo;
    private final VistaEnemigo vista;
    private final ObjetivoEnemigo objetivo;
    private final Observador<Colisionable> observadorColision;
    private boolean contactoConObjetivo;

    public EnemigoControlador(
        ModeloEnemigo modelo,
        VistaEnemigo vista,
        ObjetivoEnemigo objetivo
    ) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista no puede ser nula");
        this.objetivo = Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
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
        modelo.actualizarEnfriamientoAtaque(dt);

        if (contactoConObjetivo) {
            modelo.detener();
            contactoConObjetivo = false;
            return;
        }

        modelo.perseguir(objetivo.getPosicion(), dt);
        actualizarVista();
    }

    private void alColisionar(Colisionable otro) {
        if (otro instanceof ObjetivoEnemigo objetivoColisionado && objetivoColisionado == objetivo) {
            contactoConObjetivo = true;
            modelo.atacar(objetivo);
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}
