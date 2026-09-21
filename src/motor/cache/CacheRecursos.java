package motor.cache;

import java.util.HashMap;
import java.util.Map;

public abstract class CacheRecursos<K,V> {

    protected final Map<K,V> cache = new HashMap<>();
    
    protected String resolverPathRecurso(String nombre, Extension.IExtension ext) {
        return nombre + ext.getExtension();
    }

    public abstract V getRecurso(K clave);
    public void limpiar() { cache.clear(); }

}