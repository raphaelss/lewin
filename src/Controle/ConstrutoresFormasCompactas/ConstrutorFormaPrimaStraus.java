/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.ConstrutoresFormasCompactas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Hildegard
 */
public class ConstrutorFormaPrimaStraus extends ConstrutorFormaCompactaStraus {
    public ConstrutorFormaPrimaStraus(ArrayList<Integer> classe) {
        super(classe);
    }
    
    protected ArrayList<Integer> procedimentoEspecifico(LinkedList<Integer> indicesRotacoes) {
        mudaMelhorRotacao(indicesRotacoes);
        return maisCompacto(new ConstrutorFormaNormalStraus(getInverso()).retornaForma());
    }
}
