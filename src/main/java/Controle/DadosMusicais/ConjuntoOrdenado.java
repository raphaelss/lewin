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
import java.util.Collections;
import java.util.Iterator;

public class ConjuntoOrdenado implements Iterable<ClasseDeAltura> {
    private final ArrayList<ClasseDeAltura> classes;

    public ConjuntoOrdenado() {
        classes = new ArrayList<ClasseDeAltura>();
    }

    //TODO: Remover este construtor e o próximo ou garantir que eles não gerem alturas duplicadas
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

    public Iterator<ClasseDeAltura> iterator() {
         return classes.iterator();
    }

    public boolean add(ClasseDeAltura c) {
        if (classes.contains(c)) {
            return false;
        }
        classes.add(c);
        return true;
    }

    public ClasseDeAltura remove(int i) {
        return classes.remove(i);
    }

    public void clear() {
        classes.clear();
    }

    public boolean isEmpty() {
        return classes.isEmpty();
    }

    public void transpor(int n) {
        for (int i = 0; i < classes.size(); ++i) {
             classes.set(i, classes.get(i).transpor(n));
        }
    }

    public void transporParaZero() {
        transpor(-classes.get(0).inteiro());
    }

    public void rotacionar() {
        classes.add(classes.remove(0));
    }

    public void sort() {
        Collections.sort(classes);
    }

    public ClasseDeAltura get(int indice) {
        return classes.get(indice);
    }

    public int size() {
        return classes.size();
    }

    public boolean contains(ClasseDeAltura x) {
        return classes.contains(x);
    }

    public boolean containsAll(ConjuntoOrdenado co) {
        return classes.containsAll(co.classes);
    }

    public boolean equals(ConjuntoOrdenado forma) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i) != forma.get(i)) {
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
