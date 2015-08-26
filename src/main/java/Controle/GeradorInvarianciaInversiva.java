/*
 * This file is part of Lewin, a compositional calculator.
 * Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino@gmail.com
 * Copyright (C) 2013 Liduino José Pitombeira de Oliveira, http://www.pitombeira.com
 * Coptyright (C) 2015 Raphael Sousa Santos, http://www.raphaelss.com
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

package Controle;

import Controle.DadosMusicais.ConjuntoOrdenado;
import Controle.DadosMusicais.MatrizDodecafonica;
import java.util.ArrayList;

public class GeradorInvarianciaInversiva {
    public static ArrayList<String> gerar(ConjuntoOrdenado conjunto, int repeticoes) {
        ArrayList<String> resultado = new ArrayList<String>();
        int tamanho = conjunto.size();
        MatrizDodecafonica tabela = GeradorTabelaAdicao.geraTabela(conjunto);
        int[] contadores = new int[12];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                contadores[tabela.get(i, j).inteiro()]++;
            }
        }

        for (int i = 0; i < 12; i++) {
            if (contadores[i] == repeticoes) {
                resultado.add("T" + i + "I  ");
            }
        }
        return resultado;
    }
}