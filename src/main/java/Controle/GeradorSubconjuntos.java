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
import java.util.HashSet;

public class GeradorSubconjuntos {
    public static ArrayList<ConjuntoOrdenado> gerar(ConjuntoOrdenado conjunto, int n) {
        HashSet<ConjuntoOrdenado> tmp = new HashSet<ConjuntoOrdenado>();
        constroi(conjunto, 0, n, new ConjuntoOrdenado(), tmp);
        return new ArrayList<ConjuntoOrdenado>(tmp);
    }

    private static void constroi(ConjuntoOrdenado conjunto, int indice, int n, ConjuntoOrdenado subconjunto, HashSet<ConjuntoOrdenado> subconjuntos) {
        for (; indice < conjunto.size(); indice++) {
            subconjunto.add(conjunto.get(indice));
            if (subconjunto.size() == n) {
                subconjuntos.add(new ConjuntoOrdenado(subconjunto));
                subconjunto.remove(subconjunto.size() - 1);
            }
            else {
                constroi(conjunto, indice + 1, n, subconjunto, subconjuntos);
            }
        }
        if (!subconjunto.isEmpty()) {
            subconjunto.remove(subconjunto.size() - 1);
        }
    }

}
