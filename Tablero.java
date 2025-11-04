public class Tablero {
    private int ancho;
    private int alto;
    private int[][] celdas;

    public Tablero(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.celdas = new int[alto][ancho];
    }

    public void inicializar() {
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                celdas[i][j] = 0;
            }
        }
    }

    public void imprimir() {
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                System.out.print(celdas[i][j] + " ");
            }
            System.out.println();
        }
    }
}
