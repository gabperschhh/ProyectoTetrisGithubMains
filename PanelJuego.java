import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

    public PanelJuego(){
        t = new Tablero(20,10);

        generador = new Pieza(null, null);
        
        setPreferredSize(new Dimension(t.getAncho() * pixelCelda, t.getAlto() * pixelCelda));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        gravedad();
        if (tocoSuelo()) {
            t.fijarPieza(piezaActual);
            t.limpiarLineas(t.getAlto() - 1);
            inicializarPieza();
            generarNuevaPieza();
        }
        repaint(); // redibuja el panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Bloque[][] celdas = t.getCeldas();

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
                    
                    

                    g.fillRect(j * pixelCelda, i * pixelCelda, pixelCelda, pixelCelda);
                }
                g.setColor(Color.DARK_GRAY);
                g.drawRect(j * pixelCelda, i * pixelCelda, pixelCelda, pixelCelda);
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



                //g.setColor(Color.CYAN); // si quieres, mapea b.getColor()
                g.fillRect(x * pixelCelda, y * pixelCelda, pixelCelda, pixelCelda);
                g.setColor(Color.BLACK);
                g.drawRect(x * pixelCelda, y * pixelCelda, pixelCelda, pixelCelda);
            }
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
        } else if (code == KeyEvent.VK_R || code == KeyEvent.VK_UP) {
            rotarPieza();
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

}