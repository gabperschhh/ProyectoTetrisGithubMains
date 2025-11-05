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
}
