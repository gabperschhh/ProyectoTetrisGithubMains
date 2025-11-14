public class Tablero {
    private int alto;
    private int ancho;
    private Bloque[][] celdas;
    private boolean[][] bloqueFijo;

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
                boolean esBloqueDePieza = false;

                if (piezaActual != null) {
                    for (Bloque b : piezaActual.getBloques()) {
                        int[] c = b.getCoords();
                        if(c[0] == j && c[1] == i){
                            esBloqueDePieza = true;
                            break;
                        }
                    }
                }

                if(esBloqueDePieza){
                    System.out.print("X | ");
                } else if(celdas[i][j] == null) {
                    
                    System.out.print("0 | ");
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

    public boolean hayGameOver(Pieza pieza){
        for (int x = 0; x < ancho; x++) {
        if (celdas[0][x] != null) {
            return true;
        }
    }
        return false;
    }
}

