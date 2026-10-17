package controlador;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.Proyectil;
import modelo.ProyectilAliado;
import modelo.ProyectilEnemigo;
import modelo.ObjetivoDanio;
import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.util.VecDouble2D;

public final class ProyectilControlador {
    private static final double RADIO_VISTA = 5;
    private static final double TAMANIO_HITBOX = RADIO_VISTA * 2;
    private static final Color COLOR_ALIADO = Color.ORANGERED;
    private static final Color COLOR_ENEMIGO = Color.MEDIUMPURPLE;

    private final Pane escenario;
    private final List<Colisionable> objetivos = new ArrayList<>();
    private final List<ProyectilActivo> proyectiles = new ArrayList<>();
    private CombateControlador combate;

    public ProyectilControlador(Pane escenario) {
        if (escenario == null) {
            throw new IllegalArgumentException("El escenario no puede ser nulo.");
        }
        this.escenario = escenario;
    }

    public void agregarObjetivo(Colisionable objetivo) {
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no puede ser nulo.");
        }
        if (!objetivos.contains(objetivo)) {
            objetivos.add(objetivo);
        }
    }

    public boolean quitarObjetivo(Colisionable objetivo) {
        return objetivos.remove(objetivo);
    }

    void usarCombateControlador(CombateControlador combate) {
        if (combate == null) {
            throw new IllegalArgumentException("El controlador de combate no puede ser nulo.");
        }
        this.combate = combate;
    }

    public Proyectil dispararAliado(
        String nombre,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double danio,
        double tiempoVida,
        int penetracion
    ) {
        validarNombreYPosicion(nombre, posicion);
        Proyectil proyectil = new ProyectilAliado(
            nombre,
            crearHitbox(
                CategoriaColision.ALIADO,
                CategoriaColision.ENEMIGO,
                CategoriaColision.MURO
            ),
            posicion,
            direccion,
            velocidad,
            danio,
            tiempoVida,
            penetracion
        );
        agregarProyectil(proyectil, COLOR_ALIADO);
        return proyectil;
    }

    public Proyectil dispararEnemigo(
        String nombre,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double danio,
        double tiempoVida,
        int penetracion
    ) {
        validarNombreYPosicion(nombre, posicion);
        Proyectil proyectil = new ProyectilEnemigo(
            nombre,
            crearHitbox(
                CategoriaColision.ENEMIGO,
                CategoriaColision.JUGADOR,
                CategoriaColision.ALIADO,
                CategoriaColision.MURO
            ),
            posicion,
            direccion,
            velocidad,
            danio,
            tiempoVida,
            penetracion
        );
        agregarProyectil(proyectil, COLOR_ENEMIGO);
        return proyectil;
    }

    private void agregarProyectil(Proyectil proyectil, Color color) {
        Circle vista = new Circle(RADIO_VISTA, color);
        vista.setStroke(Color.WHITE);
        actualizarVista(vista, proyectil);
        proyectil.getHitbox().actualizarTransformacion(proyectil.getPosicion());
        escenario.getChildren().add(vista);
        proyectiles.add(new ProyectilActivo(proyectil, vista));
    }

    private static HitboxRectangular crearHitbox(
        CategoriaColision categoria,
        CategoriaColision... capas
    ) {
        return new HitboxRectangular(
            new Rectangle2D.Double(0, 0, TAMANIO_HITBOX, TAMANIO_HITBOX),
            TipoHitbox.SOLIDA,
            MascaraColision.of(categoria),
            MascaraColision.of(capas)
        );
    }

    private static void validarNombreYPosicion(
        String nombre,
        VecDouble2D posicion
    ) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        if (posicion == null) {
            throw new IllegalArgumentException("La posición no puede ser nula.");
        }
    }

    public void tick(Double dt) {
        if (dt == null || !Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException(
                "El tiempo de actualización debe ser finito y no negativo."
            );
        }

        Iterator<ProyectilActivo> iterador = proyectiles.iterator();
        while (iterador.hasNext()) {
            ProyectilActivo activo = iterador.next();
            Proyectil proyectil = activo.modelo;
            proyectil.actualizar(dt);
            actualizarVista(activo.vista, proyectil);
            comprobarColisiones(activo);

            if (!proyectil.estaActivo() || proyectil.debeDesaparecer()) {
                escenario.getChildren().remove(activo.vista);
                iterador.remove();
            }
        }
    }

    private void comprobarColisiones(ProyectilActivo activo) {
        Proyectil proyectil = activo.modelo;
        for (Colisionable objetivo : List.copyOf(objetivos)) {
            if (!proyectil.estaActivo()) {
                break;
            }
            if (objetivo == null
                    || !objetivo.colisionesActivas()
                    || activo.objetivosImpactados.contains(objetivo.getHitbox().getId())) {
                continue;
            }

            var hitboxProyectil = proyectil.getHitbox();
            var hitboxObjetivo = objetivo.getHitbox();
            if (hitboxProyectil.esCompatible(hitboxObjetivo)
                    && hitboxProyectil.intersecta(hitboxObjetivo)) {
                activo.objetivosImpactados.add(hitboxObjetivo.getId());
                if (combate == null) {
                    proyectil.colisionar(objetivo);
                } else {
                    proyectil.colisionar(objetivo, combate::recibirDanio);
                }
            }
        }
    }

    private static void actualizarVista(Circle vista, Proyectil proyectil) {
        vista.setLayoutX(proyectil.getPosicion().getX());
        vista.setLayoutY(proyectil.getPosicion().getY());
    }

    public List<Proyectil> getProyectiles() {
        return proyectiles.stream().map(proyectil -> proyectil.modelo).toList();
    }

    private static final class ProyectilActivo {
        private final Proyectil modelo;
        private final Circle vista;
        private final Set<Integer> objetivosImpactados = new HashSet<>();

        private ProyectilActivo(Proyectil modelo, Circle vista) {
            this.modelo = modelo;
            this.vista = vista;
        }
    }
}
