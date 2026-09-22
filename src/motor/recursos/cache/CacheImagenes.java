package motor.recursos.cache;
import java.util.logging.Level;

import javafx.scene.image.Image;
import motor.recursos.Extension;

public class CacheImagenes extends CacheRecursos<String,Image> {

    public CacheImagenes(String carpeta) { super(CacheImagenes.class.getName(), carpeta); }

    @Override
    public Image getRecurso(String nombre, Extension.IExtension ext) {
        if (!(ext instanceof Extension.Imagen)) { throw new IllegalArgumentException("Extension invalida."); }
        return cache.computeIfAbsent(
            resolverPathRecurso(nombre, ext),
            k -> { try {
                Image imagen = new Image(k);
                imagen.getException();
                this.logger.log(Level.INFO,"Cargado: " + k);
                return imagen; 
            } catch (Exception e) { throw new RuntimeException(e.getMessage()); }}
        );
    }
}
