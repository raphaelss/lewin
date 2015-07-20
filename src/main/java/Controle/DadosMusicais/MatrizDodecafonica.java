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
    private ClasseDeAltura[][] matriz;

    public MatrizDodecafonica() {
        matriz = new ClasseDeAltura[12][12];
    }

    public void preenchePosicao(int linha, int coluna, ClasseDeAltura valor) {
        matriz[linha][coluna] = valor;
    }

    public void setLinha(int linha, ClasseDeAltura[] serie) {
        matriz[linha] = serie;
    }

    public void setColuna(int coluna, ClasseDeAltura[] serie) {
        for (int i = 0; i < 12; i++) {
            matriz[i][coluna] = serie[i];
        }
    }

    public ClasseDeAltura getValor(int linha, int coluna) {
        return matriz[linha][coluna];
    }

    public ClasseDeAltura[] getP(int indice) {
        return matriz[indice];
    }

    public ClasseDeAltura[] getR(int indice) {
        ClasseDeAltura[] serie = new ClasseDeAltura[12];

        for (int i = 11; i >= 0; i--) {
            serie[11 - i] = matriz[indice][i];
        }

        return serie;
    }

    public ClasseDeAltura[] getI(int indice) {
        ClasseDeAltura[] serie = new ClasseDeAltura[12];

        for (int i = 0; i < 12; i++) {
            serie[i] = matriz[i][indice];
        }

        return serie;
    }

    public ClasseDeAltura[] getRI(int indice) {
        ClasseDeAltura[] serie = new ClasseDeAltura[12];

        for (int i = 11; i >= 0; i--) {
            serie[11 - i] = matriz[i][indice];
        }

        return serie;
    }
}
