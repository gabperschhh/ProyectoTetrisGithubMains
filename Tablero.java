/**
*Esta clase establece genera el tablero de juego, maneja las filas y determina el game over
*@author Benjamín Hernández, Julian Barrantes y Gabriel Pérez
*@Version 1.0
*/
public class Tablero {
    private int alto;
    private int ancho;
    private Bloque[][] celdas;
    private boolean[][] bloqueFijo;
    ConsoleColors c = new ConsoleColors();

    public Tablero(int alto, int ancho) {
        this.alto = alto;
        this.ancho = ancho;
        this.celdas = new Bloque[alto][ancho];
        this.bloqueFijo = new boolean[alto][ancho];
    }

    public int getAlto(){
        return alto;
    }

    public int getAncho(){
        return ancho;
    }

    public Bloque[][] getCeldas(){
        return celdas;
    }

    public boolean[][] getBloqueFijo(){
        return bloqueFijo;
    }

    public void inicializar() {
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                celdas[i][j] = null;
                bloqueFijo[i][j] = false;
            }
        }
    }

    public void imprimir(Pieza piezaActual) {
        for (int i = 0; i < alto; i++) {
            System.out.print("|");
            for (int j = 0; j < ancho; j++) {
                String color = null;
                String colorPieza = null;
                boolean esBloqueDePieza = false;
                if(celdas[i][j] != null){
                    color = celdas[i][j].getColor();
                }

                if (piezaActual != null) {
                    for (Bloque b : piezaActual.getBloques()) {
                        int[] c = b.getCoords();
                        colorPieza = b.getColor();
                        if(c[0] == j && c[1] == i){
                            esBloqueDePieza = true;
                            break;
                        }
                    }
                }

                if(esBloqueDePieza){
                    switch(colorPieza) {
                        case "azul" -> {
                            System.out.print(c.BLUE + "X" + c.RESET + " | ");
                        }
                        case "rojo" -> {
                            System.out.print(c.RED + "X" + c.RESET + " | ");
                        }
                        case "naranja" -> {
                            System.out.print("X | ");
                        }
                        case "verde" -> {
                            System.out.print(c.GREEN + "X" + c.RESET + " | ");
                        }
                        case "amarillo" -> {
                            System.out.print(c.YELLOW + "X" + c.RESET + " | ");
                        }
                        case "rosado" -> {
                            System.out.print(c.CYAN + "X" + c.RESET + " | ");
                        }
                        case "morado" -> {
                            System.out.print(c.PURPLE + "X" + c.RESET + " | ");
                        }
                    }
                } else if(celdas[i][j] == null) {
                    System.out.print("0 | ");
                } else if(celdas[i][j] != null){
                    switch(color) {
                        case "azul" -> {
                            System.out.print(c.BLUE + "1" + c.RESET + " | ");
                        }
                        case "rojo" -> {
                            System.out.print(c.RED + "1" + c.RESET + " | ");
                        }
                        case "naranja" -> {
                            System.out.print("1 | ");
                        }
                        case "verde" -> {
                            System.out.print(c.GREEN + "1" + c.RESET + " | ");
                        }
                        case "amarillo" -> {
                            System.out.print(c.YELLOW + "1" + c.RESET + " | ");
                        }
                        case "rosado" -> {
                            System.out.print(c.CYAN + "1" + c.RESET + " | ");
                        }
                        case "morado" -> {
                            System.out.print(c.PURPLE + "1" + c.RESET + " | ");
                        }
                    }
                }
                
            }
            System.out.println();
        }
    }

    public void fijarPieza(Pieza piezaActual) {
        for (Bloque b : piezaActual.getBloques()) {
            int[] c = b.getCoords();
            int x = c[0];
            int y = c[1];

            celdas[y][x] = b; 
            bloqueFijo[y][x] = true;
        }   
    }

    public boolean puedeMover(Pieza pieza, int dx, int dy) {
        if(pieza == null){
            return false;
        }
        for (Bloque b : pieza.getBloques()) {
            int[] coords = b.getCoords();
            int xA = coords[0];
            int yA = coords[1];

            int xN = xA + dx;
            int yN = yA + dy;


            if (xN < 0 || xN >= ancho) {
                return false;
            }else if (yN < 0 || yN >= alto) {
                return false;
            }else if (celdas[yN][xN] != null) {
                return false;
            }
        }

    return true;
    }

    public boolean tocoSuelo(Bloque bloque){
         if (bloque == null) {
            return false;
        }
        int[] coords = bloque.getCoords();
        int coordX = coords[0];
        int coordY = coords[1];
        if(coordY == 19 || celdas[coordY + 1][coordX] != null){
            return true;
        }
        return false;
    }

    public boolean filaLlena(int fila){
        for(int i = 0; i < ancho; i++){
            if(celdas[fila][i] == null){
                return false;
            } 
        }
        return true;
    }

    public void eliminarFila(int fila){
        for(int i = 0; i < ancho; i++){
            celdas[fila][i] = null;
        }
    }

    public void eliminarCuatroFilas(int[] filas) {
    if (filas == null) {
        return;
    }

    for (int x = 0; x < filas.length; x++) {
        int fila = filas[x];
        for (int j = 0; j < ancho; j++) {
            celdas[fila][j] = null;
        }
    }
}

    // devuelve un int[] con las 4 filas (de abajo hacia arriba)
    // si no hay nada que eliminar, devuelve null
    public int[] cuatroEnLinea() {
        for (int x = 0; x < ancho; x++) {
            for (int y = alto - 1; y >= 3; y--) {
                if (celdas[y][x] == null) {
                    continue;
                }

                String color = celdas[y][x].getColor();
                boolean sonCuatro = true;

                // revisamos los 3 bloques de arriba de este
                for (int k = 1; k < 4; k++) {
                    //si son null o si no se cumple el .equals rompe el ciclo y devuelve null
                    if (celdas[y - k][x] == null || !celdas[y - k][x].getColor().equals(color)) {
                        sonCuatro = false;
                        break;
                    }
                }

                if (sonCuatro) {
                    // devolvemos las filas de abajo hacia arriba
                    return new int[]{ y, y - 1, y - 2, y - 3 };
                }
            }
        }

        return null;
    }
    /**
    Metodo que hace que bajen los bloques que quedan flotando cuando se completa una linea
    *@param int x
    *@param int y
    *@param bloque[][] celdas
    *@param int alto
    *@param int nY
    *@param int[] coords
    *@param int[] newCoords
    */
    public void bajarBloques(int fila){
        for (int x = 0; x < ancho; x++) {
            for (int y = alto - 2; y >= 0; y--) {     // de abajo hacia arriba
                if (celdas[y][x] != null) {
                    int nY = y;
                    while (nY + 1 < alto && celdas[nY + 1][x] == null) {
                        nY++;
                    }
                    if (nY != y) {
                        celdas[nY][x] = celdas[y][x];
                        celdas[y][x] = null;

                        int[] coords = celdas[nY][x].getCoords();
                        int[] newCoords = new int[]{coords[0], nY};
                        celdas[nY][x].setCoords(newCoords);
                    }
                }
            }
        }
    }

    /* public void bajarFilasDesde(int fila){
        for(int f = fila; f > 0; f--){
            for(int j = 0; j < ancho; j ++){
                celdas[f][j] = celdas[f - 1][j];
            }
        }

        for (int c = 0; c < ancho; c++) {
            celdas[0][c] = null;
        }

    } */

    public int limpiarLineas(int fila) {
    if (fila < 0) {
        return 0; // caso base, ya no hay filas que revisar
    }

    int puntos = 0;

    if (filaLlena(fila)) {
        eliminarFila(fila);
        bajarBloques(fila);
        // vuelvo a revisar la misma fila, porque ahora tiene contenido nuevo (por ahora no funciona porq no caen xd)
        puntos = 1 + limpiarLineas(fila);
    } else {
        // fila no esta llena, sigo con la de arriba
        puntos = limpiarLineas(fila - 1);
    }

    return puntos;
    }

    public int limpiarLineas4(int[] filas) {
    // si filas es null, buscamos en cuatroEnLinea
    if (filas == null) {
        filas = cuatroEnLinea();
    }

    if (filas == null) {
        return 0; // caso base
    }

    int filaBase = filas[0];

    eliminarCuatroFilas(filas);

    bajarBloques(filaBase);

    return 1 + limpiarLineas4(null);
    }
       /**
    Metodo que da game over cuando una pieza toca el techo o lo sobrepasa
    *@param int x
    *@param int ancho
    *@param bloque[][] celdas
    *@return true si las celdas[0][x] no están vacias
    *@return false en caso contrario
    */
    public boolean hayGameOver(Pieza pieza){
        for (int x = 0; x < ancho; x++) {
        if (celdas[0][x] != null) {
            return true;
        }
    }
        return false;
    }
}

