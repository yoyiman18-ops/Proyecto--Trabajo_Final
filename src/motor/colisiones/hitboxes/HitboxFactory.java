package motor.colisiones.hitboxes;

public abstract class HitboxFactory {
        protected TipoHitbox tipo;
        protected MascaraColision categoriasColision;
        protected MascaraColision capasColision;
        protected boolean activa;

        public HitboxFactory(TipoHitbox tipo, MascaraColision categoriasColision, MascaraColision
        capasColision) {
            this(tipo, categoriasColision, capasColision, true);
        }

        public HitboxFactory(TipoHitbox tipo, MascaraColision categoriasColision, MascaraColision
        capasColision, boolean activa) {
            this.tipo = tipo;
            this.categoriasColision = categoriasColision;
            this.capasColision = capasColision;
            this.activa = activa;
        }

        public abstract Hitbox getHitbox();

        public HitboxFactory setTipo(TipoHitbox tipo) {
            this.tipo = tipo;
            return this;
        }

        public HitboxFactory setCategoriasColision(MascaraColision categorias) {
            this.categoriasColision = categorias;
            return this;
        }

        public HitboxFactory setCapasColision(MascaraColision capas) {
            this.capasColision = capas;
            return this;
        }

        public HitboxFactory setActiva(boolean activa) {
            this.activa = activa;
            return this;
        }

}
