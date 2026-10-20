package motor.modelo.graficos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import motor.recursos.Extension;

public class Sheet {
    private final int idSheet;
    private final String path;
    private final Extension.Imagen extension;
    private final Image imagen;
    private final Map<String,Animacion> animaciones;
    private final Map<String,Recorte> recortes;

    public Sheet(
        int idSheet,
        String path,
        Image imagen,
        Extension.Imagen extension,
        Map<String,Animacion> animaciones,
        Map<String,Recorte> recortes
    ) {
        if (path == null || path.isEmpty() || path.isBlank()) {
            throw new IllegalArgumentException("Path de sheet vacia o nula, no es valido");
        }
        if (recortes == null) {
            throw new IllegalArgumentException("Recortes nulos no validos");
        }
        this.idSheet = idSheet;
        this.path = path;
        this.imagen = imagen;
        this.extension = extension;
        this.animaciones = animaciones;
        this.recortes = recortes;
    }

    // 
    public int getIdSheet() { return this.idSheet; }
    public String getPath() { return this.path; }
    public Extension.Imagen getExtension() { return this.extension; }
    public Image getImagen() { return this.imagen; }
    public List<Animacion> getAnimaciones() { return new ArrayList<>(this.animaciones.values()); } // nota: no deberia usarse para obtener directamente una animacion en especifico nunca
    public List<Recorte> getRecortes() { return new ArrayList<>(this.recortes.values()); }

    public Animacion getAnimacion(String nombre) { return animaciones.get(nombre); }
    public Recorte getRecorte(String nombre) { return recortes.get(nombre); }


}
