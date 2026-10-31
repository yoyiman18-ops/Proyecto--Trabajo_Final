package controlador;

import motor.recursos.Extension;
import motor.recursos.GestorAudio;

public enum SonidoJuego {
    DISPARO("Sound effects/Shoot/Default", Extension.Sonido.WAV),
    DISPARO_CRITICO("Sound effects/Shoot/Critico", Extension.Sonido.WAV),
    DAÑO_ENEMIGO("Sound effects/Hit/Hit41", Extension.Sonido.WAV),
    EXPIRAR_ENEMIGO("Sound effects/Hit/WAV/Hit 6 - Sound effects Pack 2", Extension.Sonido.WAV),
    DAÑO_JUGADOR("Sound effects/Hit/WAV/Hit 1 - Sound effects Pack 2", Extension.Sonido.WAV),
    MUERTE_JUGADOR("Sound effects/Lose/WAV/Lose 3 - Sound effects Pack 2", Extension.Sonido.WAV),
    EXPLOSION("Sound effects/Explosions/WAV/Explosion 7 - Sound effects Pack 2", Extension.Sonido.WAV),
    RECOGER_EXPERIENCIA("Sound effects/Coins/WAV/Coins 1 - Sound effects Pack 2", Extension.Sonido.WAV),
    SUBIR_NIVEL("Sound effects/1up/WAV/1up 2 - Sound effects Pack 2", Extension.Sonido.WAV),
    MOSTRAR_MEJORAS("Sound effects/Power-up/PowerUp", Extension.Sonido.WAV),
    MEJORA_APLICADA("Sound effects/Power-up/PowerUp9", Extension.Sonido.WAV),
    VICTORIA("Sound effects/Power-up/WAV/Powerup 1 - Sound effects Pack 2", Extension.Sonido.WAV),
    DERROTA("Sound effects/Lose/WAV/Lose 1 - Sound effects Pack 2", Extension.Sonido.WAV),
    BOTON("Sound effects/Blip/WAV/Blip 1 - Sound effects Pack 2", Extension.Sonido.WAV);

    private final String nombre;
    private final Extension.Sonido extension;

    SonidoJuego(String nombre, Extension.Sonido extension) {
        this.nombre = nombre;
        this.extension = extension;
    }

    public String getNombre() { return nombre; }
    public Extension.Sonido getExtension() { return extension; }

    public void reproducirEn(GestorAudio gestorAudio) {
        if (gestorAudio != null) {
            gestorAudio.reproducirSonido(nombre, extension);
        }
    }

    public static void precargarTodos(GestorAudio gestorAudio) {
        if (gestorAudio == null) {
            return;
        }
        for (SonidoJuego sonido : values()) {
            gestorAudio.precargarSonido(sonido.nombre, sonido.extension);
        }
    }
}
