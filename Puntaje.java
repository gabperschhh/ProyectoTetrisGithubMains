/**
*Genera la puntuación del juego tetris basado en la frecuencia de los colores
*@author Benjamin Hernández, Julian Barrantes y Gabriel perez
*@Version 1.0
*/
public class Puntaje{
    private int puntajeTotal;
    private String ultimoColorDominante;
    private int combo;
    private String raizColor;
    private int raizFrecuencia;
    private int raizAltura;
    private Puntaje arbolIzquierdo;
    private Puntaje arbolDerecho;
    
    public Puntaje(){
        this.puntajeTotal = 0;
        this.combo = 0;
        this.ultimoColorDominante = null;
        this.raizColor = null;
        this.raizFrecuencia = 0;
        this.raizAltura = 0;
        this.arbolIzquierdo = null;
        this.arbolDerecho = null;
    }
    /**
    *pone el Puntaje en un nodo del árbol con ese color, y si ya hay lo compara con el nuevo y si es el mismo sube la frecuencia 
    *@param String color
    *@param String raizColor
    *@param int raizFrecuencia
    *@param int raizAltura
    *@param int comparacion
    *@param Puntaje arbolIzquierdo
    *@param Puntaje arbolDerecho
    */
    public void agregarColor(String color){
        if (color == null) return;
        if (this == arbolIzquierdo || this == arbolDerecho){
            System.err.println("Error: dayum");
            return;
        }
        if (raizColor == null){
            raizColor = color;
            raizFrecuencia = 1;
            raizAltura = 1;
            return;
        }
        int comparacion = color.compareTo(raizColor);
        if (comparacion == 0){
            raizFrecuencia++;
        }
        else if (comparacion < 0){
            if (arbolIzquierdo == null){
                arbolIzquierdo = new Puntaje();
            }
            if (arbolIzquierdo != this){
            arbolIzquierdo.agregarColor(color);
            }
        }
        else{
            if (arbolDerecho == null){
                arbolDerecho = new Puntaje();
            }
            if(arbolDerecho != this){
            arbolDerecho.agregarColor(color);
            }
        }
        actualizarAltura();
        Balancear();
    }
    /**
    *Busca el color que tenga más frecuencia, que haya aparecido más 
    *@param String color 
    *@param String raizColor
    *@param int comparacion 
    *@param int raizFrecuencia 
    *@param Puntaje arbolIzquierdo
    *@param Puntaje arbolDerecho
    *@return 0
    */
    public int buscarFrecuencia(String color){
        if (raizColor == null){
            return 0;
        }
        int comparacion = color.compareTo(raizColor);
        if (comparacion == 0){
            return raizFrecuencia;
        }
        else if (comparacion < 0){
            if (arbolIzquierdo != null){
                return arbolIzquierdo.buscarFrecuencia(color);
            }
        }
        else{
            if (arbolDerecho != null){
                return arbolDerecho.buscarFrecuencia(color);
            }
        }
        return 0;
    }
    /**
    *Es el metodo que actualiza la altura del nodo en el arbol AVL
    *@param int alturaIzq
    *@param Puntaje arbolIzquierdo
    *@param int alturaDer
    *@param Puntaje arbolDerecho
    *@param int mayorAltura
    *@param int raizAltura
    */
    private void actualizarAltura(){
        int alturaIzq;
        if (arbolIzquierdo == null){
            alturaIzq = 0;
        }
        else{
            alturaIzq = arbolIzquierdo.raizAltura;
        }
        int alturaDer;
        if (arbolDerecho == null){
            alturaDer = 0;
        }
        else{
            alturaDer = arbolDerecho.raizAltura;
        }
        int mayorAltura;
        if (alturaIzq > alturaDer){
            mayorAltura = alturaIzq;
        }
        else{
            mayorAltura = alturaDer;
        }
        raizAltura = 1 + mayorAltura;
    }
    
    private int getFactorBalance(){
        if (this.raizColor == null){
            return 0;
        }
        int alturaIzq;
        if (arbolIzquierdo == null){
            alturaIzq = 0;
        }
        else{
            alturaIzq = arbolIzquierdo.raizAltura;
        }
        int alturaDer;
        if (arbolDerecho == null){
            alturaDer = 0;
        }
        else{
            alturaDer = arbolDerecho.raizAltura;
        }
        return alturaIzq - alturaDer;
    }

