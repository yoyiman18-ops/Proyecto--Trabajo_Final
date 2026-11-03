package modelo;

import java.util.ArrayList;
import java.util.List;

import controlador.SonidoJuego;
import modelo.proyectil.ProyectilFactory;
import modelo.proyectil.ProyectilRojoFactory;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxFactory;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.EstrategiaMovimientoAcciones;
import motor.modelo.EstrategiaMovimientoLineal;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class JugadorFactory extends EntidadFactory {

    public static final int ID_GLOBAL_JUGADOR = 1;
    public static final String NOMBRE_JUGADOR_DEFAULT = "jugador";
    public static final String ANIMACION_DEFAULT = "caminar_abajo";
    public static final String ANIMACION_EXPIRAR = "expirar";

    public static final double ACELERACION_DEFAULT = 800.0;
    public static final double FRICCION_DEFAULT = 600.0;
    public static final double VELOCIDAD_MAXIMA_DEFAULT = 200.0;
    public static final double VIDA_MAXIMA_DEFAULT = 100.0;
    public static final double DEFENSA_MAXIMA_DEFAULT = 75.0;

    public static final int NIVEL_INICIAL_DEFAULT = 1;
    public static final int EXP_INICIAL_DEFAULT = 30;
    public static final int EXP_INCREMENTO_POR_NIVEL_DEFAULT = 25;

    public static final double DANIO_ATAQUE_DEFAULT = 10.0;
    public static final double VELOCIDAD_ATAQUE_DEFAULT = 1.0;
    public static final double CRITICO_DEFAULT = 5.0;
    public static final double MULTIDISPARO_DEFAULT = 0.0;
    public static final double MULTIPLICADOR_CRITICO_DEFAULT = 2.0;

    public static final double OFFSET_ORIGEN_DISPARO_X_DEFAULT = -15.0;
    public static final double OFFSET_ORIGEN_DISPARO_Y_DEFAULT = -16.0;
    public static final double DESVIACION_MULTIDISPARO_GRADOS_DEFAULT = 15.0;

    public static final double VELOCIDAD_PROYECTIL_DEFAULT = 320.0;
    public static final double TIEMPO_VIDA_ADICIONAL_PROYECTIL_DEFAULT = 2.0;
    public static final int PENETRACION_ADICIONAL_PROYECTIL_DEFAULT = 0;

    private int idGlobal = ID_GLOBAL_JUGADOR;
    private String nombre = NOMBRE_JUGADOR_DEFAULT;
    private String animacionDefault = ANIMACION_DEFAULT;
    private String animacionExpirar = ANIMACION_EXPIRAR;
    private double vidaMaxima = VIDA_MAXIMA_DEFAULT;
    private double defensaMaximaPorcentual = DEFENSA_MAXIMA_DEFAULT;
    private int nivelInicial = NIVEL_INICIAL_DEFAULT;
    private int experienciaSiguienteInicial = EXP_INICIAL_DEFAULT;
    private int incrementoExperienciaPorNivel = EXP_INCREMENTO_POR_NIVEL_DEFAULT;
    private double danioAtaque = DANIO_ATAQUE_DEFAULT;
    private double velocidadAtaque = VELOCIDAD_ATAQUE_DEFAULT;
    private double probabilidadCritico = CRITICO_DEFAULT;
    private double probabilidadMultidisparo = MULTIDISPARO_DEFAULT;
    private double multiplicadorCritico = MULTIPLICADOR_CRITICO_DEFAULT;
    private double offsetDisparoX = OFFSET_ORIGEN_DISPARO_X_DEFAULT;
    private double offsetDisparoY = OFFSET_ORIGEN_DISPARO_Y_DEFAULT;
    private double desviacionMultidisparoGrados = DESVIACION_MULTIDISPARO_GRADOS_DEFAULT;
    private EstrategiaMovimiento estrategiaMovimiento;
    private ProyectilFactory proyectilFactory;
    private Integer idSonidoDaño = SonidoJuego.DAÑO_JUGADOR.getId();
    private Integer idSonidoExpiracion = SonidoJuego.MUERTE_JUGADOR.getId();
    private Integer idSonidoSubirNivel = SonidoJuego.SUBIR_NIVEL.getId();
    private Integer idSonidoDisparo = SonidoJuego.DISPARO.getId();
    private Integer idSonidoDisparoCritico = SonidoJuego.DISPARO_CRITICO.getId();

    public JugadorFactory(double anchoHitbox, double altoHitbox, VecDouble2D posicion) {
        this(
            new HitboxRectangularFactory(
                anchoHitbox,
                altoHitbox,
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(
                    CategoriaColision.ENEMIGO,
                    CategoriaColision.MURO,
                    CategoriaColision.ITEM,
                    CategoriaColision.EXP
                )
            ),
            posicion,
            new VecDouble2D(0.0, 0.0),
            ACELERACION_DEFAULT,
            FRICCION_DEFAULT,
            VELOCIDAD_MAXIMA_DEFAULT,
            new EstrategiaMovimientoAcciones(),
            crearProyectilFactoryDefault(),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public JugadorFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        ProyectilFactory proyectilFactory,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            hitboxFactory,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion,
            interaccionesColision
        );
        this.estrategiaMovimiento = estrategiaMovimiento;
        this.proyectilFactory = proyectilFactory;
    }

    private static ProyectilFactory crearProyectilFactoryDefault() {
        return new ProyectilRojoFactory(
            MascaraColision.of(CategoriaColision.ALIADO),
            MascaraColision.of(CategoriaColision.ENEMIGO, CategoriaColision.MURO),
            new VecDouble2D(),
            1000.0,
            0.0,
            VELOCIDAD_PROYECTIL_DEFAULT,
            new EstrategiaMovimientoLineal(VecDouble2D.abajo()),
            TIEMPO_VIDA_ADICIONAL_PROYECTIL_DEFAULT,
            PENETRACION_ADICIONAL_PROYECTIL_DEFAULT,
            0.0
        );
    }

    @Override
    public Jugador get() {
        return new Jugador(
            idGlobal,
            nombre,
            getHitboxFactory().getHitbox(),
            getPosicion(),
            getVelocidad(),
            getAceleracion(),
            getFriccion(),
            getVelocidadMax(),
            estrategiaMovimiento != null ? estrategiaMovimiento : new EstrategiaMovimientoAcciones(),
            getEstrategiasExpiracion(),
            getInteraccionesColision(),
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
            proyectilFactory != null ? proyectilFactory : crearProyectilFactoryDefault(),
            idSonidoDisparo,
            idSonidoDisparoCritico,
            idSonidoSubirNivel
        );
    }
}
