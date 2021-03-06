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

import Controle.DadosMusicais.ConjuntoOrdenado;

import java.util.ArrayList;

public class GeradorInvarianciaTranspositiva {
    public static ArrayList<String> gerar(ConjuntoOrdenado conjunto, int repeticoes) {
        ArrayList<String> resultado = new ArrayList<String>();
        int [] vetInt = conjunto.vetorIntervalar();
        for (int i = 0; i < 5; i++) {
            if (vetInt[i] == repeticoes) {
                resultado.add("T" + (i + 1) + "  ");
                resultado.add("T" + (11 - i) + "  ");
            }
        }
        if (repeticoes == 2 * vetInt[5]) {
            resultado.add("T6");
        }
        if (repeticoes == conjunto.size()) {
            resultado.add("T0");
        }
        return resultado;
    }
}
