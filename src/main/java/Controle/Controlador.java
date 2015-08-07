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

package Controle;

import Controle.DadosMusicais.ConjuntoOrdenado;
import Controle.DadosMusicais.ClasseDeAltura;
import Controle.DadosMusicais.MatrizDodecafonica;
import Controle.DadosMusicais.MatrizDeAcordes;
import Controle.DadosMusicais.SerieDodecafonica;
import Controle.DadosMusicais.SegmentoInvariancia;
import Controle.FormasCompactas;
import Excecoes.DadosProibidos;
import java.awt.Point;
import java.util.*;
import javax.swing.JOptionPane;

public class Controlador {
    private ConjuntoOrdenado numeros = new ConjuntoOrdenado(),
                             segundoConjunto = new ConjuntoOrdenado(),
                             formaCompacta, resultadoMultiplicacao;
    private ArrayList<SerieDodecafonica> listadeFormas;
    private SerieDodecafonica serieEscolhida;
    private MatrizDodecafonica matriz;
    private ArrayList<SegmentoInvariancia> acordesALimpo;
    private ArrayList<String> informacoesAssociada, invarianciaDerivativa, invariancias;
    private ArrayList<ClasseDeAltura[]> resultadoRotacaoStravinskyana;
    private ArrayList<ArrayList<ClasseDeAltura[]>> resultadosPaleta;
    private HashSet<LinkedList<ClasseDeAltura>> subconjuntos;
    private int[] vetorIntervalar = new int[6];
    private double similaridade;

    public void limparDados() {
        numeros.clear();
        segundoConjunto.clear();
    }

    public ConjuntoOrdenado getConjuntoPrincipal() {
        return numeros;
    }

    public ConjuntoOrdenado getSegundoConjunto() {
        return segundoConjunto;
    }

    public MatrizDodecafonica getMatriz() {
        return matriz;
    }

    public void adicionaEntradaConjuntoPrincipal(ClasseDeAltura c) throws DadosProibidos {
        if (!numeros.add(c)) {
            throw new DadosProibidos("Dados repetidos não são permitidos");
        }
    }

    public void substitui_FormaCompacta() {
        numeros = formaCompacta;
    }

    public void substituir_Multiplicacao() {
        numeros = resultadoMultiplicacao;
    }

