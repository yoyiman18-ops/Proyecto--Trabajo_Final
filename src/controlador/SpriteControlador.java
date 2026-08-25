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

        configurarEventos();
        actualizarVista();
    }

    private void configurarEventos() {

        vista.setOnKeyPressed(evento -> {

            switch (evento.getCode()) {

                case LEFT:
                    break;
                case RIGHT:
                    break;
                case UP:
                    break;
                case DOWN:
                    break;
                default:
                    break;
            }

            actualizarVista();
        });
        
        vista.setFocusTraversable(true);
    }

    public void actualizarVista() {
        this.vista.actualizar(modelo);
    }
}
