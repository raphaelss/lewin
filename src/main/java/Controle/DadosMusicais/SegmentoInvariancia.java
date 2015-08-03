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
import java.util.Objects;

public class SegmentoInvariancia {
    private ArrayList<ClasseDeAltura> segmento = new ArrayList<ClasseDeAltura>();

    public void adicionaNumero(ClasseDeAltura classe) {
        segmento.add(classe);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof SegmentoInvariancia) &&
                segmento.equals(((SegmentoInvariancia)o).segmento);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(segmento);
    }

    public String toString() {
        String resultado = "";
        for (ClasseDeAltura c : segmento) {
            resultado += c.toString() + " ";
        }
        return resultado;
    }

    public int tamanhoSegmento() {
        return segmento.size();
    }

    public ClasseDeAltura get(int indice) {
        return segmento.get(indice);
    }
}
