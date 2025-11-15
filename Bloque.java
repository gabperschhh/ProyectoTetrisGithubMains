/**
*Esta clase establece el color y coordenadas del bloque para la pieza
*@author Benjamín Hernández, Julian Barrantes y Gabriel Pérez
*@Version 1.0
*/
public class Bloque{
    private int[] coords;
    private String color;
    private int x;
    private int y;

    public Bloque(int[] coords, String color, int x, int y){
        this.coords = new int[2];
        this.coords[0] = x;
        this.coords[1] = y;
        this.color = color;
    }

    public int[] getCoords(){
        return coords;
    }

    public String getColor(){
        return color;
    }

    public void setCoords(int[] nuevasCoords){
        this.coords = nuevasCoords;
    }

    public void setColor(String color){
        this.color = color;
    }
}