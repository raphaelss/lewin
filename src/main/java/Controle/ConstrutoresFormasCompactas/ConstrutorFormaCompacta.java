/*
 * This file is part of Lewin, a compositional calculator.
 * Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino@gmail.com
 * Copyright (C) 2013 Liduino José Pitombeira de Oliveira, http://www.pitombeira.com
 * Copyright (C) 2015 Raphael Sousa Santos, http://www.raphaelss.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package Controle.ConstrutoresFormasCompactas;

import Controle.DadosMusicais.ClasseDeAltura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public abstract class ConstrutorFormaCompacta {
    protected ArrayList<ClasseDeAltura> classes;

    public ConstrutorFormaCompacta(ArrayList<ClasseDeAltura> classe) {
        classes = new ArrayList<ClasseDeAltura>(classe);
    }

    protected ArrayList<ClasseDeAltura> getInverso() {
        ArrayList<ClasseDeAltura> inverso = new ArrayList<ClasseDeAltura>();
        //espelhamento
        //transformar a lista espelhada no modulo 12 dela mesma
        for (int j = 0; j < classes.size(); j++) {
            inverso.add(classes.get(j).inverter());
        }
        return inverso;
    }

    protected static ArrayList<ClasseDeAltura> getTransposicao(ArrayList<ClasseDeAltura> formaPrima) {
        ArrayList<ClasseDeAltura> subtraida = new ArrayList<ClasseDeAltura>();
        ClasseDeAltura primeiro = formaPrima.get(0), subtracao;
        for (ClasseDeAltura elem : formaPrima) {
            subtracao = elem.transpor(-primeiro.inteiro());
            subtraida.add(subtracao);
        }
        return subtraida;
    }

    public final ArrayList<ClasseDeAltura> retornaForma() {
        Collections.sort(classes);
        //pega os modulos e rotaciona
        ClasseDeAltura atual;
        int tamanhoDoPadrao = classes.size(), minimo;
        ArrayList<ClasseDeAltura> modulos = new ArrayList<ClasseDeAltura>(tamanhoDoPadrao);
        LinkedList<Integer> indicesRotacoes = new LinkedList<Integer>();

        for (int i = 0; i < tamanhoDoPadrao; i++) {
            indicesRotacoes.add(i);
        }

        for (int j = 0; j < tamanhoDoPadrao; j++) {
            atual = classes.get(tamanhoDoPadrao - 1).transpor(-classes.get(0).inteiro());
            modulos.add(atual);
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

    protected ArrayList<ClasseDeAltura> maisCompacto(ArrayList<ClasseDeAltura> forma) {
        int tamanhoPadrao = classes.size(), sub1, sub2;
        for (int i = 1; i < tamanhoPadrao; i++) {
            sub1 = classes.get(i).diferenca(classes.get(0));
            if (sub1 < 0) {
                sub1 = 12 - Math.abs(sub1);
            }

            sub2 = forma.get(i).diferenca(forma.get(0));
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

    protected abstract ArrayList<ClasseDeAltura> procedimentoEspecifico(LinkedList<Integer> indicesRotacoes);

    protected abstract void mudaMelhorRotacao(LinkedList<Integer> indicesRotacoes);
}
