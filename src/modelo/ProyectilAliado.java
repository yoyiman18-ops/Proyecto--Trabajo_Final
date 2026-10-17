package modelo;

import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.Hitbox;
import motor.util.VecDouble2D;

public final class ProyectilAliado extends Proyectil {

    public ProyectilAliado(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double danio,
        double tiempoVida,
        int penetracion
    ) {
        super(nombre, hitbox, posicion, direccion, velocidad, danio, tiempoVida, penetracion);
    }

    @Override
    protected boolean puedeImpactar(CategoriaColision categoria) {
        return categoria == CategoriaColision.ENEMIGO;
    }
}
