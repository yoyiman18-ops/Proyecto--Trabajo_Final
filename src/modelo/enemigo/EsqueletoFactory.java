package modelo.enemigo;

import java.util.ArrayList;
import java.util.List;

import modelo.EstrategiaExpiracionSpawnear;
import modelo.experiencia.GemaExperienciaFactory;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimientoPersecucion;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;

public class EsqueletoFactory extends EnemigoFactory {

    @Override
    public Enemigo get(Posicionable objetivo, double velocidadOleada) {
        List<EstrategiaExpiracion> estrategias = new ArrayList<>();
        estrategias.add(new EstrategiaExpiracionSpawnear(
            List.of(new GemaExperienciaFactory().get(new VecDouble2D(), 10))
        ));

        return new Esqueleto(
            new VecDouble2D(0.0, 0.0),
            new HitboxRectangularFactory().get(
                0, 0, 28.0, 36.0,
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ENEMIGO),
                MascaraColision.of(
                    CategoriaColision.JUGADOR,
                    CategoriaColision.ALIADO,
                    CategoriaColision.ENEMIGO,
                    CategoriaColision.MURO
                ),
                true
            ),
            600.0,
            600.0,
            velocidadOleada,
            40.0,
            10.0,
            1.0,
            new EstrategiaMovimientoPersecucion(objetivo),
            estrategias,
            new ArrayList<>()
        );
    }
}
