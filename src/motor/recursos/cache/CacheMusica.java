package motor.recursos.cache;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import motor.recursos.Extension;

public class CacheMusica extends CacheRecursos<Media> {

    public CacheMusica(String carpeta) { super(carpeta); }

    @Override
    public Media getRecurso(String nombre, Extension.IExtension ext) {
        if (!(ext instanceof Extension.Sonido)) { throw new IllegalArgumentException("Extension invalida."); }
        return cache.computeIfAbsent(
            resolverPathRecurso(nombre, ext),
            k -> { try {
                URL url = getClass().getResource(k);
                if (url == null) {
                    throw new RuntimeException("No se encontro el recurso de musica: " + k);
                }
                Media musica = new Media(url.toExternalForm()); 
                musica.getError(); 
                loggearCarga(k);
                return musica;
            } catch (MediaException e) { throw new RuntimeException(e.getMessage()); }}
        );
    }
}
