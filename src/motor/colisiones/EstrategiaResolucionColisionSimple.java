package motor.colisiones;

import java.awt.geom.Rectangle2D;
import motor.colisiones.hitboxes.Hitbox;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.util.Eje;
import motor.util.VecDouble2D;

/**
 * Lógica AABB implementada con IA
 * 
 * Implementación de resolución física de colisiones basada en AABB y
 * vector de mínima penetración (MTV).
 * Distribuye el desplazamiento simétricamente (50% / 50%) si ambos colisionables
 * son sólidos, o el 100% al colisionable sólido si el otro es inamovible.
 */
public class EstrategiaResolucionColisionSimple implements EstrategiaResolucionColision {

    @Override
    public boolean resolverSeparacionFisica(Colisionable c1, Colisionable c2) {
        if (c1 == null || c2 == null) { return false; }

        Hitbox h1 = c1.getHitbox();
        Hitbox h2 = c2.getHitbox();
        if (h1 == null || h2 == null) { return false; }

        if (c1.getPosicion() == null || c2.getPosicion() == null) { return false; }

        TipoHitbox tipo1 = h1.getTipo();
        TipoHitbox tipo2 = h2.getTipo();

        if (tipo1 == TipoHitbox.FANTASMA || tipo2 == TipoHitbox.FANTASMA) { return false; }
        if (tipo1 == TipoHitbox.INAMOVIBLE && tipo2 == TipoHitbox.INAMOVIBLE) { return false; }

        if (!h1.intersecta(h2)) { return false; }

        Rectangle2D b1 = h1.getBounds();
        Rectangle2D b2 = h2.getBounds();
        if (b1 == null || b2 == null) { return false; }

        double overlapLeft = b1.getMaxX() - b2.getMinX();
        double overlapRight = b2.getMaxX() - b1.getMinX();
        double overlapUp = b1.getMaxY() - b2.getMinY();
        double overlapDown = b2.getMaxY() - b1.getMinY();

        if (overlapLeft <= 0 || overlapRight <= 0 || overlapUp <= 0 || overlapDown <= 0) { return false; }

        double pushX = (overlapLeft < overlapRight) ? -overlapLeft : overlapRight;
        double pushY = (overlapUp < overlapDown) ? -overlapUp : overlapDown;

        double absPushX = Math.abs(pushX);
        double absPushY = Math.abs(pushY);

        Eje eje;
        double push;
        if (absPushX < absPushY) {
            eje = Eje.X;
            push = pushX;
        } else {
            eje = Eje.Y;
            push = pushY;
        }

        double peso1;
        double peso2;
        if (tipo1 == TipoHitbox.SOLIDA && tipo2 == TipoHitbox.SOLIDA) {
            peso1 = 0.5;
            peso2 = 0.5;
        } else if (tipo1 == TipoHitbox.SOLIDA && tipo2 == TipoHitbox.INAMOVIBLE) {
            peso1 = 1.0;
            peso2 = 0.0;
        } else if (tipo1 == TipoHitbox.INAMOVIBLE && tipo2 == TipoHitbox.SOLIDA) {
            peso1 = 0.0;
            peso2 = 1.0;
        } else {
            return false;
        }

        if (peso1 > 0) {
            VecDouble2D pos1 = c1.getPosicion().clone().sumar(push * peso1, eje);
            c1.setPosicion(pos1);
        }
        if (peso2 > 0) {
            VecDouble2D pos2 = c2.getPosicion().clone().sumar(-push * peso2, eje);
            c2.setPosicion(pos2);
        }

        if (h1.intersecta(h2)) {
            double epsilon = Math.copySign(1e-4, push);
            if (peso1 > 0) {
                VecDouble2D pos1 = c1.getPosicion().clone().sumar(epsilon * peso1, eje);
                c1.setPosicion(pos1);
            }
            if (peso2 > 0) {
                VecDouble2D pos2 = c2.getPosicion().clone().sumar(-epsilon * peso2, eje);
                c2.setPosicion(pos2);
            }
        }

        return true;
    }
}
