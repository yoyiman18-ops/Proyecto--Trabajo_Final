package motor.recursos;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import motor.recursos.cache.CacheImagenes;
import motor.recursos.cache.CacheMusica;
import motor.recursos.cache.CacheRecursos;
import motor.recursos.cache.CacheSonido;

// patron singleton
public class GestorRecursos {
    private static GestorRecursos instancia;
    private final CacheRecursos<String,Image> imagenes;
    private final CacheRecursos<String,AudioClip> sonido;
    private final CacheRecursos<String,Media> musica;
    private final Logger logger;

    private static final String CARPETA_IMAGENES = "imagenes";
    private static final String CARPETA_SONIDO = "sonido";
    private static final String CARPETA_MUSICA = "musica";

    private GestorRecursos() {
        this.logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO,"Creado: "+ getClass().getName());
        this.imagenes = new CacheImagenes(CARPETA_IMAGENES);
        this.sonido = new CacheSonido(CARPETA_SONIDO);
        this.musica = new CacheMusica(CARPETA_MUSICA);

    }

    public static GestorRecursos getInstancia() {
        if (GestorRecursos.instancia == null) { GestorRecursos.instancia = new GestorRecursos(); }
        return GestorRecursos.instancia;
    }

    public Image getImagen(String nombre, Extension.Imagen ext) { return imagenes.getRecurso(nombre, ext); }
    public Media getMusica(String nombre, Extension.Sonido ext) { return musica.getRecurso(nombre, ext); }
    public AudioClip getSonido(String nombre, Extension.Sonido ext) { return sonido.getRecurso(nombre, ext); } 
    
}
