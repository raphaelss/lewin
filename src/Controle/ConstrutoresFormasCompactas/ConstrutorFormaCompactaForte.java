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
public abstract class ConstrutorFormaCompactaForte extends ConstrutorFormaCompacta {
    public ConstrutorFormaCompactaForte(ArrayList<Integer> classes) {
        super(classes);
    }
    
    protected void mudaMelhorRotacao(LinkedList<Integer> indicesRotacoes) {
        //pega os modulos e rotaciona
        int atual, tamanhoDoPadrao = classes.size(), indiceComparacao = 1, minimo;
        ArrayList<Integer> modulos = new ArrayList<Integer>(tamanhoDoPadrao);
        
        do {
            modulos.clear();
            for (int j = 0; j < tamanhoDoPadrao; j++) {
                if (indicesRotacoes.contains(j)) {
                    atual = classes.get(indiceComparacao) - classes.get(0);

                    if (atual > 0) {
                        modulos.add(atual % 12);
                    }
                    else {
                        modulos.add(12 - Math.abs(atual));
                    }
                }

                classes.add(classes.remove(0));
            }
            
            indiceComparacao++;
            
            minimo = Collections.min(modulos);
            for (int i = 0, j = 0; i < modulos.size(); i++, j++) {
                if (modulos.get(i) != minimo) {
                    indicesRotacoes.remove(j--);
                }
            }
        }
        while(indicesRotacoes.size() > 1 && indiceComparacao < (tamanhoDoPadrao - 1));

        int posicao;
        if (indicesRotacoes.size() == 1) {
            posicao = indicesRotacoes.get(0);
        }
        else {
            int indiceMinimo = 0;
            minimo = classes.get(indicesRotacoes.get(0));
            for (int j = 1; j < indicesRotacoes.size(); j++) {
                if (minimo > classes.get(indicesRotacoes.get(j))) {
                    minimo = classes.get(indicesRotacoes.get(j));
                    indiceMinimo = j;
                }
            }
            
            posicao = indicesRotacoes.get(indiceMinimo);
        }
        
        //rotaciona ate o menor modulo
        for (int j = 0; j < posicao; j++) {
            classes.add(classes.remove(0));
        }
    }
}
