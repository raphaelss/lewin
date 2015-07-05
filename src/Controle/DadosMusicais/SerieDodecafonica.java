/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controle.DadosMusicais;

import java.util.ArrayList;

/**
 *
 * @author Bill's
 */
public class SerieDodecafonica {
    private ArrayList<Acorde> formasPrimas = new ArrayList<Acorde>();

    public boolean eCompativel(Acorde forma) {
        int tamanho = forma.tamanho();

        for (Acorde f : formasPrimas) {
            for (int i = 0; i < tamanho; i++) {
                if (f.contem(forma.getDado(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public void adiciona(Acorde forma) {
        formasPrimas.add(forma);
    }

    public void remove() {
        formasPrimas.remove(formasPrimas.size() - 1);
    }

    public String toString() {
        String resultado = "";
        int tamanhoAtual;

        for (Acorde f : formasPrimas) {
            tamanhoAtual = f.tamanho();

            for (int i = 0; i < tamanhoAtual; i++) {
                resultado += f.getDado(i) + "   ";
            }
        }

        return resultado;
    }

    public int getTamanho() {
        return formasPrimas.size();
    }

    public SerieDodecafonica copia() {
        SerieDodecafonica retorno = new SerieDodecafonica();
        retorno.formasPrimas = (ArrayList<Acorde>)this.formasPrimas.clone();

        return retorno;
    }

    public Acorde getAcorde(int indice) {
        return formasPrimas.get(indice);
    }

    public int getDado(int indice) {
        int tamanho = formasPrimas.get(0).tamanho();

        return formasPrimas.get(indice/tamanho).getDado(indice%tamanho);
    }

    public ArrayList<Integer> toIntegerList(int subTamanho) {
        ArrayList<Integer> integerList = new ArrayList<Integer>();
        Acorde corrente = null;
        int tamanho = formasPrimas.size(), indiceReal = 0;

        for (int i = 0; i < tamanho && indiceReal < subTamanho; i++) {
            corrente = formasPrimas.get(i);
            
            for (int j = 0; j < corrente.tamanho() && indiceReal < subTamanho; j++, indiceReal++) {
                integerList.add(corrente.getDado(j));
            }
        }

        return integerList;
    }
}