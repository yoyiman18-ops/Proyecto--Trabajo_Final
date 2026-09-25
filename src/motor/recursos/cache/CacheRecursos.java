package motor.recursos.cache;
import java.util.logging.Level;
import java.util.logging.Logger;

import motor.recursos.Extension;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CacheRecursos<V> {
    protected final Map<String,V> cache;
    private final String carpeta;
    protected static final Logger logger = Logger.getLogger(CacheRecursos.class.getName());

    protected CacheRecursos(String carpeta) {
        this.cache = new ConcurrentHashMap<>();
        this.carpeta = carpeta;
    }
    


    public abstract V getRecurso(String nombre, Extension.IExtension ext);

    public void loggearCarga(String nombre) {
        logger.log(Level.INFO,"Se ha cargado en memoria: " + nombre);
    }
    public void limpiar() { cache.clear(); }

    protected String resolverPathRecurso(String nombre, Extension.IExtension ext) {
        return "/recursos/" + carpeta + "/" + nombre + ext.getExtension();
    }

}