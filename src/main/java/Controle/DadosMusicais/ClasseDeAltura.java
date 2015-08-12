/*
 * This file is part of Lewin, a compositional calculator.
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

public class ClasseDeAltura implements Comparable<ClasseDeAltura> {
    public enum TipoRepresentacao {
        Inteiro,
        NomeSustenido
    }

    public static ClasseDeAltura criar(int x) {
        return memo[mod12(x)];
    }

    public int inteiro() {
        return valor;
    }

    public ClasseDeAltura transpor(int n) {
        return criar(valor + n);
    }

    public ClasseDeAltura inverter() {
        return criar(12 - valor);
    }

    public int intervaloOrd(ClasseDeAltura outra) {
        return mod12(outra.valor - valor);
    }

    public int intervaloDesord(ClasseDeAltura outra) {
        return Math.min(this.intervaloOrd(outra), outra.intervaloOrd(this));
    }

    @Override
    public String toString() {
        return Integer.toString(valor);
    }

    public String nome() {
        return nomes[valor];
    }

    public String representacao(TipoRepresentacao tipo) {
        switch (tipo) {
        case Inteiro:
            return toString();
        case NomeSustenido:
            return nome();
        }
        //Nunca deve chegar aqui
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return valor;
    }

    public int compareTo(ClasseDeAltura c) {
        return Integer.compare(valor, c.inteiro());
    }

    private final int valor;
    private final static String[] nomes = {"C", "C#", "D", "D#", "E", "F",
                                           "F#","G", "G#", "A", "A#", "B"};
    private final static ClasseDeAltura[] memo = {
        new ClasseDeAltura(0), new ClasseDeAltura(1), new ClasseDeAltura(2),
        new ClasseDeAltura(3), new ClasseDeAltura(4), new ClasseDeAltura(5),
        new ClasseDeAltura(6), new ClasseDeAltura(7), new ClasseDeAltura(8),
        new ClasseDeAltura(9), new ClasseDeAltura(10), new ClasseDeAltura(11)
    };

    private static int mod12(int x) {
        x %= 12;
        if (x < 0) {
            x += 12;
        }
        return x;
    }

    //Não deve ser utilizado diretamente. Usar o método estático criar.
    private ClasseDeAltura(int x) {
        valor = x;
    }
}
