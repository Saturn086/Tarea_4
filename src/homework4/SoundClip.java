package homework4;

/**
 * Clase SoundClip
 * 
 * Clase que sirve para ejecutar audio en el JFrame
 */
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.URL;

public class SoundClip {
    // Variables
    private AudioInputStream aisSample;
    private Clip cliClip;
    private boolean bLooping = false;
    private int iRepeat = 0;
    private String strFileName = ""; // Nombre del archivo 

    /**
     * SoundClip
     * 
     * Constructor default
     */
    public SoundClip() {
        try {
            //crea el Buffer de sonido
            cliClip = AudioSystem.getClip();
        }
        catch (LineUnavailableException lueException) { 
        }
    }

    /** 
     * SoundClip
     * 
     * Constructor con parametros, que carga manda llamar a load
     * esto carga el archivo de sonido.
     * 
     * @param strFileName es el <code>String</code> del archivo.
     */
    public SoundClip(String strFileName) {
        //Llama al constructor default.
        this();
        //Carga el archivo de sonido.
        load(strFileName);
    }

    /**
     * getClip
     * 
     * Metodo de acceso que regresa un objeto de tipo Clip
     * 
     * @return cliClip es un <code>objeto Clip</code>.
     */
    public Clip getClip() { 
        return cliClip; 
    }

    /** 
     * setLooping
     * 
     * Metodo modificador usado para modificar si el sonido se repite.
     * 
     * @param bLooping es un valor <code>boleano</code>. 
     */
    public void setLooping(boolean bLooping) {
        this.bLooping = bLooping; 
    }

    /**
     * getLooping
     * 
     * Metodo de acceso que regresa un booleano para ver si hay repeticion.
     * 
     * @return bLooping  es un valor <code>boleano</code>. 
     */
    public boolean getLooping() {
        return bLooping;
    }

    /** 
     * setRepeat
     * 
     * Metodo modificador usado para definir el numero de repeticiones.
     * 
     * @param iRepeat es un <code>entero</code> que es el numero de
     * repeticiones. 
     */
    public void setRepeat(int iRepeat) {
        this.iRepeat = iRepeat;
    }

    /**
     * getRepeat
     * 
     * Metodo de acceso que regresa el numero de repeticiones.
     * 
     * @return iRepeat es un valor <code>entero</code> con el numero de 
     * repeticiones. 
     */
    public int getRepeat() { 
        return iRepeat; 
    }

    /**
     * setFilename
     * 
     * Metodo modificador que asigna un nombre al archivo.
     * 
     * @param strFileName es un <code>String</code> con el 
     * nombre del archivo. 
     */
    public void setFilename(String strFileName) { 
        this.strFileName = strFileName; 
    }

    /**
     * getFilename
     * 
     * Metodo de acceso que regresa el nombre del archivo.
     * 
     * @return strFileName es un <code>String</code> con el
     * nombre del archivo. 
     */
    public String getFilename() { 
        return strFileName;
    }

    /**
     * isLoaded
     * 
     * Metodo que verifica si el archivo de audio esta cargado.
     * 
     * @return aisSample es un <code>objeto aisSample</code>.
     */
    public boolean isLoaded() {
        return (boolean)(aisSample != null);
    }

    /** 
     * Metodo de acceso que regresa el url del archivo
     * 
     * @param strFileName es un <code>String</code> con el
     * nombre del archivo. 
     */
    private URL getURL(String strFileName) {
        URL url = null;
        try {
            url = this.getClass().getResource(strFileName);
        }
        catch (Exception excException) { 
        }

        return url;
    }

    /**
     * load
     * 
     * Metodo que carga el archivo de sonido.
     * 
     * @param strAudioFile es un <code>String</code> con el nombre
     * del archivo de sonido.
     * @return <code> boolean </code> si se pudo cargar el archivo
     */
    public boolean load(String strAudioFile) {
        try {
            setFilename(strAudioFile);
            aisSample = AudioSystem.getAudioInputStream(getURL(strFileName));
            cliClip.open(aisSample);
            return true;
        } 
        catch (IOException ioeException) {
            return false;
        }
        catch (UnsupportedAudioFileException ioeException) {
            return false;
        }
        catch (LineUnavailableException ioeException) {
            return false;
        }
    }

    /**
     * play
     * 
     * Metodo que reproduce el sonido.
     */
    public void play() {
        //se sale si el sonido no a sido cargado
        if (!isLoaded()) {
            return;
        }
        //vuelve a empezar el sound cliClip
        cliClip.setFramePosition(0);

        //Reproduce el sonido con repeticion opcional.
        if (bLooping) {
            cliClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            cliClip.loop(iRepeat);
        }
    }

    /**
     * stop
     * 
     * Metodo que detiene el sonido.
     */
    public void stop() {
        cliClip.stop();
    }
}
