package motor.recursos.cache;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import motor.recursos.Extension;

public class CacheMusica extends CacheRecursos<String,Media> {

    public CacheMusica(String carpeta) { super(CacheMusica.class.getName(), carpeta); }

    @Override
    public Media getRecurso(String nombre, Extension.IExtension ext) {
        if (!(ext instanceof Extension.Sonido)) { throw new IllegalArgumentException("Extension invalida."); }
        return cache.computeIfAbsent(
            resolverPathRecurso(nombre, ext),
            k -> { try {
                Media musica = new Media(k); 
                musica.getError(); return musica;
            } catch (MediaException e) { throw new RuntimeException(e.getMessage()); }}
        );
    }
}
