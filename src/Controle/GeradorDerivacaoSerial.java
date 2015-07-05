/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controle;

import Controle.DadosMusicais.Acorde;
import Controle.DadosMusicais.SerieDodecafonica;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class GeradorDerivacaoSerial {
    private Acorde formaOriginal;
    private ArrayList<Acorde> tabelaDeGrupos = new ArrayList<Acorde>();
    private ArrayList<SerieDodecafonica> listadeFormas = new ArrayList<SerieDodecafonica>();
    private final int TAMANHO_TABELA_GRUPOS = 23;

    public GeradorDerivacaoSerial(Acorde form) {
        formaOriginal = form;
    }

    public ArrayList<SerieDodecafonica> resultado() {
        if (formaOriginal.tamanho() == 3) {
            for (int i = 1; i < 12; i++) {
                tabelaDeGrupos.add(new Acorde(new int[] {(formaOriginal.getDado(0) + i) % 12,
                    (formaOriginal.getDado(1) + i) % 12, (formaOriginal.getDado(2) + i) % 12}));
            }
        }
        else if (formaOriginal.tamanho() == 4) {
            for (int i = 1; i < 12; i++) {
                tabelaDeGrupos.add(new Acorde(new int[] {(formaOriginal.getDado(0) + i) % 12,
                    (formaOriginal.getDado(1) + i) % 12, (formaOriginal.getDado(2) + i) % 12, (formaOriginal.getDado(3) + i) % 12}));
            }
        }
        else {
            for (int i = 1; i < 12; i++) {
                tabelaDeGrupos.add(new Acorde(new int[] {(formaOriginal.getDado(0) + i) % 12,
                    (formaOriginal.getDado(1) + i) % 12, (formaOriginal.getDado(2) + i) % 12, (formaOriginal.getDado(3) + i) % 12,
                    (formaOriginal.getDado(4) + i) % 12, (formaOriginal.getDado(5) + i) % 12}));
            }
        }

        Acorde espelhada = formaOriginal.espelhada();
        tabelaDeGrupos.add(espelhada);

        if (espelhada.tamanho() == 3) {
            for (int i = 1; i < 12; i++) {
                tabelaDeGrupos.add(new Acorde(new int[] {(espelhada.getDado(0) + i) % 12,
                    (espelhada.getDado(1) + i) % 12, (espelhada.getDado(2) + i) % 12}));
            }
        }
        else if (espelhada.tamanho() == 4) {
            for (int i = 1; i < 12; i++) {
                tabelaDeGrupos.add(new Acorde(new int[] {(espelhada.getDado(0) + i) % 12,
                    (espelhada.getDado(1) + i) % 12, (espelhada.getDado(2) + i) % 12, (espelhada.getDado(3) + i) % 12}));
            }
        }
        else {
            for (int i = 1; i < 12; i++) {
                tabelaDeGrupos.add(new Acorde(new int[] {(espelhada.getDado(0) + i) % 12,
                    (espelhada.getDado(1) + i) % 12, (espelhada.getDado(2) + i) % 12, (espelhada.getDado(3) + i) % 12,
                    (espelhada.getDado(4) + i) % 12, (espelhada.getDado(5) + i) % 12}));
            }
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