/**
 * Homework 4
 * 
 * Un juego de Chimpy/Diddy que se ejecuta en un JFrame
 * 
 * @author Marco Antonio Peyrot A00815262
 * @author Mario Sergio Fuentes A01036141
 * @version 1.0
 * @date 18/02/2015
 */

package homework4;

import javax.swing.JFrame;

public class Homework4 {

    /**
     * main
     * 
     * Entrada principal para la ejecución del programa
     * 
     * @param args es un <code> String[] </code> con los argumentos de la
     * línea de comandos
     */
    public static void main(String[] args) {
        // Crear un nuevo juego
        Juego jueJuego = new Juego();
        // Activar la visualización del juego
        jueJuego.setVisible(true);
        // El juego se cierra cuando el JFrame se cierre
        jueJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }  
}
