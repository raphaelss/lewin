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

public class Similaridade {
    public static double calcular(ConjuntoOrdenado a, ConjuntoOrdenado b) {
        int tamanhoA = a.size(),
            tamanhoB = b.size();

        int[] vetorA = a.vetorIntervalar(),
              vetorB = b.vetorIntervalar();
        double resultado = 0;

        for (int i = 0; i < 6; i++) {
            resultado += Math.sqrt(vetorA[i] * vetorB[i]);
        }
        resultado *= 2;
        resultado /= Math.sqrt(tamanhoA * (tamanhoA - 1)
                               * tamanhoB * (tamanhoB - 1));

        return resultado;
    }
}
