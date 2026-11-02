package modelo.experiencia;

import java.util.List;

import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class Experiencia extends Entidad {
    private double cantidadExp;

    public Experiencia(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double cantidadExp,
        String animacionDefault,
        String animacionExpirar
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad != null ? velocidad : new VecDouble2D(),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar
        );
        if (!Double.isFinite(cantidadExp) || cantidadExp <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser positiva y finita.");
        }
        this.cantidadExp = cantidadExp;
        añadirInteraccionColision(new Interaccion(TipoInteraccion.GANAREXP.clase(), cantidadExp));
    }

    public Experiencia(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double cantidadExp,
        String animacionDefault,
        String animacionExpirar
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(0, 0),
            0.0,
            0.0,
            0.0,
            estrategiasExpiracion,
            interaccionesColision,
            cantidadExp,
            animacionDefault,
            animacionExpirar
        );
    }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        super.tick(dt);
    }

    @Override
    public boolean interactuar(Interaccion i) {
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == null) { return false; }
        return switch (interaccion) {
            case GANAREXP, AÑADIR_EXP -> {
                recoger();
                yield true;
            }
            default -> false;
        };
    }

    public boolean recoger() {
        if (!activo()) { return false; }
        expirar();
        return true;
    }

    public double getCantidadExp() {
        return this.cantidadExp;
    }

    public void setCantidadExp(double cantidadExp) {
        if (!Double.isFinite(cantidadExp) || cantidadExp <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser positiva y finita.");
        }
        this.cantidadExp = cantidadExp;
    }
}
