package vista;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Animacion {
	private static final double DURACION_FRAME_DEFAULT = 0.12;

	private final int columnas;
	private final int filas;
	private final double anchoFrame;
	private final double altoFrame;
	private final double duracionFrame;
	private int columnaActual;
	private int filaActual;
	private int columnaVisible = -1;
	private int filaVisible = -1;
	private double tiempoAcumulado;
	private Rectangle2D viewport;

	public Animacion(Image hoja, int columnas, int filas) {
		this(hoja, columnas, filas, DURACION_FRAME_DEFAULT);
	}

	public Animacion(Image hoja, int columnas, int filas, double duracionFrame) {
		if (columnas <= 0 || filas <= 0 || hoja.getWidth() <= 0 || hoja.getHeight() <= 0) {
			throw new IllegalArgumentException("La hoja de sprites debe tener dimensiones positivas.");
		}
		if (duracionFrame <= 0) {
			throw new IllegalArgumentException("La duración de cada frame debe ser positiva.");
		}

		this.columnas = columnas;
		this.filas = filas;
		this.anchoFrame = hoja.getWidth() / columnas;
		this.altoFrame = hoja.getHeight() / filas;
		this.duracionFrame = duracionFrame;
		actualizarViewport();
	}

	public Rectangle2D actualizar(int fila, boolean activa, double dt) {
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
}
