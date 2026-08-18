/**
 * Almacena dos componentes (x,y) de tipo primitive double.
 */
public class Vec2 implements Cloneable {

    public double x,y;

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /* 
    // no tiene mucho sentido mantener getters/setters cuando no hay
    // condiciones y son públicos, solo complica el acceso.
    // si se quiere poner el vector privado que se haga en la clase que
    // lo instancie
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    } 
    public void setX(double x) {
        this.x = x; 
    }
    public void setY(double y) {
        this.y = y;
    }
    */

    /*
     * Convierte el vector en un vector unitario.
     * Mantiene la dirección y sentido originales, pero la magnitud pasa a ser 1.
     * No hace nada si el vector ya era unitario, o si equivale a (0,0).
     */
    public void normalizar() {
    // esto es para la direccion de las entidades, que se puede representar
    // como un vector normalizado de 2 componentes

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
