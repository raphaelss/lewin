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
public abstract class ConstrutorFormaCompacta {
    protected ArrayList<Integer> classes;

    public ConstrutorFormaCompacta(ArrayList<Integer> classe) {
        classes = (ArrayList<Integer>)classe.clone();
    }
    
    protected ArrayList<Integer> getInverso() {
        ArrayList<Integer> inverso = new ArrayList<Integer>();
        //espelhamento
        //transformar a lista espelhada no modulo 12 dela mesma
        for (int j = 0; j < classes.size(); j++) {
            inverso.add((12 - classes.get(j)) % 12);
        }
        return inverso;
    }
    
    protected ArrayList<Integer> getTransposicao(ArrayList<Integer> formaPrima) {
        ArrayList<Integer> subtraida = new ArrayList<Integer>();
        
        int primeiro = formaPrima.get(0), subtracao;
        for (int elem : formaPrima) {
            subtracao = elem - primeiro;
            if (subtracao < 0) {
                subtracao = 12 - Math.abs(subtracao);
            }
            subtraida.add(subtracao);
        }
        
        return subtraida;
    }
    
    public final ArrayList<Integer> retornaForma() {
        Collections.sort(classes);
        //pega os modulos e rotaciona
        int atual, tamanhoDoPadrao = classes.size(), minimo;
        ArrayList<Integer> modulos = new ArrayList<Integer>(tamanhoDoPadrao);
        LinkedList<Integer> indicesRotacoes = new LinkedList<Integer>();
        
        for (int i = 0; i < tamanhoDoPadrao; i++) {
            indicesRotacoes.add(i);
        }
        
        for (int j = 0; j < tamanhoDoPadrao; j++) {
            atual = classes.get(tamanhoDoPadrao - 1) - classes.get(0);

            if (atual > 0) {
                modulos.add(atual % 12);
            }
            else {
                modulos.add(12 - Math.abs(atual));
            }

            classes.add(classes.remove(0));
        }

        minimo = Collections.min(modulos);
        for (int i = 0, j = 0; i < modulos.size(); i++, j++) {
            if (modulos.get(i) != minimo) {
                indicesRotacoes.remove(j--);
            }
        }
        
        if (indicesRotacoes.size() > 1) {
            return procedimentoEspecifico(indicesRotacoes);
        }

        int posicao = indicesRotacoes.get(0);
        //rotaciona ate o menor modulo
        for (int j = 0; j < posicao; j++) {
            classes.add(classes.remove(0));
        }
        
        return classes;
    }
    
    protected ArrayList<Integer> maisCompacto(ArrayList<Integer> forma) {
        int tamanhoPadrao = classes.size(), sub1, sub2;
        for (int i = 1; i < tamanhoPadrao; i++) {
            sub1 = classes.get(i) - classes.get(0);
            if (sub1 < 0) {
                sub1 = 12 - Math.abs(sub1);
            }
            
            sub2 = forma.get(i) - forma.get(0);
            if (sub2 < 0) {
                sub2 = 12 - Math.abs(sub2);
            }
            
            if (sub1 > sub2) {
                return getTransposicao(forma);
            }
            else if (sub1 < sub2) {
                return getTransposicao(classes);
            }
        }
        return classes;
    }
    
    protected abstract ArrayList<Integer> procedimentoEspecifico(LinkedList<Integer> indicesRotacoes);
    
    protected abstract void mudaMelhorRotacao(LinkedList<Integer> indicesRotacoes);
}
