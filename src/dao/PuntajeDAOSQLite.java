package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import modelo.Puntaje;

public final class PuntajeDAOSQLite implements PuntajeDAO, AutoCloseable {

    private final String crearTabla;
    private final String insertarPuntaje;
    private final String borrarPuntaje;
    private final String seleccionarPuntajes;
    private final Connection conexion;
    private final ExecutorService executor;
    private final boolean executorPropio;

    public PuntajeDAOSQLite(Connection conexion) throws SQLException {
        this(conexion, Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Hilo-PuntajeDAOSQLite");
            t.setDaemon(true);
            return t;
        }), true);
    }

    public PuntajeDAOSQLite(Connection conexion, ExecutorService executor) throws SQLException {
        this(conexion, executor, false);
    }

    private PuntajeDAOSQLite(Connection conexion, ExecutorService executor, boolean executorPropio) throws SQLException {
        this.conexion = Objects.requireNonNull(conexion, "La conexión no puede ser nula.");
        this.executor = Objects.requireNonNull(executor, "El executor no puede ser nulo.");
        this.executorPropio = executorPropio;
        this.crearTabla =
            "CREATE TABLE IF NOT EXISTS puntajes ("
                + "fecha INTEGER NOT NULL, "
                + "puntos INTEGER NOT NULL"
                + ")";
        this.insertarPuntaje = "INSERT INTO puntajes (fecha, puntos) VALUES (?, ?)";
        this.borrarPuntaje = "DELETE FROM puntajes WHERE fecha = ? AND puntos = ?";
        this.seleccionarPuntajes = "SELECT fecha, puntos FROM puntajes ORDER BY puntos DESC";

        synchronized (this.conexion) {
            try (Statement sentencia = this.conexion.createStatement()) {
                sentencia.executeUpdate(this.crearTabla);
            }
        }
    }

    @Override
    public void guardar(Puntaje puntaje) {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");

        synchronized (conexion) {
            try (PreparedStatement sentencia = conexion.prepareStatement(insertarPuntaje)) {
                sentencia.setInt(1, puntaje.getFecha());
                sentencia.setInt(2, puntaje.getPuntos());
                sentencia.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Error al guardar el puntaje: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void borrar(Puntaje puntaje) {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");

        synchronized (conexion) {
            try (PreparedStatement sentencia = conexion.prepareStatement(borrarPuntaje)) {
                sentencia.setInt(1, puntaje.getFecha());
                sentencia.setInt(2, puntaje.getPuntos());
                sentencia.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Error al borrar el puntaje: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Puntaje> listar() {
        synchronized (conexion) {
            try (Statement sentencia = conexion.createStatement();
                 ResultSet rs = sentencia.executeQuery(seleccionarPuntajes)) {
                List<Puntaje> puntajes = new ArrayList<>();
                while (rs.next()) {
                    puntajes.add(new Puntaje(rs.getInt("fecha"), rs.getInt("puntos")));
                }
                return puntajes;
            } catch (SQLException e) {
                throw new IllegalStateException("Error al listar los puntajes: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public CompletableFuture<Void> guardarAsync(Puntaje puntaje) {
        return guardarAsync(puntaje, this.executor);
    }

    @Override
    public CompletableFuture<Void> borrarAsync(Puntaje puntaje) {
        return borrarAsync(puntaje, this.executor);
    }

    @Override
    public CompletableFuture<Void> guardarAsync(Puntaje puntaje, Executor executor) {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");
        Objects.requireNonNull(executor, "El executor no puede ser nulo.");
        return CompletableFuture.runAsync(() -> guardar(puntaje), executor);
    }

    @Override
    public CompletableFuture<Void> borrarAsync(Puntaje puntaje, Executor executor) {
        Objects.requireNonNull(puntaje, "El puntaje no puede ser nulo.");
        Objects.requireNonNull(executor, "El executor no puede ser nulo.");
        return CompletableFuture.runAsync(() -> borrar(puntaje), executor);
    }

    @Override
    public CompletableFuture<List<Puntaje>> listarAsync() {
        return listarAsync(this.executor);
    }

    @Override
    public CompletableFuture<List<Puntaje>> listarAsync(Executor executor) {
        Objects.requireNonNull(executor, "El executor no puede ser nulo.");
        return CompletableFuture.supplyAsync(this::listar, executor);
    }

    @Override
    public void close() {
        if (executorPropio) {
            executor.shutdown();
        }
    }
}
