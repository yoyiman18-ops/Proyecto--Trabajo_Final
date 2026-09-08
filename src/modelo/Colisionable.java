package modelo;

import javafx.geometry.Rectangle2D;

public interface Colisionable {
    public Rectangle2D getPoligonoColision();
    public boolean colisionesActivas();
    public boolean intersecta(Colisionable otro);
}
