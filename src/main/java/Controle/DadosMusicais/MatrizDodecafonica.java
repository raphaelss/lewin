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

package Controle.DadosMusicais;

public class MatrizDodecafonica {
    private ClasseDeAltura[][] matriz = new ClasseDeAltura[12][12];

    public void preenchePosicao(int linha, int coluna, ClasseDeAltura valor) {
        matriz[linha][coluna] = valor;
    }

    public void setLinha(int linha, ConjuntoOrdenado serie) {
        for (int i = 0; i < 12; ++i) {
            matriz[linha][i] = serie.get(i);
        }
    }

    public void setColuna(int coluna, ConjuntoOrdenado serie) {
        for (int i = 0; i < 12; i++) {
            matriz[i][coluna] = serie.get(i);
        }
    }

    public ClasseDeAltura get(int linha, int coluna) {
        return matriz[linha][coluna];
    }

    public ConjuntoOrdenado getP(int indice) {
        ConjuntoOrdenado co = new ConjuntoOrdenado();
        for (int i = 0; i < 12; ++i) {
            co.add(matriz[indice][i]);
        }
        return co;
    }

    public ConjuntoOrdenado getR(int indice) {
        return getP(indice).retrogradar();
    }

    public ConjuntoOrdenado getI(int indice) {
        ConjuntoOrdenado co = new ConjuntoOrdenado();
        for (int i = 0; i < 12; ++i) {
            co.add(matriz[i][indice]);
        }
        return co;
    }

    public ConjuntoOrdenado getRI(int indice) {
        return getI(indice).retrogradar();
    }
}
