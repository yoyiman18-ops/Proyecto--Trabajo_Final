package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.*;
import motor.SpatialHashGrid;
import motor.cache.CacheImagenes;
import motor.colisiones.FaseEspecifica;
import motor.colisiones.FaseGeneral;
import vista.SpriteVista;

import controlador.SpriteControlador;

public class App extends Application {

    CacheImagenes cache = new CacheImagenes();
    SpatialHashGrid<Entidad> cuadricula = new SpatialHashGrid<>(64); 
    FaseGeneral faseGen = new FaseGeneral();
    FaseEspecifica faseEsp = new FaseEspecifica();

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

        cuadricula.insertar(e1);
        cuadricula.insertar(e2);
        for (int i = -10; i <= 10; i++) {
            for (int j = -10; j <= 10; j++) {
                if (!faseGen.calcularParesEnCelda(cuadricula.getCelda(i, j))) {
                    System.out.printf("Celda vacia | %d ; %d%n ", i, j);
                } else {
                    System.out.printf("CELDA NO VACIA | %d ; %d%n ", i, j);
                };   
            }
        }
        if (faseGen.getPares().isEmpty()) { System.out.println("No hay parejas posibles. "); }
        else { System.out.println("Hay parejas posibles."); }
        faseEsp.procesarColisiones(faseGen.getPares());



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
