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
    private boolean gameOver = false;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return; // por si acaso se dispara algo
        }

        gravedad();
        if (tocoSuelo()) {
            t.fijarPieza(piezaActual);
            int lineasLimpias = t.limpiarLineas(t.getAlto() - 1);

            if (lineasLimpias > 0) {
                // uso el color de la pieza actual como dominante
                String colorPieza = piezaActual.getBloques()[0].getColor();
                int puntosGanados = puntaje.procesarLineaLimpia(colorPieza, lineasLimpias);
                System.out.println("Ganaste " + puntosGanados + " puntos. Total: " + puntaje.getPuntajeTotal());
            }
            
            if (t.hayGameOver(piezaActual)) {
            gameOver = true;
            timer.stop();     // detiene la caída automática
            }
            
            inicializarPieza();
            generarNuevaPieza();
        }
        repaint();
        if(t.hayGameOver(piezaActual) == true){
            System.out.println("Game Over");
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Bloque[][] celdas = t.getCeldas();
        t.hayGameOver(piezaActual);

        int tableroAnchoPx = t.getAncho() * pixelCelda; 
        int tableroAltoPx  = t.getAlto() * pixelCelda;  

   
        int offsetX = (getWidth()  - tableroAnchoPx) / 2;
        int offsetY = (getHeight() - tableroAltoPx)  / 2;

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

        if (gameOver) {
            Graphics2D g2 = (Graphics2D) g.create();

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

            String texto1 = menu("up")[0];
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
            fm = g2.getFontMetrics();
            textWidth = fm.stringWidth(texto);
            textHeight = fm.getAscent();

            g2.drawString(texto1, x, y + 60);

            String texto2 = menu("up")[1];
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
            fm = g2.getFontMetrics();
            textWidth = fm.stringWidth(texto);
            textHeight = fm.getAscent();

            g2.drawString(texto2, x, y + 120);

            g2.dispose();
        }
    }

    // === TECLADO ===
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            moverPieza("a");
        } else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            moverPieza("d");
        } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            moverPieza("s");
            menu("down");
        } else if (code == KeyEvent.VK_R || code == KeyEvent.VK_UP) {
            rotarPieza();
            menu("up");
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

}