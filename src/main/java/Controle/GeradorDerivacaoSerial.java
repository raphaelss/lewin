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
    private static final int TAMANHO_TABELA_GRUPOS = 23;

    public static ArrayList<ConjuntoOrdenado> gerar(ConjuntoOrdenado formaOriginal) throws DadosProibidos {
        int tamanho = formaOriginal.size();

        if (tamanho == 3) {
            ConjuntoOrdenado formaPrima = FormasCompactas.formaPrimaStraus(formaOriginal);
            ClasseDeAltura segundo = formaPrima.get(1),
                           terceiro = formaPrima.get(2);
            if (segundo == ClasseDeAltura.criar(3) && terceiro == ClasseDeAltura.criar(6)) {
                throw new DadosProibidos("Impossível gerar uma série derivada deste tricorde.");
            }
        } else if (tamanho == 4) {
            int[] vetor = formaOriginal.vetorIntervalar();
            if (vetor[3] > 0) {
                throw new DadosProibidos("Impossível gerar uma série derivada de um tetracorde contendo a classe intervalar 4.");
            }
        }

        ArrayList<ConjuntoOrdenado> tabelaDeGrupos = new ArrayList<ConjuntoOrdenado>();
        for (int i = 1; i < 12; ++i) {
            ConjuntoOrdenado co = new ConjuntoOrdenado(formaOriginal);
            co.transpor(i);
            tabelaDeGrupos.add(co);
        }
        ConjuntoOrdenado espelhada = new ConjuntoOrdenado(formaOriginal);
        espelhada.inverter();
        tabelaDeGrupos.add(espelhada);
        for (int i = 1; i < 12; ++i) {
            ConjuntoOrdenado co = new ConjuntoOrdenado(espelhada);
            co.transpor(i);
            tabelaDeGrupos.add(co);
        }
        return encontraGruposCompativeis(formaOriginal, tabelaDeGrupos);
    }

    private static ArrayList<ConjuntoOrdenado> encontraGruposCompativeis(ConjuntoOrdenado formaOriginal, ArrayList<ConjuntoOrdenado> tabelaDeGrupos) {
        ArrayList<ConjuntoOrdenado> listaDeFormas = new ArrayList<ConjuntoOrdenado>();
        ConjuntoOrdenado formas = new ConjuntoOrdenado();
        formas.add(formaOriginal);

        encontraGruposCompativeis(formaOriginal, formas, listaDeFormas, tabelaDeGrupos);

        return listaDeFormas;
    }

    private static void encontraGruposCompativeis(ConjuntoOrdenado formaOriginal,
            ConjuntoOrdenado formas, ArrayList<ConjuntoOrdenado> listaDeFormas,
            ArrayList<ConjuntoOrdenado> tabelaDeGrupos) {
        try {
        ConjuntoOrdenado atual = null;

        for (int i = 0; i < TAMANHO_TABELA_GRUPOS; i++) {
            atual = tabelaDeGrupos.get(i);
            if (formas.disjuntos(atual)) {
                formas.add(atual);
                encontraGruposCompativeis(formaOriginal, formas, listaDeFormas, tabelaDeGrupos);
            }
        }

        if (formas.size() == 12/formaOriginal.size()) {
            listaDeFormas.add(new ConjuntoOrdenado(formas));
        }
        for (int i = 0; i < formaOriginal.size(); ++i) {
            formas.remove(formas.size() - 1);
        }
        } catch (Exception e) { System.out.println(e.getMessage());}
    }
}
