package motor.recursos;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import motor.recursos.cache.CacheImagenes;
import motor.recursos.cache.CacheRecursos;

// patron singleton
public class GestorRecursos {
    private static GestorRecursos instancia;
    private final CacheRecursos<String,Image> imagenes;
    private final CacheRecursos<String,AudioClip> audio;
    private final CacheRecursos<String,Media> musica;

    private static final String CARPETA_IMAGENES = "imagenes";

    private GestorRecursos() {
        this.imagenes = new CacheImagenes(CARPETA_IMAGENES);
        this.audio = null;
    }

    public static GestorRecursos getInstancia() {
        if (GestorRecursos.instancia == null) { GestorRecursos.instancia = new GestorRecursos(); }
        return GestorRecursos.instancia;
    }

    public Image getImagen(String nombre, Extension.Imagen ext) { return imagenes.getRecurso(nombre, ext); } 
    public Audio
    
}
