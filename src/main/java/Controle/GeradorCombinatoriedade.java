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
import Controle.DadosMusicais.MatrizDodecafonica;

import java.util.ArrayList;
import java.awt.Point;

public class GeradorCombinatoriedade {
    public static ArrayList<Point> gerar(MatrizDodecafonica matriz) {
        ArrayList<Point> camposColorir = new ArrayList<Point>();
        ConjuntoOrdenado hexacordeCorrente, hexacordeRejeitado = matriz.getP(0).subSeq(0, 6);

        for (int i = 1; i < 12; i++) {
            hexacordeCorrente = matriz.getP(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(i, j));
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente = matriz.getR(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(i, 11 - j));
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente = matriz.getI(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(j, i));
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente = matriz.getRI(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(11 - j, i));
                }
            }
        }
        return camposColorir;
    }
}
