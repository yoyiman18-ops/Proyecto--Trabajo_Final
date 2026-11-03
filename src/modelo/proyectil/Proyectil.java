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
    private VecDouble2D direccion;
    private double velocidad;
    private double tiempoVida;
    private int penetracion; // <0 para que tenga penetracion infinita

    public Proyectil(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
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
            direccion.clone().normalizar().producto(velocidad),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar
        );
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) { throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito."); }
        if (daño > 0) { System.out.println("Hola"); añadirInteraccionColision(new Interaccion(TipoInteraccion.DAÑAR.clase(), daño)); }
        this.direccion = direccion.clone().normalizar();
        this.velocidad = velocidad;
        this.tiempoVida = tiempoVida;
        this.penetracion = Math.max(-1,penetracion);
    }

    @Override public void tick(double dt) {
        if (!activo()) { return; }
        tiempoVida = Math.max(0, tiempoVida - dt); 
        super.tick(dt);
        if (estaExpirado()) { expirar(); }
    }

    @Override public boolean interactuar(Interaccion i) {
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

    public VecDouble2D getDireccion() { return this.direccion.clone(); }
    public double getVelocidadDouble() { return this.velocidad; }

    private boolean estaExpirado() { return tiempoVida == 0; }
}
