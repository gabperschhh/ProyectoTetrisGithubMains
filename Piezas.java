public class Piezas{
    private int bloque;
    private String color;
    private String forma;
    public Piezas(int bloque, String color, String forma){
        this.bloque = bloque;
        this.color = color;
        this.forma = forma;
    }
    public int getBloque(){
        return bloque;
    }
    public String getColor(){
        return color;
    }
    public String getForma(){
        return forma;
    }
    public void setBloque(int bloque){
        this.bloque = bloque;
    }
    public void setColor(String color){
        this.color = color;
    }
    public void setForma(String forma){
        this.forma = forma;
    }
}
