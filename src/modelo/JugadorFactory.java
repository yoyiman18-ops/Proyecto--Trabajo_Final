package modelo;

import java.util.ArrayList;

import controlador.SonidoJuego;
import modelo.proyectil.ProyectilRojoFactory;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.EstrategiaMovimientoAcciones;
import motor.util.VecDouble2D;

public class JugadorFactory {

    public Jugador get(double anchoHitbox, double altoHitbox, VecDouble2D posicion) {
        return new Jugador(
            1,
            "jugador",
            new HitboxRectangularFactory().get(
                0, 0, anchoHitbox, altoHitbox,
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(
                    CategoriaColision.ENEMIGO,
                    CategoriaColision.MURO,
                    CategoriaColision.ITEM,
                    CategoriaColision.EXP
                ),
                true
            ),
            posicion != null ? posicion : new VecDouble2D(),
            new VecDouble2D(0.0, 0.0),
            800.0,
            600.0,
            200.0,
            new EstrategiaMovimientoAcciones(),
            new ArrayList<>(),
            new ArrayList<>(),
            "caminar_abajo",
            null,
            SonidoJuego.MUERTE_JUGADOR.getId(),
            100.0,
            75.0,
            SonidoJuego.DAÑO_JUGADOR.getId(),
            1,
            30,
            25,
            10.0,
            1.0,
            new ProyectilRojoFactory(),
            SonidoJuego.DISPARO.getId(),
            SonidoJuego.SUBIR_NIVEL.getId()
        );
    }
}
