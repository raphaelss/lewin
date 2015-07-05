/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.ConstrutoresFormasCompactas;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Hildegard
 */
public class ConstrutorFormaNormalStraus extends ConstrutorFormaCompactaStraus {
    public ConstrutorFormaNormalStraus(ArrayList<Integer> classe) {
        super(classe);
    }
    
    protected ArrayList<Integer> procedimentoEspecifico(LinkedList<Integer> indicesRotacoes) {
        mudaMelhorRotacao(indicesRotacoes);
        return classes;
    }
}
