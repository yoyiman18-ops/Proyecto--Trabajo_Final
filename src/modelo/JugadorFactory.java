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

    private final double vidaMaxima;
    private final double defensaMaximaPorcentual;
    private final int nivelInicial;
    private final int experienciaSiguienteInicial;
    private final int incrementoExperienciaPorNivel;
    private final double danioAtaque;
    private final double velocidadAtaque;
    private final ProyectilFactory proyectilFactory;

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
            800.0,
            600.0,
            200.0,
            100.0,
            75.0,
            1,
            30,
            25,
            10.0,
            1.0,
            new EstrategiaMovimientoAcciones(),
            new ProyectilRojoFactory(
                MascaraColision.of(CategoriaColision.ALIADO),
                MascaraColision.of(CategoriaColision.ENEMIGO, CategoriaColision.MURO),
                new VecDouble2D(),
                1000.0,
                0.0,
                320.0,
                new EstrategiaMovimientoLineal(VecDouble2D.abajo()),
                2.0,
                0,
                0.0
            ),
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
        double vidaMaxima,
        double defensaMaximaPorcentual,
        int nivelInicial,
        int experienciaSiguienteInicial,
        int incrementoExperienciaPorNivel,
        double danioAtaque,
        double velocidadAtaque,
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
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
        this.vidaMaxima = vidaMaxima;
        this.defensaMaximaPorcentual = defensaMaximaPorcentual;
        this.nivelInicial = nivelInicial;
        this.experienciaSiguienteInicial = experienciaSiguienteInicial;
        this.incrementoExperienciaPorNivel = incrementoExperienciaPorNivel;
        this.danioAtaque = danioAtaque;
        this.velocidadAtaque = velocidadAtaque;
        this.proyectilFactory = proyectilFactory;
    }

    @Override
    public Jugador get() {
        return new Jugador(
            1,
            "jugador",
            getHitboxFactory().getHitbox(),
            getPosicion(),
            getVelocidad(),
            getAceleracion(),
            getFriccion(),
            getVelocidadMax(),
            getEstrategiaMovimiento(),
            getEstrategiasExpiracion(),
            getInteraccionesColision(),
            "caminar_abajo",
            "expirar",
            SonidoJuego.MUERTE_JUGADOR.getId(),
            vidaMaxima,
            defensaMaximaPorcentual,
            SonidoJuego.DAÑO_JUGADOR.getId(),
            nivelInicial,
            experienciaSiguienteInicial,
            incrementoExperienciaPorNivel,
            danioAtaque,
            velocidadAtaque,
            proyectilFactory,
            SonidoJuego.DISPARO.getId(),
            SonidoJuego.SUBIR_NIVEL.getId()
        );
    }
}
