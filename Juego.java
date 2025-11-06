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
        String wasdUser = null;
        wasdUser = sc.nextLine();
        return wasdUser;
    }

    public boolean usuarioToco(String wasdUser){
        if(wasdUser.equals("a") || wasdUser.equals("s") || wasdUser.equals("d")){
            return true;
        }
        return false;
    }

    public boolean usuarioRoto(String wasdUser){
        if(wasdUser.equals("r")){
            return true;
        }
        return false;
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

    /* public void coordenadasBloques(){ //para tests
        for(Bloque b : piezaActual.generarBloques()){
            int[] coords = b.getCoords();
            int xAbs = coords[0];
            int yAbs = coords[1];
            System.out.println(xAbs + " " + yAbs);
        }
    } */

    public void moverPieza(String wasdUser){
        switch (wasdUser) {
            case "a"->{
                for(Bloque b : piezaActual.getBloques()){
                    int[] coords = b.getCoords();
                    int xAbs = coords[0] - 1;
                    int yAbs = coords[1];
                    b.setCoords(new int[]{xAbs, yAbs});
                }
            }
            case "d"->{
                for(Bloque b : piezaActual.getBloques()){
                    int[] coords = b.getCoords();
                    int xAbs = coords[0] + 1;
                    int yAbs = coords[1];
                    b.setCoords(new int[]{xAbs, yAbs});
                }
            }     
            case "s"->{
                for(Bloque b : piezaActual.getBloques()){
                    int[] coords = b.getCoords();
                    int xAbs = coords[0];
                    int yAbs = coords[1] + 1;
                    b.setCoords(new int[]{xAbs, yAbs});
                }
            }     
        }
    }

    public void rotarPieza(){ //setea el tercer bloque como el eje, le resta el bloque a mover, cambia el signo
    // de y y luego le suma el eje invertido (y suma con ejeX y x suma con ejeY), luego los pone al orden original
        for(Bloque b : piezaActual.getBloques()){
                int[] coordsEje = piezaActual.getBloques()[3].getCoords();
                int ejeX = coordsEje[0];
                int ejeY = coordsEje[1];
                if(b != piezaActual.getBloques()[3]){
                    int[] coords = b.getCoords();
                    int xAbs = coords[0];
                    int yAbs = coords[1];

                    int xxAbs = ejeX - xAbs;
                    int yyAbs = ejeY - yAbs;

                    int yyyAbs = Math.negateExact(yyAbs);

                    int xFinal = yyyAbs + ejeX;
                    int yFinal = xxAbs + ejeY;

                    b.setCoords(new int[]{xFinal, yFinal});
                }
        }
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
                String wasdUser = teclaUsuario();
                usuarioToco(wasdUser);
                if(usuarioToco(wasdUser) == true){
                    moverPieza(wasdUser);
                }
                if(usuarioRoto(wasdUser) == true){
                    rotarPieza();
                }
                gravedad();
                limpiarPantalla();
                if(tocoSuelo() == true){
                    t.fijarPieza(piezaActual);
                    inicializarPieza();
                    generarNuevaPieza();  
                }
                t.limpiarLineas(t.getAlto() - 1);
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}