package motor.recursos;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import motor.util.observer.Observador;

public class GestorAudio {

    public record RecursoSonido(String nombre, Extension.Sonido extension) {
        public RecursoSonido {
            Objects.requireNonNull(nombre, "El nombre del recurso de sonido no puede ser nulo");
            Objects.requireNonNull(extension, "La extensión del recurso de sonido no puede ser nula");
        }
    }

    private final GestorRecursos gestorRecursos;
    private final ExecutorService hiloAudio;
    private final Logger logger;
    private final Map<Integer, RecursoSonido> sonidosPorId;
    private final Observador<Integer> observadorSonido;

    private MediaPlayer reproductorMusica;
    private String musicaActual;

    public GestorAudio(GestorRecursos gestorRecursos) {
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        this.logger = Logger.getLogger(getClass().getName());
        this.sonidosPorId = new ConcurrentHashMap<>();
        this.observadorSonido = this::reproducirSonido;
        this.hiloAudio = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Hilo-GestorAudio");
            t.setDaemon(true);
            return t;
        });
    }

    public Observador<Integer> getObservadorSonido() {
        return this.observadorSonido;
    }

    public void registrarSonido(int id, String nombre, Extension.Sonido ext) {
        if (nombre == null || ext == null) {
            return;
        }
        sonidosPorId.put(id, new RecursoSonido(nombre, ext));
        precargarSonido(nombre, ext);
    }

    public void reproducirSonido(int id) {
        RecursoSonido recurso = sonidosPorId.get(id);
        if (recurso != null) {
            reproducirSonido(recurso.nombre(), recurso.extension());
        }
    }

    public void precargarSonido(String nombre, Extension.Sonido ext) {
        if (nombre == null || ext == null) {
            return;
        }
        hiloAudio.submit(() -> {
            try {
                gestorRecursos.getSonido(nombre, ext);
            } catch (Exception e) {
                logger.log(Level.WARNING, "No se pudo precargar sonido: " + nombre, e);
            }
        });
    }

    public void reproducirSonido(String nombre, Extension.Sonido ext) {
        if (nombre == null || ext == null) {
            return;
        }
        hiloAudio.submit(() -> {
            try {
                AudioClip clip = gestorRecursos.getSonido(nombre, ext);
                if (clip != null) {
                    clip.play();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "No se pudo reproducir sonido: " + nombre, e);
            }
        });
    }

    public void reproducirMusica(String nombre, Extension.Sonido ext) {
        if (nombre == null || ext == null) {
            return;
        }
        if (nombre.equals(musicaActual) && reproductorMusica != null) {
            reproductorMusica.play();
            return;
        }
        detenerMusica();
        try {
            Media media = gestorRecursos.getMusica(nombre, ext);
            reproductorMusica = new MediaPlayer(media);
            reproductorMusica.setCycleCount(MediaPlayer.INDEFINITE);
            reproductorMusica.play();
            musicaActual = nombre;
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo reproducir musica: " + nombre, e);
        }
    }

    public void pausarMusica() {
        if (reproductorMusica != null) {
            reproductorMusica.pause();
        }
    }

    public void reanudarMusica() {
        if (reproductorMusica != null) {
            reproductorMusica.play();
        }
    }

    public void detenerMusica() {
        if (reproductorMusica != null) {
            reproductorMusica.stop();
            reproductorMusica.dispose();
            reproductorMusica = null;
            musicaActual = null;
        }
    }

    public void cerrar() {
        detenerMusica();
        hiloAudio.shutdown();
    }
}
