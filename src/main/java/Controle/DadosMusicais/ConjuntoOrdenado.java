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

    public int add(ConjuntoOrdenado co) {
        int n = 0;
        for (ClasseDeAltura x : co) {
            if (add(x)) {
                ++n;
            }
        }
        return n;
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

    public boolean disjuntos(ConjuntoOrdenado co) {
        return Collections.disjoint(classes, co.classes);
    }

    public ConjuntoOrdenado subSeq(int a, int b) {
        ConjuntoOrdenado resultado = new ConjuntoOrdenado();
        for (ClasseDeAltura c : classes.subList(a,b)) {
            resultado.add(c);
        }
        return resultado;
    }

    public ConjuntoOrdenado transpor(int n) {
        for (int i = 0; i < classes.size(); ++i) {
             classes.set(i, classes.get(i).transpor(n));
        }
        return this;
    }

    public ConjuntoOrdenado transporPara(ClasseDeAltura c) {
        return transpor(classes.get(0).intervaloOrd(c));
    }

    public ConjuntoOrdenado retrogradar() {
        for (int i = 0, j = classes.size() - 1; i < j; ++i, --j) {
            ClasseDeAltura tmp = classes.get(j);
            classes.set(j, classes.get(i));
            classes.set(i, tmp);
        }
        return this;
    }

    public ConjuntoOrdenado rotacionar() {
        classes.add(classes.remove(0));
        return this;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConjuntoOrdenado) {
            ConjuntoOrdenado co = (ConjuntoOrdenado) obj;
            return classes.equals(co.classes);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return classes.hashCode();
    }

    public String representacao(ClasseDeAltura.TipoRepresentacao formato) {
        StringBuilder builder = new StringBuilder();
        for (ClasseDeAltura c : classes) {
            builder.append(c.representacao(formato)).append("  ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    public ConjuntoOrdenado inverter() {
        for (int i = 0; i < classes.size(); ++i) {
            classes.set(i, classes.get(i).inverter());
        }
        return this;
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
                ++vetor[classes.get(i).intervaloDesord(classes.get(j)) - 1];
            }
        }
    }
}
