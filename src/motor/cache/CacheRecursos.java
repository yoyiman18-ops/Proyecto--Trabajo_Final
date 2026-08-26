package motor.cache;

import java.util.concurrent.ConcurrentHashMap;

public abstract class CacheRecursos<K,V> {

    protected final ConcurrentHashMap<K,V> cache = new ConcurrentHashMap<>();
    public abstract V getRecurso(K clave);

    /**
     * Limpia el HashMap cache.
     */
    public void limpiar() { cache.clear(); }

}