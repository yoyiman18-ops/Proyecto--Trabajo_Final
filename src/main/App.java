package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.*;
import motor.cache.CacheImagenes;
import vista.SpriteVista;
import motor.colisiones.*;
import java.util.ArrayList;

import controlador.SpriteControlador;

public class App extends Application {

    CacheImagenes cache = new CacheImagenes();
    ArrayList<Entidad> entidades = new ArrayList<>();
    ControladorColisiones colisiones = new ControladorColisiones(64);

    @Override
    public void start(Stage stage) {           
        EntidadViva e1 = new EntidadViva.Builder()
                        .nombre("Brotato")
                        .posicion(10,10)
                        .direccion(0.45, 0.55)
                        .velocidadMax(1)
                        .aceleracion(10)
                        .hitbox(10, 10, 0, 0,true)
                        .build();
        EntidadViva e2 = new EntidadViva.Builder()
                        .nombre("Brotato")
                        .posicion(0, 0)
                        .hitbox(11, 10, 0, 0, true)
                        .build();

        entidades.add(e1);
        entidades.add(e2);

        colisiones.resolverColisiones(entidades);

        SpriteVista vista1 = new SpriteVista(cache);
        SpriteControlador controlador1 = new SpriteControlador(e1, vista1);
        Scene escena = new Scene(vista1, 300, 300);
        stage.setScene(escena);
        stage.show();
        vista1.requestFocus();

    }

    public static void main(String[] args) throws Exception {

        launch();

    }
}
