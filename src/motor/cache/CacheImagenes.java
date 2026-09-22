package motor.cache;
import javafx.scene.image.Image;

// arquitectura singleton
public class CacheImagenes extends CacheRecursos<String,Image> {

    private static final String CARPETA = "imagenes"; 
    private static CacheImagenes instancia;

    private CacheImagenes() { super(CacheImagenes.class.getName(), CARPETA); }
    
    public static CacheImagenes getInstancia() {
        if (CacheImagenes.instancia == null) { CacheImagenes.instancia = new CacheImagenes(); }
        return instancia;
    }

    @Override
    public Image getRecurso(String nombre, Extension.IExtension ext) {
        if (!(ext instanceof Extension.Imagen)) { throw new IllegalArgumentException("Extension invalida."); }
        return cache.computeIfAbsent(
            resolverPathRecurso(nombre, ext),
            k -> new Image(k)
        );
    }
}
