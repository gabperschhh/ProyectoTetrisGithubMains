import javax.swing.JFrame;

public class Pantalla extends JFrame{
    public Pantalla(){
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PanelJuego panel = new PanelJuego();
        add(panel);
        pack();              // ajusta al tamaño preferido del panel
        setLocationRelativeTo(null); // centrar
        setResizable(true);
        setVisible(true);
    }

     public static void main(String[] args) {
        new Pantalla();
    }
}