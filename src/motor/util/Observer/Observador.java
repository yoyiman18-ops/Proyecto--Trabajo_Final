package motor.util;

@FunctionalInterface 
public interface Observador<T> {
    public void cambio(T observado);
}
