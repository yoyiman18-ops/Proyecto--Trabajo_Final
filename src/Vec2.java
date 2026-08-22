public class Vec2 implements Cloneable {

    private double x,y;

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; } 
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public void normalizar() {
        // en caso de ser (0,0) no hace nada
        if (x != 0 || y != 0) {
            // magnitud = raiz cuadrada(x**2 + y**2)
            double magnitud = Math.sqrt(x * x + y * y);
            // vector normalizado: todas las componentes divididas por la magnitud del vec
            if (magnitud != 1) {
                x = x/magnitud;
                y = y/magnitud;
            }
        }
    }

    public Vec2 normalizado() {
        if (x != 0 || y != 0) {
            double magnitud = Math.sqrt(x * x + y * y);
            if (magnitud != 1) { 
                x = x/magnitud;
                y = y/magnitud;
            }
        }
        return this;
    }

    @Override
    public Vec2 clone() {
        return new Vec2(x,y);
    }

    @Override
    public String toString() {
        return String.format("(%.2f;%.2f)", x,y);
    }
}
