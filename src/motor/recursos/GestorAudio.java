package motor.recursos;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GestorAudio {

    private final GestorRecursos gestorRecursos;
    private final ExecutorService hiloAudio;
    private final Logger logger;

    private MediaPlayer reproductorMusica;
    private String musicaActual;

    public GestorAudio(GestorRecursos gestorRecursos) {
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        this.logger = Logger.getLogger(getClass().getName());
        this.hiloAudio = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Hilo-GestorAudio");
            t.setDaemon(true);
            return t;
        });
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
}
