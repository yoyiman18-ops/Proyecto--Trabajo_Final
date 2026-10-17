package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import modelo.Oleada;
import modelo.Personaje;
import motor.util.VecDouble2D;

public final class OleadaControlador {
    private final Oleada modelo;
    private final Personaje jugador;
    private final CombateControlador combate;
    private final IntFunction<VecDouble2D> posicionAparicion;
    private final List<EnemigoControlador> enemigosAparecidos = new ArrayList<>();
    private double tiempoHastaAparicion;

    public OleadaControlador(
        Oleada modelo,
        Personaje jugador,
        CombateControlador combate,
        IntFunction<VecDouble2D> posicionAparicion
    ) {
        if (modelo == null || jugador == null || combate == null || posicionAparicion == null) {
            throw new IllegalArgumentException(
                "El modelo, el jugador, el combate y las posiciones de aparición son obligatorios."
            );
        }
        this.modelo = modelo;
        this.jugador = jugador;
        this.combate = combate;
        this.posicionAparicion = posicionAparicion;
        this.tiempoHastaAparicion = modelo.intervaloAparicion();
    }

    public void tick(Double dt) {
        if (dt == null || !Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException(
                "El tiempo de actualización debe ser finito y no negativo."
            );
        }
        if (quedanPorAparecer() == 0) {
            return;
        }

        tiempoHastaAparicion -= dt;
        while (tiempoHastaAparicion <= 0 && quedanPorAparecer() > 0) {
            aparecerEnemigo();
            tiempoHastaAparicion += modelo.intervaloAparicion();
        }
    }

    private void aparecerEnemigo() {
        VecDouble2D posicion = posicionAparicion.apply(enemigosAparecidos.size());
        if (posicion == null
                || !Double.isFinite(posicion.getX())
                || !Double.isFinite(posicion.getY())) {
            throw new IllegalStateException(
                "La posición de aparición debe tener coordenadas finitas."
            );
        }

        EnemigoControlador enemigo = new EnemigoControlador(
            jugador,
            posicion.getX(),
            posicion.getY(),
            modelo.multiplicadorVida(),
            modelo.multiplicadorDanio()
        );
        combate.registrarEnemigo(enemigo);
        enemigosAparecidos.add(enemigo);
    }

    public Oleada getModelo() {
        return modelo;
    }

    public int quedanPorAparecer() {
        return modelo.cantidadEnemigos() - enemigosAparecidos.size();
    }

    public int getEnemigosAparecidos() {
        return enemigosAparecidos.size();
    }

    public boolean estaCompletada() {
        return quedanPorAparecer() == 0
            && enemigosAparecidos.stream().allMatch(enemigo -> enemigo.getModelo().getVida() <= 0);
    }
}
