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

package Controle;

import java.util.ArrayList;

/**
 *
 * @author Bill's
 */
public class GeradorVetorIntervalar {
    public static int[] geraVetor(ArrayList<Integer> entrada) {
        ArrayList<Integer> diferencas = new ArrayList<Integer>();

        for (int i = 0; i < entrada.size(); i++) {
            for (int j = i + 1; j < entrada.size(); j++) {
                diferencas.add(Math.abs(entrada.get(i) - entrada.get(j)));
            }
        }
        int[] contadores = new int[6];

        for (int numero : diferencas) {
            switch(numero) {
                case 1:
                case 11:
                    contadores[0]++;
                    break;
                case 2:
                case 10:
                    contadores[1]++;
                    break;
                case 3:
                case 9:
                    contadores[2]++;
                    break;
                case 4:
                case 8:
                    contadores[3]++;
                    break;
                case 5:
                case 7:
                    contadores[4]++;
                    break;
                case 6:
                    contadores[5]++;
            }
        }

        return contadores;
    }
}
