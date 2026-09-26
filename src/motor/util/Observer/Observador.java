package motor.util.observer;

@FunctionalInterface 
public interface Observador<T> {
    public void cambio(T observado);
}
