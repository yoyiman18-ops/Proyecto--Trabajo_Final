package controlador;

import motor.recursos.Extension;
import motor.recursos.GestorAudio;

  public enum MusicaJuego {
        MENU(Extension.Sonido.MP3, "Soundtrack/blinkenlights"),
        PARTIDA(Extension.Sonido.MP3,
            "Soundtrack/Heat Of Battle",
            "Soundtrack/Battle",
            "Soundtrack/cannontube",
            "Soundtrack/Chamber",
            "Soundtrack/Death-To-All-Who-Groove",
            "Soundtrack/RoadTrip"
        );

        private final Extension.Sonido extension;
        private final String[] pistas;

        MusicaJuego(Extension.Sonido extension, String... pistas) {
            this.extension = extension;
            this.pistas = pistas;
        }

        public void reproducirEn(GestorAudio gestorAudio) {
            if (gestorAudio != null && pistas.length > 0) {
                int indice = (int) (Math.random() * pistas.length);
                gestorAudio.reproducirMusica(pistas[indice], extension);
            }
        }
    }
