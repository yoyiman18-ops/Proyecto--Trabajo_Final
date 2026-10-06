package motor.util.graficos;
import java.util.HashMap;
import java.util.Map;


public class SpritesheetAnimada {
    private final SheetCuadricula sheet;
    private final Map<String,Animacion> animaciones;

    public SpritesheetAnimada(int filas, int columnas, double anchoRecorte, double altoRecorte, double espaciadoHorizontal, double espaciadoVertical) {
        this.sheet = new SheetCuadricula(filas, columnas, anchoRecorte, altoRecorte, espaciadoHorizontal, espaciadoVertical);
        this.animaciones = new HashMap<>();
    }

    public void añadirAnimacion(String nombre, Animacion animacion) {
        animaciones.computeIfAbsent(
            nombre,
            n -> animacion
        );
    }

    public Animacion getAnimacion(String nombre) { return animaciones.get(nombre); }
    




}
