/*
 * This file is part of Lewin, a compositional calculator.
 * Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino@gmail.com
 * Copyright (C) 2013 Liduino José Pitombeira de Oliveira, http://www.pitombeira.com
 * Coptyright (C) 2015 Raphael Sousa Santos, http://www.raphaelss.com
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

import Controle.DadosMusicais.ClasseDeAltura;
import Controle.DadosMusicais.ConjuntoOrdenado;
import Excecoes.DadosProibidos;
import java.util.ArrayList;

public class GeradorDerivacaoSerial {
    public static ArrayList<ConjuntoOrdenado> gerar(ConjuntoOrdenado original) throws DadosProibidos {
        int tamanho = original.size();

        if (tamanho == 3) {
            ConjuntoOrdenado formaPrima = FormasCompactas.formaPrimaStraus(original);
            ClasseDeAltura segundo = formaPrima.get(1),
                           terceiro = formaPrima.get(2);
            if (segundo == ClasseDeAltura.criar(3) && terceiro == ClasseDeAltura.criar(6)) {
                throw new DadosProibidos("Impossível gerar uma série derivada deste tricorde.");
            }
        } else if (tamanho == 4) {
            int[] vetor = original.vetorIntervalar();
            if (vetor[3] > 0) {
                throw new DadosProibidos("Impossível gerar uma série derivada de um tetracorde contendo a classe intervalar 4.");
            }
        } else if (tamanho != 6) {
                throw new DadosProibidos("Só é possível derivar séries dodecafônicas a partir de tricordes, tetracordes e hexacordes.");
        }

        ArrayList<ArrayList<ConjuntoOrdenado>> paleta = GeradorPaleta.gerar(original);
        ArrayList<ConjuntoOrdenado> resultado = new ArrayList<ConjuntoOrdenado>();
        ConjuntoOrdenado[] parts = new ConjuntoOrdenado [12 / original.size()];
        encontraGruposCompativeis(resultado, 0, parts, paleta);
        return resultado;
    }

    private static void encontraGruposCompativeis(ArrayList<ConjuntoOrdenado> resultado, int pos,
            ConjuntoOrdenado[] parts, ArrayList<ArrayList<ConjuntoOrdenado>> paleta) {
        if (pos == parts.length) {
            ConjuntoOrdenado serie = new ConjuntoOrdenado();
            for (ConjuntoOrdenado co : parts) {
                serie.add(co);
            }
            resultado.add(serie);
        } else {
            for (ArrayList<ConjuntoOrdenado> subPaleta : paleta) {
                proximoPaleta:
                for (ConjuntoOrdenado co : subPaleta) {
                    for (int i = 0; i < pos; ++i) {
                        if (!co.disjuntos(parts[i])) {
                            continue proximoPaleta;
                        }
                    }
                    parts[pos] = co;
                    encontraGruposCompativeis(resultado, pos + 1, parts, paleta);
                }
            }
        }
    }
}
