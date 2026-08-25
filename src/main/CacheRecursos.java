package main;

import javafx.scene.image.Image;
import java.util.Hashtable;

public class CacheRecursos {

    private Hashtable<String,Image> cache = new Hashtable<>();

    public Image getImagen(String path) {
        return cache.computeIfAbsent(path, clave -> new Image(path));
    }



}