import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PanelJuego extends JPanel implements ActionListener, KeyListener {

    private Tablero t;
    private Pieza piezaActual;
    private Pieza generador;
    private Timer timer;

    Color customPink = new Color(251, 116, 168);
    Color customPurple = new Color(82, 14, 125);

    private final int pixelCelda = 30;
    private Puntaje puntaje;
    private int opcionMenu = 0;
    private int faseJuego = 1;

    public PanelJuego(){
        t = new Tablero(20,10);

        puntaje = new Puntaje();

        generador = new Pieza(null, null);

        
        
        setPreferredSize(new Dimension(t.getAlto() * pixelCelda, t.getAlto() * pixelCelda));
        setBackground(Color.BLACK);

        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(500, this);
        timer.start();

        inicializarPieza();
        generarNuevaPieza();


    }

    public Pieza inicializarPieza(){
        int[][] forma = generador.generarForma();
        Bloque[] bloques = generador.generarBloques(forma);
        return piezaActual = new Pieza(bloques, forma);
    }

    public void generarNuevaPieza(){
        int spawnX = t.getAncho() / 2;
        int spawnY = 0; //fila superior

        for(Bloque b : piezaActual.getBloques()){
            int[] coords = b.getCoords();
            int xAbs = coords[0] + spawnX;
            int yAbs = coords[1] + spawnY;
            b.setCoords(new int[]{xAbs, yAbs});
        }
    } 

    public void gravedad(){
        if(piezaActual == null){
            return;
        }
        if (t.puedeMover(piezaActual, 0, 1) == true){
            for(Bloque b : piezaActual.getBloques()){
                int[] coords = b.getCoords();
                int xAbs = coords[0];
                int yAbs = coords[1] + 1;
                b.setCoords(new int[]{xAbs, yAbs});
            }
        }
    }

    public boolean tocoSuelo(){ 
        return !t.puedeMover(piezaActual, 0, 1);
    }

    public void moverPieza(String wasdUser){
        switch (wasdUser) {
            case "a"->{
                if(t.puedeMover(piezaActual, -1, 0) == true){
                    for(Bloque b : piezaActual.getBloques()){
                        int[] coords = b.getCoords();
                        int xAbs = coords[0] - 1;
                        int yAbs = coords[1];
                        b.setCoords(new int[]{xAbs, yAbs});
                    }
                }
            }
            case "d"->{
                if(t.puedeMover(piezaActual, 1, 0) == true){
                    for(Bloque b : piezaActual.getBloques()){
                        int[] coords = b.getCoords();
                        int xAbs = coords[0] + 1;
                        int yAbs = coords[1];
                        b.setCoords(new int[]{xAbs, yAbs});
                    }
                }
            }     
            case "s"->{
                if(t.puedeMover(piezaActual, 0, 1) == true){
                    for(Bloque b : piezaActual.getBloques()){
                        int[] coords = b.getCoords();
                        int xAbs = coords[0];
                        int yAbs = coords[1] + 1;
                        b.setCoords(new int[]{xAbs, yAbs});
                    }
                }
            }     
        }
    }

    public void rotarPieza(){ //setea el tercer bloque como el eje, le resta el bloque a mover, cambia el signo
    // de y y luego le suma el eje invertido (y suma con ejeX y x suma con ejeY), luego los pone al orden original
        
        Bloque[] bloques = piezaActual.getBloques();
        int[] coordsEje = bloques[3].getCoords();
        int ejeX = coordsEje[0];
        int ejeY = coordsEje[1];
        int[][] nuevasCoords = new int[bloques.length][2];

        for(int i = 0; i < bloques.length; i++){
            //calculamos las coords sin aplicar los cambios para manejar errores posteriormente
            Bloque b = bloques[i];

            if(i == 3){
                nuevasCoords[i][0] = ejeX;
                nuevasCoords[i][1] = ejeY;
            } else {
                int[] coords = b.getCoords();
                int xAbs = coords[0];
                int yAbs = coords[1];

                int xxAbs = ejeX - xAbs;
                int yyAbs = ejeY - yAbs;

                int yyyAbs = Math.negateExact(yyAbs);

                int xFinal = yyyAbs + ejeX;
                int yFinal = xxAbs + ejeY;

                nuevasCoords[i][0] = xFinal;
                nuevasCoords[i][1] = yFinal;
            }
        }
        for (int i = 0; i < nuevasCoords.length; i++) {
            int x = nuevasCoords[i][0];
            int y = nuevasCoords[i][1];

            // fuera de límites entonces cancelamos rotación
            if (x < 0 || x >= t.getAncho() || y < 0 || y >= t.getAlto()) {
                return; // no rota
            }

            // choca contra un bloque fijo del tablero entonces cancelamos rotación
            if (t.getCeldas()[y][x] != null) {
                return; // no rota
            }
        }

        // todo es valido entonces ahora si rota
        for (int i = 0; i < bloques.length; i++) {
            bloques[i].setCoords(new int[]{ nuevasCoords[i][0], nuevasCoords[i][1] });
        }
        
    }

    public String[] menu(String direccion){
        int[] opciones = {0 , 1};
        int indice = opciones[0];
        if(direccion.equals("up")){
            indice = opciones[0];
            String texto1 = "- Play Again";
            String texto2 = "Exit";
            return new String[]{texto1, texto2};
        } else if (direccion.equals("down")){
            indice = opciones[1];
            String texto1 = "Play Again";
            String texto2 = "- Exit";
            return new String[]{texto1, texto2};
        }
        return null;
    }

    public void reiniciarJuego() {
        //reinicia todo
        faseJuego = 2;

        puntaje = new Puntaje();

        t.inicializar();   // limpia celdas

        piezaActual = inicializarPieza();

        timer.restart();

        repaint();
    }

    public void inicioJuego(){
        faseJuego = 2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (faseJuego != 2) {
            return; 
        }

        gravedad();
        if (tocoSuelo()) {
            t.fijarPieza(piezaActual);
            int lineasLimpias = t.limpiarLineas(t.getAlto() - 1);
            int puntosVerticales = t.limpiarLineas4(null);

            if (lineasLimpias > 0) {
                // uso el color de la pieza actual como dominante
                String colorPieza = piezaActual.getBloques()[0].getColor();
                int puntosGanados = puntaje.procesarLineaLimpia(colorPieza, lineasLimpias);
                System.out.println("Ganaste " + puntosGanados + " puntos. Total: " + puntaje.getPuntajeTotal());
            }
            
            if (t.hayGameOver(piezaActual)) {
            faseJuego = 3;
            timer.stop();     // detiene la caída automática
            }
            
            inicializarPieza();
            generarNuevaPieza();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        if(faseJuego == 1){
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            String textoTitulo = "TETRIS 1.0";
            g2.setColor(Color.MAGENTA);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 100f));

            FontMetrics fmTitulo = g2.getFontMetrics();
            int textWidthTitulo = fmTitulo.stringWidth(textoTitulo);
            int textHeightTitulo = fmTitulo.getAscent();

            int xTitulo = (getWidth() - textWidthTitulo) / 2;
            int yTitulo = (getHeight() + textHeightTitulo) / 2;

            g2.drawString(textoTitulo, xTitulo, yTitulo - 120);

            String textoTitulo1 = "CONTROLES:";
            g2.setColor(Color.CYAN);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));

            g2.drawString(textoTitulo1, xTitulo, yTitulo - 20);

            String textoTitulo2 = "W / Flecha arr. -> Rotar";
            g2.setColor(Color.BLUE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));

            g2.drawString(textoTitulo2, xTitulo, yTitulo);

            String textoTitulo3 = "A / Flecha izq. -> Mover izquierda";
            g2.setColor(Color.GREEN);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));

            g2.drawString(textoTitulo3, xTitulo, yTitulo + 20);

            String textoTitulo4 = "S / Flecha aba. -> Bajar más rapido";
            g2.setColor(Color.RED);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));

            g2.drawString(textoTitulo4, xTitulo, yTitulo + 40);

            String textoTitulo5 = "D / Flecha der. -> Mover derecha";
            g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));

            g2.drawString(textoTitulo5, xTitulo, yTitulo + 60);

            String textoTitulo6 = "Presiona enter para iniciar...";
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));

            g2.drawString(textoTitulo6, xTitulo, yTitulo + 120);

            String textoStudio = "By: GithubMains Games";
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 10f));

            g2.drawString(textoStudio, 10 , getHeight() - 10);

        } 

        Bloque[][] celdas = t.getCeldas();
        int tableroAnchoPx = t.getAncho() * pixelCelda; 
        int tableroAltoPx  = t.getAlto() * pixelCelda;  

        int offsetX = (getWidth()  - tableroAnchoPx) / 2;
        int offsetY = (getHeight() - tableroAltoPx)  / 2;

        if(faseJuego == 2){
            // dibujar celdas fijas
            for (int i = 0; i < t.getAlto(); i++) {
                for (int j = 0; j < t.getAncho(); j++) {
                    if (celdas[i][j] != null) {
                        
                        String color = celdas[i][j].getColor();
                        switch (color) {
                        case "azul" ->{
                            g.setColor(Color.BLUE);
                        }
                        case "rojo" ->{
                            g.setColor(Color.RED);
                        }
                        case "verde" ->{
                            g.setColor(Color.GREEN);
                        }
                        case "naranja" ->{
                            g.setColor(Color.ORANGE);
                        }
                        case "amarillo" ->{
                            g.setColor(Color.YELLOW);
                        }
                        case "rosado" ->{
                            g.setColor(customPink);
                        }
                        case "morado" ->{
                            g.setColor(customPurple);
                        } default -> {
                            g.setColor(Color.WHITE);
                        }
                    }
                        g.fillRect(j * pixelCelda + offsetX, i * pixelCelda + offsetY, pixelCelda, pixelCelda);
                    }
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(j * pixelCelda + offsetX, i * pixelCelda + offsetY, pixelCelda, pixelCelda);
                }
            }
            // dibujar pieza actual
            if (piezaActual != null) {
                for (Bloque b : piezaActual.getBloques()) {
                    int[] coords = b.getCoords();
                    int x = coords[0];
                    int y = coords[1];
                    String color = b.getColor();
                    switch (color) {
                        case "azul" ->{
                            g.setColor(Color.BLUE);
                        }
                        case "rojo" ->{
                            g.setColor(Color.RED);
                        }
                        case "verde" ->{
                            g.setColor(Color.GREEN);
                        }
                        case "naranja" ->{
                            g.setColor(Color.ORANGE);
                        }
                        case "amarillo" ->{
                            g.setColor(Color.YELLOW);
                        }
                        case "rosado" ->{
                            g.setColor(customPink);
                        }
                        case "morado" ->{
                            g.setColor(customPurple);
                        }
                    }
                    g.fillRect(x * pixelCelda + offsetX, y * pixelCelda + offsetY, pixelCelda, pixelCelda);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * pixelCelda + offsetX, y * pixelCelda + offsetY, pixelCelda, pixelCelda);
                    g.setColor(Color.WHITE);
                    g.drawString("Puntaje: " + puntaje.getPuntajeTotal(), 10, 20);
                    g.drawString("Combo: x" + puntaje.getCombo(), 10, 40);
                }
            }
        }
        if (faseJuego == 3) {
            //Graphics2D g2 = (Graphics2D) g.create();

            // Fondo semi-transparente
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Texto centrado
            String texto = "GAME OVER";
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40f));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(texto);
            int textHeight = fm.getAscent();

            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2;

            g2.drawString(texto, x, y);

            String texto1 = (opcionMenu == 0 ? "- Play Again" : "  Play Again");
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));

            g2.drawString(texto1, x, y + 60);

            String texto2 = (opcionMenu == 1 ? "- Exit" : "  Exit");
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));

            g2.drawString(texto2, x, y + 120);

            g2.dispose();
        }
    }

    // === TECLADO ===
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (faseJuego == 3) {
            // controlar menú
            if (code == KeyEvent.VK_UP) {
                opcionMenu = 0;
            } else if (code == KeyEvent.VK_DOWN) {
                opcionMenu = 1;
            } else if (code == KeyEvent.VK_ENTER) {
                if (opcionMenu == 0) {
                    // play again
                    reiniciarJuego();
                } else {
                    // terminar programa
                    System.exit(0);
                }
            }
        } else if (faseJuego == 2) {
            // controlar pieza normalmente
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                moverPieza("a");
            } else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                moverPieza("d");
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                moverPieza("s");
            } else if (code == KeyEvent.VK_R || code == KeyEvent.VK_UP) {
                rotarPieza();
            }
        } else if (faseJuego == 1){
            if (code == KeyEvent.VK_ENTER){
                inicioJuego();
            }
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

}