public class Puntaje{
    private int puntajeTotal;
    private String colorDominante;
    private int combo;
    private String raizColor;
    private int raizFrecuencia;
    private int raizAltura;
    private Puntaje arbolIzquierdo;
    private Puntaje arbolDerecho;
    
    public puntaje(){
        this.puntajeTotal = 0;
        this.raiz = null;
        this.combo = 0;
        this.colorDominante = null;
        this.raizcolor = null;
        this.raizFrecuencia = 0;
        this.raizAltura = 0;
        this.arbolIzquierdo = null;
        this.arbolDerecho = null;
    }
    public void agregarColor(String color)
        if (raizColor == null){
            raizColor = color;
            raizFrecuencia = 1;
            raizAltura = 1;
            return;
        }
    public int buscarFrecuencia(String color){
        if (raizColor == null){
            return 0;
        }
    }
    public int calcularPuntajeBase(String color){
        int frecuencia = buscarFrecuencia(color);
    }
    public int LineaVertical(){
        int puntaje = 200; //el bonus que te dan por la linea vertical basicamente
        puntajeTotal += puntaje;
        return puntaje;
    }
    public int getFrecuenciaColor(String color){
        return buscarFrecuencia(color)
    }

    public int getPuntajeTotal(){
        return puntajeTotal;
    }
    public int getCombo(){
        return combo;
    }

}