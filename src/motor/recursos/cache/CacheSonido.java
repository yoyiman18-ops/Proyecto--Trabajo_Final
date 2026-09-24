package motor.recursos.cache;
import javafx.scene.media.AudioClip;
import motor.recursos.Extension;

public class CacheSonido extends CacheRecursos<AudioClip> {

    public CacheSonido(String carpeta) { super(CacheSonido.class.getName(), carpeta); }

    @Override
    public AudioClip getRecurso(String nombre, Extension.IExtension ext) {
        if (!(ext instanceof Extension.Sonido)) { throw new IllegalArgumentException("Extension invalida."); }
        return cache.computeIfAbsent(
            resolverPathRecurso(nombre, ext),
            k -> new AudioClip(k)
        );
    }
}
