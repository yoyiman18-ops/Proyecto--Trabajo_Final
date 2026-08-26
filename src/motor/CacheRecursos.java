package motor;

import javafx.scene.image.Image;
import java.util.concurrent.ConcurrentHashMap;

public class CacheRecursos {

    private final ConcurrentHashMap<String,Image> cache = new ConcurrentHashMap<>();

    /**
     * Busca la imagen correspondiente al path en el HashMap.
     * 
     * <p>Si la imagen ya está cargada, la retorna directamente. 
     * Sino, la carga a memoria, la guarda en el HashMap y la retorna.
     * 
     * @param path Ubicación del archivo.
     * @return Imagen correspondiente a path, cargada en el HashMap.
     */
    public Image getImagen(String path) {
        return cache.computeIfAbsent(path, clave -> new Image(path));
    }

    /**
     * Limpia el HashMap cache.
     */
    public void limpiar() { cache.clear(); }

}