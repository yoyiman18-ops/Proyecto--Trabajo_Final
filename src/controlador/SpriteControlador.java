package controlador;

// import javafx.scene.input.KeyCode;
import modelo.SpriteModelo;
import vista.SpriteVista;

public class SpriteControlador {

    private SpriteModelo modelo;
    private SpriteVista vista;

    public SpriteControlador(SpriteModelo modelo, SpriteVista vista) {
        this.modelo = modelo;
        this.vista = vista;
        actualizarVista();
    }

    public void actualizarVista() {
        this.vista.actualizar(modelo);
    }
}
