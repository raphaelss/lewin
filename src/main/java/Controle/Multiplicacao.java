/*
 * This file is part of Lewin, a compositional calculator.
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

public class Multiplicacao {
    public static ConjuntoOrdenado rahn(ConjuntoOrdenado co, int fator) {
        ConjuntoOrdenado resultado = new ConjuntoOrdenado();
        for (ClasseDeAltura c : co) {
            resultado.add(ClasseDeAltura.criar(c.inteiro() * fator));
        }
        return resultado;
    }

    public static ConjuntoOrdenado rahn(ConjuntoOrdenado a, ConjuntoOrdenado b) {
        int tamanho = a.size() + b.size() - 1;
        ClasseDeAltura[] resultadoParcial = new ClasseDeAltura [tamanho];
        int indiceAPartir = 0, iterandoAPartir;

        for (ClasseDeAltura doPrimeiroFator : a) {
            iterandoAPartir = indiceAPartir++;

            for (ClasseDeAltura doSegundoFator : b) {
                resultadoParcial[iterandoAPartir++].transpor(doPrimeiroFator.inteiro() * doSegundoFator.inteiro());
            }
        }

        ConjuntoOrdenado resultadoSemRepetidos = new ConjuntoOrdenado();
        for (ClasseDeAltura c : resultadoParcial) {
            resultadoSemRepetidos.add(c);
        }

        return resultadoSemRepetidos;
    }

    public static ConjuntoOrdenado boulez(ConjuntoOrdenado a, ConjuntoOrdenado b) {
        ConjuntoOrdenado resultado = new ConjuntoOrdenado();

        for (ClasseDeAltura doSegundoFator : b) {
            resultado.add(doSegundoFator);

            for (int i = 1; i < a.size(); i++) {
                doSegundoFator = doSegundoFator.transpor(a.get(i).transpor(-(a.get(i - 1).inteiro())).inteiro());
                resultado.add(doSegundoFator);
            }
        }

        resultado.sort();
        return resultado;
    }
}
