/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Excecoes;

/**
 *
 * @author UserName
 */
public class DadosProibidos extends Exception {

    /**
     * Creates a new instance of <code>DadosProibidos</code> without detail message.
     */
    public DadosProibidos() {
    }


    /**
     * Constructs an instance of <code>DadosProibidos</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DadosProibidos(String msg) {
        super(msg);
    }
}
