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

public class ConjuntoOrdenado {
    private final ArrayList<ClasseDeAltura> classes;

    public ConjuntoOrdenado() {
        classes = new ArrayList<ClasseDeAltura>();
    }

    public ConjuntoOrdenado(ClasseDeAltura[] forma) {
        classes = new ArrayList<ClasseDeAltura>(forma.length);
        for (int i = 0; i < forma.length; ++i) {
            classes.add(forma[i]);
        }
    }

    public ConjuntoOrdenado(ArrayList<ClasseDeAltura> forma) {
        classes = new ArrayList<ClasseDeAltura>();
    }

    public ConjuntoOrdenado(ConjuntoOrdenado co) {
        classes = new ArrayList<ClasseDeAltura>(co.classes);
    }

    public void transpor(int n) {
        for (int i = 0; i < classes.size(); ++i) {
             classes.set(i, classes.get(i).transpor(n));
        }
    }

    public ClasseDeAltura getDado(int indice) {
        return classes.get(indice);
    }

    public int tamanho() {
        return classes.size();
    }

    public boolean contem(ClasseDeAltura x) {
        return classes.contains(x);
    }

    public boolean equals(ConjuntoOrdenado forma) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i) != forma.getDado(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < classes.size(); i++) {
            builder.append(classes.get(i).toString()).append(" ");
        }
        builder.setLength(builder.length() - 1);
        return builder.append("]").toString();
    }

    public void inverter() {
        for (int i = 0; i < classes.size(); ++i) {
            classes.set(i, classes.get(i).inverter());
        }
    }

    public int[] vetorIntervalar() {
        int[] vetor = new int[6];
        vetorIntervalar(vetor);
        return vetor;
    }

    public void vetorIntervalar(int[] vetor) {
        for (int i = 0; i < 6; ++i) {
            vetor[i] = 0;
        }
        for (int i = 0; i < classes.size(); ++i) {
            for (int j = i + 1; j < classes.size(); ++j) {
                ++vetor[classes.get(i).intervalo_desord(classes.get(j)) - 1];
            }
        }
    }
}
