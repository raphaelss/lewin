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

package Controle.DadosMusicais;

import java.util.ArrayList;

/**
 *
 * @author Bill's
 */
public class SegmentoInvariancia {
    private ArrayList<Integer> segmento = new ArrayList<Integer>();

    public void adicionaNumero(int numero) {
        segmento.add(numero);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof SegmentoInvariancia) &&
                segmento.equals(((SegmentoInvariancia)o).segmento);
    }

    public String toString() {
        String resultado = "";
        for (int i : segmento) {
            resultado += i + " ";
        }

        return resultado;
    }

    public int tamanhoSegmento() {
        return segmento.size();
    }

    public int get(int indice) {
        return segmento.get(indice);
    }
}
