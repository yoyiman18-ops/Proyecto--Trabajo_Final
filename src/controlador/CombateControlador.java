package controlador;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.layout.Pane;
import modelo.Enemigo;
import modelo.ObjetivoDanio;
import modelo.Personaje;

public final class CombateControlador {
    private final Personaje jugador;
    private final Pane escenario;
    private final List<EnemigoControlador> enemigos = new ArrayList<>();
    private final List<ExperienciaControlador> recompensas = new ArrayList<>();
    private ProyectilControlador proyectiles;
    private int enemigosDerrotados;

    public CombateControlador(Personaje jugador, Pane escenario) {
        if (jugador == null || escenario == null) {
            throw new IllegalArgumentException("El jugador y el escenario son obligatorios.");
        }
        this.jugador = jugador;
        this.escenario = escenario;
    }

    public void conectarProyectiles(ProyectilControlador proyectiles) {
        if (proyectiles == null) {
            throw new IllegalArgumentException("El controlador de proyectiles no puede ser nulo.");
        }
        if (this.proyectiles != null && this.proyectiles != proyectiles) {
            throw new IllegalStateException("Ya hay un controlador de proyectiles conectado.");
        }
        this.proyectiles = proyectiles;
        proyectiles.usarCombateControlador(this);
        for (EnemigoControlador enemigo : enemigos) {
            proyectiles.agregarObjetivo(enemigo.getModelo());
        }
    }

    public void registrarEnemigo(EnemigoControlador enemigo) {
        if (enemigo == null) {
            throw new IllegalArgumentException("El enemigo no puede ser nulo.");
        }
        if (enemigos.contains(enemigo)) {
            return;
        }
        if (enemigo.getModelo().getVida() <= 0) {
            throw new IllegalArgumentException("No se puede registrar un enemigo derrotado.");
        }
        if (enemigo.getVista().getParent() != null
                && enemigo.getVista().getParent() != escenario) {
            throw new IllegalStateException("La vista del enemigo ya pertenece a otro escenario.");
        }

        enemigo.asociarCombate(this);
        enemigos.add(enemigo);
        if (enemigo.getVista().getParent() == null) {
            escenario.getChildren().add(enemigo.getVista());
        }
        if (proyectiles != null) {
            proyectiles.agregarObjetivo(enemigo.getModelo());
        }
    }

    public void recibirDanio(ObjetivoDanio objetivo, double cantidad) {
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo del daño no puede ser nulo.");
        }
        if (!Double.isFinite(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("El daño debe ser finito y no negativo.");
        }
        if (cantidad == 0) {
            return;
        }

        if (objetivo == jugador) {
            jugador.recibirDanio(cantidad);
            return;
        }
        if (objetivo instanceof Enemigo enemigo) {
            recibirDanioEnemigo(enemigo, cantidad);
            return;
        }
        objetivo.aplicarDanio(cantidad);
    }

    private void recibirDanioEnemigo(Enemigo modelo, double cantidad) {
        EnemigoControlador controlador = buscarEnemigo(modelo);
        if (controlador == null) {
            throw new IllegalStateException("El enemigo debe registrarse antes de recibir daño.");
        }
        if (controlador.recibirDanio(cantidad)) {
            derrotarEnemigo(controlador);
        }
    }

    private EnemigoControlador buscarEnemigo(Enemigo modelo) {
        for (EnemigoControlador enemigo : enemigos) {
            if (enemigo.getModelo() == modelo) {
                return enemigo;
            }
        }
        return null;
    }

    private void derrotarEnemigo(EnemigoControlador enemigo) {
        Enemigo modelo = enemigo.getModelo();
        modelo.desactivar();
        escenario.getChildren().remove(enemigo.getVista());
        enemigos.remove(enemigo);
        if (proyectiles != null) {
            proyectiles.quitarObjetivo(modelo);
        }

        enemigosDerrotados++;
        int experiencia = modelo.getExperienciaRecompensa();
        if (experiencia > 0) {
            ExperienciaControlador recompensa = new ExperienciaControlador(
                jugador,
                modelo.getPosicion().getX(),
                modelo.getPosicion().getY(),
                experiencia
            );
            recompensas.add(recompensa);
            escenario.getChildren().add(recompensa.getVista());
        }
    }

    public void tick(Double dt) {
        if (dt == null || !Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException(
                "El tiempo de actualización debe ser finito y no negativo."
            );
        }
        Iterator<ExperienciaControlador> iterador = recompensas.iterator();
        while (iterador.hasNext()) {
            ExperienciaControlador recompensa = iterador.next();
            recompensa.tick(dt);
            if (recompensa.fueRecogida()) {
                escenario.getChildren().remove(recompensa.getVista());
                iterador.remove();
            }
        }
    }

    public int getEnemigosDerrotados() {
        return enemigosDerrotados;
    }

}
