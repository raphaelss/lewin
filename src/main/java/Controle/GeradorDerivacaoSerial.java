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

import Controle.DadosMusicais.Acorde;
import Controle.DadosMusicais.SerieDodecafonica;
import java.util.ArrayList;

public class GeradorDerivacaoSerial {
    private Acorde formaOriginal;
    private ArrayList<Acorde> tabelaDeGrupos = new ArrayList<Acorde>();
    private ArrayList<SerieDodecafonica> listadeFormas = new ArrayList<SerieDodecafonica>();
    private final int TAMANHO_TABELA_GRUPOS = 23;

    public GeradorDerivacaoSerial(Acorde form) {
        formaOriginal = form;
    }

    public ArrayList<SerieDodecafonica> resultado() {
        for (int i = 1; i < 12; ++i) {
            tabelaDeGrupos.add(formaOriginal.transpor(i));
        }
        Acorde espelhada = formaOriginal.espelhada();
        tabelaDeGrupos.add(espelhada);
        for (int i = 1; i < 12; ++i) {
            tabelaDeGrupos.add(espelhada.transpor(i));
        }
        return encontraGruposCompativeis();
    }

    private ArrayList<SerieDodecafonica> encontraGruposCompativeis() {
        SerieDodecafonica formas = new SerieDodecafonica();
        formas.adiciona(formaOriginal);

        encontraGruposCompativeis(formas);

        return listadeFormas;
    }

    private void encontraGruposCompativeis(SerieDodecafonica formas) {
        Acorde atual = null;

        for (int i = 0; i < TAMANHO_TABELA_GRUPOS; i++) {
            atual = tabelaDeGrupos.get(i);
            if (formas.eCompativel(atual)) {
                formas.adiciona(atual);
                encontraGruposCompativeis(formas);
            }
        }

        if (formas.getTamanho() == 12/formaOriginal.tamanho()) {
            listadeFormas.add(formas.copia());
        }
        formas.remove();
    }
}
