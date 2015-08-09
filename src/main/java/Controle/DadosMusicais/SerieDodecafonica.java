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

import java.util.ArrayList;

public class SerieDodecafonica {
    private ArrayList<ConjuntoOrdenado> formasPrimas = new ArrayList<ConjuntoOrdenado>();

    public boolean eCompativel(ConjuntoOrdenado forma) {
        int tamanho = forma.size();

        for (ConjuntoOrdenado f : formasPrimas) {
            for (int i = 0; i < tamanho; i++) {
                if (f.contains(forma.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public void adiciona(ConjuntoOrdenado forma) {
        formasPrimas.add(forma);
    }

/*
    public void remove() {
        formasPrimas.remove(formasPrimas.size() - 1);
    }
*/

    public String toString() {
        String resultado = "";
        int tamanhoAtual;

        for (ConjuntoOrdenado f : formasPrimas) {
            tamanhoAtual = f.size();

            for (int i = 0; i < tamanhoAtual; i++) {
                resultado += f.get(i) + "   ";
            }
        }

        return resultado;
    }

    public int getTamanho() {
        return formasPrimas.size();
    }

    public SerieDodecafonica copia() {
        SerieDodecafonica retorno = new SerieDodecafonica();
        retorno.formasPrimas = new ArrayList<ConjuntoOrdenado>(this.formasPrimas);

        return retorno;
    }

    public ClasseDeAltura get(int indice) {
        int tamanho = formasPrimas.get(0).size();

        return formasPrimas.get(indice/tamanho).get(indice%tamanho);
    }

    public ConjuntoOrdenado toConjuntoOrdenado(int subTamanho) {
        ConjuntoOrdenado integerList = new ConjuntoOrdenado();
        ConjuntoOrdenado corrente = null;
        int tamanho = formasPrimas.size(), indiceReal = 0;

        for (int i = 0; i < tamanho && indiceReal < subTamanho; i++) {
            corrente = formasPrimas.get(i);

            for (int j = 0; j < corrente.size() && indiceReal < subTamanho;
                    j++, indiceReal++) {
                integerList.add(corrente.get(j));
            }
        }

        return integerList;
    }
}
