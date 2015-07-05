/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Excecoes;

/**
 *
 * @author Administrador
 */
public class SequenciadorNulo extends Exception {

    /**
     * Creates a new instance of <code>SequenciadorNulo</code> without detail message.
     */
    public SequenciadorNulo() {
    }


    /**
     * Constructs an instance of <code>SequenciadorNulo</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SequenciadorNulo(String msg) {
        super(msg);
    }
}
