package motor.cache;
import javafx.scene.image.Image;

public class CacheImagenes extends CacheRecursos<String,Image> {

    @Override protected String resolverPathRecurso(String nombre) {
        return "/recursos/" + nombre + ".jpg";
    }

    /**
     * Busca la imagen correspondiente al path en el HashMap.
     * 
     * <p>Si la imagen ya está cargada, la retorna directamente. 
     * Sino, la carga a memoria, la guarda en el HashMap cache y la retorna.
     * 
     * @param clave Ubicación del archivo.
     * @return Imagen correspondiente a path, cargada en el HashMap.
    */
    @Override public Image getRecurso(String clave) {
        return cache.computeIfAbsent(clave, k -> {
            var recurso = getClass().getResource(resolverPathRecurso(clave));
            if (recurso == null) { throw new IllegalArgumentException("Recurso no encontrado: " + resolverPathRecurso(clave)); }
            String path = recurso.toExternalForm();
            System.out.println("Imagen no encontrada, cargándola.");
            return new Image(path);
        }); 
    }
}
