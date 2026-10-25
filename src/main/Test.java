package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.persistencia.dao.sqlite.Conexion;
import motor.persistencia.dao.sqlite.SheetDAOSQLite;
import motor.recursos.GestorRecursos;

public class Test {

    public static void main(String[] args) throws Exception {
        Platform.startup(() -> {}); //necesario iniciar javafx para cargar imagenes
        Conexion conexion = Conexion.getInstancia();
        GestorRecursos gestorRecursos = GestorRecursos.getInstancia();
        /*
        // test 1: ram
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite::memory:")) {

            // --- Sheet 1: Zorro ---
            int idSheet1 = 1;
            String path1 = "zorro/zorro";
            Extension.Imagen ext1 = Extension.Imagen.PNG;
            Image img1 = recursos.getImagen(path1, ext1);

            Recorte r1 = new Recorte(idSheet1, "abajo_0", 0, 0, 32, 32);
            Recorte r2 = new Recorte(idSheet1, "abajo_1", 32, 0, 32, 32);
            Recorte r3 = new Recorte(idSheet1, "arriba_0", 0, 32, 32, 32);
            Recorte r4 = new Recorte(idSheet1, "arriba_1", 32, 32, 32, 32);

            Map<String, Recorte> recortesSheet1 = new HashMap<>();
            recortesSheet1.put(r1.getNombreRecorte(), r1);
            recortesSheet1.put(r2.getNombreRecorte(), r2);
            recortesSheet1.put(r3.getNombreRecorte(), r3);
            recortesSheet1.put(r4.getNombreRecorte(), r4);

            Frame f1 = new Frame(idSheet1, "caminar_abajo", 0, r1.getNombreRecorte(), r1, 150);
            Frame f2 = new Frame(idSheet1, "caminar_abajo", 1, r2.getNombreRecorte(), r2, 150);
            Animacion anim1 = new Animacion(idSheet1, "caminar_abajo", true, new ArrayList<>(List.of(f1, f2)));

            Frame f3 = new Frame(idSheet1, "caminar_arriba", 0, r3.getNombreRecorte(), r3, 150);
            Frame f4 = new Frame(idSheet1, "caminar_arriba", 1, r4.getNombreRecorte(), r4, 150);
            Animacion anim2 = new Animacion(idSheet1, "caminar_arriba", true, new ArrayList<>(List.of(f3, f4)));

            Map<String, Animacion> animacionesSheet1 = new HashMap<>();
            animacionesSheet1.put(anim1.getNombreAnimacion(), anim1);
            animacionesSheet1.put(anim2.getNombreAnimacion(), anim2);

            Sheet sheet1 = new Sheet(idSheet1, path1, img1, ext1, animacionesSheet1, recortesSheet1);

            // --- Sheet 2: GUI ---
            int idSheet2 = 2;
            String path2 = "rpg_gui_v1/RPG_GUI_v1";
            Extension.Imagen ext2 = Extension.Imagen.PNG;
            Image img2 = recursos.getImagen(path2, ext2);

            Recorte r5 = new Recorte(idSheet2, "boton_normal", 10, 10, 100, 40);
            Recorte r6 = new Recorte(idSheet2, "boton_presionado", 10, 60, 100, 40);

            Map<String, Recorte> recortesSheet2 = new HashMap<>();
            recortesSheet2.put(r5.getNombreRecorte(), r5);
            recortesSheet2.put(r6.getNombreRecorte(), r6);

            Frame f5 = new Frame(idSheet2, "click_boton", 0, r5.getNombreRecorte(), r5, 100);
            Frame f6 = new Frame(idSheet2, "click_boton", 1, r6.getNombreRecorte(), r6, 100);
            Animacion anim3 = new Animacion(idSheet2, "click_boton", false, new ArrayList<>(List.of(f5, f6)));

            Map<String, Animacion> animacionesSheet2 = new HashMap<>();
            animacionesSheet2.put(anim3.getNombreAnimacion(), anim3);

            Sheet sheet2 = new Sheet(idSheet2, path2, img2, ext2, animacionesSheet2, recortesSheet2);

            // --- Guardar en DAO<Sheet, Integer> y aJson() ---
            DAO<Sheet, Integer> sheetDAO = new SheetDAOSQLite(conexion, recursos);
            sheetDAO.guardar(sheet1);
            sheetDAO.guardar(sheet2);

            sheetDAO.eliminar(1);
            System.out.println(sheetDAO.aJson(2));
        
        } finally {
            Platform.exit();
        }
        */

        //test 2: cargar de una bd

        DAOSoloLectura<Integer,Sheet> sheetDAO = new SheetDAOSQLite(conexion.get(), gestorRecursos);
        List<Sheet> sheets = sheetDAO.cargarTodos();
        System.out.printf("Se han cargado %d spritesheets en la base de datos de sqlite.", sheets.size());
        Platform.exit();
    }

}
