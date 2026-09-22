package motor.recursos;

public class Extension {

    public interface IExtension {
        public String getExtension();
    }

    public enum Imagen implements IExtension {
        PNG,JPG;
        @Override 
        public String getExtension() { return "." + (this.toString()).toLowerCase(); }
    }

    public enum Sonido implements IExtension {
        MP3,WAV,AAC,M4A,AIF;
        @Override 
        public String getExtension() { return "." + (this.toString()).toLowerCase(); }
    }
}
