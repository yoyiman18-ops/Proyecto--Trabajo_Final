package modelo.proyectil;

import java.util.ArrayList;
import java.util.List;

import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class Proyectil extends Entidad {
    private double tiempoVida;
    private int penetracion; // <0 para que tenga penetracion infinita

    public Proyectil(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double tiempoVida,
        int penetracion,
        double daño,
        String animacionDefault,
        String animacionExpirar
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(0, 0),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar
        );
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito.");
        }
        if (daño > 0) {
            añadirInteraccionColision(new Interaccion(TipoInteraccion.DAÑAR.clase(), daño));
        }
        this.tiempoVida = tiempoVida;
        this.penetracion = Math.max(-1, penetracion);
    }

    public Proyectil(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        double tiempoVida,
        int penetracion,
        double daño
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            aceleracion,
            friccion,
            velocidadMax,
            estrategiaMovimiento,
            new ArrayList<>(),
            new ArrayList<>(),
            tiempoVida,
            penetracion,
            daño,
            "idle",
            "expirar"
        );
    }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        tiempoVida = Math.max(0, tiempoVida - dt);
        super.tick(dt);
        if (estaExpirado()) { expirar(); }
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) { return false; }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == null) { return false; }
        return switch (interaccion) {
            case DAÑAR -> recibirDaño(i.valor());
            case SOLIDO -> recibirDaño(Math.max(1.0, i.valor()));
            default -> false;
        };
    }

    protected boolean recibirDaño(double daño) {
        if (!activo() || penetracion < 0) { return false; }
        System.out.println(this.getHitbox().getId() + " ha recibido " + daño);
        penetracion -= daño;
        if (penetracion <= 0) { expirar(); }
        return true;
    }

    public double getTiempoVida() { return this.tiempoVida; }
    public int getPenetracion() { return this.penetracion; }
    private boolean estaExpirado() { return tiempoVida == 0; }
}
