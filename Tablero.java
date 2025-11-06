public class Tablero {
    private int alto;
    private int ancho;
    private int[][] celdas;

    public Tablero(int alto, int ancho) {
        this.alto = alto;
        this.ancho = ancho;
        this.celdas = new int[alto][ancho];
    }

    public int getAlto(){
        return alto;
    }

    public int getAncho(){
        return ancho;
    }

    public int[][] getCeldas(){
        return celdas;
    }

    public void inicializar() {
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                celdas[i][j] = 0;
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
                } else {
                    System.out.print(celdas[i][j] + " | ");
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

            celdas[y][x] = 1; 
        }   
    }

    public boolean filaLlena(int fila){
        for(int i = 0; i < ancho; i++){
            if(celdas[fila][i] == 0){
                return false;
            } 
        }
        return true;
    }

    public void eliminarFila(int fila){
        for(int i = 0; i < ancho; i++){
            celdas[fila][i] = 0;
        }
    }

    public void bajarFilasDesde(int fila){
        for(int f = fila; f > 0; f--){
            for(int j = 0; j < ancho; j ++){
                celdas[f][j] = celdas[f - 1][j];
            }
        }

        for (int c = 0; c < ancho; c++) {
        celdas[0][c] = 0;
        }

    }

    public int limpiarLineas(int fila) {
    if (fila < 0) {
        return 0; // caso base, ya no hay filas que revisar
    }

    int puntos = 0;

    if (filaLlena(fila)) {
        eliminarFila(fila);
        bajarFilasDesde(fila);
        // vuelvo a revisar la misma fila, porque ahora tiene contenido nuevo (por ahora no funciona porq no caen xd)
        puntos = 1 + limpiarLineas(fila);
    } else {
        // fila no esta llena, sigo con la de arriba
        puntos = limpiarLineas(fila - 1);
    }

    return puntos;
    }

}
