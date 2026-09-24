package motor.util;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Notificador<T> {
    private final List<WeakReference<Observador<T>>> observadores;
    private final ReferenceQueue<Observador<T>> colaPunteros;

    public Notificador() {
        this.observadores = new CopyOnWriteArrayList<>();
        this.colaPunteros = new ReferenceQueue<>();
    }

    public void notificar(T estado) {
        limpiarPunterosMuertos();
        Iterator<WeakReference<Observador<T>>> iterador = observadores.iterator();
        while (iterador.hasNext()) { iterador.next().get().cambio(estado); }
    }
    
    public boolean suscribir(Observador<T> o) { 
        limpiarPunterosMuertos();
        // busca el observador en la lista de punteros. si ya está retorna false
        if (getReferencia(observadores, o) != null) { return false; }
        else { observadores.add(new WeakReference<Observador<T>>(o,colaPunteros)); return true; }
    }
    public boolean desuscribir(Observador<T> o) { 
        WeakReference<Observador<T>> ptr;
        if ((ptr = getReferencia(observadores, o)) == null) { return false; }
        else { observadores.remove(ptr); limpiarPunterosMuertos(); return true; }
    }

    private void limpiarPunterosMuertos() {
        WeakReference<Observador<T>> ptr;
        while ((ptr = (WeakReference<Observador<T>>) colaPunteros.poll()) != null) {
            System.out.println("Se ha limpiado un ptr");
            observadores.remove(ptr);
        }
    }

    private WeakReference<Observador<T>> getReferencia(List<WeakReference<Observador<T>>> ptrs, Observador<T> o) {
        for (WeakReference<Observador<T>> ptr : ptrs) {
            if (ptr.get() == o) { return ptr; }
        }
        return null;
    }
}
