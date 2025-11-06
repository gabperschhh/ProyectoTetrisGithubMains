public class Pieza{
    private Bloque[] bloques;
    private int[][] forma;

    public Pieza(Bloque[] bloques,  int[][] forma){
        this.bloques = bloques;
        this.forma = forma;
    }

    public Bloque[] getBloques(){
        return bloques;
    }

    public int[][] getForma(){
        return forma;
    }

    public void setBloques(Bloque[] bloques){
        this.bloques = bloques;
    }


    public void setForma(int[][] forma){
        this.forma = forma;
    }

    public int generarRandom(){
        int randomNum = (int)(Math.random() * 7) + 1;
        return randomNum;
    }

    public int[][] generarForma(){
        int idPieza = generarRandom();
        int[][] p;

        switch (idPieza) {
            case 1 -> {
                p = new int[][] {{1}, {1}, {1}, {1}};
            }
            case 2 -> {
                p = new int[][] {{1 , 1}, {1 , 1}};
            }
            case 3 -> {
                p = new int[][] {{0, 1, 1}, {1, 1, 0}};
            }
            case 4 -> {
                p = new int[][] {{0, 1, 0}, {1, 1, 1}};
            }
            case 5 -> {
                p = new int[][] {{1, 1, 0}, {0, 1, 1}};
            }
            case 6 -> {
                p = new int[][] {{1, 0}, {1, 0}, {1, 1}};
            }
            case 7 -> {
                p = new int[][] {{0, 1}, {0, 1}, {1, 1}};
            }
            default -> {
                p = null;
            }
        }

        this.forma = p;   
        return p;
    }

    public String generarColor(){
        int idColor = generarRandom();
        switch (idColor) {
            case 1 -> {
                String azul = "azul";
                return azul;
            }
            case 2 -> {
                String rojo = "rojo";
                return rojo;
            }
            case 3 -> {
                String verde = "verde";
                return verde;
            }
            case 4 -> {
                String naranja = "naranja";
                return naranja;
            }
            case 5 -> {
                String amarillo = "amarillo";
                return amarillo;
            }
            case 6 -> {
                String rosado = "rosado";
                return rosado;
            }
            case 7 -> {
                String morado = "morado";
                return morado;
            }
            default -> {
                return null;
            }
        }
    }

    public Bloque[] generarBloques(){
        if (this.forma == null) {
            // por seguridad, genera la forma si aún no está
            generarForma();
        }
        int contador = 0; //cuenta cuantos "1" hay en la forma
        for (int i = 0; i < forma.length; i++) {           
            for (int j = 0; j < forma[i].length; j++) { 
                if (forma[i][j] == 1) {
                    contador++;
                }
            }
        }

        
        Bloque[] bloques = new Bloque[contador]; //crea el arreglo segun el contador
        int k = 0;

        
        for (int i = 0; i < forma.length; i++) { //asigna los bloques segun la forma
            for (int j = 0; j < forma[i].length; j++) {
                if (forma[i][j] == 1) {
                    int[] coords = {j, i}; 
                    bloques[k] = new Bloque(coords, generarColor(), coords[0], coords[1]);
                    k++;
                }
            }
        }

        this.bloques = bloques;
        return bloques;
    }   

    public void imprimirForma(){ // para tests
        int[][] pieza = generarForma();
        for(int i = 0; i < pieza.length; i++){
            for(int j = 0; j < pieza[i].length; j++){
                System.out.print(pieza[i][j]);
            }
            System.out.println();
        }
    }
} 