    private void Balancear(){
        int balance = getFactorBalance();
        if (balance > 1){
            if (arbolIzquierdo != null){
                 int balanceIzquierdo = arbolIzquierdo.getFactorBalance();
            }
            else{
                arbolIzquierdo.rotarIzquierda();
                rotarDerecha();
            }
        }
        if (balance < -1){
            if (arbolDerecho != null){
                int balanceDerecho = arbolDerecho.getFactorBalance();
                if (balanceDerecho <= 0){
                    rotarIzquierda();
                }
                else{
                    arbolDerecho.rotarDerecha();
                    rotarIzquierda();
                }
            }
        }
    }
    private void rotarDerecha(){
        if (arbolIzquierdo == null) return;
        Puntaje nuevaRaiz = arbolIzquierdo;
        Puntaje temp = nuevaRaiz.arbolDerecho;
        String tempColor = this.raizColor;
        int tempFrecuencia = this.raizFrecuencia;
        this.raizColor = nuevaRaiz.raizColor;
        this.raizFrecuencia = nuevaRaiz.raizFrecuencia;
        nuevaRaiz.raizColor = tempColor;
        nuevaRaiz.raizFrecuencia = tempFrecuencia;
        this.arbolIzquierdo = nuevaRaiz.arbolIzquierdo;
        nuevaRaiz.arbolIzquierdo = nuevaRaiz.arbolDerecho;
        nuevaRaiz.arbolDerecho = this.arbolDerecho;
        this.arbolDerecho = nuevaRaiz;
        if (this.arbolIzquierdo != null) this.arbolIzquierdo.actualizarAltura();
        if (this.arbolDerecho != null) this.arbolDerecho.actualizarAltura();
        this.actualizarAltura();
    }

    private void rotarIzquierda(){
        if (arbolDerecho == null) return;
        Puntaje nuevaRaiz = arbolDerecho;
        Puntaje temp = nuevaRaiz.arbolIzquierdo;
        String tempColor = this.raizColor;
        int tempFrecuencia = this.raizFrecuencia;
        this.raizColor = nuevaRaiz.raizColor;
        this.raizFrecuencia = nuevaRaiz.raizFrecuencia;
        nuevaRaiz.raizColor = tempColor;
        nuevaRaiz.raizFrecuencia = tempFrecuencia;
        this.arbolDerecho = nuevaRaiz.arbolDerecho;
        nuevaRaiz.arbolDerecho = nuevaRaiz.arbolIzquierdo;
        nuevaRaiz.arbolIzquierdo = this.arbolIzquierdo;
        this.arbolIzquierdo = nuevaRaiz;
        if (this.arbolIzquierdo != null) this.arbolIzquierdo.actualizarAltura();
        if (this.arbolDerecho != null) this.arbolDerecho.actualizarAltura();
        this.actualizarAltura();
    }
    /**
    *Calcula el puntaje de cada color en base a su frecuencia 
    *@param String Color
    *@param int frecuencia 
    *@param int puntajeBase
    *@return puntajeBase
    */
    private int calcularPuntajeBase(String color){
        int frecuencia = buscarFrecuencia(color);
        int puntajeBase = 100 - (frecuencia * 10);

        if (puntajeBase < 10){
            return 10;
        }
        else{
            return puntajeBase;
        }//para que si un color aparece muchas veces, el minimo sea 10 y no 0
    }
    public int procesarLineaLimpia(String colorDominante, int numeroLineas){
        agregarColor(colorDominante);
        int puntajeBase =calcularPuntajeBase(colorDominante) * numeroLineas;
        if (colorDominante.equals(ultimoColorDominante)){
            combo++;
            puntajeBase = puntajeBase * combo;
        }
        else{
            combo = 1;
            ultimoColorDominante = colorDominante;
        }
        if (numeroLineas > 1){
            puntajeBase += (numeroLineas - 1) * 50;
        } //este es el bonus que se da por hacer varias lineas
        puntajeTotal += puntajeBase;
        return puntajeBase;
    }

    public int LineaVertical(){
        int puntaje = 200; //el bonus que te dan por la linea vertical basicamente
        puntajeTotal += puntaje;
        return puntaje;
    }

    public void reiniciarCombo(){
        combo = 0;
        ultimoColorDominante = null;
    }

    public int getFrecuenciaColor(String color){
        return buscarFrecuencia(color);
    }

    public int getPuntajeTotal(){
        return puntajeTotal;
    }
    public int getCombo(){
        return combo;
    }

}