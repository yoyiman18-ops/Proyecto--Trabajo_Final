package modelo;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

import modelo.proyectil.Proyectil;
import modelo.proyectil.ProyectilFactory;
import motor.colisiones.hitboxes.Hitbox;
import motor.entrada.EstadoAcciones;
import motor.entrada.ReceptorEstadoAcciones;
import motor.modelo.Controlable;
import motor.modelo.Direccion;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.EstrategiaMovimientoLineal;
import motor.modelo.Interaccion;
import motor.util.ContadorSegundos;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class Jugador extends Personaje implements Controlable {

    private int experiencia;
    private int nivel;
    private int experienciaSiguiente;
    private final int incrementoExperienciaPorNivel;
    private int nivelesPendientes;
    private double dañoAtaque;
    private double velocidadAtaque;
    private final ContadorSegundos cooldownAtaque;

    private final ProyectilFactory proyectilFactory;
    private final Integer idSonidoDisparo;
    private final Integer idSonidoSubirNivel;

    private Direccion direccion;
    private Direccion ultimaDireccionAnimada = Direccion.ABAJO;
    private final VecDouble2D vectorDireccion;
    private final Notificador<Jugador> notificadorVida;

    public Jugador(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault,
        String animacionExpirar,
        Integer idSonidoExpiracion,
        double vidaMaxima,
        double defensaMaximaPorcentual,
        Integer idSonidoDaño,
        int nivelInicial,
        int experienciaSiguienteInicial,
        int incrementoExperienciaPorNivel,
        double dañoAtaque,
        double velocidadAtaque,
        ProyectilFactory proyectilFactory,
        Integer idSonidoDisparo,
        Integer idSonidoSubirNivel
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion,
            vidaMaxima,
            defensaMaximaPorcentual,
            idSonidoDaño
        );
        if (nivelInicial <= 0) {
            throw new IllegalArgumentException("El nivel inicial debe ser positivo.");
        }
        if (experienciaSiguienteInicial <= 0) {
            throw new IllegalArgumentException("La experiencia siguiente inicial debe ser positiva.");
        }
        if (incrementoExperienciaPorNivel < 0) {
            throw new IllegalArgumentException("El incremento de experiencia por nivel no puede ser negativo.");
        }
        if (!Double.isFinite(dañoAtaque) || dañoAtaque < 0) {
            throw new IllegalArgumentException("El daño de ataque no puede ser negativo.");
        }
        if (!Double.isFinite(velocidadAtaque) || velocidadAtaque <= 0) {
            throw new IllegalArgumentException("La velocidad de ataque debe ser positiva.");
        }
        this.experiencia = 0;
        this.nivel = nivelInicial;
        this.experienciaSiguiente = experienciaSiguienteInicial;
        this.incrementoExperienciaPorNivel = incrementoExperienciaPorNivel;
        this.nivelesPendientes = 0;
        this.dañoAtaque = dañoAtaque;
        this.velocidadAtaque = velocidadAtaque;
        this.cooldownAtaque = new ContadorSegundos(1.0 / velocidadAtaque, true);

        this.direccion = Direccion.ABAJO;
        this.vectorDireccion = VecDouble2D.abajo();
        this.notificadorVida = new NotificadorDebil<>();
        this.proyectilFactory = proyectilFactory;
        this.idSonidoDisparo = idSonidoDisparo;
        this.idSonidoSubirNivel = idSonidoSubirNivel;
        añadirInteraccionColision(new Interaccion(TipoInteraccion.AÑADIR_EXP.clase(), 0.0));
    }

    @Override
    public void cambio(EstadoAcciones e) {
        if (getEstrategiaMovimiento() instanceof ReceptorEstadoAcciones receptor) {
            receptor.getObservadorAcciones().cambio(e);
        }
    }

    @Override
    public void tick(double dt) {
        if (!activo()) {
            return;
        }
        super.tick(dt);
        VecDouble2D vel = getVelocidad();
        if (vel.getX() != 0.0 || vel.getY() != 0.0) {
            VecDouble2D normalizada = vel.clone().normalizado();
            vectorDireccion.setX(normalizada.getX());
            vectorDireccion.setY(normalizada.getY());
            if (Math.abs(vel.getX()) >= Math.abs(vel.getY())) {
                direccion = vel.getX() >= 0.0 ? Direccion.DERECHA : Direccion.IZQUIERDA;
            } else {
                direccion = vel.getY() >= 0.0 ? Direccion.ABAJO : Direccion.ARRIBA;
            }
            if (direccion != ultimaDireccionAnimada) {
                ultimaDireccionAnimada = direccion;
                getNotificadorAnimacion().notificar(obtenerAnimacionMovimiento(direccion));
            }
        }
        cooldownAtaque.tick(dt);
    }

    private String obtenerAnimacionMovimiento(Direccion dir) {
        return switch (dir) {
            case ARRIBA -> "caminar_arriba";
            case ABAJO -> "caminar_abajo";
            case IZQUIERDA -> "caminar_izquierda";
            case DERECHA -> "caminar_derecha";
        };
    }

    public boolean atacar() {
        if (!activo() || !cooldownAtaque.estaListo() || proyectilFactory == null) {
            return false;
        }
        cooldownAtaque.reiniciar();
        Proyectil proyectil = proyectilFactory.get(
            getPosicion().clone(),
            new EstrategiaMovimientoLineal(vectorDireccion.clone()),
            dañoAtaque
        );

        Rectangle2D boundsJugador = getHitbox().getBounds();
        Rectangle2D boundsProyectil = proyectil.getHitbox().getBounds();
        proyectil.setPosicion(new VecDouble2D(
            boundsJugador.getCenterX() - (boundsProyectil.getCenterX() - proyectil.getPosicion().getX()),
            boundsJugador.getCenterY() - (boundsProyectil.getCenterY() - proyectil.getPosicion().getY())
        ));
        getNotificadorInstanciacion().notificar(proyectil);
        if (idSonidoDisparo != null) {
            emitirSonido(idSonidoDisparo);
        }
        return true;
    }

    public int ganarExperiencia(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }
        long experienciaAcumulada = (long) experiencia + cantidad;
        int nivelesGanados = 0;
        while (experienciaAcumulada >= experienciaSiguiente) {
            experienciaAcumulada -= experienciaSiguiente;
            nivel++;
            experienciaSiguiente += incrementoExperienciaPorNivel;
            nivelesGanados++;
        }
        experiencia = (int) experienciaAcumulada;
        nivelesPendientes += nivelesGanados;
        if (nivelesGanados > 0 && idSonidoSubirNivel != null) {
            emitirSonido(idSonidoSubirNivel);
        }
        return nivelesGanados;
    }

    public boolean consumirNivelPendiente() {
        if (nivelesPendientes == 0) {
            return false;
        }
        nivelesPendientes--;
        return true;
    }

    public boolean modificarDañoAtaque(double cantidad) {
        double nuevoDaño = dañoAtaque + cantidad;
        if (nuevoDaño <= 0) { return false; }
        this.dañoAtaque = nuevoDaño; return true;
    }

    public void aumentarDanioAtaque(double cantidad) {
        modificarDañoAtaque(cantidad);
    }

    public void aumentarVelocidadAtaque(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de velocidad de ataque debe ser finito y no negativo.");
        }
        double nuevaVelocidadAtaque = velocidadAtaque * (1.0 + porcentaje / 100.0);
        if (!Double.isFinite(nuevaVelocidadAtaque)) {
            throw new IllegalArgumentException("La velocidad de ataque resultante debe ser finita.");
        }
        velocidadAtaque = nuevaVelocidadAtaque;
        cooldownAtaque.setIntervaloSegundos(1.0 / velocidadAtaque);
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) {
            return false;
        }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == TipoInteraccion.GANAREXP || interaccion == TipoInteraccion.AÑADIR_EXP) {
            ganarExperiencia((int) i.valor());
            return true;
        }
        if (interaccion == TipoInteraccion.ATAQUE_ENEMIGO) {
            recibirDaño(i.valor());
            return true;
        }
        return super.interactuar(i);
    }

    protected void alModificarVida() { notificadorVida.notificar(this); }

    public int getExperiencia() { return experiencia; }

    public int getNivel() { return nivel; }

    public int getExperienciaSiguiente() { return experienciaSiguiente; }

    public int getNivelesPendientes() { return nivelesPendientes; }

    public double getdañoAtaque() { return dañoAtaque; }

    public double getDanioAtaque() { return dañoAtaque; }

    public Notificador<Jugador> getNotificadorVida() { return notificadorVida; }
}
