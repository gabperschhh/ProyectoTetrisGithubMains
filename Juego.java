import java.util.Scanner;
public class Juego{
    Scanner sc = new Scanner(System.in);
    Tablero t = new Tablero(20, 10);

    public static void limpiarPantalla() { 
        try {
            String terminal = "exec", opcion = "", comando = "clear";
            String OS = System.getProperty("os.name");
            if (OS.contains("Windows")) {
                terminal = "cmd";
                opcion = "/c";
                comando = "cls";
            }
            new ProcessBuilder(terminal, opcion, comando).inheritIO().start().waitFor();
        } catch (Exception ex) {
            // Si falla, hacer una limpieza básica
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private Pieza piezaActual;

    Pieza p = new Pieza(null, null);
    Bloque b = new Bloque(null, null, 0, 0);

    public Pieza inicializarPieza(){
        return piezaActual = new Pieza(p.generarBloques(), p.generarForma());
    }
    
    public String teclaUsuario(){
        String wasdUser = sc.next();
        return wasdUser;
    }

    public void generarNuevaPieza(){
        int spawnX = t.getAncho() / 2;
        int spawnY = 0; //fila superior

        for(Bloque b : piezaActual.getBloques()){
            int[] coords = b.getCoords();
            int xAbs = coords[0] + spawnX;
            int yAbs = coords[1] + spawnY;
            b.setCoords(new int[]{xAbs, yAbs});
        }
    } 

    public void moverPieza(){

    }

    public void rotarPieza(){

    }

    public void gravedad(){
        for(Bloque b : piezaActual.getBloques()){
            int[] coords = b.getCoords();
            int xAbs = coords[0];
            int yAbs = coords[1] + 1;
            b.setCoords(new int[]{xAbs, yAbs});
        }
    }

    public boolean tocoSuelo(){ 
        for(Bloque b : piezaActual.getBloques()){
            int[] coords = b.getCoords();
            int xAbs = coords[0];
            int yAbs = coords[1];
            if(yAbs == 19 || t.getCeldas()[yAbs + 1][xAbs] == 1){
                return true;
            }
        }
        return false;
    }

   
    public boolean bucleJuego(){
        boolean gameOver = false;
        try{
            t.inicializar();
            inicializarPieza();
            generarNuevaPieza();
            while(!gameOver){
                t.imprimir(piezaActual);
                gravedad();
                sc.nextLine();
                limpiarPantalla();
                if(tocoSuelo() == true){
                    t.fijarPieza(piezaActual);
                    inicializarPieza();
                    generarNuevaPieza();  
                }
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    /*public void rotarDerecha(){
        int n = forma.length;
        int[][] rotar = new int [n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++){
            rotar[j][n - 1 - i] = forma[i][j];
            }
        }   
        forma = rotar;
    }// por cualquier vara la gira en 90° no sé si tiene que ser menos

      public void rotarIzquierda(){
      int n = forma.length;
        int[][] rotar = new int [n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++){
            rotar[n - 1 - j][i] = forma[i][j];
            }
        }   
        forma = rotar;
      }
      //no sé si te sirva
    */
}