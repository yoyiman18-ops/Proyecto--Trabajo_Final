package modelo.proyectil;
import motor.modelo.Dañable;

import java.util.List;

import modelo.EstrategiaExpiracion;
import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.Solido;
import motor.util.VecDouble2D;

public class Proyectil
extends Entidad
implements ModeloProyectil
{

    private final double daño;
    private double tiempoVida;
    private int penetracion; // <0 para que tenga penetracion infinita

    public Proyectil(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double daño,
        double tiempoVida,
        int penetracion,
        List<EstrategiaExpiracion> estrategiasExpiracion
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
            estrategiasExpiracion
        );
        if (!Double.isFinite(daño) || daño < 0) { throw new IllegalArgumentException("El daño debe ser finito y no negativo.");}
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) { throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito."); }
        this.daño = daño;
        this.tiempoVida = tiempoVida;
        this.penetracion = penetracion;
    }

    @Override public void tick(double dt) {
        if (!activo()) { return; }
        tiempoVida = Math.max(0, tiempoVida - dt); 
        super.tick(dt);
        if (estaExpirado()) { expirar(1); }
    }

    @Override public double getDaño() { return daño; }
    @Override public boolean dañar(Dañable o) { return o.recibirDaño(this.getDaño()); }
    @Override public boolean recibirDaño(double daño) {
        if (!activo() || penetracion < 0) { return false; }
        System.out.println(this.getHitbox().getId() + " ha recibido " + daño);
        penetracion -= daño;
        if (penetracion <= 0) { expirar(1); }
        return true;
    }

    @Override public void colisionar(Colisionable o) {
        if (activo()) {
            o.colisionarCon(getDañable());
        }
    }
    
    @Override public void colisionarCon(Dañable o) { if (activo()) { dañar(o); recibirDaño(1); }}
    @Override public void colisionarCon(Solido o) { 
        if (activo()) { 
            if (o.getNivelSolidez() > 0) { recibirDaño(o.getNivelSolidez()); }
            else { expirar(1); }
        }
    }

    @Override public Dañable getDañable() { return this; }
    private boolean estaExpirado() { return tiempoVida == 0; }

    

}
