public class Tablero{
    private int ancho;
    private int alto;
    public Tablero(int ancho, int alto){
        this.ancho = ancho;
        this.alto = alto;
    }
    public int getAncho(){
        return ancho;
    }
    public String getAlto(){
        return alto;
    }
    public void setAncho(int ancho){
        this.ancho = ancho;
    }
    public void setAlto(int alto){
        this.alto = alto;
    }
}