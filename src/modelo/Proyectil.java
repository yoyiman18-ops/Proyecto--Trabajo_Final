
package modelo;

import java.util.function.BiConsumer;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.Hitbox;
import motor.colisiones.hitboxes.MascaraColision;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public abstract class Proyectil extends Entidad {

    private final double danio;
    private double tiempoVida;
    private int penetracion;

    protected Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double danio,
        double tiempoVida,
        int penetracion
    ) {
        super(
            nombre,
            hitbox,
            posicion,
            crearVelocidad(direccion, velocidad),
            0,
            0,
            velocidad
        );

        if (!Double.isFinite(danio) || danio < 0) {
            throw new IllegalArgumentException(
                "El daño debe ser finito y no negativo."
            );
        }

        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException(
                "El tiempo de vida debe ser positivo y finito."
            );
        }

        if (penetracion <= 0) {
            throw new IllegalArgumentException(
                "La penetración debe ser positiva."
            );
        }

        this.danio = danio;
        this.tiempoVida = tiempoVida;
        this.penetracion = penetracion;
    }

    private static VecDouble2D crearVelocidad(
        VecDouble2D direccion,
        double velocidad
    ) {
        if (direccion == null) {
            throw new IllegalArgumentException(
                "La dirección no puede ser nula."
            );
        }

        if (!Double.isFinite(velocidad) || velocidad <= 0) {
            throw new IllegalArgumentException(
                "La velocidad debe ser positiva y finita."
            );
        }

        VecDouble2D direccionNormalizada = direccion.clone();

        if (direccionNormalizada.getX() == 0
                && direccionNormalizada.getY() == 0) {
            throw new IllegalArgumentException(
                "La dirección del proyectil no puede ser cero."
            );
        }

        direccionNormalizada.normalizar();
        direccionNormalizada.producto(velocidad);

        return direccionNormalizada;
    }

    public double getDanio() {
        return danio;
    }

    public double getTiempoVida() {
        return tiempoVida;
    }

    public boolean estaActivo() {
        return colisionesActivas();
    }

    public int getPenetracion() {
        return penetracion;
    }

    public void actualizar(double dt) {
        if (!Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException(
                "El tiempo de actualización debe ser finito y no negativo."
            );
        }

        if (!estaActivo()) {
            return;
        }

        tiempoVida -= dt;

        if (tiempoVida <= 0) {
            tiempoVida = 0;
            desactivar();
            return;
        }

        mover(dt);
    }

    protected abstract boolean puedeImpactar(CategoriaColision categoria);

    @Override
    public final void colisionar(Colisionable otra) {
        colisionar(otra, ObjetivoDanio::aplicarDanio);
    }

    public final void colisionar(
        Colisionable otra,
        BiConsumer<ObjetivoDanio, Double> aplicarDanio
    ) {
        if (otra == null) {
            throw new IllegalArgumentException("El objetivo del proyectil no puede ser nulo.");
        }
        if (aplicarDanio == null) {
            throw new IllegalArgumentException("El resolutor de daño no puede ser nulo.");
        }
        if (!estaActivo()) {
            return;
        }

        MascaraColision capasOtra = otra.getHitbox().getCapasColision();
        if (capasOtra.contiene(CategoriaColision.MURO)) {
            desactivar();
            return;
        }

        MascaraColision categoriasOtra = otra.getHitbox().getCategoriasColision();
        if (puedeImpactar(categoriasOtra)) {
            if (!(otra instanceof ObjetivoDanio objetivo)) {
                throw new IllegalStateException(
                    "El colisionable de categoría compatible debe poder recibir daño."
                );
            }
            aplicarDanio.accept(objetivo, danio);
        }

        penetracion--;
        if (penetracion == 0) {
            desactivar();
        }
    }

    private boolean puedeImpactar(
        MascaraColision categorias
    ) {
        for (CategoriaColision categoria : CategoriaColision.values()) {
            if (categorias.contiene(categoria) && puedeImpactar(categoria)) {
                return true;
            }
        }
        return false;
    }
}
