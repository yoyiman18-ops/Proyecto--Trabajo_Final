package motor.modelo;

import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;

public class EstrategiaMovimientoPersecucion implements EstrategiaMovimiento {

    private VecDouble2D posicionObjetivo;
    private double distanciaFreno;
    private Observador<VecDouble2D> observadorPosicion;

    public EstrategiaMovimientoPersecucion(Posicionable objetivo) {
        this(objetivo, 0.0);
    }

    public EstrategiaMovimientoPersecucion(Posicionable objetivo, double distanciaFreno) {
        this.observadorPosicion = this::setPosicionObjetivo;
        if (objetivo != null) {
            if (objetivo.getNotificadorPosicion() != null) {
                objetivo.getNotificadorPosicion().suscribirObservador(this.observadorPosicion);
            }
            this.posicionObjetivo = objetivo.getPosicion();
        }
        this.distanciaFreno = Math.max(0.0, distanciaFreno);
    }

    @Override
    public void ejecutar(Movil sujeto, double dt) {
        if (sujeto == null) { return; }
        if (posicionObjetivo == null) {
            sujeto.frenar(Eje.X, dt);
            sujeto.frenar(Eje.Y, dt);
            return;
        }

        VecDouble2D posicionObjetivoActual = posicionObjetivo.clone();
        VecDouble2D posPropia = sujeto.getPosicion();
        VecDouble2D direccion = posicionObjetivoActual.restar(posPropia);
        double distCuadrada = (direccion.getX() * direccion.getX()) + (direccion.getY() * direccion.getY());

        if (distCuadrada > distanciaFreno * distanciaFreno) {
            sujeto.acelerar(direccion, dt);
        } else {
            sujeto.frenar(Eje.X, dt);
            sujeto.frenar(Eje.Y, dt);
        }
    }

    public void setObjetivo(Posicionable objetivo) {
        this.observadorPosicion = this::setPosicionObjetivo;
        if (objetivo != null) {
            if (objetivo.getNotificadorPosicion() != null) {
                objetivo.getNotificadorPosicion().suscribirObservador(observadorPosicion);
            }
            this.posicionObjetivo = objetivo.getPosicion();
        } else {
            this.posicionObjetivo = null;
        }
    }

    public void setDistanciaFreno(double distanciaFreno) { this.distanciaFreno = Math.max(0.0, distanciaFreno); }

    private void setPosicionObjetivo(VecDouble2D posicionObjetivo) { this.posicionObjetivo = posicionObjetivo; }
}
