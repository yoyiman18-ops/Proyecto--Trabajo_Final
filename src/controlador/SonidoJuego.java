package controlador;

import motor.recursos.Extension;
import motor.recursos.GestorAudio;

public enum SonidoJuego {
    DISPARO(1, "Sound effects/Shoot/Default", Extension.Sonido.WAV),
    DISPARO_CRITICO(2, "Sound effects/Shoot/Critico", Extension.Sonido.WAV),
    DAÑO_ENEMIGO(3, "Sound effects/Hit/wav/Hit 1 - Sound effects Pack 2", Extension.Sonido.WAV),
    EXPIRAR_ENEMIGO(4, "Sound effects/Hit/WAV/Hit 6 - Sound effects Pack 2", Extension.Sonido.WAV),
    DAÑO_JUGADOR(5, "Sound effects/Hit/Hit41 - Sound effects Pack 2", Extension.Sonido.WAV),
    MUERTE_JUGADOR(6, "Sound effects/Lose/WAV/Lose 3 - Sound effects Pack 2", Extension.Sonido.WAV),
    EXPLOSION(7, "Sound effects/Explosions/WAV/Explosion 7 - Sound effects Pack 2", Extension.Sonido.WAV),
    RECOGER_EXPERIENCIA(8, "Sound effects/Coins/WAV/Coins 1 - Sound effects Pack 2", Extension.Sonido.WAV),
    SUBIR_NIVEL(9, "Sound effects/1up/WAV/1up 2 - Sound effects Pack 2", Extension.Sonido.WAV),
    MOSTRAR_MEJORAS(10, "Sound effects/Power-up/PowerUp", Extension.Sonido.WAV),
    MEJORA_APLICADA(11, "Sound effects/Power-up/PowerUp9", Extension.Sonido.WAV),
    VICTORIA(12, "Sound effects/Power-up/WAV/Powerup 1 - Sound effects Pack 2", Extension.Sonido.WAV),
    DERROTA(13, "Sound effects/Lose/WAV/Lose 2 - Sound effects Pack 2", Extension.Sonido.WAV),
    BOTON(14, "Sound effects/Blip/WAV/Blip 1 - Sound effects Pack 2", Extension.Sonido.WAV);

    private final int id;
    private final String nombre;
    private final Extension.Sonido extension;

    SonidoJuego(int id, String nombre, Extension.Sonido extension) {
        this.id = id;
        this.nombre = nombre;
        this.extension = extension;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Extension.Sonido getExtension() { return extension; }

    public void reproducirEn(GestorAudio gestorAudio) {
        if (gestorAudio != null) {
            gestorAudio.reproducirSonido(this.id);
        }
    }

    public static void precargarTodos(GestorAudio gestorAudio) {
        if (gestorAudio == null) {
            return;
        }
        for (SonidoJuego sonido : values()) {
            gestorAudio.registrarSonido(sonido.id, sonido.nombre, sonido.extension);
        }
    }
}
