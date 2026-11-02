package dao;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import modelo.Puntaje;

public interface PuntajeDAO {
    void guardar(Puntaje puntaje);

    void borrar(Puntaje puntaje);

    default CompletableFuture<Void> guardarAsync(Puntaje puntaje) {
        return CompletableFuture.runAsync(() -> guardar(puntaje));
    }

    default CompletableFuture<Void> borrarAsync(Puntaje puntaje) {
        return CompletableFuture.runAsync(() -> borrar(puntaje));
    }

    default CompletableFuture<Void> guardarAsync(Puntaje puntaje, Executor executor) {
        return CompletableFuture.runAsync(() -> guardar(puntaje), executor);
    }

    default CompletableFuture<Void> borrarAsync(Puntaje puntaje, Executor executor) {
        return CompletableFuture.runAsync(() -> borrar(puntaje), executor);
    }

    default List<Puntaje> listar() {
        return Collections.emptyList();
    }

    default CompletableFuture<List<Puntaje>> listarAsync() {
        return CompletableFuture.supplyAsync(this::listar);
    }

    default CompletableFuture<List<Puntaje>> listarAsync(Executor executor) {
        return CompletableFuture.supplyAsync(this::listar, executor);
    }
}
