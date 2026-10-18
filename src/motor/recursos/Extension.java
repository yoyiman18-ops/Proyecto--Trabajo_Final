package motor.recursos;

public class Extension {

    public interface IExtension {
        public String getExtension();
        public boolean coincide(String ext);
    }

    public enum Imagen implements IExtension {
        PNG,JPG;

        @Override 
        public String getExtension() { return "." + (this.toString()).toLowerCase(); }

        @Override
        public boolean coincide(String ext) {
            if (ext == null || ext.isBlank()) {
                return false;
            }
            String clean = ext.trim().toLowerCase();
            if (clean.startsWith(".")) {
                clean = clean.substring(1);
            }
            return this.name().equalsIgnoreCase(clean);
        }

        public static Imagen fromString(String ext) {
            if (ext == null || ext.isBlank()) {
                throw new IllegalArgumentException("Extension no puede ser nula ni vacia");
            }
            for (Imagen img : Imagen.values()) {
                if (img.coincide(ext)) {
                    return img;
                }
            }
            throw new IllegalArgumentException("No se encontro Extension.Imagen para: " + ext);
        }
    }

    public enum Sonido implements IExtension {
        MP3,WAV,AAC,M4A,AIF;

        @Override 
        public String getExtension() { return "." + (this.toString()).toLowerCase(); }

        @Override
        public boolean coincide(String ext) {
            if (ext == null || ext.isBlank()) {
                return false;
            }
            String clean = ext.trim().toLowerCase();
            if (clean.startsWith(".")) {
                clean = clean.substring(1);
            }
            return this.name().equalsIgnoreCase(clean);
        }

        public static Sonido fromString(String ext) {
            if (ext == null || ext.isBlank()) {
                throw new IllegalArgumentException("Extension no puede ser nula ni vacia");
            }
            for (Sonido snd : Sonido.values()) {
                if (snd.coincide(ext)) {
                    return snd;
                }
            }
            throw new IllegalArgumentException("No se encontro Extension.Sonido para: " + ext);
        }
    }

    public static Imagen fromString(String ext) {
        return Imagen.fromString(ext);
    }
}
