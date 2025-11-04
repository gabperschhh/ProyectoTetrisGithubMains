public class Pieza{
    private Bloque bloque;
    private String color;
    private int[][] forma;
    public Pieza(Bloque bloque, String color, int[][] forma){
        this.bloque = bloque;
        this.color = color;
        this.forma = forma;
    }
    public Bloque getBloque(){
        return bloque;
    }
    public String getColor(){
        return color;
    }
    public int[][] getForma(){
        return forma;
    }
    public void setBloque(Bloque bloque){
        this.bloque = bloque;
    }
    public void setColor(String color){
        this.color = color;
    }
    public void setForma(int[][] forma){
        this.forma = forma;
    }
} 
