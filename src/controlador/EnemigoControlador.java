package controlador;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Enemigo;
import modelo.Personaje;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.mvc.Controlador;
import motor.util.VecDouble2D;

public final class EnemigoControlador implements Controlador {
    private static final double VELOCIDAD = 75;
    private static final double VIDA_MAXIMA = 50;
    private static final double DISTANCIA_ATAQUE = 34;
    private static final double DANIO = 10;
    private static final int EXPERIENCIA_RECOMPENSA = 10;
    private static final double INTERVALO_ATAQUE = 1;
    private static final double ANCHO_VISTA = 48;
    private static final double ALTO_VISTA = 52;
    private static final double DURACION_FRAME_IDLE = 0.35;
    private static final double DURACION_REACCION_GOLPE = 0.22;
    private static final Image FRAME_IDLE_1 = cargarFrame("idle/frame-1.png");
    private static final Image FRAME_IDLE_2 = cargarFrame("idle/frame-2.png");
    private static final Image FRAME_GOLPE = cargarFrame("got hit/frame.png");

    private final Personaje objetivo;
    private final Enemigo modelo;
    private final ImageView vista = new ImageView(FRAME_IDLE_1);
    private CombateControlador combate;
    private double tiempoHastaAtaque;
    private double tiempoAnimacionIdle;
    private double tiempoReaccionGolpe;
    private boolean frameIdleAlterno;

    public EnemigoControlador(Personaje objetivo) {
        this(objetivo, 360, 240, VELOCIDAD);
    }

    public EnemigoControlador(Personaje objetivo, double x, double y, double velocidad) {
        this(objetivo, x, y, velocidad, VIDA_MAXIMA, DANIO, EXPERIENCIA_RECOMPENSA);
    }

    public EnemigoControlador(
        Personaje objetivo,
        double x,
        double y,
        double multiplicadorVida,
        double multiplicadorDanio
    ) {
        this(
            objetivo,
            x,
            y,
            VELOCIDAD,
            VIDA_MAXIMA * validarMultiplicador(multiplicadorVida, "vida"),
            DANIO * validarMultiplicador(multiplicadorDanio, "daño"),
            EXPERIENCIA_RECOMPENSA
        );
    }

    private static double validarMultiplicador(double multiplicador, String atributo) {
        if (!Double.isFinite(multiplicador) || multiplicador <= 0) {
            throw new IllegalArgumentException(
                "El multiplicador de " + atributo + " debe ser positivo y finito."
            );
        }
        return multiplicador;
    }

    private EnemigoControlador(
        Personaje objetivo,
        double x,
        double y,
        double velocidad,
        double vidaMaxima,
        double danio,
        int experienciaRecompensa
    ) {
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo del enemigo no puede ser nulo.");
        }
        this.objetivo = objetivo;
        VecDouble2D posicion = new VecDouble2D(x, y);
        modelo = new Enemigo(
            "enemigo",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 32, 32),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ENEMIGO),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            posicion,
            velocidad,
            vidaMaxima,
            danio,
            experienciaRecompensa
        );
        vista.setFitWidth(ANCHO_VISTA);
        vista.setFitHeight(ALTO_VISTA);
        vista.setPreserveRatio(true);
        actualizarVista();
    }

    @Override
    public Enemigo getModelo() {
        return modelo;
    }

    @Override
    public Node getVista() {
        return vista;
    }

    public boolean recibirDanio(double cantidad) {
        boolean derrotado = modelo.recibirDanio(cantidad);
        double proporcionVida = modelo.getVida() / modelo.getVidaMaxima();
        vista.setOpacity(0.55 + 0.45 * proporcionVida);
        tiempoReaccionGolpe = DURACION_REACCION_GOLPE;
        vista.setImage(FRAME_GOLPE);
        return derrotado;
    }

    @Override
    public void tick(Double dt) {
        if (objetivo.getVida() <= 0 || modelo.getVida() <= 0) {
            return;
        }
        actualizarAnimacion(dt);

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
                if (combate == null) {
                    objetivo.recibirDanio(modelo.getDanio());
                } else {
                    combate.recibirDanio(objetivo, modelo.getDanio());
                }
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

    void asociarCombate(CombateControlador combate) {
        if (combate == null) {
            throw new IllegalArgumentException("El controlador de combate no puede ser nulo.");
        }
        if (this.combate != null && this.combate != combate) {
            throw new IllegalStateException("El enemigo ya pertenece a otro combate.");
        }
        this.combate = combate;
    }

    private void actualizarVista() {
        vista.setLayoutX(modelo.getPosicion().getX() - (ANCHO_VISTA - 32) / 2);
        vista.setLayoutY(modelo.getPosicion().getY() - (ALTO_VISTA - 32) / 2);
    }

    private void actualizarAnimacion(double dt) {
        if (tiempoReaccionGolpe > 0) {
            tiempoReaccionGolpe = Math.max(0, tiempoReaccionGolpe - dt);
            if (tiempoReaccionGolpe == 0) {
                vista.setImage(frameIdleAlterno ? FRAME_IDLE_2 : FRAME_IDLE_1);
            }
            return;
        }

        tiempoAnimacionIdle += dt;
        if (tiempoAnimacionIdle >= DURACION_FRAME_IDLE) {
            tiempoAnimacionIdle %= DURACION_FRAME_IDLE;
            frameIdleAlterno = !frameIdleAlterno;
            vista.setImage(frameIdleAlterno ? FRAME_IDLE_2 : FRAME_IDLE_1);
        }
    }

    private static Image cargarFrame(String ruta) {
        var recurso = EnemigoControlador.class.getResource(
            "/recursos/imagenes/Transparent PNG/" + ruta);
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el sprite del enemigo: " + ruta);
        }
        return new Image(recurso.toExternalForm());
    }
}