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
import java.util.ArrayList;

public class GeradorPaleta {
    public static ArrayList<ArrayList<ConjuntoOrdenado>> gerar(ConjuntoOrdenado original) {
        ArrayList<ArrayList<ConjuntoOrdenado>> resultados = new ArrayList<ArrayList<ConjuntoOrdenado>>(2);
        resultados.add(new ArrayList<ConjuntoOrdenado>(12));
        resultados.add(new ArrayList<ConjuntoOrdenado>(12));
        return gerar(resultados, original);
    }

    public static ArrayList<ArrayList<ConjuntoOrdenado>> gerar(ArrayList<ArrayList<ConjuntoOrdenado>> resultados, ConjuntoOrdenado original) {
        for (ArrayList<ConjuntoOrdenado> conjuntos : resultados) {
            loop:
            for (int i = 0; i < 12; ++i) {
                ConjuntoOrdenado atual = new ConjuntoOrdenado(original).transpor(i);
                for (ArrayList<ConjuntoOrdenado> conjuntos2 : resultados) {
                    for (ConjuntoOrdenado co : conjuntos2) {
                        if (co.containsAll(atual)) {
                           continue loop;
                        }
                    }
                }
                conjuntos.add(atual);
            }
            original.inverter();
        }
        return resultados;
    }
}
