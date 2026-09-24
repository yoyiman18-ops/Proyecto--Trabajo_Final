package motor.util.Observer;

@FunctionalInterface 
public interface Observador<T> {
    public void cambio(T observado);
}
