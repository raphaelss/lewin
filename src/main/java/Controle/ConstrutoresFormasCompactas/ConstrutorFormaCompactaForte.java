/*
 * This file is part of Lewin, a compositional calculator.
 * Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino@gmail.com
 * Copyright (C) 2013 Liduino José Pitombeira de Oliveira, http://www.pitombeira.com
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

import Controle.DadosMusicais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public abstract class ConstrutorFormaCompactaForte extends ConstrutorFormaCompacta {
    public ConstrutorFormaCompactaForte(ArrayList<ClasseDeAltura> classes) {
        super(classes);
    }

    protected void mudaMelhorRotacao(LinkedList<Integer> indicesRotacoes) {
        //pega os modulos e rotaciona
        int atual, tamanhoDoPadrao = classes.size(), indiceComparacao = 1, minimo;
        ArrayList<ClasseDeAltura> modulos = new ArrayList<ClasseDeAltura>(tamanhoDoPadrao);

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
