package motor.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import motor.modelo.graficos.Animacion;
import motor.modelo.graficos.Frame;
import motor.persistencia.PKAnimacion;
import motor.persistencia.PKFrame;

public class AnimacionDAOSQLite implements DAO<Animacion, PKAnimacion> {

    private final Connection conexion;
    private final int idSheet;

    public AnimacionDAOSQLite(Connection conexion, int idSheet) {
        if (conexion == null) {
            throw new IllegalArgumentException("Conexion no puede ser nula");
        }
        this.conexion = conexion;
        this.idSheet = idSheet;
    }

    @Override
    public boolean guardar(Animacion objeto) {
        if (objeto == null) {
            return false;
        }
        String sqlCrearTabla = """
            CREATE TABLE IF NOT EXISTS animacion (
                id_sheet INTEGER NOT NULL,
                nombre_animacion TEXT NOT NULL,
                es_bucle INTEGER NOT NULL,
                FOREIGN KEY (id_sheet) REFERENCES sheet(id_sheet) ON DELETE CASCADE,
                PRIMARY KEY (id_sheet, nombre_animacion)
            );
            """;
        String sqlInsertar = """
            INSERT INTO animacion (id_sheet, nombre_animacion, es_bucle)
            VALUES (?, ?, ?);
            """;
        try {
            try (Statement sentencia = conexion.createStatement()) {
                sentencia.execute(sqlCrearTabla);
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlInsertar)) {
                sentencia.setInt(1, objeto.getIdSheet());
                sentencia.setString(2, objeto.getNombreAnimacion());
                sentencia.setInt(3, objeto.esBucle() ? 1 : 0);
                int filas = sentencia.executeUpdate();
                if (filas > 0) {
                    DAO<Frame, PKFrame> frameDAO = new FrameDAOSQLite(
                        this.conexion,
                        this.idSheet,
                        objeto.getNombreAnimacion()
                    );
                    if (objeto.getFrames() != null) {
                        for (Frame f : objeto.getFrames()) {
                            frameDAO.guardar(f);
                        }
                    }
                    frameDAO.cargarTodos();
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Animacion cargar(PKAnimacion clave) {
        if (clave == null) {
            return null;
        }
        String sql = "SELECT id_sheet,nombre_animacion,es_bucle FROM animacion WHERE id_sheet = ? AND nombre_animacion = ?;";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, clave.idSheet());
            sentencia.setString(2, clave.nombreAnimacion());
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    int idAnimacion = rs.getInt("id_sheet");
                    String nombreAnimacion = rs.getString("nombre_animacion");
                    boolean esBucle = rs.getInt("es_bucle") == 1;

                    DAO<Frame, PKFrame> frameDAO = new FrameDAOSQLite(
                        this.conexion,
                        clave.idSheet(),
                        clave.nombreAnimacion()
                    );
                    List<Frame> frames = frameDAO.cargarTodos(); // puede ser vacia, la excepcion aparece en el constructor de animacion
                    return new Animacion(idAnimacion, nombreAnimacion, esBucle, frames);
                }
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean eliminar(PKAnimacion clave) {
        if (clave == null) {
            return false;
        }
        String sqlAnim = "DELETE FROM animacion WHERE id_sheet = ? AND nombre_animacion = ?;";
        try {
            try (PreparedStatement sentenciaAnim = conexion.prepareStatement(sqlAnim)) {
                sentenciaAnim.setInt(1, clave.idSheet());
                sentenciaAnim.setString(2, clave.nombreAnimacion());
                return sentenciaAnim.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Animacion> cargarTodos() {
        List<Animacion> animaciones = new ArrayList<>();
        String sql = """
            SELECT id_sheet, nombre_animacion, es_bucle
            FROM animacion
            WHERE id_sheet = ?
            ORDER BY nombre_animacion ASC;
            """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, this.idSheet);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    int idAnimacion = rs.getInt("id_sheet");
                    String nombreAnimacion = rs.getString("nombre_animacion");
                    boolean esBucle = rs.getInt("es_bucle") == 1;

                    DAO<Frame, PKFrame> frameDAO = new FrameDAOSQLite(
                        this.conexion,
                        this.idSheet,
                        nombreAnimacion
                    );
                    List<Frame> frames = frameDAO.cargarTodos(); // nota: puede llegar a estar vacia, en ese caso el error se resuelve en el constructor de animacion
                    animaciones.add(new Animacion(idAnimacion, nombreAnimacion, esBucle, frames));
                }
            }
        } catch (SQLException e) {
            return animaciones;
        }
        return animaciones;
    }

    @Override
    public boolean modificar(Animacion objeto) {
        if (objeto == null) {
            return false;
        }
        String sql = """
            UPDATE animacion
            SET es_bucle = ?
            WHERE id_sheet = ? AND nombre_animacion = ?;
            """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, objeto.esBucle() ? 1 : 0);
            sentencia.setInt(2, objeto.getIdSheet());
            sentencia.setString(3, objeto.getNombreAnimacion());
            int filas = sentencia.executeUpdate();
            if (filas > 0) {
                if (objeto.getFrames() != null) {
                    DAO<Frame, PKFrame> frameDAO = new FrameDAOSQLite(
                        this.conexion,
                        this.idSheet,
                        objeto.getNombreAnimacion()
                    );
                    for (Frame f : objeto.getFrames()) {
                        frameDAO.modificar(f);
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
    public String aJson(PKAnimacion clave) {
        Animacion a = cargar(clave);
        if (a == null) {
            return "null";
        }
        DAO<Frame, PKFrame> frameDAO = new FrameDAOSQLite(
            this.conexion,
            a.getIdSheet(),
            a.getNombreAnimacion()
        );
        return String.format(
            "{\n        \"idSheet\": %d,\n        \"nombreAnimacion\": \"%s\",\n        \"esBucle\": %b,\n        \"frames\": %s\n      }",
            a.getIdSheet(), a.getNombreAnimacion(), a.esBucle(), frameDAO.aJson()
        );
    }

    @Override
    public String aJson() {
        List<Animacion> animaciones = cargarTodos();
        if (animaciones == null || animaciones.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < animaciones.size(); i++) {
            Animacion a = animaciones.get(i);
            sb.append("      ").append(aJson(new PKAnimacion(a.getIdSheet(), a.getNombreAnimacion())));
            if (i < animaciones.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("    ]");
        return sb.toString();
    }
}
