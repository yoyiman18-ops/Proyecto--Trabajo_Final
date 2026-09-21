package motor.cache;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;

// arquitectura singleton
public abstract class CacheRecursos<K,V> {

    protected final Map<K,V> cache;
    private final String carpeta;
    protected final Logger logger;

    protected CacheRecursos(String nombre, String carpeta) {
        this.logger = Logger.getLogger(nombre);
        this.cache = new HashMap<>();
        this.carpeta = carpeta;
        logger.log(Level.FINE, "Creado cache: " + nombre);
        
    }
    
    protected String resolverPathRecurso(String nombre, Extension.IExtension ext) {
        return "/assets/" + carpeta + "/" + nombre + ext.getExtension();
    }

    public abstract CacheRecursos<K,V> getInstancia();
    public abstract V getRecurso(K nombre, Extension.IExtension ext);
    public void limpiar() { cache.clear(); }

}