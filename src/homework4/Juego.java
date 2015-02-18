package homework4;

// Hola

/**
 * Juego
 *
 * Tarea basada en examen primer parcial.
 * JFrame en el que se implementa un juego en el cual el jugador se mueve en
 * una cuadricula y debe recoger changuitas para obtener puntos y evitar chocar
 * con changuitos para no perder vidas.
 * El movimiento del jugador se da con las flechas del teclado
 * El juego acaba cuando se presiona la tecla ESC o bien se pierden todas las
 * vidas. Es posible pausarlo presionando la tecla P
 * 
 * Se puede importar un juego salvado al momento de inicializar el juego
 * 
 * @author Marco Antonio Peyrot A00815262
 * @author Mario Sergio Fuentes A01036141
 * @version 1.0
 * @date 18/02/2015
 */
 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Juego extends JFrame implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8; // maxuimo numero de personajes por alto
    private Base basJuanito; // objeto de jugador Juanito
    private LinkedList<Base> lklChimpys; // lista encadenada con changuitas
    private LinkedList<Base> lklDiddys; // lista encadenada con changuitos
    private int iPosXJuan; // numero de columna en que esta juanito
    private int iPosYJuan; // numero de fila en que esta juanito
    private int iVidasTotales; // vidas disponibles en el juego
    private int iVidasPerdidas; // vidas perdidas
    private int iVelChanguitos; // velocidad de chimpys y diddys
    private int iTotalChimpys; // total de chimpys
    private int iTotalDiddys; // total de diddys
    private int iPuntos; // puntuacion del jugador
    int iPosX; // posicion horizontal
    int iPosY; // posicion vertical
    private boolean boolPausa; // bandera de pausa
    private boolean boolGameOver; // bandera de fin de juego
    /* objetos para manejar el buffer del JFrame y este no parpadee */
    private Image    imaImagenApplet; // Imagen a proyectar en JFrame	
    private Image imaImagenGameOver; // imagen de juego terminado
    private Graphics graGraficaApplet; // Objeto grafico de la Imagen
    private SoundClip socSonidoChimpy; // Objeto sonido de Chimpy
    private SoundClip socSonidoDiddy; // Objeto sonido de Diddy
    private String nombreArchivo; // Nombre del archivo
    private URL urlImagenChimpy; // url de imagen de chimpy
    private URL urlImagenDiddy; // url de imagen de diddy
    private int iHeight; // entero con valor del alto disminuido
                        // para poder desplazar ancho de menu superior

    /**
     * Juego
     * 
     * Constructor default del juego. Sirve para llamar a los métodos
     * init y start explícitamente cuando se cree una variable juego
     */
    public Juego() {
        init();
        start();
    }
    
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>JFrame</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el JFrame de un tamaño 800,500
        setSize(800, 500);
        // inicializa nombre de archivo
        nombreArchivo = "datosJuego.txt";
        // bandera de pausa apagada
        boolPausa = false;
        // originalmente no se ha terminado el juego
        boolGameOver = false;
        // imagen de juanito
	URL urlImagenPrincipal = this.getClass().getResource("juanito.gif");
        // alto modificado
        iHeight = getHeight() - 50 - 30;
        // se crea juanito
	basJuanito = new Base(0, 0, getWidth() / iMAXANCHO,
                iHeight / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));
        // originalmente esta posicionado en una casilla intermedia del tablero
        iPosXJuan = 3;
        iPosYJuan = 3;
        // se posiciona la coordenada con base en la fila y columna
        basJuanito.setX(iPosXJuan * getWidth() / iMAXANCHO);
        basJuanito.setY(50 + iPosYJuan * iHeight / iMAXALTO);
        // se inicializa cantidad de vidas totales de juanito, entre 4 y 6
        iVidasTotales = (int) (Math.random() * 3) + 4;
        // originalmente hay 0 vidas perdidas
        iVidasPerdidas = 0;
        // puntuacion inicializada en cero
        iPuntos = 0;
        // originalmente chimpys y diddys se desplazan con esta velocidad
        iVelChanguitos = 2; 
        // Inicializacion de chimpys
        // defino la imagen del chimpy
	urlImagenChimpy = this.getClass().getResource("chimpy.gif");
        // sonido de chimpy
        socSonidoChimpy = new SoundClip("monkey1.wav");
        // lista encadenada de chimpys
        lklChimpys = new LinkedList();
        // se crean chimpys aleatorios entre 10 y 15
        iTotalChimpys = (int) (Math.random() * 6) + 10;
        // genero cada chimpy y lo meto a la lista
        for (int iI = 0; iI < iTotalChimpys; iI ++) {
            iPosX = (int) (Math.random() * iMAXANCHO) * getWidth() / iMAXANCHO
                    + getWidth(); // desplazado hacia la derecha
            iPosY = 50 + (int) (Math.random() * iMAXALTO) * iHeight / iMAXALTO;
            // se crea chimpy
            Base basChimpy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                iHeight / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenChimpy));
            // se agrega a lista
            lklChimpys.add(basChimpy);
        }
        
        // Inicializacion de diddys
        // defino la imagen del diddy
	urlImagenDiddy = this.getClass().getResource("diddy.gif");
        // sonido de diddy
        socSonidoDiddy = new SoundClip("monkey2.wav");
        // se crea lista encadenada de diddys
        lklDiddys = new LinkedList();
        // se crean diddys aleatorios entre 10 y 15
        iTotalDiddys = (int) (Math.random() * 6) + 10;
        // genero cada diddy y lo meto a la lista
        for (int iI = 0; iI < iTotalDiddys; iI ++) {
            iPosX = (int) (Math.random() * iMAXANCHO) * getWidth() / iMAXANCHO
                    - getWidth(); // desplazado a la izquierda
            iPosY = 50 + (int) (Math.random() * iMAXALTO) * iHeight / iMAXALTO;
            
            Base basDiddy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                iHeight / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenDiddy));
            lklDiddys.add(basDiddy);
        }
        
        // se crea la imagen de juego terminado
        URL urlImagenGameOver = this.getClass().getResource("gameOver.jpg");
        imaImagenGameOver = 
                Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);
      
        /* se le añade la opcion al JFrame de ser escuchado por los eventos
           del mouse  */
	addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>JFrame</code>
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        
        // mientras no este prendida bandera de fin de juego
        while (!boolGameOver) { 
            // el juego corre si no esta en pausa
            if (!boolPausa) {
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}  
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     */
    public void actualiza() {
        // actualizo posicion de juanito
        // se valida que no se salga de la cuadricula
        if (iPosXJuan < 0) { // que no se salga a la izquierda
            iPosXJuan = 0;
        }
        if (iPosXJuan >= iMAXANCHO) { // que no se salga a la derecha
            iPosXJuan = iMAXANCHO - 1;
        }
        if (iPosYJuan < 0) { // que no se salga para arriba
            iPosYJuan = 0;
        }
        if (iPosYJuan >= iMAXALTO) { // que no se salga para abajo
            iPosYJuan = iMAXALTO - 1;
        }
        
        // se posiciona la coordenada con base en la fila y columna
        basJuanito.setX(iPosXJuan * getWidth() / iMAXANCHO);
        basJuanito.setY(50 + iPosYJuan * iHeight / iMAXALTO);
        
        // actualizo posiciones de chimpys, hacia la izquierda
        for (Base basChimpy : lklChimpys) {
            basChimpy.setX(basChimpy.getX() - iVelChanguitos);
        }
        // actualizo posiciones de diddys, hacia la derecha
        for (Base basDiddy : lklDiddys) {
            basDiddy.setX(basDiddy.getX() + iVelChanguitos);
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     */
    public void checaColision(){
        // checa colision de chimpys con pared izquierda o juanito
        for (Base basChimpy : lklChimpys) {
            // checa desborde por izquierda (pared)
            if (basChimpy.getX() <= 0) {
                // se reposiciona chimpy
                // desplazado hacia la derecha
                iPosX = (int) (Math.random() * iMAXANCHO) 
                        * getWidth() / iMAXANCHO + getWidth(); 
                iPosY = 50 + (int) (Math.random() * iMAXALTO) 
                        * iHeight / iMAXALTO;
                // actualiza coordenadas
                basChimpy.setX(iPosX);
                basChimpy.setY(iPosY);
            }
            // checa colision con juanito (aumenta puntuacion)
            else if (basChimpy.intersecta(basJuanito)) {
                iPuntos += 10; // aumenta puntuacion
                socSonidoChimpy.play(); // sonido de chimpy
                // se reposiciona chimpy
                // desplazado hacia la derecha
                iPosX = (int) (Math.random() * iMAXANCHO) 
                        * getWidth() / iMAXANCHO + getWidth(); 
                iPosY = 50 + (int) (Math.random() * iMAXALTO) 
                        * iHeight / iMAXALTO;
                // actualiza coordenadas
                basChimpy.setX(iPosX);
                basChimpy.setY(iPosY);
            }
        }
        
        // checa colision de diddys con pared derecha o juanito
        for (Base basDiddy : lklDiddys) {
            // checa desborde por derecha (pared)
            if (basDiddy.getX() + basDiddy.getAncho() >= getWidth()) {
                // se reposiciona diddy
                // desplazado hacia la izquierda
                iPosX = (int) (Math.random() * iMAXANCHO) 
                        * getWidth() / iMAXANCHO - getWidth(); 
                iPosY = 50 + (int) (Math.random() * iMAXALTO) 
                        * iHeight / iMAXALTO;
                // actualiza coordenadas
                basDiddy.setX(iPosX);
                basDiddy.setY(iPosY);
            }
            // checa colision con juanito (disminuye vidas)
            else if (basDiddy.intersecta(basJuanito)) {
                iVidasPerdidas += 1; // aumentan vidas perdidas
                iVelChanguitos += 2; // aumenta velocidad de changuitos en 2
                socSonidoDiddy.play();
                // se reposiciona diddy
                // desplazado hacia la derecha
                iPosX = (int) (Math.random() * iMAXANCHO) 
                        * getWidth() / iMAXANCHO - getWidth(); 
                iPosY = 50 + (int) (Math.random() * iMAXALTO) 
                        * iHeight / iMAXALTO;
                // actualiza coordenadas
                basDiddy.setX(iPosX);
                basDiddy.setY(iPosY);
            }
        }
        
        // se checa si se perdieron todas las vidas
        if (iVidasTotales - iVidasPerdidas <= 0) {
            boolGameOver = true; // se detiene juego
        }         
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>JFrame</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint (Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null) {
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = 
                Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 
                 50, getWidth(), iHeight, this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint2(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>JFrame</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> 
     * usado para dibujar.
     */
    public void paint2(Graphics graDibujo) {
        // se verifica que el juego no haya terminado
        if (boolGameOver) {
            // se dibuja imagen de fin de juego
            graDibujo.drawImage(imaImagenGameOver, 210, 100, this);
        }
        else {
            // si la imagen de juanito y las listas ya se cargaron
            if (basJuanito != null && lklChimpys != null &&
                    lklDiddys != null) {
                //Dibuja la imagen de principal en el JFrame
                basJuanito.paint(graDibujo, this);
                //Dibuja lista de chimpys
                for (Base basChimpy : lklChimpys) {
                    basChimpy.paint(graDibujo, this);
                }
                // Dibuja lista de diddys
                for (Base basDiddy : lklDiddys) {
                    basDiddy.paint(graDibujo, this);
                }

                // se dibuja puntuacion y vidas disponibles
                graDibujo.drawString("Puntos = " + iPuntos, 600, 60);
                graDibujo.drawString("Vidas = " +
                        (iVidasTotales - iVidasPerdidas), 600, 80);

            } // sino se ha cargado se dibuja un mensaje 
            else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
            }
        }
    }
    
    /**
     * grabaArchivo
     * 
     * Metodo que almacena en un archivo los datos de un juego
     */
    public void grabaArchivo() throws IOException {
        PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));

        // imprime datos de juego
        fileOut.println(iPuntos); // puntuacion
        fileOut.println(iVidasTotales); // vidas totales
        fileOut.println(iVidasPerdidas); // vidas perdidas

        // imprime datos de Juanito
        fileOut.println(iPosXJuan); // numero de columna
        fileOut.println(iPosYJuan); // numero de fila
        fileOut.println(basJuanito.getX()); // posicionen X
        fileOut.println(basJuanito.getY()); // posicion en Y

        // imprime datos de changuitos
        fileOut.println(iVelChanguitos); // velocidad de changuitos

        fileOut.println(iTotalDiddys); // total de diddys
        // posiciones de Diddys
        for (Base basDiddy : lklDiddys) {
            fileOut.println(basDiddy.getX() + "\n" + basDiddy.getY());
        }

        fileOut.println(iTotalChimpys); // total de chimpy
        // posiciones de chimpys
        for (Base basChimpy : lklChimpys) {
            fileOut.println(basChimpy.getX() + "\n" + basChimpy.getY());
        }

        fileOut.close();
    }
    
    /**
     * cargaArchivo
     * 
     * Metodo que carga de un archivo los datos de un juego
     */
    public void cargaArchivo() throws IOException {
        BufferedReader fileIn;
        
        try {
            fileIn = new BufferedReader(new FileReader(nombreArchivo));
            boolPausa = true; // originalmente se carga en pausa
                        
            // importar datos del juego
            // se lee puntuacion
            String strLinea = fileIn.readLine();
            iPuntos = Integer.parseInt(strLinea); 
            // se lee vidas totales
            strLinea = fileIn.readLine();
            iVidasTotales = Integer.parseInt(strLinea);
            // se lee vidas perdidas
            strLinea = fileIn.readLine();
            iVidasPerdidas = Integer.parseInt(strLinea);
            
            // importar datos de Juanito
            // se lee numero de columna
            strLinea = fileIn.readLine();
            iPosXJuan = Integer.parseInt(strLinea);
            // se lee numero de fila
            strLinea = fileIn.readLine();
            iPosYJuan = Integer.parseInt(strLinea);
            // se lee coordenada en X de Juanito
            strLinea = fileIn.readLine();
            basJuanito.setX(Integer.parseInt(strLinea));
            // se lee coordenada en Y de Juanito
            strLinea = fileIn.readLine();
            basJuanito.setY(Integer.parseInt(strLinea));
            
            // importar datos de changuitos
            // se lee velocidad de changuitos
            strLinea = fileIn.readLine();
            iVelChanguitos = Integer.parseInt(strLinea);
            
            // se lee total de diddys
            strLinea = fileIn.readLine();
            iTotalDiddys = Integer.parseInt(strLinea);
            lklDiddys.clear(); // se limpia lista de diddys
            for (int iI = 0; iI < iTotalDiddys; iI ++) {
                // se lee posicion en X
                strLinea = fileIn.readLine();
                iPosX = Integer.parseInt(strLinea);
                // se lee posicion en Y
                strLinea = fileIn.readLine();
                iPosY = Integer.parseInt(strLinea);
                Base basDiddy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                    iHeight / iMAXALTO,
                        Toolkit.getDefaultToolkit().getImage(urlImagenDiddy));
                lklDiddys.add(basDiddy);
            }
            
            // se lee total de chimpys
            strLinea = fileIn.readLine();
            iTotalChimpys = Integer.parseInt(strLinea);
            lklChimpys.clear(); // se limpia lista de diddys
            for (int iI = 0; iI < iTotalChimpys; iI ++) {
                // se lee posicion en X
                strLinea = fileIn.readLine();
                iPosX = Integer.parseInt(strLinea);
                // se lee posicion en Y
                strLinea = fileIn.readLine();
                iPosY = Integer.parseInt(strLinea);
                Base basChimpy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                    iHeight / iMAXALTO,
                        Toolkit.getDefaultToolkit().getImage(urlImagenChimpy));
                lklChimpys.add(basChimpy);
            }
            // se cierra archivo
            fileIn.close();
        } 
        catch (FileNotFoundException e){      
        }   
    }

    /**
     * keyTyped
     * 
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una 
     * tecla que no es de accion.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al presionar
     */
    public void keyTyped(KeyEvent keyEvent) {
        // no hay codigo pero se debe escribir el metodo
    }

    /**
     * keyPressed
     * 
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al dejar presionada
     * alguna tecla.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al presionar
     */
    public void keyPressed(KeyEvent keyEvent) {
        // no hay codigo pero se debe escribir el metodo
    }

    /**
     * keyReleased
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al soltar.
     * 
     */
    public void keyReleased(KeyEvent keyEvent) {
        
        // se verifica que el juego no este pausado
        if (!boolPausa) {
            // se checa presion de teclas y se actualiza casilla correspondiente
            if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                iPosYJuan --; // casilla hacia arriba
            }
            else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                iPosYJuan ++; // casilla hacia abajo
            }
            else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                iPosXJuan ++; // casilla hacia derecha
            }
            else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                iPosXJuan --; // casilla hacia izquierda
            }
            // terminar juego
            if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {  
                boolGameOver = true;
            }
        }
        
        // se cambia el estado de pausa
        if (keyEvent.getKeyCode() == 'P') {
            boolPausa = !boolPausa;
        }
        
        // se graban datos del juego en archivo
        if (keyEvent.getKeyCode() == 'G' ) {
            // se intenta grabar datos del juego en archivo
            try {
                grabaArchivo();
            }
            catch (IOException ioError) {
                System.out.println("Hubo un error en escritura de archivo" + 
                            ioError.toString());
            }
        }
        
        // se cargan datos del juego en archivo
        if (keyEvent.getKeyCode() == 'C' ) {
            // se intenta cargar datos del juego en archivo
            try {
                cargaArchivo();
            }
            catch (IOException ioError) {
                System.out.println("Hubo un error en lectura de archivo" + 
                            ioError.toString());
            }
            repaint();
        }   
    }
}