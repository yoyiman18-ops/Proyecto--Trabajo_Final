package controlador;

import motor.recursos.Extension;
import motor.recursos.GestorAudio;

public enum MusicaJuego {
    MENU("Soundtrack/blinkenlights", Extension.Sonido.MP3),
    PARTIDA("Soundtrack/Heat Of Battle", Extension.Sonido.MP3);

    private final String nombre;
    private final Extension.Sonido extension;

    MusicaJuego(String nombre, Extension.Sonido extension) {
        this.nombre = nombre;
        this.extension = extension;
    }

    public String getNombre() { return nombre; }
    public Extension.Sonido getExtension() { return extension; }

    public void reproducirEn(GestorAudio gestorAudio) {
        if (gestorAudio != null) {
            gestorAudio.reproducirMusica(nombre, extension);
        }
    }
}
