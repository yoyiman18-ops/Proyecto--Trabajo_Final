package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.*;
import motor.CacheRecursos;
import vista.SpriteVista;

// import java.util.Random;

import controlador.SpriteControlador;

public class App extends Application {

    CacheRecursos cr = new CacheRecursos();

    @Override
    public void start(Stage stage) {           
        EntidadViva e1 = new EntidadViva.Builder()
                        .nombre("Brotato")
                        .posicion(-12,53)
                        .direccion(0.45, 0.55)
                        .velocidadMax(1)
                        .aceleracion(10)
                        .build();

        SpriteVista vista = new SpriteVista(cr);
        SpriteControlador controlador = new SpriteControlador(e1, vista);

        Scene escena = new Scene(vista, 800, 600);
        stage.setScene(escena);
        stage.show();
        vista.requestFocus();
    }

    public static void main(String[] args) throws Exception {

        launch();

    }
}
