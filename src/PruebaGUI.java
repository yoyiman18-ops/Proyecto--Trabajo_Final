import javax.swing.JOptionPane;


public class PruebaGUI {

    public static void probarMensaje() {

    String nombre = JOptionPane.showInputDialog("Ingrese un numero.");

    String mensaje = String.format("Su nombre es %s", nombre);

    JOptionPane.showMessageDialog(null,mensaje);
}
}
