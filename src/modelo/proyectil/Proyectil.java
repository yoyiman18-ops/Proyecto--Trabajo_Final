package modelo.proyectil;

import java.util.List;

import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class Proyectil
extends Entidad
{
    private double tiempoVida;
    private int penetracion; // <0 para que tenga penetracion infinita

    public Proyectil(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double tiempoVida,
        int penetracion,
        double daño,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad,
            0.0,
            0.0,
            0.0,
            estrategiasExpiracion,
            interaccionesColision
        );
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) { throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito."); }
        if (daño > 0) { System.out.println("Hola"); añadirInteraccionColision(new Interaccion(TipoInteraccion.DAÑAR.clase(), daño)); }
        this.tiempoVida = tiempoVida;
        this.penetracion = penetracion;
    }

    @Override public void tick(double dt) {
        if (!activo()) { return; }
        tiempoVida = Math.max(0, tiempoVida - dt); 
        super.tick(dt);
        if (estaExpirado()) { expirar(); }
    }

    @Override public boolean interactuar(Interaccion i) {
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        return switch (interaccion) {
            case DAÑAR -> recibirDaño(i.valor());
            case SOLIDO -> recibirDaño(i.valor());
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

    private boolean estaExpirado() { return tiempoVida == 0; }
}
