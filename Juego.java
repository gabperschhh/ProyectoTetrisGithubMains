import java.util.Scanner;
public class Juego{
    Scanner sc = new Scanner(System.in);
    Tablero t = new Tablero(20, 10); 
    Puntaje puntaje = new Puntaje();

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
        int[][] forma = p.generarForma();
        Bloque[] bloques = p.generarBloques(forma);
        return piezaActual = new Pieza(bloques, forma);
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

    public void moverPieza(String wasdUser){
        switch (wasdUser) {
            case "a"->{
                if(t.puedeMover(piezaActual, -1, 0) == true){
                    for(Bloque b : piezaActual.getBloques()){
                        int[] coords = b.getCoords();
                        int xAbs = coords[0] - 1;
                        int yAbs = coords[1];
                        b.setCoords(new int[]{xAbs, yAbs});
                    }
                }
            }
            case "d"->{
                if(t.puedeMover(piezaActual, 1, 0) == true){
                    for(Bloque b : piezaActual.getBloques()){
                        int[] coords = b.getCoords();
                        int xAbs = coords[0] + 1;
                        int yAbs = coords[1];
                        b.setCoords(new int[]{xAbs, yAbs});
                    }
                }
            }     
            case "s"->{
                if(t.puedeMover(piezaActual, 0, 1) == true){
                    for(Bloque b : piezaActual.getBloques()){
                        int[] coords = b.getCoords();
                        int xAbs = coords[0];
                        int yAbs = coords[1] + 1;
                        b.setCoords(new int[]{xAbs, yAbs});
                    }
                }
            }     
        }
    }

    public void rotarPieza(){ //setea el tercer bloque como el eje, le resta el bloque a mover, cambia el signo
    // de y y luego le suma el eje invertido (y suma con ejeX y x suma con ejeY), luego los pone al orden original
        
        Bloque[] bloques = piezaActual.getBloques();
        int[] coordsEje = bloques[3].getCoords();
        int ejeX = coordsEje[0];
        int ejeY = coordsEje[1];
        int[][] nuevasCoords = new int[bloques.length][2];

        for(int i = 0; i < bloques.length; i++){
            //calculamos las coords sin aplicar los cambios para manejar errores posteriormente
            Bloque b = bloques[i];

            if(i == 3){
                nuevasCoords[i][0] = ejeX;
                nuevasCoords[i][1] = ejeY;
            } else {
                int[] coords = b.getCoords();
                int xAbs = coords[0];
                int yAbs = coords[1];

                int xxAbs = ejeX - xAbs;
                int yyAbs = ejeY - yAbs;

                int yyyAbs = Math.negateExact(yyAbs);

                int xFinal = yyyAbs + ejeX;
                int yFinal = xxAbs + ejeY;

                nuevasCoords[i][0] = xFinal;
                nuevasCoords[i][1] = yFinal;
            }
        }
        for (int i = 0; i < nuevasCoords.length; i++) {
            int x = nuevasCoords[i][0];
            int y = nuevasCoords[i][1];

            // fuera de límites entonces cancelamos rotación
            if (x < 0 || x >= t.getAncho() || y < 0 || y >= t.getAlto()) {
                return; // no rota
            }

            // choca contra un bloque fijo del tablero entonces cancelamos rotación
            if (t.getCeldas()[y][x] != null) {
                return; // no rota
            }
        }

        // todo es valido entonces ahora si rota
        for (int i = 0; i < bloques.length; i++) {
            bloques[i].setCoords(new int[]{ nuevasCoords[i][0], nuevasCoords[i][1] });
        }
        
    }

    public void gravedad(){
        if (t.puedeMover(piezaActual, 0, 1) == true){
            for(Bloque b : piezaActual.getBloques()){
                int[] coords = b.getCoords();
                int xAbs = coords[0];
                int yAbs = coords[1] + 1;
                b.setCoords(new int[]{xAbs, yAbs});
            }
        }
    }

    public boolean tocoSuelo(){ 
        return !t.puedeMover(piezaActual, 0, 1);
    }

   
    public void bucleJuego(){
        boolean gameOver = false;
        try{
            t.inicializar();
            inicializarPieza();
            generarNuevaPieza();
            t.hayGameOver(piezaActual);
            while(t.hayGameOver(piezaActual) == false){
                t.imprimir(piezaActual);
                System.out.println("Puntuacion: " + puntaje.getPuntajeTotal());
                System.out.println("Combo: x" + puntaje.getCombo());
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
                    t.hayGameOver(piezaActual);  
                }
                t.limpiarLineas(t.getAlto() - 1);
                int lineasLimpias = t.limpiarLineas(t.getAlto() - 1);
                if (lineasLimpias > 0){
                    String colorPieza = piezaActual.getBloques()[0].getColor();
                    int puntosGanados = puntaje.procesarLineaLimpia(colorPieza, lineasLimpias);
                    System.out.println(puntosGanados + "puntos");
                }
            }
            System.out.println("Game Over");
            System.out.println("Puntuacion final: " + puntaje.getPuntajeTotal());
            sc.nextLine();
            
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}