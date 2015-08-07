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

package Controle;

import Controle.DadosMusicais.ClasseDeAltura;
import Controle.DadosMusicais.ConjuntoOrdenado;
import Controle.DadosMusicais.MatrizDodecafonica;
import java.util.ArrayList;

public class GeradorTabelaAdicao {
    public static MatrizDodecafonica geraTabela(ConjuntoOrdenado numeros) {
        int i, j, tamanho = numeros.size();
        MatrizDodecafonica matriz = new MatrizDodecafonica();

        for (i = 0; i < tamanho; i++) {
            for (j = 0; j < tamanho; j++) {
                matriz.preenchePosicao(i, j, numeros.get(i).transpor(numeros.get(j).inteiro()));
            }
            for (; j < 12; j++) {
                matriz.preenchePosicao(i, j, null);
            }
        }

        for (; i < 12; i++) {
            for (j = 0; j < 12; j++) {
                matriz.preenchePosicao(i, j, null);
            }
        }

        return matriz;
    }
}
