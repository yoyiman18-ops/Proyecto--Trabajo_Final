package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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
import motor.modelo.ModeloEntidad;
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
    private double danioAtaque;
    private double velocidadAtaque;
    private double tiempoEnfriamientoAtaque;
    private double probabilidadCritico;
    private double probabilidadMultidisparo;
    private double multiplicadorCritico;
    private double offsetDisparoX;
    private double offsetDisparoY;
    private double desviacionMultidisparoGrados;

    private ProyectilFactory proyectilFactory;
    private Integer idSonidoDisparo;
    private Integer idSonidoDisparoCritico;

    private Direccion direccion;
    private Direccion ultimaDireccionAnimada = Direccion.ABAJO;
    private EstadoAcciones estadoAccionesActual;
    private final VecDouble2D vectorDireccion;
    private final Notificador<Jugador> notificadorVida;
    private final Integer idSonidoSubirNivel;

    public Jugador(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<Integer> notificadorSonido,
        Notificador<String> notificadorAnimacion,
        Notificador<Double> notificadorDaño,
        Notificador<Jugador> notificadorVida,
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
        double danioAtaque,
        double velocidadAtaque,
        double probabilidadCritico,
        double probabilidadMultidisparo,
        double multiplicadorCritico,
        double offsetDisparoX,
        double offsetDisparoY,
        double desviacionMultidisparoGrados,
        ProyectilFactory proyectilFactory,
        Integer idSonidoDisparo,
        Integer idSonidoDisparoCritico,
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
            notificadorPosicion,
            notificadorInstanciacion,
            notificadorExpiracion,
            notificadorSonido,
            notificadorAnimacion,
            notificadorDaño,
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
        if (!Double.isFinite(danioAtaque) || danioAtaque < 0) {
            throw new IllegalArgumentException("El daño de ataque no puede ser negativo.");
        }
        if (!Double.isFinite(velocidadAtaque) || velocidadAtaque <= 0) {
            throw new IllegalArgumentException("La velocidad de ataque debe ser positiva.");
        }
        if (!Double.isFinite(probabilidadCritico) || probabilidadCritico < 0 || probabilidadCritico > 100.0) {
            throw new IllegalArgumentException("La probabilidad de crítico debe estar entre 0 y 100.0.");
        }
        if (!Double.isFinite(probabilidadMultidisparo) || probabilidadMultidisparo < 0 || probabilidadMultidisparo > 100.0) {
            throw new IllegalArgumentException("La probabilidad de multidisparo debe estar entre 0 y 100.0.");
        }
        if (!Double.isFinite(multiplicadorCritico) || multiplicadorCritico <= 0) {
            throw new IllegalArgumentException("El multiplicador de daño crítico debe ser positivo.");
        }
        this.experiencia = 0;
        this.nivel = nivelInicial;
        this.experienciaSiguiente = experienciaSiguienteInicial;
        this.incrementoExperienciaPorNivel = incrementoExperienciaPorNivel;
        this.nivelesPendientes = 0;
        this.danioAtaque = danioAtaque;
        this.velocidadAtaque = velocidadAtaque;
        this.tiempoEnfriamientoAtaque = 0.0;
        this.probabilidadCritico = probabilidadCritico;
        this.probabilidadMultidisparo = probabilidadMultidisparo;
        this.multiplicadorCritico = multiplicadorCritico;
        this.offsetDisparoX = offsetDisparoX;
        this.offsetDisparoY = offsetDisparoY;
        this.desviacionMultidisparoGrados = desviacionMultidisparoGrados;

        this.direccion = Direccion.ABAJO;
        this.vectorDireccion = VecDouble2D.abajo();
        this.notificadorVida = Objects.requireNonNull(notificadorVida, "El notificadorVida no puede ser nulo");
        this.proyectilFactory = proyectilFactory;
        this.idSonidoDisparo = idSonidoDisparo;
        this.idSonidoDisparoCritico = idSonidoDisparoCritico;
        this.idSonidoSubirNivel = idSonidoSubirNivel;
        añadirInteraccionColision(new Interaccion(TipoInteraccion.AÑADIR_EXP.clase(), 0.0));
    }

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
        double danioAtaque,
        double velocidadAtaque,
        double probabilidadCritico,
        double probabilidadMultidisparo,
        double multiplicadorCritico,
        double offsetDisparoX,
        double offsetDisparoY,
        double desviacionMultidisparoGrados,
        ProyectilFactory proyectilFactory,
        Integer idSonidoDisparo,
        Integer idSonidoDisparoCritico,
        Integer idSonidoSubirNivel
    ) {
        this(
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
            new NotificadorFuerte<>(),
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion,
            vidaMaxima,
            defensaMaximaPorcentual,
            idSonidoDaño,
            nivelInicial,
            experienciaSiguienteInicial,
            incrementoExperienciaPorNivel,
            danioAtaque,
            velocidadAtaque,
            probabilidadCritico,
            probabilidadMultidisparo,
            multiplicadorCritico,
            offsetDisparoX,
            offsetDisparoY,
            desviacionMultidisparoGrados,
            proyectilFactory,
            idSonidoDisparo,
            idSonidoDisparoCritico,
            idSonidoSubirNivel
        );
    }

    @Override
    public void cambio(EstadoAcciones e) {
        this.estadoAccionesActual = e;
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
        if (tiempoEnfriamientoAtaque > 0) {
            tiempoEnfriamientoAtaque = Math.max(0.0, tiempoEnfriamientoAtaque - dt);
        }
    }

    private String obtenerAnimacionMovimiento(Direccion dir) {
        return switch (dir) {
            case ARRIBA -> "caminar_arriba";
            case ABAJO -> "caminar_abajo";
            case IZQUIERDA -> "caminar_izquierda";
            case DERECHA -> "caminar_derecha";
        };
    }

    public boolean intentarAtacar() {
        if (!activo() || tiempoEnfriamientoAtaque > 0) {
            return false;
        }
        tiempoEnfriamientoAtaque = 1.0 / velocidadAtaque;
        return true;
    }

    public List<DatosDisparo> realizarAtaque() {
        if (!intentarAtacar()) {
            return List.of();
        }
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        boolean esCritico = aleatorio.nextDouble(100.0) < probabilidadCritico;
        double danioFinal = esCritico ? danioAtaque * multiplicadorCritico : danioAtaque;
        VecDouble2D origen = new VecDouble2D(getPosicion().getX() + offsetDisparoX, getPosicion().getY() + offsetDisparoY);
        VecDouble2D dirPrincipal = getVectorDireccion();

        List<DatosDisparo> disparos = new ArrayList<>();
        disparos.add(new DatosDisparo(origen.clone(), dirPrincipal.clone(), danioFinal, esCritico));

        if (aleatorio.nextDouble(100.0) < probabilidadMultidisparo) {
            double anguloBase = Math.atan2(dirPrincipal.getY(), dirPrincipal.getX());
            double desviacion = Math.toRadians(desviacionMultidisparoGrados);
            VecDouble2D dirExtra = new VecDouble2D(
                Math.cos(anguloBase + desviacion),
                Math.sin(anguloBase + desviacion)
            );
            disparos.add(new DatosDisparo(origen.clone(), dirExtra, danioFinal, esCritico));
        }
        return List.copyOf(disparos);
    }

    public boolean atacar() {
        if (proyectilFactory == null) {
            return false;
        }
        List<DatosDisparo> disparos = realizarAtaque();
        if (disparos.isEmpty()) {
            return false;
        }
        boolean algunCritico = false;
        for (DatosDisparo disparo : disparos) {
            algunCritico |= disparo.esCritico();
            proyectilFactory.setPosicion(disparo.origen());
            proyectilFactory.setEstrategiaMovimiento(new EstrategiaMovimientoLineal(disparo.direccion()));
            proyectilFactory.setDañoAdicional(Math.max(0.0, disparo.danio() - 1.0));
            getNotificadorInstanciacion().notificar(proyectilFactory.get());
        }
        if (algunCritico && idSonidoDisparoCritico != null) {
            emitirSonido(idSonidoDisparoCritico);
        } else if (idSonidoDisparo != null) {
            emitirSonido(idSonidoDisparo);
        }
        return true;
    }

    public ProyectilFactory getProyectilFactory() {
        return proyectilFactory;
    }

    public void setProyectilFactory(ProyectilFactory proyectilFactory) {
        this.proyectilFactory = proyectilFactory;
    }

    public Integer getIdSonidoDisparo() {
        return idSonidoDisparo;
    }

    public void setIdSonidoDisparo(Integer idSonidoDisparo) {
        this.idSonidoDisparo = idSonidoDisparo;
    }

    public Integer getIdSonidoDisparoCritico() {
        return idSonidoDisparoCritico;
    }

    public void setIdSonidoDisparoCritico(Integer idSonidoDisparoCritico) {
        this.idSonidoDisparoCritico = idSonidoDisparoCritico;
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

    public void aumentarDanioAtaque(double cantidad) {
        validarCantidadNoNegativa(cantidad);
        double nuevoDanio = danioAtaque + cantidad;
        if (!Double.isFinite(nuevoDanio)) {
            throw new IllegalArgumentException("El daño de ataque resultante debe ser finito.");
        }
        danioAtaque = nuevoDanio;
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
    }

    public void aumentarVelocidadMaxima(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de velocidad debe ser finito y no negativo.");
        }
        double nuevaVelocidadMaxima = getVelocidadMax() * (1.0 + porcentaje / 100.0);
        if (!Double.isFinite(nuevaVelocidadMaxima)) {
            throw new IllegalArgumentException("La velocidad máxima resultante debe ser finita.");
        }
        setVelocidadMax(nuevaVelocidadMaxima);
    }

    public void aumentarProbabilidadCritico(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de crítico debe ser finito y no negativo.");
        }
        probabilidadCritico = Math.min(100.0, probabilidadCritico + porcentaje);
    }

    public void aumentarProbabilidadMultidisparo(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de multidisparo debe ser finito y no negativo.");
        }
        probabilidadMultidisparo = Math.min(100.0, probabilidadMultidisparo + porcentaje);
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

    @Override
    protected void alModificarVida() {
        if (notificadorVida != null) {
            notificadorVida.notificar(this);
        }
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperienciaSiguiente() {
        return experienciaSiguiente;
    }

    public int getIncrementoExperienciaPorNivel() {
        return incrementoExperienciaPorNivel;
    }

    public int getNivelesPendientes() {
        return nivelesPendientes;
    }

    public double getDanioAtaque() {
        return danioAtaque;
    }

    public double getVelocidadAtaque() {
        return velocidadAtaque;
    }

    public double getProbabilidadCritico() {
        return probabilidadCritico;
    }

    public double getProbabilidadMultidisparo() {
        return probabilidadMultidisparo;
    }

    public double getMultiplicadorCritico() {
        return multiplicadorCritico;
    }

    public double getOffsetDisparoX() {
        return offsetDisparoX;
    }

    public double getOffsetDisparoY() {
        return offsetDisparoY;
    }

    public double getDesviacionMultidisparoGrados() {
        return desviacionMultidisparoGrados;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public VecDouble2D getVectorDireccion() {
        return vectorDireccion.clone();
    }

    public Notificador<Jugador> getNotificadorVida() {
        return notificadorVida;
    }

    public Integer getIdSonidoSubirNivel() {
        return idSonidoSubirNivel;
    }
}
