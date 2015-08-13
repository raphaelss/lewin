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

import java.util.ArrayList;

public class RotacaoStravinsky {
    public static ArrayList<ConjuntoOrdenado> gerar(ConjuntoOrdenado serie) {
        ArrayList<ConjuntoOrdenado> resultado = new ArrayList<ConjuntoOrdenado>(7);
        resultado.add(serie.subSeq(0, 6));
        ConjuntoOrdenado hexacordeBase = serie.subSeq(6, 12);
        resultado.add(hexacordeBase);
        ClasseDeAltura c = hexacordeBase.get(0);
        for (int i = 0; i < 5; ++i) {
            hexacordeBase = (new ConjuntoOrdenado(hexacordeBase)).rotacionar().transporPara(c);
            resultado.add(hexacordeBase);
        }
        return resultado;
    }
}