    public void substitui_DerivacaoSerial() {
        int indice = 0;
        while((indice =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Selecione o \u00edndice da s\u00e9rie a ser usada")))
                >= listadeFormas.size());

        numeros.clear();
        for (int i = 0; i < 12; i++) {
            numeros.add(listadeFormas.get(indice).get(i));
        }
    }

    public void substitui_Paleta() {
        int indice = 0;
        while((indice =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Selecione o \u00edndice")))
                >= resultadoRotacaoStravinskyana.size());

        numeros.clear();
        ClasseDeAltura[] escolhido = resultadoRotacaoStravinskyana.get(indice);
        for (int i = 0; i < escolhido.length; i++) {
            numeros.add(escolhido[i]);
        }
    }

    public void removerConjuntoPrincipal() {
        numeros.remove(numeros.size() - 1);
    }

    public void adicionaEntradaSegundoConjunto(ClasseDeAltura c) throws DadosProibidos {
        if (segundoConjunto.contains(c)) {
            throw new DadosProibidos("Dados repetidos não são permitidos");
        }
        segundoConjunto.add(c);
    }

    public boolean removerSegundoConjunto() {
        segundoConjunto.remove(segundoConjunto.size() - 1);
        return !segundoConjunto.isEmpty();
    }

    public void geraDerivacaoSerial() throws DadosProibidos {
        int tamanho = numeros.size();

        if (tamanho == 3) {
            ConjuntoOrdenado formaPrima = FormasCompactas.formaPrimaStraus(numeros);
            ClasseDeAltura segundo = formaPrima.get(1),
                           terceiro = formaPrima.get(2);
            if (segundo == ClasseDeAltura.criar(3) && terceiro == ClasseDeAltura.criar(6)) {
                throw new DadosProibidos("Impossível gerar uma série derivada deste tricorde.");
            }
        } else if (tamanho == 4) {
            int[] vetor = new ConjuntoOrdenado(numeros).vetorIntervalar();
            if (vetor[3] > 0) {
                throw new DadosProibidos("Impossível gerar uma série derivada de um tetracorde contendo a classe intervalar 4.");
            }
        }
        listadeFormas = new GeradorDerivacaoSerial(new ConjuntoOrdenado(numeros)).resultado();
    }

    public ArrayList<SerieDodecafonica> getDerivacaoSerial() {
        return listadeFormas;
    }

    public void geraMatrizDodecafonica() {
        matriz = new MatrizDodecafonica();

        ClasseDeAltura[] acorde = new ClasseDeAltura[12];

        if (numeros.size() < 12) {
            int indice = 0;
            while((indice =
                    Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Selecione o \u00edndice da s\u00e9rie de entrada da matriz")))
                    >= listadeFormas.size());

            serieEscolhida = listadeFormas.get(indice);

            for (int i = 0; i < 12; i++) {
                acorde[i] = serieEscolhida.get(i);
            }
        }
        else {
            for (int i = 0; i < 12; i++) {
                acorde[i] = numeros.get(i);
            }

            serieEscolhida = new SerieDodecafonica();
            serieEscolhida.adiciona(new ConjuntoOrdenado(acorde));
        }

        try {
            matriz.setLinha(0, acorde);

            ClasseDeAltura[] inversoProvisorio = new ClasseDeAltura[12];

            for (int i = 0; i < 12; i++) {
                inversoProvisorio[i] = serieEscolhida.get(i).inverter();
            }

            int intervalo = inversoProvisorio[0].intervalo_ord(serieEscolhida.get(0));

            for (int i = 0; i < 12; i++) {
                matriz.preenchePosicao(i, 0, inversoProvisorio[i].transpor(-intervalo));
            }

            for (int i = 1; i < 12; i++) {
                intervalo = matriz.getValor(i, 0).intervalo_ord(matriz.getValor(i - 1, 0));

                for (int j = 1; j < 12; j++) {
                    matriz.preenchePosicao(i, j, (matriz.getValor(i - 1, j).transpor(-intervalo)));
                }
            }
        }
        catch(NumberFormatException dp) {
            JOptionPane.showMessageDialog(null, "H\u00e1 dados n\u00e3o permitidos. Preencha corretamente");
        }
    }

    public void geraFormaPrimaStraus() {
        formaCompacta = FormasCompactas.formaPrimaStraus(numeros);
    }

    public void geraFormaPrimaForte() {
        formaCompacta = FormasCompactas.formaPrimaForte(numeros);
    }

    public ConjuntoOrdenado getFormaCompacta() {
        return formaCompacta;
    }

    public void geraFormaNormalStraus() {
        formaCompacta = FormasCompactas.formaNormalStraus(numeros);
    }

    public void geraFormaNormalForte() {
        formaCompacta = FormasCompactas.formaNormalForte(numeros);
    }

    public void geraRotacaoStravinskyana() {
        ConjuntoOrdenado segundaParte = new ConjuntoOrdenado(numeros.subList(6, 12)),
                                  subtraido = new ConjuntoOrdenado();
        final ClasseDeAltura primeiro = segundaParte.get(0);
        int subtrator;

        resultadoRotacaoStravinskyana = new ArrayList<ClasseDeAltura[]>();
        resultadoRotacaoStravinskyana.add(numeros.subList(0, 6).toArray(new ClasseDeAltura[6]));
        resultadoRotacaoStravinskyana.add(segundaParte.toArray(new ClasseDeAltura[6]));

        for (int i = 0; i < 5; i++) {
            segundaParte.add(segundaParte.remove(0));
            subtrator = segundaParte.get(0).intervalo_ord(primeiro);

            for (ClasseDeAltura seg : segundaParte) {
                subtraido.add(seg.transpor(subtrator));
            }

            resultadoRotacaoStravinskyana.add(subtraido.toArray(new ClasseDeAltura[6]));
            subtraido.clear();
        }
    }

    public ArrayList<ClasseDeAltura[]> getRotacaoStravinskyana() {
        return resultadoRotacaoStravinskyana;
    }

    public void geraPaleta() {
        int tamanho = numeros.size();
        ArrayList<ConjuntoOrdenado> conjuntosDiretos = new ArrayList<ConjuntoOrdenado>(),
                                      conjuntosInversos = new ArrayList<ConjuntoOrdenado>();
        ConjuntoOrdenado conjuntoReferencia = new ConjuntoOrdenado(numeros);

        adicionaTransposicoesDistintas(conjuntosDiretos, conjuntoReferencia);

        for(int i = 0; i < tamanho; i++) {
            conjuntoReferencia.set(i, conjuntoReferencia.get(i).inverter());
        }

        adicionaTransposicoesDistintas(conjuntosInversos, conjuntoReferencia);
        resultadosPaleta = new ArrayList<ArrayList<ClasseDeAltura[]>>(2);

        resultadosPaleta.add(new ArrayList<ClasseDeAltura[]>());
        for (ConjuntoOrdenado conjunto : conjuntosDiretos) {
            resultadosPaleta.get(0).add(conjunto.toArray(new ClasseDeAltura[tamanho]));
        }

        resultadosPaleta.add(new ArrayList<ClasseDeAltura[]>());
        loop:
        for (ConjuntoOrdenado conjunto : conjuntosInversos) {
            for (ConjuntoOrdenado direto : conjuntosDiretos) {
                if (direto.containsAll(conjunto)) {
                    continue loop;
                }
            }
            resultadosPaleta.get(1).add(conjunto.toArray(new ClasseDeAltura[tamanho]));
        }
    }

    public ArrayList<ArrayList<ClasseDeAltura[]>> getPaleta() {
        return resultadosPaleta;
    }

    private void adicionaTransposicoesDistintas(ArrayList<ConjuntoOrdenado> conjuntos,
                                                ConjuntoOrdenado primeiroConjunto) {
        conjuntos.add(new ConjuntoOrdenado(primeiroConjunto));
        loop:
        for (int i = 1; i < 12; i++) {
            ConjuntoOrdenado atual = new ConjuntoOrdenado();
            for (int j = 0; j < primeiroConjunto.size(); j++) {
                atual.add(primeiroConjunto.get(j).transpor(i));
            }
            for (ConjuntoOrdenado c : conjuntos) {
                if (c.containsAll(atual)) {
                    continue loop;
                }
            }
            conjuntos.add(atual);
        }
    }

    public void geraTabelaAdicao() {
        matriz = GeradorTabelaAdicao.geraTabela(numeros);
    }

    public void gerarVetorIntervalar() {
        new ConjuntoOrdenado(numeros).vetorIntervalar(vetorIntervalar);
    }

    public int[] getVetorIntervalar() {
        return vetorIntervalar;
    }

    public void geraSubconjuntos() {
        subconjuntos = new HashSet<LinkedList<ClasseDeAltura>>();

        int tamanhoSubconjuntos = 0;
        while((tamanhoSubconjuntos =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe o tamanho dos subconjuntos")))
                >= numeros.size());

        constroi(0, tamanhoSubconjuntos, new LinkedList<ClasseDeAltura>(), subconjuntos);
    }

    public HashSet<LinkedList<ClasseDeAltura>> getSubconjuntos() {
        return subconjuntos;
    }

    private void constroi(int indice, int tamanhoSubconjuntos, LinkedList<ClasseDeAltura> subconjunto, HashSet<LinkedList<ClasseDeAltura>> subconjuntos) {
        for (; indice < numeros.size(); indice++) {
            subconjunto.add(numeros.get(indice));
            if (subconjunto.size() == tamanhoSubconjuntos) {
                subconjuntos.add(new LinkedList<ClasseDeAltura>(subconjunto));
                subconjunto.removeLast();
            }
            else {
                constroi(indice + 1, tamanhoSubconjuntos, subconjunto, subconjuntos);
            }
        }

        try {
            subconjunto.removeLast();
        }
        catch(NoSuchElementException nsee) {}
    }

    public void geraSimilaridade() {
        int tamanhoPrimeiro = numeros.size(), tamanhoSegundo = segundoConjunto.size();

        if (vetorIntervalar == null) {
            vetorIntervalar = new ConjuntoOrdenado(numeros).vetorIntervalar();
        }

        int[] vetorIntervalarSegundoConjunto = new ConjuntoOrdenado(segundoConjunto).vetorIntervalar();
        double resultado = 0;

        for (int i = 0; i < 6; i++) {
            resultado += Math.sqrt(vetorIntervalar[i]*vetorIntervalarSegundoConjunto[i]);
        }
        resultado *= 2;
        resultado /= Math.sqrt(tamanhoPrimeiro*(tamanhoPrimeiro - 1)*
                                tamanhoSegundo*(tamanhoSegundo - 1));

        similaridade = resultado;
    }

    public double getSimilaridade() {
        return similaridade;
    }

    public void geraInvarianciaTranspositiva() {
        invarianciaDerivativa = new ArrayList<String>();

        int tamanhoEntrada = numeros.size(),
            quantidadeRepeticoes = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe quantidade de repeti\u00e7\u00f5es desejada"));

        if (quantidadeRepeticoes > tamanhoEntrada) {
            JOptionPane.showMessageDialog(null, "N\u00e3o \u00e9 poss\u00edvel retornar este resultado");
        }
        else {
            int [] vetInt = new ConjuntoOrdenado(numeros).vetorIntervalar();
            for (int i = 0; i < 5; i++) {
                if (vetInt[i] == quantidadeRepeticoes) {
                    invarianciaDerivativa.add("T" + (i + 1) + "  ");
                    invarianciaDerivativa.add("T" + (11 - i) + "  ");
                }

            }

            if (quantidadeRepeticoes == 2*vetInt[5]) {
                invarianciaDerivativa.add("T6");
            }

            if (quantidadeRepeticoes == tamanhoEntrada) {
                invarianciaDerivativa.add("T0");
            }
        }
    }

    public ArrayList<String> getInvarianciaDerivativa() {
        return invarianciaDerivativa;
    }

    public void geraInvarianciaInversiva() {
        invarianciaDerivativa = new ArrayList<String>();

        int tamanhoEntrada = numeros.size(),
            quantidadeRepeticoes = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe quantidade de repeti\u00e7\u00f5es desejada"));

        if (quantidadeRepeticoes > tamanhoEntrada) {
            JOptionPane.showMessageDialog(null, "N\u00e3o \u00e9 poss\u00edvel retornar este resultado");
        }
        else {
            MatrizDodecafonica tabela = GeradorTabelaAdicao.geraTabela(numeros);
            int[] contadores = new int[12];

            for (int i = 0; i < tamanhoEntrada; i++) {
                for (int j = 0; j < tamanhoEntrada; j++) {
                    contadores[tabela.getValor(i, j).inteiro()]++;
                }
            }

            for (int i = 0; i < 12; i++) {
                if (contadores[i] == quantidadeRepeticoes) {
                    invarianciaDerivativa.add("T" + i + "I  ");
                }
            }
        }
    }

    public void geraMultiplicacaoBoulez() {
        resultadoMultiplicacao = new ConjuntoOrdenado();

        for (ClasseDeAltura doSegundoFator : segundoConjunto) {
            resultadoMultiplicacao.add(doSegundoFator);

            for (int i = 1; i < numeros.size(); i++) {
                doSegundoFator = doSegundoFator.transpor(numeros.get(i).transpor(-(numeros.get(i - 1).inteiro())).inteiro());
                resultadoMultiplicacao.add(doSegundoFator);
            }
        }

        resultadoMultiplicacao.sort();
        resultadoMultiplicacao = new ConjuntoOrdenado(new HashSet<ClasseDeAltura>(resultadoMultiplicacao));
    }

    public void geraMultiplicacaoRahn1() {
        resultadoMultiplicacao = new ConjuntoOrdenado();

        ClasseDeAltura doSegundoFator = segundoConjunto.get(0);

        for (ClasseDeAltura doPrimeiroFator : numeros) {
            resultadoMultiplicacao.add(ClasseDeAltura.criar(doSegundoFator.inteiro()*doPrimeiroFator.inteiro()));
        }
    }

    public void geraMultiplicacaoRahn2() {
        int tamanho = numeros.size() + segundoConjunto.size() - 1;
        ClasseDeAltura[] resultadoParcial = new ClasseDeAltura [tamanho];
        int indiceAPartir = 0, iterandoAPartir;

        for (ClasseDeAltura doPrimeiroFator : numeros) {
            iterandoAPartir = indiceAPartir++;

            for (ClasseDeAltura doSegundoFator : segundoConjunto) {
                resultadoParcial[iterandoAPartir++].transpor(doPrimeiroFator.inteiro() * doSegundoFator.inteiro());
            }
        }

        HashSet<ClasseDeAltura> resultadoSemRepetidos = new HashSet<ClasseDeAltura>();
        for (ClasseDeAltura i : resultadoParcial) {
            resultadoSemRepetidos.add(i);
        }

        resultadoMultiplicacao = new ConjuntoOrdenado(resultadoSemRepetidos);
    }

    public ConjuntoOrdenado getMultiplicacao() {
        return resultadoMultiplicacao;
    }

    public ArrayList<Point> geraCombinatoriedade() {
        ArrayList<Point> camposColorir = new ArrayList<Point>();
        ConjuntoOrdenado hexacordeCorrente = new ConjuntoOrdenado(),
                hexacordeRejeitado = serieEscolhida.toClasseDeAlturaList(6);

        //procura nas series P
        lacoPrincipal:
        for (int i = 1; i < 12; i++) {
            hexacordeCorrente.clear();

            ClasseDeAltura[] r = matriz.getP(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 0; j < 6; j++) {
                camposColorir.add(new Point(i, j));
            }
        }

        //procura nas series R
        lacoPrincipal:
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente.clear();

            ClasseDeAltura[] r = matriz.getR(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 6; j < 12; j++) {
                camposColorir.add(new Point(i, j));
            }
        }

        //procura nas series I
        lacoPrincipal:
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente.clear();

            ClasseDeAltura[] r = matriz.getI(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 0; j < 6; j++) {
                camposColorir.add(new Point(j, i));
            }
        }

        //procura nas series RI
        lacoPrincipal:
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente.clear();

            ClasseDeAltura[] r = matriz.getRI(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 6; j < 12; j++) {
                camposColorir.add(new Point(j, i));
            }
        }

        return camposColorir;
    }

    public Object geraInvariancia() {
        informacoesAssociada = new ArrayList<String>();
        acordesALimpo = new ArrayList<SegmentoInvariancia>();
        invariancias = new ArrayList<String>();

        encontraInvariancia(3);
        encontraInvariancia(4);
        encontraInvariancia(6);

        int segmentos = acordesALimpo.size();
        String informacaoCorrente, finalLinha, inicio = "";

        for (int i = 0; i < segmentos; i++) {
            informacaoCorrente = informacoesAssociada.get(i);
            finalLinha = informacaoCorrente + "  " + "(" +
                    new StringTokenizer(informacaoCorrente).countTokens() + ")\n\n";

            invariancias.add(acordesALimpo.get(i).toString() + inicio + finalLinha);
        }

        return invariancias;
    }

    public ArrayList<String> getInvariancia() {
        return invariancias;
    }

    private void encontraInvariancia(int tamanhoSegmentos) {
        MatrizDeAcordes acordes = new MatrizDeAcordes(tamanhoSegmentos, matriz);
        ArrayList<SegmentoInvariancia> corrente = null;
        int tamanhoLinha;
        String informacaoDoCorrente = "";
        SegmentoInvariancia procurado = null;

        do {
            procurado = acordes.getProximoElemento();

            tamanhoLinha = acordes.getMatrizP().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizP().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "P" + matriz.getP(j)[0] + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizI().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizI().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "I" + matriz.getI(j)[0] + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizR().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizR().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "R" + matriz.getP(j)[0] + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizRI().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizRI().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "RI" + matriz.getI(j)[0] + " ";
                }
            }

            if (informacaoDoCorrente.length() >= 6) {
                acordesALimpo.add(procurado);
                informacoesAssociada.add(informacaoDoCorrente);
            }

            informacaoDoCorrente = "";
        }
        while (acordes.getTamanhoTotal() > 0);
    }


}
