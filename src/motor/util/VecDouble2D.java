package motor.util;

import java.util.Objects;

public class VecDouble2D implements Cloneable {

    private double x,y;

    public VecDouble2D() { 
        this.x = 0; 
        this.y = 0; 
    }

    public VecDouble2D(double x, double y) { 
        this.x = x;
        this.y = y;
    }

    public VecDouble2D(VecDouble2D original) {
        this.x = original.getX();
        this.y = original.getY();
    }

    public double getX() { return x; }
    public double getY() { return y; } 
    public VecDouble2D setX(double x) { this.x = x; return this; }
    public VecDouble2D setY(double y) { this.y = y; return this; }

    public VecDouble2D normalizar() {
        if (x != 0 || y != 0) {
            double magnitud = Math.sqrt(x * x + y * y);
            if (magnitud != 1) {
                x = x/magnitud;
                y = y/magnitud;
            }
        }
        return this;
    }

    public VecDouble2D sumar(VecDouble2D otro) {
        this.x += otro.getX();
        this.y += otro.getY();
        return this;
    }

    public VecDouble2D sumar(double x, Eje eje) {
        switch (eje) {
            case Eje.X -> this.x += x;
            case Eje.Y -> this.y += x;
        }
        return this;
    }

    public VecDouble2D restar(VecDouble2D otro) {
        this.x -= otro.getX();
        this.y -= otro.getY();
        return this;
    }

    public VecDouble2D restar(double x, Eje eje) {
        switch (eje) {
            case Eje.X -> this.x += x;
            case Eje.Y -> this.y += x;
        }
        return this;
    }

    public VecDouble2D producto(double x) {
        this.x *= x;
        this.y *= x;
        return this;
    }

    public VecDouble2D normalizado() {
        if (x != 0 || y != 0) {
            double magnitud = Math.sqrt(x * x + y * y);
            if (magnitud != 1) { 
                x = x/magnitud;
                y = y/magnitud;
            }
        }
        return this;
    }

    public static VecDouble2D izquierda() { return new VecDouble2D(-1,0); }
    public static VecDouble2D derecha() { return new VecDouble2D(1,0); }
    public static VecDouble2D abajo() { return new VecDouble2D(0,1); }
    public static VecDouble2D arriba() { return new VecDouble2D(0, -1); }

    @Override
    public VecDouble2D clone() { return new VecDouble2D(this.x,this.y); }

    @Override
    public boolean equals(Object obj) {
        // si apuntan al mismo objeto devuelve true
        if (this == obj) { return true; }
        // si son distintos objetos, y el otro es nulo o son de clases distintas devuelve falso
        if ( obj == null || this.getClass() != obj.getClass() ) { return false; }
        // son de la misma clase, retorna (x1,y1) == (x2,y2)
        VecDouble2D otroVec = (VecDouble2D) obj;
        return (this.x == otroVec.x && this.y == otroVec.y);
    }

    @Override
    public int hashCode() { return Objects.hash(x,y); }

    @Override
    public String toString() { return String.format("(%.2f;%.2f)", x,y); }


}
