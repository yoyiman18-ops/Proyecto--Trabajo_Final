package motor.cache;

public class Extension {

    public interface IExtension {
        public String getExtension();
     }

    public enum Imagen implements IExtension {
        PNG,JPG;
        @Override 
        public String getExtension() { return "." + (this.toString()).toLowerCase(); }
    }
}
