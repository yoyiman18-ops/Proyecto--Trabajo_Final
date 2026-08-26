package modelo;

import javafx.geometry.Rectangle2D;

public interface Colisionable {
    public Rectangle2D getHitbox();
    public boolean intersecta(Colisionable otro);
}