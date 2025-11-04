import java.util.Scanner;
public class Juego{
    Scanner sc = new Scanner(System.in);
    Tablero t = new Tablero(10, 20);

    public String teclaUsuario(){
        String wasdUser = sc.next();
        return wasdUser;
    }
   
    public boolean bucleJuego(){
        boolean gameOver = false;
        try {
            while(!gameOver);{
                t.imprimir();

            }
        } catch (Exception e) {

        }
        return false;
    }
}