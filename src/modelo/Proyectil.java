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
import motor.util.observer.NotificadorFuerte;

public final class Proyectil
extends Entidad
implements ModeloProyectil
{

    private final double daño;
    private double tiempoVida;
    private int penetracion;
    private final Notificador<Integer> notificadorExpiracion;

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
        this.notificadorExpiracion = new NotificadorFuerte<>();
    }

    @Override public void tick(double dt) {
        if (!activo()) { return; }
        tiempoVida = Math.max(0, tiempoVida - dt); 
        super.tick(dt);
        if (estaExpirado()) { setActivo(false); }
    }

    @Override public double getDaño() { return daño; }
    @Override public boolean dañar(Dañable o) { return o.recibirDaño(this.getDaño()); }
    @Override public boolean recibirDaño(double daño) {
        penetracion -= daño;
        if (penetracion <= 0) { expirar(); }
        return true;
    }

    @Override public void colisionar(Colisionable o) {
        o.colisionarCon(getDañable());
    }
    
    @Override public void colisionarCon(Dañable o) { if (activo()) { dañar(o); }}
    @Override public void colisionarCon(Solido o) { 
        if (activo()) { 
            if (o.getNivelSolidez() > 0) { recibirDaño(o.getNivelSolidez()); }
            else { expirar(); }
        }
    }

    @Override public Dañable getDañable() { return this; }

    private void expirar() {
        setActivo(false);

    }
    private boolean estaExpirado() { return tiempoVida == 0; }


}
