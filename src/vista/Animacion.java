package vista;

import java.util.List;
import java.util.Objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Animacion {
	public static final double DURACION_FRAME_DEFAULT = 0.12;

	private final Image hoja;
	private final int columnas;
	private final int filas;
	private final double anchoFrame;
	private final double altoFrame;
	private final double duracionFrame;
	private final List<Image> frames;
	private int columnaActual;
	private int filaActual;
	private int frameActual;
	private int columnaVisible = -1;
	private int filaVisible = -1;
	private double tiempoAcumulado;
	private Rectangle2D viewport;

	public Animacion(Image hoja, int columnas, int filas) {
		this(hoja, columnas, filas, DURACION_FRAME_DEFAULT);
	}

	public Animacion(Image hoja, int columnas, int filas, double duracionFrame) {
		Objects.requireNonNull(hoja, "La hoja de sprites no puede ser nula.");
		if (columnas <= 0 || filas <= 0 || hoja.getWidth() <= 0 || hoja.getHeight() <= 0) {
			throw new IllegalArgumentException("La hoja de sprites debe tener dimensiones positivas.");
		}

		this.hoja = hoja;
		this.columnas = columnas;
		this.filas = filas;
		this.anchoFrame = hoja.getWidth() / columnas;
		this.altoFrame = hoja.getHeight() / filas;
		this.duracionFrame = validarDuracion(duracionFrame);
		this.frames = null;
		actualizarViewport();
	}

	public Animacion(List<Image> frames) {
		this(frames, DURACION_FRAME_DEFAULT);
	}

	public Animacion(List<Image> frames, double duracionFrame) {
		if (frames == null || frames.isEmpty()) {
			throw new IllegalArgumentException("La secuencia debe contener al menos un frame.");
		}
		for (Image frame : frames) {
			if (frame == null || frame.getWidth() <= 0 || frame.getHeight() <= 0) {
				throw new IllegalArgumentException("Cada frame debe ser una imagen válida.");
			}
		}

		this.hoja = null;
		this.columnas = 0;
		this.filas = 0;
		this.anchoFrame = 0;
		this.altoFrame = 0;
		this.duracionFrame = validarDuracion(duracionFrame);
		this.frames = List.copyOf(frames);
	}

	public Image getHoja() {
		return hoja;
	}

	/**
	 * Aplica la animación polimórficamente sobre el ImageView provisto,
	 * abstrayendo si la animación se basa en una hoja de sprites con viewport
	 * o en una secuencia discreta de imágenes.
	 *
	 * @param vista ImageView sobre el que se proyecta la animación.
	 * @param fila Fila del sprite sheet a animar (ignorado en secuencias de imágenes).
	 * @param activa Indica si el avance temporal de los frames está habilitado.
	 * @param dt Delta de tiempo transcurrido en segundos.
	 */
	public void aplicar(ImageView vista, int fila, boolean activa, double dt) {
		Objects.requireNonNull(vista, "El ImageView no puede ser nulo.");
		if (esSecuenciaImagenes()) {
			Image img = actualizarSecuencia(activa, dt);
			if (vista.getImage() != img) {
				vista.setImage(img);
			}
			if (vista.getViewport() != null) {
				vista.setViewport(null);
			}
		} else {
			if (hoja != null && vista.getImage() != hoja) {
				vista.setImage(hoja);
			}
			Rectangle2D vp = actualizar(fila, activa, dt);
			vista.setViewport(vp);
		}
	}

	/**
	 * Sobrecarga conveniente para animaciones sin filas (o secuencias de imágenes).
	 */
	public void aplicar(ImageView vista, boolean activa, double dt) {
		aplicar(vista, 0, activa, dt);
	}

	public Rectangle2D actualizar(int fila, boolean activa, double dt) {
		if (frames != null) {
			throw new IllegalStateException("Esta animación usa una secuencia de imágenes.");
		}
		if (fila < 0 || fila >= filas) {
			throw new IllegalArgumentException("La fila de animación está fuera de la hoja de sprites.");
		}
		if (dt < 0) {
			throw new IllegalArgumentException("El delta tiempo no puede ser negativo.");
		}

		if (!activa) {
			columnaActual = 0;
			tiempoAcumulado = 0;
		} else {
			if (fila != filaActual) {
				filaActual = fila;
				columnaActual = 0;
				tiempoAcumulado = 0;
			}
			tiempoAcumulado += dt;
			while (tiempoAcumulado >= duracionFrame) {
				tiempoAcumulado -= duracionFrame;
				columnaActual = (columnaActual + 1) % columnas;
			}
		}

		actualizarViewport();
		return viewport;
	}

	public Image actualizarSecuencia(boolean activa, double dt) {
		if (frames == null) {
			throw new IllegalStateException("Esta animación usa una hoja de sprites.");
		}
		if (!Double.isFinite(dt) || dt < 0) {
			throw new IllegalArgumentException("El delta tiempo debe ser finito y no negativo.");
		}

		if (!activa) {
			frameActual = 0;
			tiempoAcumulado = 0;
		} else {
			tiempoAcumulado += dt;
			while (tiempoAcumulado >= duracionFrame) {
				tiempoAcumulado -= duracionFrame;
				frameActual = (frameActual + 1) % frames.size();
			}
		}
		return getImagenActual();
	}

	public boolean esSecuenciaImagenes() {
		return frames != null;
	}

	public Image getImagenActual() {
		return frames == null ? null : frames.get(frameActual);
	}

	public Rectangle2D getViewport() {
		return viewport;
	}

	private void actualizarViewport() {
		if (columnaActual == columnaVisible && filaActual == filaVisible) { return; }
		viewport = new Rectangle2D(
			columnaActual * anchoFrame,
			filaActual * altoFrame,
			anchoFrame,
			altoFrame
		);
		columnaVisible = columnaActual;
		filaVisible = filaActual;
	}

	private static double validarDuracion(double duracionFrame) {
		if (!Double.isFinite(duracionFrame) || duracionFrame <= 0) {
			throw new IllegalArgumentException("La duración de cada frame debe ser positiva y finita.");
		}
		return duracionFrame;
	}
}
