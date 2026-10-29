package modelo;
import motor.modelo.Dañable;
import motor.modelo.Dañador;
import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.Solido;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Proyectil
extends Entidad
implements Dañable,Dañador
{

    private final double daño;
    private double tiempoVida;
    private int penetracion;


    public Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double daño,
        double tiempoVida,
        int penetracion
    ) {
        super(
            nombre,
            hitbox,
            posicion,
            velocidad,
            0.0,
            0.0,
            0.0
        );
        if (!Double.isFinite(daño) || daño < 0) { throw new IllegalArgumentException("El daño debe ser finito y no negativo.");}
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) { throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito."); }
        this.daño = daño;
        this.tiempoVida = tiempoVida;
        this.penetracion = penetracion;
    }

    @Override public double getDaño() { return daño; }
    @Override public void tick(double dt) {
        if (!super.activo()) { return; }
        tiempoVida = Math.max(0, tiempoVida - dt); 
        super.tick(dt);
        if (estaExpirado()) { setActivo(false); }
    }

    @Override public boolean dañar(Dañable o) { return o.recibirDaño(this.getDaño()); }
    @Override public boolean recibirDaño(double daño) {
        if (activo()) {
            penetracion -= daño;
            if (penetracion <= 0) { setActivo(false); }
            return true;
        }
        else return false;
    }



    @Override public void colisionar(Colisionable o) {
        o.colisionarCon(getDañador());
        o.colisionarCon(getDañable());
    }

    @Override public void colisionarCon(Dañador o) { if (activo()) { recibirDaño(o.getDaño()); }}
    @Override public void colisionarCon(Dañable o) { if (activo()) { o.recibirDaño(this.getDaño()); }}
    @Override public void colisionarCon(Solido o) { if (activo()) { recibirDaño(o.getNivelSolidez()); }
    }

    @Override public Dañable getDañable() { return this; }
    @Override public Dañador getDañador() { return this; }

    private boolean estaExpirado() { return tiempoVida == 0; }


}
