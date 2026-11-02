package motor.colisiones.hitboxes;

import java.awt.geom.Rectangle2D;

public class HitboxRectangularFactory extends HitboxFactory {

        private double ancho;
        private double alto;
        private double offsetX;
        private double offsetY;

        public HitboxRectangularFactory(
            double ancho,
            double alto,
            TipoHitbox tipo,
            MascaraColision categorias,
            MascaraColision capas
        ) {
            this(0, 0, ancho, alto, tipo, categorias,
            capas, true);
        }

        public HitboxRectangularFactory(
            double offsetX,
            double offsetY,
            double ancho,
            double alto,
            TipoHitbox tipo,
            MascaraColision categorias,
            MascaraColision capas,
            boolean activa
        ) {
            super(tipo, categorias, capas, activa);
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.ancho = ancho;
            this.alto = alto;
        }

        @Override
        public Hitbox getHitbox() {
            return new HitboxRectangular(
                new Rectangle2D.Double(offsetX,offsetY,ancho,alto), 
                this.tipo,
                this.categoriasColision,
                this.capasColision,
                this.activa
            );  
        }

        public HitboxRectangularFactory setAncho(double ancho) {
            this.ancho = ancho;
            return this;
        }

        public HitboxRectangularFactory setAlto(double alto) {
            this.alto = alto;
            return this;
        }

        public HitboxRectangularFactory setOffsetX(double offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public HitboxRectangularFactory setOffsetY(double offsetY) {
            this.offsetY = offsetY;
            return this;
        }



}
