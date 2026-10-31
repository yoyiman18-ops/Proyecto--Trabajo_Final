package motor.recursos.cache;
import java.net.URL;
import javafx.scene.media.AudioClip;
import motor.recursos.Extension;

public class CacheSonido extends CacheRecursos<AudioClip> {

    public CacheSonido(String carpeta) { super(carpeta); }

    @Override
    public AudioClip getRecurso(String nombre, Extension.IExtension ext) {
        if (!(ext instanceof Extension.Sonido)) { throw new IllegalArgumentException("Extension invalida."); }
        return cache.computeIfAbsent(
            resolverPathRecurso(nombre, ext),
            k -> { 
                URL url = getClass().getResource(k);
                if (url == null) {
                    throw new RuntimeException("No se encontro el recurso de sonido: " + k);
                }
                AudioClip audio = new AudioClip(url.toExternalForm());
                loggearCarga(k);
                return audio;
            }
        );
    }
}
