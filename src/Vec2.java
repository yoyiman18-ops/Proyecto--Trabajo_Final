public class Vec2 {

    private double x,y;

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }


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

    // esto es para la direccion de las entidades, que se puede representar
    // como un vector normalizado de 2 componentes
    public void normalizar() {

        // en caso de ser (0,0) no hace nada
        if (x != 0 || y != 0) {
        // magnitud = raiz cuadrada(x**2 + y**2)
        double magnitud = Math.sqrt(this.x * this.x + this.y * this.y);
        // vector normalizado: todas las componentes divididas por la magnitud del vec
        x = x/magnitud;
        y = y/magnitud;
        // System.out.printf("vec normalizado: %f -- %f%n",this.x,this.y);
        }
    }

}