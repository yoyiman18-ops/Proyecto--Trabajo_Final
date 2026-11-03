package motor.recursos.cache;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javafx.scene.image.Image;
import motor.recursos.Extension;

public class CacheImagenes extends CacheRecursos<Image> {

    private final ExecutorService hiloImagenes;
    private final boolean executorPropio;

    public CacheImagenes(String carpeta) {
        this(carpeta, Executors.newFixedThreadPool(
            Math.max(2, Math.min(4, Runtime.getRuntime().availableProcessors())),
            r -> {
                Thread t = new Thread(r, "Hilo-CacheImagenes");
                t.setDaemon(true);
                return t;
            }
        ), true);
    }

    public CacheImagenes(String carpeta, ExecutorService executor) {
        this(carpeta, executor, false);
    }

    private CacheImagenes(String carpeta, ExecutorService executor, boolean executorPropio) {
        super(carpeta);
        this.hiloImagenes = Objects.requireNonNull(executor, "El executor de imágenes no puede ser nulo");
        this.executorPropio = executorPropio;
    }

    @Override
    public Image getRecurso(String nombre, Extension.IExtension ext) {
        if (!(ext instanceof Extension.Imagen)) {
            throw new IllegalArgumentException("Extension invalida para imagen: " + ext);
        }
        return cache.computeIfAbsent(
            resolverPathRecurso(nombre, ext),
            k -> {
                try {
                    Image imagen = new Image(k);
                    if (imagen.isError()) {
                        throw new RuntimeException("Error al cargar imagen " + k, imagen.getException());
                    }
                    loggearCarga(k);
                    return imagen;
                } catch (Exception e) {
                    throw new RuntimeException("No se pudo cargar la imagen: " + resolverPathRecurso(nombre, ext) + " (" + e.getMessage() + ")", e);
                }
            }
        );
    }

    public CompletableFuture<Image> getRecursoAsync(String nombre, Extension.Imagen ext) {
        Objects.requireNonNull(nombre, "El nombre del recurso de imagen no puede ser nulo");
        Objects.requireNonNull(ext, "La extensión de la imagen no puede ser nula");
        return CompletableFuture.supplyAsync(() -> getRecurso(nombre, ext), hiloImagenes);
    }

    public CompletableFuture<Image> precargar(String nombre, Extension.Imagen ext) {
        if (nombre == null || ext == null) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getRecurso(nombre, ext);
            } catch (Exception e) {
                logger.log(Level.WARNING, "No se pudo precargar la imagen: " + nombre, e);
                return null;
            }
        }, hiloImagenes);
    }

    public CompletableFuture<Void> precargar(Collection<String> nombres, Extension.Imagen ext) {
        if (nombres == null || nombres.isEmpty() || ext == null) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<?>[] futuros = nombres.stream()
            .map(nombre -> precargar(nombre, ext))
            .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futuros);
    }

    public boolean estaCargado(String nombre, Extension.Imagen ext) {
        if (nombre == null || ext == null) {
            return false;
        }
        return contiene(nombre, ext);
    }

    public void cerrar() {
        if (executorPropio) {
            hiloImagenes.shutdown();
        }
    }
}
