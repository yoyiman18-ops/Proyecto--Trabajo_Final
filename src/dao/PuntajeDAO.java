package dao;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import modelo.Puntaje;

public interface PuntajeDAO {
    void guardar(Puntaje puntaje);

    void borrar(Puntaje puntaje);

    default void borrarTodos() {}

    default CompletableFuture<Void> guardarAsync(Puntaje puntaje) {
        return CompletableFuture.runAsync(() -> guardar(puntaje));
    }

    default CompletableFuture<Void> borrarAsync(Puntaje puntaje) {
        return CompletableFuture.runAsync(() -> borrar(puntaje));
    }

    default CompletableFuture<Void> borrarTodosAsync() {
        return CompletableFuture.runAsync(this::borrarTodos);
    }

    default List<Puntaje> listar() {
        return Collections.emptyList();
    }

    default CompletableFuture<List<Puntaje>> listarAsync() {
        return CompletableFuture.supplyAsync(this::listar);
    }
}
