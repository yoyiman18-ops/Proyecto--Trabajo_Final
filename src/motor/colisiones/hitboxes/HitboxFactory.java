package motor.colisiones.hitboxes;

public abstract class HitboxFactory {

    public abstract Hitbox get(
        double offsetX,
        double offsetY,
        double ancho,
        double alto,
        TipoHitbox tipo,
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        boolean activa
    );
}
