package modelo.proyectil;

import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.util.VecDouble2D;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import modelo.EstrategiaExpiracionSpawnear;

public class ProyectilRojo extends Proyectil {
    
    public ProyectilRojo(VecDouble2D posicion, VecDouble2D direccion) {
        super(
            4,
            "proyectil_rojo",
            new HitboxRectangular(
                new Rectangle2D.Double(), TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ALIADO),
                MascaraColision.of(CategoriaColision.ENEMIGO)
            ),
            posicion,
            new VecDouble2D(50, 0),
            10,
            10,
            5,
            new ArrayList<>(List.of(new EstrategiaExpiracionSpawnear(List.of(
                new Proyectil(5, "test", new HitboxRectangular(
                new Rectangle2D.Double(), TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ALIADO),
                MascaraColision.of(CategoriaColision.ENEMIGO)
            ), new VecDouble2D(), new VecDouble2D(0,-50), 3, 0.5, -1, null)
            ))))
        );
    }
}
