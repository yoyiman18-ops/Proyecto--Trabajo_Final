package motor.modelo.graficos;

import java.util.Map;

import motor.recursos.Extension;

public class Sheet {
    private final int id;
    private final String path;
    private final Extension.Imagen extension;
    private final Map<String,Animacion> animaciones;
    private final Map<String,Recorte> recortes;
}
