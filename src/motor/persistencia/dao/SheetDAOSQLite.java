package motor.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import motor.modelo.graficos.Animacion;
import motor.modelo.graficos.Recorte;
import motor.modelo.graficos.Sheet;
import motor.persistencia.PKAnimacion;
import motor.persistencia.PKRecorte;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;

public class SheetDAOSQLite implements DAO<Sheet, Integer> {

    private final Connection conexion;
    private final GestorRecursos gestorRecursos;

    public SheetDAOSQLite(Connection conexion, GestorRecursos gestorRecursos) {
        if (conexion == null) {
            throw new IllegalArgumentException("Conexion no puede ser nula");
        }
        if (gestorRecursos == null) {
            throw new IllegalArgumentException("GestorRecursos no puede ser nulo");
        }
        this.conexion = conexion;
        this.gestorRecursos = gestorRecursos;
    }

    @Override
    public boolean guardar(Sheet objeto) {
        if (objeto == null) {
            return false;
        }
        String sqlCrearTabla = """
            CREATE TABLE IF NOT EXISTS sheet (
                id_sheet INTEGER NOT NULL,
                path TEXT NOT NULL,
                extension TEXT NOT NULL,
                PRIMARY KEY (id_sheet)
            );
            """;
        String sqlInsertar = """
            INSERT INTO sheet (id_sheet, path, extension)
            VALUES (?, ?, ?);
            """;
        try {
            try (Statement sentencia = conexion.createStatement()) {
                sentencia.execute(sqlCrearTabla);
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlInsertar)) {
                sentencia.setInt(1, objeto.getIdSheet());
                sentencia.setString(2, objeto.getPath());
                sentencia.setString(3, objeto.getExtension().name());
                int filas = sentencia.executeUpdate();
                if (filas > 0) {
                    if (objeto.getRecortes() != null) {
                        DAO<Recorte, PKRecorte> recorteDAO = new RecorteDAOSQLite(this.conexion, objeto.getIdSheet());
                        for (Recorte r : objeto.getRecortes()) {
                            recorteDAO.guardar(r);
                        }
                    }
                    if (objeto.getAnimaciones() != null) {
                        DAO<Animacion, PKAnimacion> animacionDAO = new AnimacionDAOSQLite(this.conexion, objeto.getIdSheet());
                        for (Animacion a : objeto.getAnimaciones()) {
                            animacionDAO.guardar(a);
                        }
                    }
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Sheet cargar(Integer clave) {
        if (clave == null) {
            return null;
        }
        String sql = "SELECT id_sheet, path, extension FROM sheet WHERE id_sheet = ?;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, clave);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    int idSheet = rs.getInt("id_sheet");
                    String path = rs.getString("path");
                    Extension.Imagen extension = Extension.Imagen.fromString(rs.getString("extension"));
                    Image imagen = this.gestorRecursos.getImagen(path, extension);

                    DAO<Recorte, PKRecorte> recorteDAO = new RecorteDAOSQLite(this.conexion, idSheet);
                    List<Recorte> recortesList = recorteDAO.cargarTodos();
                    Map<String, Recorte> recortesMap = new HashMap<>();
                    if (recortesList != null) {
                        for (Recorte r : recortesList) {
                            recortesMap.put(r.getNombreRecorte(), r);
                        }
                    }

                    DAO<Animacion, PKAnimacion> animacionDAO = new AnimacionDAOSQLite(this.conexion, idSheet);
                    List<Animacion> animacionesList = animacionDAO.cargarTodos();
                    Map<String, Animacion> animacionesMap = new HashMap<>();
                    if (animacionesList != null) {
                        for (Animacion a : animacionesList) {
                            animacionesMap.put(a.getNombreAnimacion(), a);
                        }
                    }

                    return new Sheet(idSheet, path, imagen, extension, animacionesMap, recortesMap);
                }
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean eliminar(Integer clave) {
        if (clave == null) {
            return false;
        }
        String sql = "DELETE FROM sheet WHERE id_sheet = ?;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, clave);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Sheet> cargarTodos() {
        List<Sheet> sheets = new ArrayList<>();
        String sql = "SELECT id_sheet, path, extension FROM sheet ORDER BY id_sheet ASC;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet rs = sentencia.executeQuery()) {
            while (rs.next()) {
                int idSheet = rs.getInt("id_sheet");
                String path = rs.getString("path");
                Extension.Imagen extension = Extension.Imagen.fromString(rs.getString("extension"));
                Image imagen = this.gestorRecursos.getImagen(path, extension);

                DAO<Recorte, PKRecorte> recorteDAO = new RecorteDAOSQLite(this.conexion, idSheet);
                List<Recorte> recortesList = recorteDAO.cargarTodos();
                Map<String, Recorte> recortesMap = new HashMap<>();
                if (recortesList != null) {
                    for (Recorte r : recortesList) {
                        recortesMap.put(r.getNombreRecorte(), r);
                    }
                }

                DAO<Animacion, PKAnimacion> animacionDAO = new AnimacionDAOSQLite(this.conexion, idSheet);
                List<Animacion> animacionesList = animacionDAO.cargarTodos();
                Map<String, Animacion> animacionesMap = new HashMap<>();
                if (animacionesList != null) {
                    for (Animacion a : animacionesList) {
                        animacionesMap.put(a.getNombreAnimacion(), a);
                    }
                }

                sheets.add(new Sheet(idSheet, path, imagen, extension, animacionesMap, recortesMap));
            }
        } catch (SQLException e) {
            return sheets;
        }
        return sheets;
    }

    @Override
    public boolean modificar(Sheet objeto) {
        if (objeto == null) {
            return false;
        }
        String sql = """
            UPDATE sheet
            SET path = ?, extension = ?
            WHERE id_sheet = ?;
            """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, objeto.getPath());
            sentencia.setString(2, objeto.getExtension().name());
            sentencia.setInt(3, objeto.getIdSheet());
            int filas = sentencia.executeUpdate();
            if (filas > 0) {
                if (objeto.getRecortes() != null) {
                    DAO<Recorte, PKRecorte> recorteDAO = new RecorteDAOSQLite(this.conexion, objeto.getIdSheet());
                    for (Recorte r : objeto.getRecortes()) {
                        recorteDAO.modificar(r);
                    }
                }
                if (objeto.getAnimaciones() != null) {
                    DAO<Animacion, PKAnimacion> animacionDAO = new AnimacionDAOSQLite(this.conexion, objeto.getIdSheet());
                    for (Animacion a : objeto.getAnimaciones()) {
                        animacionDAO.modificar(a);
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void mostrar(Integer clave) {
        Sheet s = cargar(clave);
        if (s == null) {
            System.out.println("Sheet no encontrada para clave: " + clave);
            return;
        }
        System.out.printf("Sheet [idSheet=%d, path=%s, extension=%s]%n",
            s.getIdSheet(), s.getPath(), s.getExtension().name());
        System.out.println(" Recortes:");
        DAO<Recorte, PKRecorte> recorteDAO = new RecorteDAOSQLite(this.conexion, s.getIdSheet());
        recorteDAO.mostrarTodos();
        System.out.println(" Animaciones:");
        DAO<Animacion, PKAnimacion> animacionDAO = new AnimacionDAOSQLite(this.conexion, s.getIdSheet());
        animacionDAO.mostrarTodos();
    }

    @Override
    public void mostrarTodos() {
        List<Sheet> sheets = cargarTodos();
        if (sheets == null || sheets.isEmpty()) {
            System.out.println("(Sin sheets registradas)");
            return;
        }
        for (Sheet s : sheets) {
            mostrar(s.getIdSheet());
            System.out.println("--------------------------------------------------");
        }
    }
}
