package modelo.personaje;

import java.util.ArrayList;
import java.util.List;
import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

/**
 * Representa un personaje en el modelo (jugador, enemigo o NPC).
 * Extiende de {@link Entidad} integrando estadísticas de combate y
 * delega su comportamiento cinemático a la {@link EstrategiaMovimiento} gestionada por {@link Entidad}.
 */
public class Personaje extends Entidad {

    private double vidaMax;
    private double vida;
    private double defensa;
    private double dañoContacto;
    private double velocidadBase;

    public Personaje(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadBase,
        double vidaMax,
        double defensa,
        double dañoContacto,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault,
        String animacionExpirar
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad != null ? velocidad : new VecDouble2D(0, 0),
            aceleracion,
            friccion,
            velocidadBase,
            estrategiaMovimiento,
            estrategiasExpiracion != null ? estrategiasExpiracion : new ArrayList<>(),
            interaccionesColision != null ? new ArrayList<>(interaccionesColision) : new ArrayList<>(),
            animacionDefault,
            animacionExpirar
        );
        this.vidaMax = Math.max(1.0, vidaMax);
        this.vida = this.vidaMax;
        this.defensa = Math.max(0.0, defensa);
        this.dañoContacto = Math.max(0.0, dañoContacto);
        this.velocidadBase = velocidadBase;

        if (this.dañoContacto > 0) {
            actualizarInteraccionDañoContacto();
        }
    }

    public Personaje(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadBase,
        double vidaMax,
        double defensa,
        double dañoContacto,
        EstrategiaMovimiento estrategiaMovimiento
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(0, 0),
            aceleracion,
            friccion,
            velocidadBase,
            vidaMax,
            defensa,
            dañoContacto,
            estrategiaMovimiento,
            new ArrayList<>(),
            new ArrayList<>(),
            "idle",
            "expirar"
        );
    }

    // --- Interacciones y Combate ---

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) { return false; }
        TipoInteraccion tipo = TipoInteraccion.deInt(i.clase());
        if (tipo == null) { return false; }

        return switch (tipo) {
            case DAÑAR -> recibirDaño(i.valor());
            default    -> false;
        };
    }

    public boolean recibirDaño(double dañoBruto) {
        if (!activo() || vida <= 0 || dañoBruto <= 0) { return false; }

        double dañoEfectivo = Math.max(0.0, dañoBruto - defensa);
        this.vida = Math.max(0.0, this.vida - dañoEfectivo);

        if (this.vida == 0) {
            expirar();
        }
        return true;
    }

    public void curar(double cantidad) {
        if (!activo() || cantidad <= 0 || vida <= 0) { return; }
        this.vida = Math.min(this.vidaMax, this.vida + cantidad);
    }

    private void actualizarInteraccionDañoContacto() {
        getInteracciones().removeIf(inter -> inter != null && inter.clase() == TipoInteraccion.DAÑAR.clase());
        if (this.dañoContacto > 0) {
            añadirInteraccionColision(new Interaccion(TipoInteraccion.DAÑAR.clase(), this.dañoContacto));
        }
    }

    // --- Getters y Setters ---

    public double getVida() { return this.vida; }
    public void setVida(double vida) { this.vida = Math.clamp(vida, 0.0, this.vidaMax); }

    public double getVidaMax() { return this.vidaMax; }
    public void setVidaMax(double vidaMax) {
        this.vidaMax = Math.max(1.0, vidaMax);
        if (this.vida > this.vidaMax) {
            this.vida = this.vidaMax;
        }
    }

    public double getDefensa() { return this.defensa; }
    public void setDefensa(double defensa) { this.defensa = Math.max(0.0, defensa); }

    public double getDañoContacto() { return this.dañoContacto; }
    public void setDañoContacto(double dañoContacto) {
        this.dañoContacto = Math.max(0.0, dañoContacto);
        actualizarInteraccionDañoContacto();
    }

    public double getVelocidadBase() { return this.velocidadBase; }
    public void setVelocidadBase(double velocidadBase) {
        this.velocidadBase = velocidadBase;
        setVelocidadMax(velocidadBase);
    }
}
