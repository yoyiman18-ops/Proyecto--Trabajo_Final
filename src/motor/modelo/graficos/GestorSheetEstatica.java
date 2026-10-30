package motor.modelo.graficos;

import javafx.geometry.Rectangle2D;

public class GestorSheetEstatica extends GestorSheet {
    private Recorte recorteActual;

    public GestorSheetEstatica(Sheet spritesheet) { super(spritesheet); }

    public boolean setRecorte(String nombre) { 
        Recorte r = getSheet().getRecorte(nombre); 
        if (r == null) { return false;} 
        else { recorteActual = r; this.notificar(); return true; }
    }

    @Override
    protected Rectangle2D getRectangulo() { return this.recorteActual.getRectangulo(); }
}
