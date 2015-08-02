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

package GUI;

import Controle.Controlador;
import Controle.DadosMusicais.ClasseDeAltura;
import Controle.DadosMusicais.MatrizDodecafonica;
import Controle.DadosMusicais.SerieDodecafonica;
import Controle.FormasCompactas;
import Excecoes.DadosProibidos;
import Excecoes.SequenciadorNulo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import java.awt.event.*;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class InterfaceGrafica extends javax.swing.JFrame {
    private JTextField[][] camposMatriz;
    private JLabel[] rotulosDasLinhas, rotulosDasColunas;
    private ArrayList<JButton> botoesEntrada = new ArrayList<JButton>();
    private TipoTabela guiEscolherTabela = new TipoTabela();
    private ClasseDeAltura.TipoRepresentacao formatoRepresentacao = ClasseDeAltura.TipoRepresentacao.Inteiro;
    private StringBuilder stringBuilder = new StringBuilder();
    private Controlador controlador = new Controlador();
    private static final String espacamento = "  ";

    public static final int SAIDA_DERIVACAO_SERIAL = 1, MATRIZ_DODECAFONICA = 2,
                            SAIDA_FORMA_COMPACTA = 3, SAIDA_PALETA = 4,
                            SAIDA_ROTACAO_STRAVINSKYANA = 5, MATRIZ_TABELA_ADICAO = 6, SAIDA_VETOR_INTERVALAR = 7,
                            SAIDA_SUBCONJUNTOS = 8, SAIDA_SIMILARIDADE = 9, SAIDA_INVARIANCIA_DERIVATIVA = 10,
                            SAIDA_MULTIPLICACAO = 11, SAIDA_INVARIANCIA = 15;
    private int[] modosAtuais = {-1, -1};
    private boolean midiDisponivel = true;

    private Sequencer sequenciador = null;
    private Synthesizer sintetizador = null;

    private void adicionarAlturaBuilder(ClasseDeAltura c) {
        stringBuilder.append(c.representacao(formatoRepresentacao));
    }

    private void adicionarAlturasBuilder(ArrayList<ClasseDeAltura> classes) {
        for (ClasseDeAltura c : classes) {
            adicionarAlturaBuilder(c);
            stringBuilder.append(espacamento);
        }
        stringBuilder.setLength(stringBuilder.length() - espacamento.length());
    }

    private void adicionarAlturasBuilder(ClasseDeAltura[] classes) {
        for (ClasseDeAltura c : classes) {
            adicionarAlturaBuilder(c);
            stringBuilder.append(espacamento);
        }
        stringBuilder.setLength(stringBuilder.length() - espacamento.length());
    }

    private void adicionarAlturasBuilder(LinkedList<ClasseDeAltura> classes) {
        for (ClasseDeAltura c : classes) {
            adicionarAlturaBuilder(c);
            stringBuilder.append(espacamento);
        }
        stringBuilder.setLength(stringBuilder.length() - espacamento.length());
    }

    private void inserirNovaClasse(int inteiro) {
        ActionEvent ev = new ActionEvent(botoesEntrada.get(inteiro), 0, null);
        if (!botaoInserirSegundoFator.isSelected()) {
            buttonListenerEntrada.actionPerformed(ev);
        }
        else {
            buttonListenerEntradaSegundoConjunto.actionPerformed(ev);
        }
    }


    private String extrairResultadoBuilder() {
        String resultado = stringBuilder.toString();
        stringBuilder.setLength(0);
        return resultado;
    }

    public InterfaceGrafica() {
        initComponents();

        botoesEntrada.addAll(new CopyOnWriteArrayList<JButton>(
               new JButton[] {botaoValor0, botaoValor1, botaoValor2, botaoValor3,
                              botaoValor4, botaoValor5, botaoValor6, botaoValor7,
                              botaoValor8, botaoValor9, botaoValor10, botaoValor11}));

        for (JButton botao : botoesEntrada) {
            botao.addActionListener(buttonListenerEntrada);
        }

        camposMatriz = new JTextField[][] {
        new JTextField[] {campoTexto_0_0_Matriz, campoTexto_0_1_Matriz, campoTexto_0_2_Matriz,
                          campoTexto_0_3_Matriz, campoTexto_0_4_Matriz, campoTexto_0_5_Matriz,
                          campoTexto_0_6_Matriz, campoTexto_0_7_Matriz, campoTexto_0_8_Matriz,
                          campoTexto_0_9_Matriz, campoTexto_0_10_Matriz, campoTexto_0_11_Matriz},
        new JTextField[] {campoTexto_1_0_Matriz, campoTexto_1_1_Matriz, campoTexto_1_2_Matriz,
                          campoTexto_1_3_Matriz, campoTexto_1_4_Matriz, campoTexto_1_5_Matriz,
                          campoTexto_1_6_Matriz, campoTexto_1_7_Matriz, campoTexto_1_8_Matriz,
                          campoTexto_1_9_Matriz, campoTexto_1_10_Matriz, campoTexto_1_11_Matriz},
        new JTextField[] {campoTexto_2_0_Matriz, campoTexto_2_1_Matriz, campoTexto_2_2_Matriz,
                          campoTexto_2_3_Matriz, campoTexto_2_4_Matriz, campoTexto_2_5_Matriz,
                          campoTexto_2_6_Matriz, campoTexto_2_7_Matriz, campoTexto_2_8_Matriz,
                          campoTexto_2_9_Matriz, campoTexto_2_10_Matriz, campoTexto_2_11_Matriz},
        new JTextField[] {campoTexto_3_0_Matriz, campoTexto_3_1_Matriz, campoTexto_3_2_Matriz,
                          campoTexto_3_3_Matriz, campoTexto_3_4_Matriz, campoTexto_3_5_Matriz,
                          campoTexto_3_6_Matriz, campoTexto_3_7_Matriz, campoTexto_3_8_Matriz,
                          campoTexto_3_9_Matriz, campoTexto_3_10_Matriz, campoTexto_3_11_Matriz},
        new JTextField[] {campoTexto_4_0_Matriz, campoTexto_4_1_Matriz, campoTexto_4_2_Matriz,
                          campoTexto_4_3_Matriz, campoTexto_4_4_Matriz, campoTexto_4_5_Matriz,
                          campoTexto_4_6_Matriz, campoTexto_4_7_Matriz, campoTexto_4_8_Matriz,
                          campoTexto_4_9_Matriz, campoTexto_4_10_Matriz, campoTexto_4_11_Matriz},
        new JTextField[] {campoTexto_5_0_Matriz, campoTexto_5_1_Matriz, campoTexto_5_2_Matriz,
                          campoTexto_5_3_Matriz, campoTexto_5_4_Matriz, campoTexto_5_5_Matriz,
                          campoTexto_5_6_Matriz, campoTexto_5_7_Matriz, campoTexto_5_8_Matriz,
                          campoTexto_5_9_Matriz, campoTexto_5_10_Matriz, campoTexto_5_11_Matriz},
        new JTextField[] {campoTexto_6_0_Matriz, campoTexto_6_1_Matriz, campoTexto_6_2_Matriz,
                          campoTexto_6_3_Matriz, campoTexto_6_4_Matriz, campoTexto_6_5_Matriz,
                          campoTexto_6_6_Matriz, campoTexto_6_7_Matriz, campoTexto_6_8_Matriz,
                          campoTexto_6_9_Matriz, campoTexto_6_10_Matriz, campoTexto_6_11_Matriz},
        new JTextField[] {campoTexto_7_0_Matriz, campoTexto_7_1_Matriz, campoTexto_7_2_Matriz,
                          campoTexto_7_3_Matriz, campoTexto_7_4_Matriz, campoTexto_7_5_Matriz,
                          campoTexto_7_6_Matriz, campoTexto_7_7_Matriz, campoTexto_7_8_Matriz,
                          campoTexto_7_9_Matriz, campoTexto_7_10_Matriz, campoTexto_7_11_Matriz},
        new JTextField[] {campoTexto_8_0_Matriz, campoTexto_8_1_Matriz, campoTexto_8_2_Matriz,
                          campoTexto_8_3_Matriz, campoTexto_8_4_Matriz, campoTexto_8_5_Matriz,
                          campoTexto_8_6_Matriz, campoTexto_8_7_Matriz, campoTexto_8_8_Matriz,
                          campoTexto_8_9_Matriz, campoTexto_8_10_Matriz, campoTexto_8_11_Matriz},
        new JTextField[] {campoTexto_9_0_Matriz, campoTexto_9_1_Matriz, campoTexto_9_2_Matriz,
                          campoTexto_9_3_Matriz, campoTexto_9_4_Matriz, campoTexto_9_5_Matriz,
                          campoTexto_9_6_Matriz, campoTexto_9_7_Matriz, campoTexto_9_8_Matriz,
                          campoTexto_9_9_Matriz, campoTexto_9_10_Matriz, campoTexto_9_11_Matriz},
        new JTextField[] {campoTexto_10_0_Matriz, campoTexto_10_1_Matriz, campoTexto_10_2_Matriz,
                          campoTexto_10_3_Matriz, campoTexto_10_4_Matriz, campoTexto_10_5_Matriz,
                          campoTexto_10_6_Matriz, campoTexto_10_7_Matriz, campoTexto_10_8_Matriz,
                          campoTexto_10_9_Matriz, campoTexto_10_10_Matriz, campoTexto_10_11_Matriz},
        new JTextField[] {campoTexto_11_0_Matriz, campoTexto_11_1_Matriz, campoTexto_11_2_Matriz,
                          campoTexto_11_3_Matriz, campoTexto_11_4_Matriz, campoTexto_11_5_Matriz,
                          campoTexto_11_6_Matriz, campoTexto_11_7_Matriz, campoTexto_11_8_Matriz,
                          campoTexto_11_9_Matriz, campoTexto_11_10_Matriz, campoTexto_11_11_Matriz}
        };

        rotulosDasLinhas = new JLabel[] {rotuloLinha_0, rotuloLinha_1, rotuloLinha_2,
                                         rotuloLinha_3, rotuloLinha_4, rotuloLinha_5,
                                         rotuloLinha_6, rotuloLinha_7, rotuloLinha_8,
                                         rotuloLinha_9, rotuloLinha_10, rotuloLinha_11};
        rotulosDasColunas = new JLabel[] {rotuloColuna_0, rotuloColuna_1, rotuloColuna_2,
                                         rotuloColuna_3, rotuloColuna_4, rotuloColuna_5,
                                         rotuloColuna_6, rotuloColuna_7, rotuloColuna_8,
                                         rotuloColuna_9, rotuloColuna_10, rotuloColuna_11};

        try {
            if ((sequenciador = MidiSystem.getSequencer()) == null) {
                throw new SequenciadorNulo("N\u00e3o foi poss\u00edvel "
                        + "obter um sequenciador");
            }

            if (!(sequenciador instanceof Synthesizer)) {
                sintetizador = MidiSystem.getSynthesizer();
            }

            sequenciador.addMetaEventListener(new MetaEventListener() {
                public void meta(MetaMessage event) {
                    if (event.getType() == 47) {
                        sequenciador.stop();
                    }
                }
            });

            tocarClasseInformada(78);
        }
        catch(SequenciadorNulo sn) {
            JOptionPane.showMessageDialog(null, "N\u00e3o ser\u00e1 poss\u00edvel reproduzir os conjuntos");
            midiDisponivel = false;
        }
        catch(MidiUnavailableException mue) {
            JOptionPane.showMessageDialog(null, "N\u00e3o ser\u00e1 poss\u00edvel reproduzir os conjuntos");
            midiDisponivel = false;
        }

        this.setResizable(false);

        KeyListener l = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    char char_valor = e.getKeyChar();
                    switch(char_valor) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            inserirNovaClasse(char_valor - '0');
                            break;
                        case 'a':
                        case 'A':
                            inserirNovaClasse(10);
                            break;
                        case 'b':
                        case 'B':
                            inserirNovaClasse(11);
                            break;
                        case 8:
                            botaoRemoverActionPerformed(null);
                    }
                }
                catch(NumberFormatException nfe) {
                    System.err.println("Erro na inserção de nova classe");
                    System.err.println(nfe.getMessage());
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        };
        areaTextoEntrada.requestFocus();
        relacionarListenerTeclado(l);
    }

    private void relacionarListenerTeclado(KeyListener listener) {
        botaoLimpar.addKeyListener(listener);
        botaoAlternarNumeroNotas.addKeyListener(listener);

        botaoGerarFormaNormalStraus.addKeyListener(listener);
        botaoGerarFormaPrimaForte.addKeyListener(listener);
        botaoGerarFormaPrimaStraus.addKeyListener(listener);
        botaoGerarSubconjuntos.addKeyListener(listener);
        botaoGerarRotacaoStravinskyana.addKeyListener(listener);
        botaoGerarTabelaAdicao.addKeyListener(listener);
        botaoGerarVetorIntervalar.addKeyListener(listener);
        botaoInvarianciaTranspositiva.addKeyListener(listener);
        botaoInvarianciaInversiva.addKeyListener(listener);
        botaoPaleta.addKeyListener(listener);

        botaoInserirSegundoFator.addKeyListener(listener);
        botaoMultiplicacaoBoulez.addKeyListener(listener);
        botaoMultiplicacaoRahn.addKeyListener(listener);
        botaoMultiplicacaoRahnEstendido.addKeyListener(listener);
        botaoGerarSimilaridade.addKeyListener(listener);

        botaoGerarDerivacaoSerial.addKeyListener(listener);
        botaoGerarMatrizDodecafonica.addKeyListener(listener);
        botaoCombinatoriedade.addKeyListener(listener);
        botaoInvariancia.addKeyListener(listener);

        botaoExportarHTML.addKeyListener(listener);
        botaoTabelasCordais.addKeyListener(listener);

        botaoValor0.addKeyListener(listener);
        botaoValor1.addKeyListener(listener);
        botaoValor2.addKeyListener(listener);
        botaoValor3.addKeyListener(listener);
        botaoValor4.addKeyListener(listener);
        botaoValor5.addKeyListener(listener);
        botaoValor6.addKeyListener(listener);
        botaoValor7.addKeyListener(listener);
        botaoValor8.addKeyListener(listener);
        botaoValor9.addKeyListener(listener);
        botaoValor10.addKeyListener(listener);
        botaoValor11.addKeyListener(listener);

        botaoRemover.addKeyListener(listener);
        botaoReproduzir.addKeyListener(listener);
        botaoSubstituirEntrada.addKeyListener(listener);

        areaTextoEntrada.addKeyListener(listener);
        areaTextoResultados.addKeyListener(listener);
        painelEntrada.addKeyListener(listener);
        painelMatriz.addKeyListener(listener);
    }

    private void tocarClasseInformada(int valor) throws MidiUnavailableException {
        try {
            Sequence sequencia = new Sequence(Sequence.PPQ, 5040, 1);
            Track trilha = sequencia.getTracks()[0];

            ShortMessage mensagem = new ShortMessage();
            mensagem.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0);
            trilha.add(new MidiEvent(mensagem, 0));

            mensagem = new ShortMessage();
            mensagem.setMessage(ShortMessage.CONTROL_CHANGE, 0, 7, 64);
            trilha.add(new MidiEvent(mensagem, 0));

            mensagem = new ShortMessage();
            mensagem.setMessage(ShortMessage.NOTE_ON, 0, valor + 48, 127);
            trilha.add(new MidiEvent(mensagem, 1));

            mensagem = new ShortMessage();
            mensagem.setMessage(ShortMessage.NOTE_OFF, 0, valor + 48, 127);
            trilha.add(new MidiEvent(mensagem, 1261));

            sequenciador.setSequence(sequencia);
            sequenciador.open();
            sintetizador.open();

            sequenciador.getTransmitter().setReceiver(sintetizador.getReceiver());
            sequenciador.start();
        }
        catch(InvalidMidiDataException imde) {}
    }

    private ActionListener buttonListenerEntrada = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                int valorEscolhido = botoesEntrada.indexOf(e.getSource());
                controlador.adicionaEntradaConjuntoPrincipal(ClasseDeAltura.criar(valorEscolhido));

                atualizaEntrada();
                atualizaHabilitacaoBotoesFuncionalidades();
            }
            catch (DadosProibidos ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    };

    private ActionListener buttonListenerEntradaSegundoConjunto = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                int valorEscolhido = botoesEntrada.indexOf(e.getSource());
                controlador.adicionaEntradaSegundoConjunto(ClasseDeAltura.criar(valorEscolhido));

                atualizaEntrada();
                atualizaHabilitacaoBotoesFuncionalidades();
            }
            catch (DadosProibidos ex) {
                Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    private void atualizaEntrada() {
        if (!botaoInserirSegundoFator.isSelected()) {
            adicionarAlturasBuilder(controlador.getConjuntoPrincipal());
        }
        else {
            stringBuilder.append("x =  ");
            adicionarAlturasBuilder(controlador.getConjuntoPrincipal());
            stringBuilder.append("\n\ny =  ");
            adicionarAlturasBuilder(controlador.getSegundoConjunto());
        }
        areaTextoEntrada.setText(extrairResultadoBuilder());
    }

    private void atualizaSaida() {
        switch(modosAtuais[0]) {
            case SAIDA_MULTIPLICACAO:
                adicionarAlturasBuilder(controlador.getMultiplicacao());
                break;
            case SAIDA_DERIVACAO_SERIAL:
                ArrayList<SerieDodecafonica> listadeFormas = controlador.getDerivacaoSerial();

                for (int i = 0; i < listadeFormas.size(); ++i) {
                    for (int j = 0; j < 12; ++j) {
                        adicionarAlturaBuilder(listadeFormas.get(i).getDado(j));
                        stringBuilder.append(espacamento);
                    }
                    stringBuilder.append("  (").append(i).append(")\n\n");
                }

                switch(listadeFormas.get(0).getTamanho()) {
                    case 4:
                        stringBuilder.append("\nAcrescentem-se permuta\u00e7\u00f5es tricordais internas");
                        break;
                    case 3:
                        stringBuilder.append("\nAcrescentem-se permuta\u00e7\u00f5es tetracordais internas");
                        break;
                    default:
                        stringBuilder.append("\nAcrescentem-se permuta\u00e7\u00f5es hexacordais internas");
                }
                break;
            case SAIDA_ROTACAO_STRAVINSKYANA:
                stringBuilder.append("\u03B1 = ");
                ArrayList<ClasseDeAltura[]> resultadoRotacaoStravinskyana = controlador.getRotacaoStravinskyana();
                adicionarAlturasBuilder(resultadoRotacaoStravinskyana.get(0));
                for (int i = 1; i < 7; i++) {
                    stringBuilder.append("\n\n\u03B2").append(i).append(" = ");
                    adicionarAlturasBuilder(resultadoRotacaoStravinskyana.get(i));
                }
                break;
            case SAIDA_PALETA:
                ArrayList<ArrayList<ClasseDeAltura[]>> resultadoPaleta = controlador.getPaleta();
                int max = resultadoPaleta.get(0).size();
                for (int i = 0; i < max; ++i) {
                    adicionarAlturasBuilder(resultadoPaleta.get(0).get(i));
                    stringBuilder.append("    ");
                    try {
                        adicionarAlturasBuilder(resultadoPaleta.get(1).get(i));
                    }
                    catch(IndexOutOfBoundsException aioobe) {}
                    stringBuilder.append("\n\n");
                }
                stringBuilder.setLength(stringBuilder.length() - 1);
                break;
            case SAIDA_INVARIANCIA:
                for (String s : controlador.getInvariancia()) {
                    stringBuilder.append(s);
                }
                break;
            case SAIDA_SUBCONJUNTOS:
                for (LinkedList<ClasseDeAltura> subconjunto : controlador.getSubconjuntos()) {
                    adicionarAlturasBuilder(subconjunto);
                    stringBuilder.append("  [");
                    adicionarAlturasBuilder(FormasCompactas.formaPrimaStraus(new ArrayList<ClasseDeAltura>(subconjunto)));
                    stringBuilder.append("]\n\n");
                }
                stringBuilder.setLength(stringBuilder.length() - 2);
                break;
            case SAIDA_FORMA_COMPACTA:
                adicionarAlturasBuilder(controlador.getFormaCompacta());
                break;
            case SAIDA_VETOR_INTERVALAR:
                for (int dado : controlador.getVetorIntervalar()) {
                    stringBuilder.append(dado).append(espacamento);
                }
                break;
            case SAIDA_INVARIANCIA_DERIVATIVA:
                for (String s : controlador.getInvarianciaDerivativa()) {
                    stringBuilder.append(s).append(espacamento);
                }
                break;
            case SAIDA_SIMILARIDADE:
                stringBuilder.append(controlador.getSimilaridade());
        }

        areaTextoResultados.setText(extrairResultadoBuilder());
    }

    private void atualizaHabilitacaoBotoesFuncionalidades() {
        int tamanhoCorrenteEntrada = controlador.getConjuntoPrincipal().size();
        if (tamanhoCorrenteEntrada > 0) {
            if (!botaoInserirSegundoFator.isSelected()) {
                botaoRemover.setEnabled(true);

                switch(tamanhoCorrenteEntrada) {
                    case 3:
                    case 4:
                    case 6:
                        botaoGerarDerivacaoSerial.setEnabled(true);
                        break;
                    default:
                        botaoGerarDerivacaoSerial.setEnabled(false);
                }

                botaoReproduzir.setEnabled(midiDisponivel);

                botaoInserirSegundoFator.setEnabled(true);
                botaoGerarMatrizDodecafonica.setEnabled(tamanhoCorrenteEntrada == 12 ||
                                                        modosAtuais[0] == SAIDA_DERIVACAO_SERIAL);
                botaoGerarRotacaoStravinskyana.setEnabled(tamanhoCorrenteEntrada == 12);
                botaoPaleta.setEnabled((tamanhoCorrenteEntrada > 2) && (tamanhoCorrenteEntrada < 12));

                atualizaOutrasFuncionalidades(true);
                setBotoesDoisFatores(false);
                atualizaBotoesFormas(true);
            }
            else {
                setBotoesDoisFatores(true);
                atualizaOutrasFuncionalidades(false);
                atualizaBotoesFormas(false);

                botaoGerarDerivacaoSerial.setEnabled(false);
                botaoGerarMatrizDodecafonica.setEnabled(false);
                botaoGerarRotacaoStravinskyana.setEnabled(false);

                botaoRemover.setEnabled(controlador.getSegundoConjunto().size() > 0);
            }
        }
        else {
            botaoGerarDerivacaoSerial.setEnabled(false);
            botaoGerarMatrizDodecafonica.setEnabled(false);
            botaoCombinatoriedade.setEnabled(false);
            botaoInvariancia.setEnabled(false);
            botaoGerarRotacaoStravinskyana.setEnabled(false);
            botaoRemover.setEnabled(false);
            botaoInserirSegundoFator.setEnabled(false);
            botaoReproduzir.setEnabled(false);
            atualizaOutrasFuncionalidades(false);
            setBotoesDoisFatores(false);
            atualizaBotoesFormas(false);
        }
    }

    private void setBotoesDoisFatores(boolean value) {
        ArrayList<ClasseDeAltura> segundoConjunto = controlador.getSegundoConjunto();

        botaoMultiplicacaoRahn.setEnabled(value && segundoConjunto.size() == 1);

        boolean habilitaBotoes = value && segundoConjunto.size() > 0;
        botaoMultiplicacaoBoulez.setEnabled(habilitaBotoes);
        botaoMultiplicacaoRahnEstendido.setEnabled(habilitaBotoes);
        botaoGerarSimilaridade.setEnabled(habilitaBotoes);
    }

    private void atualizaBotoesFormas(boolean umFator) {
        int tamanhoEntrada = controlador.getConjuntoPrincipal().size();
        boolean habilitarFormas = umFator && (tamanhoEntrada > 2) && (tamanhoEntrada < 12);
        botaoGerarFormaPrimaStraus.setEnabled(habilitarFormas);
        botaoGerarFormaPrimaForte.setEnabled(habilitarFormas);
        botaoGerarFormaNormalStraus.setEnabled(habilitarFormas);
        botaoGerarFormaNormalForte.setEnabled(habilitarFormas);
    }

    private void atualizaOutrasFuncionalidades(boolean estado) {
        botaoGerarTabelaAdicao.setEnabled(estado);
        botaoGerarVetorIntervalar.setEnabled(estado);
        botaoGerarSubconjuntos.setEnabled(estado);
        botaoInvarianciaTranspositiva.setEnabled(estado);
        botaoInvarianciaInversiva.setEnabled(estado);
    }

    private void limpaMatriz() {
        Color originalBackground = new Color(238, 238, 238);
        for (JTextField[] array : camposMatriz) {
            for (JTextField campo : array) {
                campo.setBackground(originalBackground);
                campo.setText("");
            }
        }

        for (JLabel rotulo : rotulosDasLinhas) {
            rotulo.setText("");
        }
        for (JLabel rotulo : rotulosDasColunas) {
            rotulo.setText("");
        }

        botaoCombinatoriedade.setEnabled(false);
        botaoInvariancia.setEnabled(false);

        rotuloP.setText("");
        rotuloI.setText("");
        rotuloR.setText("");
        rotuloRI.setText("");
        ((TitledBorder)painelMatriz.getBorder()).setTitle("");
        painelMatriz.repaint();

        modosAtuais[1] = -1;
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        painelMatriz = new javax.swing.JPanel();
        campoTexto_0_5_Matriz = new javax.swing.JTextField();
        campoTexto_0_6_Matriz = new javax.swing.JTextField();
        campoTexto_0_7_Matriz = new javax.swing.JTextField();
        campoTexto_0_11_Matriz = new javax.swing.JTextField();
        campoTexto_0_3_Matriz = new javax.swing.JTextField();
        campoTexto_0_8_Matriz = new javax.swing.JTextField();
        campoTexto_0_10_Matriz = new javax.swing.JTextField();
        campoTexto_0_9_Matriz = new javax.swing.JTextField();
        campoTexto_0_4_Matriz = new javax.swing.JTextField();
        campoTexto_0_2_Matriz = new javax.swing.JTextField();
        campoTexto_0_1_Matriz = new javax.swing.JTextField();
        campoTexto_1_0_Matriz = new javax.swing.JTextField();
        campoTexto_1_1_Matriz = new javax.swing.JTextField();
        campoTexto_1_2_Matriz = new javax.swing.JTextField();
        campoTexto_1_3_Matriz = new javax.swing.JTextField();
        campoTexto_1_4_Matriz = new javax.swing.JTextField();
        campoTexto_1_5_Matriz = new javax.swing.JTextField();
        campoTexto_1_6_Matriz = new javax.swing.JTextField();
        campoTexto_1_7_Matriz = new javax.swing.JTextField();
        campoTexto_1_8_Matriz = new javax.swing.JTextField();
        campoTexto_1_9_Matriz = new javax.swing.JTextField();
        campoTexto_1_10_Matriz = new javax.swing.JTextField();
        campoTexto_1_11_Matriz = new javax.swing.JTextField();
        campoTexto_2_0_Matriz = new javax.swing.JTextField();
        campoTexto_2_1_Matriz = new javax.swing.JTextField();
        campoTexto_2_2_Matriz = new javax.swing.JTextField();
        campoTexto_2_3_Matriz = new javax.swing.JTextField();
        campoTexto_2_4_Matriz = new javax.swing.JTextField();
        campoTexto_2_5_Matriz = new javax.swing.JTextField();
        campoTexto_2_6_Matriz = new javax.swing.JTextField();
        campoTexto_2_7_Matriz = new javax.swing.JTextField();
        campoTexto_2_8_Matriz = new javax.swing.JTextField();
        campoTexto_2_9_Matriz = new javax.swing.JTextField();
        campoTexto_2_10_Matriz = new javax.swing.JTextField();
        campoTexto_2_11_Matriz = new javax.swing.JTextField();
        campoTexto_3_0_Matriz = new javax.swing.JTextField();
        campoTexto_3_1_Matriz = new javax.swing.JTextField();
        campoTexto_3_2_Matriz = new javax.swing.JTextField();
        campoTexto_3_3_Matriz = new javax.swing.JTextField();
        campoTexto_3_4_Matriz = new javax.swing.JTextField();
        campoTexto_3_5_Matriz = new javax.swing.JTextField();
        campoTexto_3_6_Matriz = new javax.swing.JTextField();
        campoTexto_3_7_Matriz = new javax.swing.JTextField();
        campoTexto_3_8_Matriz = new javax.swing.JTextField();
        campoTexto_3_9_Matriz = new javax.swing.JTextField();
        campoTexto_3_10_Matriz = new javax.swing.JTextField();
        campoTexto_3_11_Matriz = new javax.swing.JTextField();
        campoTexto_4_0_Matriz = new javax.swing.JTextField();
        campoTexto_4_1_Matriz = new javax.swing.JTextField();
        campoTexto_4_2_Matriz = new javax.swing.JTextField();
        campoTexto_4_3_Matriz = new javax.swing.JTextField();
        campoTexto_4_4_Matriz = new javax.swing.JTextField();
        campoTexto_4_5_Matriz = new javax.swing.JTextField();
        campoTexto_4_6_Matriz = new javax.swing.JTextField();
        campoTexto_4_7_Matriz = new javax.swing.JTextField();
        campoTexto_4_8_Matriz = new javax.swing.JTextField();
        campoTexto_4_9_Matriz = new javax.swing.JTextField();
        campoTexto_4_10_Matriz = new javax.swing.JTextField();
        campoTexto_4_11_Matriz = new javax.swing.JTextField();
        campoTexto_5_0_Matriz = new javax.swing.JTextField();
        campoTexto_5_1_Matriz = new javax.swing.JTextField();
        campoTexto_5_2_Matriz = new javax.swing.JTextField();
        campoTexto_5_3_Matriz = new javax.swing.JTextField();
        campoTexto_5_4_Matriz = new javax.swing.JTextField();
        campoTexto_5_5_Matriz = new javax.swing.JTextField();
        campoTexto_5_6_Matriz = new javax.swing.JTextField();
        campoTexto_5_7_Matriz = new javax.swing.JTextField();
        campoTexto_5_8_Matriz = new javax.swing.JTextField();
        campoTexto_5_9_Matriz = new javax.swing.JTextField();
        campoTexto_5_10_Matriz = new javax.swing.JTextField();
        campoTexto_5_11_Matriz = new javax.swing.JTextField();
        campoTexto_6_0_Matriz = new javax.swing.JTextField();
        campoTexto_6_1_Matriz = new javax.swing.JTextField();
        campoTexto_6_2_Matriz = new javax.swing.JTextField();
        campoTexto_6_3_Matriz = new javax.swing.JTextField();
        campoTexto_6_4_Matriz = new javax.swing.JTextField();
        campoTexto_6_5_Matriz = new javax.swing.JTextField();
        campoTexto_6_6_Matriz = new javax.swing.JTextField();
        campoTexto_6_7_Matriz = new javax.swing.JTextField();
        campoTexto_6_8_Matriz = new javax.swing.JTextField();
        campoTexto_6_9_Matriz = new javax.swing.JTextField();
        campoTexto_6_10_Matriz = new javax.swing.JTextField();
        campoTexto_0_0_Matriz = new javax.swing.JTextField();
        campoTexto_6_11_Matriz = new javax.swing.JTextField();
        campoTexto_7_7_Matriz = new javax.swing.JTextField();
        campoTexto_7_4_Matriz = new javax.swing.JTextField();
        campoTexto_7_9_Matriz = new javax.swing.JTextField();
        campoTexto_8_8_Matriz = new javax.swing.JTextField();
        campoTexto_8_9_Matriz = new javax.swing.JTextField();
        campoTexto_7_10_Matriz = new javax.swing.JTextField();
        campoTexto_7_8_Matriz = new javax.swing.JTextField();
        campoTexto_8_6_Matriz = new javax.swing.JTextField();
        campoTexto_9_5_Matriz = new javax.swing.JTextField();
        campoTexto_8_7_Matriz = new javax.swing.JTextField();
        campoTexto_8_4_Matriz = new javax.swing.JTextField();
        campoTexto_7_6_Matriz = new javax.swing.JTextField();
        campoTexto_8_10_Matriz = new javax.swing.JTextField();
        campoTexto_9_3_Matriz = new javax.swing.JTextField();
        campoTexto_8_3_Matriz = new javax.swing.JTextField();
        campoTexto_7_2_Matriz = new javax.swing.JTextField();
        campoTexto_7_3_Matriz = new javax.swing.JTextField();
        campoTexto_7_5_Matriz = new javax.swing.JTextField();
        campoTexto_8_11_Matriz = new javax.swing.JTextField();
        campoTexto_9_2_Matriz = new javax.swing.JTextField();
        campoTexto_8_2_Matriz = new javax.swing.JTextField();
        campoTexto_8_1_Matriz = new javax.swing.JTextField();
        campoTexto_9_0_Matriz = new javax.swing.JTextField();
        campoTexto_8_0_Matriz = new javax.swing.JTextField();
        campoTexto_7_1_Matriz = new javax.swing.JTextField();
        campoTexto_7_0_Matriz = new javax.swing.JTextField();
        campoTexto_7_11_Matriz = new javax.swing.JTextField();
        campoTexto_9_4_Matriz = new javax.swing.JTextField();
        campoTexto_8_5_Matriz = new javax.swing.JTextField();
        campoTexto_9_1_Matriz = new javax.swing.JTextField();
        campoTexto_9_10_Matriz = new javax.swing.JTextField();
        campoTexto_9_11_Matriz = new javax.swing.JTextField();
        campoTexto_10_9_Matriz = new javax.swing.JTextField();
        campoTexto_10_10_Matriz = new javax.swing.JTextField();
        campoTexto_11_9_Matriz = new javax.swing.JTextField();
        campoTexto_9_9_Matriz = new javax.swing.JTextField();
        campoTexto_10_0_Matriz = new javax.swing.JTextField();
        campoTexto_10_8_Matriz = new javax.swing.JTextField();
        campoTexto_10_11_Matriz = new javax.swing.JTextField();
        campoTexto_11_8_Matriz = new javax.swing.JTextField();
        campoTexto_9_8_Matriz = new javax.swing.JTextField();
        campoTexto_10_6_Matriz = new javax.swing.JTextField();
        campoTexto_10_7_Matriz = new javax.swing.JTextField();
        campoTexto_11_6_Matriz = new javax.swing.JTextField();
        campoTexto_11_7_Matriz = new javax.swing.JTextField();
        campoTexto_11_5_Matriz = new javax.swing.JTextField();
        campoTexto_11_10_Matriz = new javax.swing.JTextField();
        campoTexto_11_0_Matriz = new javax.swing.JTextField();
        campoTexto_10_5_Matriz = new javax.swing.JTextField();
        campoTexto_9_7_Matriz = new javax.swing.JTextField();
        campoTexto_10_4_Matriz = new javax.swing.JTextField();
        campoTexto_9_6_Matriz = new javax.swing.JTextField();
        campoTexto_10_3_Matriz = new javax.swing.JTextField();
        campoTexto_11_4_Matriz = new javax.swing.JTextField();
        campoTexto_11_11_Matriz = new javax.swing.JTextField();
        campoTexto_11_3_Matriz = new javax.swing.JTextField();
        campoTexto_10_2_Matriz = new javax.swing.JTextField();
        campoTexto_11_2_Matriz = new javax.swing.JTextField();
        campoTexto_10_1_Matriz = new javax.swing.JTextField();
        campoTexto_11_1_Matriz = new javax.swing.JTextField();
        rotuloP = new javax.swing.JLabel();
        rotuloColuna_6 = new javax.swing.JLabel();
        rotuloR = new javax.swing.JLabel();
        rotuloRI = new javax.swing.JLabel();
        rotuloLinha_11 = new javax.swing.JLabel();
        rotuloLinha_10 = new javax.swing.JLabel();
        rotuloLinha_9 = new javax.swing.JLabel();
        rotuloLinha_8 = new javax.swing.JLabel();
        rotuloLinha_7 = new javax.swing.JLabel();
        rotuloLinha_6 = new javax.swing.JLabel();
        rotuloLinha_5 = new javax.swing.JLabel();
        rotuloLinha_4 = new javax.swing.JLabel();
        rotuloLinha_3 = new javax.swing.JLabel();
        rotuloLinha_2 = new javax.swing.JLabel();
        rotuloLinha_1 = new javax.swing.JLabel();
        rotuloLinha_0 = new javax.swing.JLabel();
        rotuloColuna_0 = new javax.swing.JLabel();
        rotuloColuna_1 = new javax.swing.JLabel();
        rotuloColuna_2 = new javax.swing.JLabel();
        rotuloColuna_3 = new javax.swing.JLabel();
        rotuloColuna_4 = new javax.swing.JLabel();
        rotuloColuna_5 = new javax.swing.JLabel();
        rotuloColuna_7 = new javax.swing.JLabel();
        rotuloColuna_8 = new javax.swing.JLabel();
        rotuloColuna_9 = new javax.swing.JLabel();
        rotuloColuna_10 = new javax.swing.JLabel();
        rotuloColuna_11 = new javax.swing.JLabel();
        rotuloI = new javax.swing.JLabel();
        scrollPaneInvariancia = new javax.swing.JScrollPane();
        areaTextoResultados = new javax.swing.JTextArea();
        painelEntrada = new javax.swing.JPanel();
        botaoValor9 = new javax.swing.JButton();
        botaoValor10 = new javax.swing.JButton();
        botaoValor11 = new javax.swing.JButton();
        botaoValor8 = new javax.swing.JButton();
        botaoValor7 = new javax.swing.JButton();
        botaoValor6 = new javax.swing.JButton();
        botaoValor3 = new javax.swing.JButton();
        botaoValor4 = new javax.swing.JButton();
        botaoValor5 = new javax.swing.JButton();
        botaoValor2 = new javax.swing.JButton();
        botaoValor1 = new javax.swing.JButton();
        botaoValor0 = new javax.swing.JButton();
        botaoRemover = new javax.swing.JButton();
        botaoSubstituirEntrada = new javax.swing.JButton();
        scrollPaneEntrada = new javax.swing.JScrollPane();
        areaTextoEntrada = new javax.swing.JTextArea();
        botaoReproduzir = new javax.swing.JButton();
        barraDeFerramentas = new javax.swing.JToolBar();
        botaoLimpar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        botaoAlternarNumeroNotas = new javax.swing.JToggleButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        botaoGerarFormaNormalStraus = new javax.swing.JButton();
        botaoGerarFormaNormalForte = new javax.swing.JButton();
        botaoGerarFormaPrimaStraus = new javax.swing.JButton();
        botaoGerarFormaPrimaForte = new javax.swing.JButton();
        botaoGerarSubconjuntos = new javax.swing.JButton();
        botaoGerarRotacaoStravinskyana = new javax.swing.JButton();
        botaoGerarTabelaAdicao = new javax.swing.JButton();
        botaoGerarVetorIntervalar = new javax.swing.JButton();
        botaoInvarianciaTranspositiva = new javax.swing.JButton();
        botaoInvarianciaInversiva = new javax.swing.JButton();
        botaoPaleta = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        botaoInserirSegundoFator = new javax.swing.JToggleButton();
        botaoMultiplicacaoBoulez = new javax.swing.JButton();
        botaoMultiplicacaoRahn = new javax.swing.JButton();
        botaoMultiplicacaoRahnEstendido = new javax.swing.JButton();
        botaoGerarSimilaridade = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        botaoGerarDerivacaoSerial = new javax.swing.JButton();
        botaoGerarMatrizDodecafonica = new javax.swing.JButton();
        botaoCombinatoriedade = new javax.swing.JButton();
        botaoInvariancia = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        botaoExportarHTML = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        botaoTabelasCordais = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setLayout(null);

        painelMatriz.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        painelMatriz.setLayout(null);

        campoTexto_0_5_Matriz.setEditable(false);
        campoTexto_0_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_5_Matriz);
        campoTexto_0_5_Matriz.setBounds(205, 40, 25, 15);

        campoTexto_0_6_Matriz.setEditable(false);
        campoTexto_0_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_6_Matriz);
        campoTexto_0_6_Matriz.setBounds(240, 40, 25, 15);

        campoTexto_0_7_Matriz.setEditable(false);
        campoTexto_0_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_7_Matriz);
        campoTexto_0_7_Matriz.setBounds(275, 40, 25, 15);

        campoTexto_0_11_Matriz.setEditable(false);
        campoTexto_0_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_11_Matriz);
        campoTexto_0_11_Matriz.setBounds(415, 40, 25, 15);

        campoTexto_0_3_Matriz.setEditable(false);
        campoTexto_0_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_3_Matriz);
        campoTexto_0_3_Matriz.setBounds(135, 40, 25, 15);

        campoTexto_0_8_Matriz.setEditable(false);
        campoTexto_0_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_8_Matriz);
        campoTexto_0_8_Matriz.setBounds(310, 40, 25, 15);

        campoTexto_0_10_Matriz.setEditable(false);
        campoTexto_0_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_10_Matriz);
        campoTexto_0_10_Matriz.setBounds(380, 40, 25, 15);

        campoTexto_0_9_Matriz.setEditable(false);
        campoTexto_0_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_9_Matriz);
        campoTexto_0_9_Matriz.setBounds(345, 40, 25, 15);

        campoTexto_0_4_Matriz.setEditable(false);
        campoTexto_0_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_4_Matriz);
        campoTexto_0_4_Matriz.setBounds(170, 40, 25, 15);

        campoTexto_0_2_Matriz.setEditable(false);
        campoTexto_0_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_2_Matriz);
        campoTexto_0_2_Matriz.setBounds(100, 40, 25, 15);

        campoTexto_0_1_Matriz.setEditable(false);
        campoTexto_0_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_1_Matriz);
        campoTexto_0_1_Matriz.setBounds(65, 40, 25, 15);

        campoTexto_1_0_Matriz.setEditable(false);
        campoTexto_1_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_0_Matriz);
        campoTexto_1_0_Matriz.setBounds(30, 65, 25, 15);

        campoTexto_1_1_Matriz.setEditable(false);
        campoTexto_1_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_1_Matriz);
        campoTexto_1_1_Matriz.setBounds(65, 65, 25, 15);

        campoTexto_1_2_Matriz.setEditable(false);
        campoTexto_1_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_2_Matriz);
        campoTexto_1_2_Matriz.setBounds(100, 65, 25, 15);

        campoTexto_1_3_Matriz.setEditable(false);
        campoTexto_1_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_3_Matriz);
        campoTexto_1_3_Matriz.setBounds(135, 65, 25, 15);

        campoTexto_1_4_Matriz.setEditable(false);
        campoTexto_1_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_4_Matriz);
        campoTexto_1_4_Matriz.setBounds(170, 65, 25, 15);

        campoTexto_1_5_Matriz.setEditable(false);
        campoTexto_1_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_5_Matriz);
        campoTexto_1_5_Matriz.setBounds(205, 65, 25, 15);

        campoTexto_1_6_Matriz.setEditable(false);
        campoTexto_1_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_6_Matriz);
        campoTexto_1_6_Matriz.setBounds(240, 65, 25, 15);

        campoTexto_1_7_Matriz.setEditable(false);
        campoTexto_1_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_7_Matriz);
        campoTexto_1_7_Matriz.setBounds(275, 65, 25, 15);

        campoTexto_1_8_Matriz.setEditable(false);
        campoTexto_1_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_8_Matriz);
        campoTexto_1_8_Matriz.setBounds(310, 65, 25, 15);

        campoTexto_1_9_Matriz.setEditable(false);
        campoTexto_1_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_9_Matriz);
        campoTexto_1_9_Matriz.setBounds(345, 65, 25, 15);

        campoTexto_1_10_Matriz.setEditable(false);
        campoTexto_1_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_10_Matriz);
        campoTexto_1_10_Matriz.setBounds(380, 65, 25, 15);

        campoTexto_1_11_Matriz.setEditable(false);
        campoTexto_1_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_1_11_Matriz);
        campoTexto_1_11_Matriz.setBounds(415, 65, 25, 15);

        campoTexto_2_0_Matriz.setEditable(false);
        campoTexto_2_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_0_Matriz);
        campoTexto_2_0_Matriz.setBounds(30, 90, 25, 15);

        campoTexto_2_1_Matriz.setEditable(false);
        campoTexto_2_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_1_Matriz);
        campoTexto_2_1_Matriz.setBounds(65, 90, 25, 15);

        campoTexto_2_2_Matriz.setEditable(false);
        campoTexto_2_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_2_Matriz);
        campoTexto_2_2_Matriz.setBounds(100, 90, 25, 15);

        campoTexto_2_3_Matriz.setEditable(false);
        campoTexto_2_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_3_Matriz);
        campoTexto_2_3_Matriz.setBounds(135, 90, 25, 15);

        campoTexto_2_4_Matriz.setEditable(false);
        campoTexto_2_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_4_Matriz);
        campoTexto_2_4_Matriz.setBounds(170, 90, 25, 15);

        campoTexto_2_5_Matriz.setEditable(false);
        campoTexto_2_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_5_Matriz);
        campoTexto_2_5_Matriz.setBounds(205, 90, 25, 15);

        campoTexto_2_6_Matriz.setEditable(false);
        campoTexto_2_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_6_Matriz);
        campoTexto_2_6_Matriz.setBounds(240, 90, 25, 15);

        campoTexto_2_7_Matriz.setEditable(false);
        campoTexto_2_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_7_Matriz);
        campoTexto_2_7_Matriz.setBounds(275, 90, 25, 15);

        campoTexto_2_8_Matriz.setEditable(false);
        campoTexto_2_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_8_Matriz);
        campoTexto_2_8_Matriz.setBounds(310, 90, 25, 15);

        campoTexto_2_9_Matriz.setEditable(false);
        campoTexto_2_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_9_Matriz);
        campoTexto_2_9_Matriz.setBounds(345, 90, 25, 15);

        campoTexto_2_10_Matriz.setEditable(false);
        campoTexto_2_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_10_Matriz);
        campoTexto_2_10_Matriz.setBounds(380, 90, 25, 15);

        campoTexto_2_11_Matriz.setEditable(false);
        campoTexto_2_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_2_11_Matriz);
        campoTexto_2_11_Matriz.setBounds(415, 90, 25, 15);

        campoTexto_3_0_Matriz.setEditable(false);
        campoTexto_3_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_0_Matriz);
        campoTexto_3_0_Matriz.setBounds(30, 115, 25, 15);

        campoTexto_3_1_Matriz.setEditable(false);
        campoTexto_3_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_1_Matriz);
        campoTexto_3_1_Matriz.setBounds(65, 115, 25, 15);

        campoTexto_3_2_Matriz.setEditable(false);
        campoTexto_3_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_2_Matriz);
        campoTexto_3_2_Matriz.setBounds(100, 115, 25, 15);

        campoTexto_3_3_Matriz.setEditable(false);
        campoTexto_3_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_3_Matriz);
        campoTexto_3_3_Matriz.setBounds(135, 115, 25, 15);

        campoTexto_3_4_Matriz.setEditable(false);
        campoTexto_3_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_4_Matriz);
        campoTexto_3_4_Matriz.setBounds(170, 115, 25, 15);

        campoTexto_3_5_Matriz.setEditable(false);
        campoTexto_3_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_5_Matriz);
        campoTexto_3_5_Matriz.setBounds(205, 115, 25, 15);

        campoTexto_3_6_Matriz.setEditable(false);
        campoTexto_3_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_6_Matriz);
        campoTexto_3_6_Matriz.setBounds(240, 115, 25, 15);

        campoTexto_3_7_Matriz.setEditable(false);
        campoTexto_3_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_7_Matriz);
        campoTexto_3_7_Matriz.setBounds(275, 115, 25, 15);

        campoTexto_3_8_Matriz.setEditable(false);
        campoTexto_3_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_8_Matriz);
        campoTexto_3_8_Matriz.setBounds(310, 115, 25, 15);

        campoTexto_3_9_Matriz.setEditable(false);
        campoTexto_3_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_9_Matriz);
        campoTexto_3_9_Matriz.setBounds(345, 115, 25, 15);

        campoTexto_3_10_Matriz.setEditable(false);
        campoTexto_3_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_10_Matriz);
        campoTexto_3_10_Matriz.setBounds(380, 115, 25, 15);

        campoTexto_3_11_Matriz.setEditable(false);
        campoTexto_3_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_3_11_Matriz);
        campoTexto_3_11_Matriz.setBounds(415, 115, 25, 15);

        campoTexto_4_0_Matriz.setEditable(false);
        campoTexto_4_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_0_Matriz);
        campoTexto_4_0_Matriz.setBounds(30, 140, 25, 15);

        campoTexto_4_1_Matriz.setEditable(false);
        campoTexto_4_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_1_Matriz);
        campoTexto_4_1_Matriz.setBounds(65, 140, 25, 15);

        campoTexto_4_2_Matriz.setEditable(false);
        campoTexto_4_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_2_Matriz);
        campoTexto_4_2_Matriz.setBounds(100, 140, 25, 15);

        campoTexto_4_3_Matriz.setEditable(false);
        campoTexto_4_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_3_Matriz);
        campoTexto_4_3_Matriz.setBounds(135, 140, 25, 15);

        campoTexto_4_4_Matriz.setEditable(false);
        campoTexto_4_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_4_Matriz);
        campoTexto_4_4_Matriz.setBounds(170, 140, 25, 15);

        campoTexto_4_5_Matriz.setEditable(false);
        campoTexto_4_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_5_Matriz);
        campoTexto_4_5_Matriz.setBounds(205, 140, 25, 15);

        campoTexto_4_6_Matriz.setEditable(false);
        campoTexto_4_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_6_Matriz);
        campoTexto_4_6_Matriz.setBounds(240, 140, 25, 15);

        campoTexto_4_7_Matriz.setEditable(false);
        campoTexto_4_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_7_Matriz);
        campoTexto_4_7_Matriz.setBounds(275, 140, 25, 15);

        campoTexto_4_8_Matriz.setEditable(false);
        campoTexto_4_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_8_Matriz);
        campoTexto_4_8_Matriz.setBounds(310, 140, 25, 15);

        campoTexto_4_9_Matriz.setEditable(false);
        campoTexto_4_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_9_Matriz);
        campoTexto_4_9_Matriz.setBounds(345, 140, 25, 15);

        campoTexto_4_10_Matriz.setEditable(false);
        campoTexto_4_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_10_Matriz);
        campoTexto_4_10_Matriz.setBounds(380, 140, 25, 15);

        campoTexto_4_11_Matriz.setEditable(false);
        campoTexto_4_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_4_11_Matriz);
        campoTexto_4_11_Matriz.setBounds(415, 140, 25, 15);

        campoTexto_5_0_Matriz.setEditable(false);
        campoTexto_5_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_0_Matriz);
        campoTexto_5_0_Matriz.setBounds(30, 165, 25, 15);

        campoTexto_5_1_Matriz.setEditable(false);
        campoTexto_5_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_1_Matriz);
        campoTexto_5_1_Matriz.setBounds(65, 165, 25, 15);

        campoTexto_5_2_Matriz.setEditable(false);
        campoTexto_5_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_2_Matriz);
        campoTexto_5_2_Matriz.setBounds(100, 165, 25, 15);

        campoTexto_5_3_Matriz.setEditable(false);
        campoTexto_5_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_3_Matriz);
        campoTexto_5_3_Matriz.setBounds(135, 165, 25, 15);

        campoTexto_5_4_Matriz.setEditable(false);
        campoTexto_5_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_4_Matriz);
        campoTexto_5_4_Matriz.setBounds(170, 165, 25, 15);

        campoTexto_5_5_Matriz.setEditable(false);
        campoTexto_5_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_5_Matriz);
        campoTexto_5_5_Matriz.setBounds(205, 165, 25, 15);

        campoTexto_5_6_Matriz.setEditable(false);
        campoTexto_5_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_6_Matriz);
        campoTexto_5_6_Matriz.setBounds(240, 165, 25, 15);

        campoTexto_5_7_Matriz.setEditable(false);
        campoTexto_5_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_7_Matriz);
        campoTexto_5_7_Matriz.setBounds(275, 165, 25, 15);

        campoTexto_5_8_Matriz.setEditable(false);
        campoTexto_5_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_8_Matriz);
        campoTexto_5_8_Matriz.setBounds(310, 165, 25, 15);

        campoTexto_5_9_Matriz.setEditable(false);
        campoTexto_5_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_9_Matriz);
        campoTexto_5_9_Matriz.setBounds(345, 165, 25, 15);

        campoTexto_5_10_Matriz.setEditable(false);
        campoTexto_5_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_10_Matriz);
        campoTexto_5_10_Matriz.setBounds(380, 165, 25, 15);

        campoTexto_5_11_Matriz.setEditable(false);
        campoTexto_5_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_5_11_Matriz);
        campoTexto_5_11_Matriz.setBounds(415, 165, 25, 15);

        campoTexto_6_0_Matriz.setEditable(false);
        campoTexto_6_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_0_Matriz);
        campoTexto_6_0_Matriz.setBounds(30, 190, 25, 15);

        campoTexto_6_1_Matriz.setEditable(false);
        campoTexto_6_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_1_Matriz);
        campoTexto_6_1_Matriz.setBounds(65, 190, 25, 15);

        campoTexto_6_2_Matriz.setEditable(false);
        campoTexto_6_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_2_Matriz);
        campoTexto_6_2_Matriz.setBounds(100, 190, 25, 15);

        campoTexto_6_3_Matriz.setEditable(false);
        campoTexto_6_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_3_Matriz);
        campoTexto_6_3_Matriz.setBounds(135, 190, 25, 15);

        campoTexto_6_4_Matriz.setEditable(false);
        campoTexto_6_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_4_Matriz);
        campoTexto_6_4_Matriz.setBounds(170, 190, 25, 15);

        campoTexto_6_5_Matriz.setEditable(false);
        campoTexto_6_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_5_Matriz);
        campoTexto_6_5_Matriz.setBounds(205, 190, 25, 15);

        campoTexto_6_6_Matriz.setEditable(false);
        campoTexto_6_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_6_Matriz);
        campoTexto_6_6_Matriz.setBounds(240, 190, 25, 15);

        campoTexto_6_7_Matriz.setEditable(false);
        campoTexto_6_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_7_Matriz);
        campoTexto_6_7_Matriz.setBounds(275, 190, 25, 15);

        campoTexto_6_8_Matriz.setEditable(false);
        campoTexto_6_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_8_Matriz);
        campoTexto_6_8_Matriz.setBounds(310, 190, 25, 15);

        campoTexto_6_9_Matriz.setEditable(false);
        campoTexto_6_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_9_Matriz);
        campoTexto_6_9_Matriz.setBounds(345, 190, 25, 15);

        campoTexto_6_10_Matriz.setEditable(false);
        campoTexto_6_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_10_Matriz);
        campoTexto_6_10_Matriz.setBounds(380, 190, 25, 15);

        campoTexto_0_0_Matriz.setEditable(false);
        campoTexto_0_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_0_0_Matriz);
        campoTexto_0_0_Matriz.setBounds(30, 40, 25, 15);

        campoTexto_6_11_Matriz.setEditable(false);
        campoTexto_6_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_6_11_Matriz);
        campoTexto_6_11_Matriz.setBounds(415, 190, 25, 15);

        campoTexto_7_7_Matriz.setEditable(false);
        campoTexto_7_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_7_Matriz);
        campoTexto_7_7_Matriz.setBounds(275, 215, 25, 15);

        campoTexto_7_4_Matriz.setEditable(false);
        campoTexto_7_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_4_Matriz);
        campoTexto_7_4_Matriz.setBounds(170, 215, 25, 15);

        campoTexto_7_9_Matriz.setEditable(false);
        campoTexto_7_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_9_Matriz);
        campoTexto_7_9_Matriz.setBounds(345, 215, 25, 15);

        campoTexto_8_8_Matriz.setEditable(false);
        campoTexto_8_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_8_Matriz);
        campoTexto_8_8_Matriz.setBounds(310, 240, 25, 15);

        campoTexto_8_9_Matriz.setEditable(false);
        campoTexto_8_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_9_Matriz);
        campoTexto_8_9_Matriz.setBounds(345, 240, 25, 15);

        campoTexto_7_10_Matriz.setEditable(false);
        campoTexto_7_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_10_Matriz);
        campoTexto_7_10_Matriz.setBounds(380, 215, 25, 15);

        campoTexto_7_8_Matriz.setEditable(false);
        campoTexto_7_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_8_Matriz);
        campoTexto_7_8_Matriz.setBounds(310, 215, 25, 15);

        campoTexto_8_6_Matriz.setEditable(false);
        campoTexto_8_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_6_Matriz);
        campoTexto_8_6_Matriz.setBounds(240, 240, 25, 15);

        campoTexto_9_5_Matriz.setEditable(false);
        campoTexto_9_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_5_Matriz);
        campoTexto_9_5_Matriz.setBounds(205, 265, 25, 15);

        campoTexto_8_7_Matriz.setEditable(false);
        campoTexto_8_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_7_Matriz);
        campoTexto_8_7_Matriz.setBounds(275, 240, 25, 15);

        campoTexto_8_4_Matriz.setEditable(false);
        campoTexto_8_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_4_Matriz);
        campoTexto_8_4_Matriz.setBounds(170, 240, 25, 15);

        campoTexto_7_6_Matriz.setEditable(false);
        campoTexto_7_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_6_Matriz);
        campoTexto_7_6_Matriz.setBounds(240, 215, 25, 15);

        campoTexto_8_10_Matriz.setEditable(false);
        campoTexto_8_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_10_Matriz);
        campoTexto_8_10_Matriz.setBounds(380, 240, 25, 15);

        campoTexto_9_3_Matriz.setEditable(false);
        campoTexto_9_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_3_Matriz);
        campoTexto_9_3_Matriz.setBounds(135, 265, 25, 15);

        campoTexto_8_3_Matriz.setEditable(false);
        campoTexto_8_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_3_Matriz);
        campoTexto_8_3_Matriz.setBounds(135, 240, 25, 15);

        campoTexto_7_2_Matriz.setEditable(false);
        campoTexto_7_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_2_Matriz);
        campoTexto_7_2_Matriz.setBounds(100, 215, 25, 15);

        campoTexto_7_3_Matriz.setEditable(false);
        campoTexto_7_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_3_Matriz);
        campoTexto_7_3_Matriz.setBounds(135, 215, 25, 15);

        campoTexto_7_5_Matriz.setEditable(false);
        campoTexto_7_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_5_Matriz);
        campoTexto_7_5_Matriz.setBounds(205, 215, 25, 15);

        campoTexto_8_11_Matriz.setEditable(false);
        campoTexto_8_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_11_Matriz);
        campoTexto_8_11_Matriz.setBounds(415, 240, 25, 15);

        campoTexto_9_2_Matriz.setEditable(false);
        campoTexto_9_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_2_Matriz);
        campoTexto_9_2_Matriz.setBounds(100, 265, 25, 15);

        campoTexto_8_2_Matriz.setEditable(false);
        campoTexto_8_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_2_Matriz);
        campoTexto_8_2_Matriz.setBounds(100, 240, 25, 15);

        campoTexto_8_1_Matriz.setEditable(false);
        campoTexto_8_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_1_Matriz);
        campoTexto_8_1_Matriz.setBounds(65, 240, 25, 15);

        campoTexto_9_0_Matriz.setEditable(false);
        campoTexto_9_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_0_Matriz);
        campoTexto_9_0_Matriz.setBounds(30, 265, 25, 15);

        campoTexto_8_0_Matriz.setEditable(false);
        campoTexto_8_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_0_Matriz);
        campoTexto_8_0_Matriz.setBounds(30, 240, 25, 15);

        campoTexto_7_1_Matriz.setEditable(false);
        campoTexto_7_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_1_Matriz);
        campoTexto_7_1_Matriz.setBounds(65, 215, 25, 15);

        campoTexto_7_0_Matriz.setEditable(false);
        campoTexto_7_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_0_Matriz);
        campoTexto_7_0_Matriz.setBounds(30, 215, 25, 15);

        campoTexto_7_11_Matriz.setEditable(false);
        campoTexto_7_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_7_11_Matriz);
        campoTexto_7_11_Matriz.setBounds(415, 215, 25, 15);

        campoTexto_9_4_Matriz.setEditable(false);
        campoTexto_9_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_4_Matriz);
        campoTexto_9_4_Matriz.setBounds(170, 265, 25, 15);

        campoTexto_8_5_Matriz.setEditable(false);
        campoTexto_8_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_8_5_Matriz);
        campoTexto_8_5_Matriz.setBounds(205, 240, 25, 15);

        campoTexto_9_1_Matriz.setEditable(false);
        campoTexto_9_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_1_Matriz);
        campoTexto_9_1_Matriz.setBounds(65, 265, 25, 15);

        campoTexto_9_10_Matriz.setEditable(false);
        campoTexto_9_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_10_Matriz);
        campoTexto_9_10_Matriz.setBounds(380, 265, 25, 15);

        campoTexto_9_11_Matriz.setEditable(false);
        campoTexto_9_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_11_Matriz);
        campoTexto_9_11_Matriz.setBounds(415, 265, 25, 15);

        campoTexto_10_9_Matriz.setEditable(false);
        campoTexto_10_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_9_Matriz);
        campoTexto_10_9_Matriz.setBounds(345, 290, 25, 15);

        campoTexto_10_10_Matriz.setEditable(false);
        campoTexto_10_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_10_Matriz);
        campoTexto_10_10_Matriz.setBounds(380, 290, 25, 15);

        campoTexto_11_9_Matriz.setEditable(false);
        campoTexto_11_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_9_Matriz);
        campoTexto_11_9_Matriz.setBounds(345, 315, 25, 15);

        campoTexto_9_9_Matriz.setEditable(false);
        campoTexto_9_9_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_9_Matriz);
        campoTexto_9_9_Matriz.setBounds(345, 265, 25, 15);

        campoTexto_10_0_Matriz.setEditable(false);
        campoTexto_10_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_0_Matriz);
        campoTexto_10_0_Matriz.setBounds(30, 290, 25, 15);

        campoTexto_10_8_Matriz.setEditable(false);
        campoTexto_10_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_8_Matriz);
        campoTexto_10_8_Matriz.setBounds(310, 290, 25, 15);

        campoTexto_10_11_Matriz.setEditable(false);
        campoTexto_10_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_11_Matriz);
        campoTexto_10_11_Matriz.setBounds(415, 290, 25, 15);

        campoTexto_11_8_Matriz.setEditable(false);
        campoTexto_11_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_8_Matriz);
        campoTexto_11_8_Matriz.setBounds(310, 315, 25, 15);

        campoTexto_9_8_Matriz.setEditable(false);
        campoTexto_9_8_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_8_Matriz);
        campoTexto_9_8_Matriz.setBounds(310, 265, 25, 15);

        campoTexto_10_6_Matriz.setEditable(false);
        campoTexto_10_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_6_Matriz);
        campoTexto_10_6_Matriz.setBounds(240, 290, 25, 15);

        campoTexto_10_7_Matriz.setEditable(false);
        campoTexto_10_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_7_Matriz);
        campoTexto_10_7_Matriz.setBounds(275, 290, 25, 15);

        campoTexto_11_6_Matriz.setEditable(false);
        campoTexto_11_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_6_Matriz);
        campoTexto_11_6_Matriz.setBounds(240, 315, 25, 15);

        campoTexto_11_7_Matriz.setEditable(false);
        campoTexto_11_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_7_Matriz);
        campoTexto_11_7_Matriz.setBounds(275, 315, 25, 15);

        campoTexto_11_5_Matriz.setEditable(false);
        campoTexto_11_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_5_Matriz);
        campoTexto_11_5_Matriz.setBounds(205, 315, 25, 15);

        campoTexto_11_10_Matriz.setEditable(false);
        campoTexto_11_10_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_10_Matriz);
        campoTexto_11_10_Matriz.setBounds(380, 315, 25, 15);

        campoTexto_11_0_Matriz.setEditable(false);
        campoTexto_11_0_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_0_Matriz);
        campoTexto_11_0_Matriz.setBounds(30, 315, 25, 15);

        campoTexto_10_5_Matriz.setEditable(false);
        campoTexto_10_5_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_5_Matriz);
        campoTexto_10_5_Matriz.setBounds(205, 290, 25, 15);

        campoTexto_9_7_Matriz.setEditable(false);
        campoTexto_9_7_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_7_Matriz);
        campoTexto_9_7_Matriz.setBounds(275, 265, 25, 15);

        campoTexto_10_4_Matriz.setEditable(false);
        campoTexto_10_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_4_Matriz);
        campoTexto_10_4_Matriz.setBounds(170, 290, 25, 15);

        campoTexto_9_6_Matriz.setEditable(false);
        campoTexto_9_6_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_9_6_Matriz);
        campoTexto_9_6_Matriz.setBounds(240, 265, 25, 15);

        campoTexto_10_3_Matriz.setEditable(false);
        campoTexto_10_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_3_Matriz);
        campoTexto_10_3_Matriz.setBounds(135, 290, 25, 15);

        campoTexto_11_4_Matriz.setEditable(false);
        campoTexto_11_4_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_4_Matriz);
        campoTexto_11_4_Matriz.setBounds(170, 315, 25, 15);

        campoTexto_11_11_Matriz.setEditable(false);
        campoTexto_11_11_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_11_Matriz);
        campoTexto_11_11_Matriz.setBounds(415, 315, 25, 15);

        campoTexto_11_3_Matriz.setEditable(false);
        campoTexto_11_3_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_3_Matriz);
        campoTexto_11_3_Matriz.setBounds(135, 315, 25, 15);

        campoTexto_10_2_Matriz.setEditable(false);
        campoTexto_10_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_2_Matriz);
        campoTexto_10_2_Matriz.setBounds(100, 290, 25, 15);

        campoTexto_11_2_Matriz.setEditable(false);
        campoTexto_11_2_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_2_Matriz);
        campoTexto_11_2_Matriz.setBounds(100, 315, 25, 15);

        campoTexto_10_1_Matriz.setEditable(false);
        campoTexto_10_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_10_1_Matriz);
        campoTexto_10_1_Matriz.setBounds(65, 290, 25, 15);

        campoTexto_11_1_Matriz.setEditable(false);
        campoTexto_11_1_Matriz.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(campoTexto_11_1_Matriz);
        campoTexto_11_1_Matriz.setBounds(65, 315, 25, 15);

        rotuloP.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        painelMatriz.add(rotuloP);
        rotuloP.setBounds(10, 175, 10, 13);

        rotuloColuna_6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_6);
        rotuloColuna_6.setBounds(245, 20, 15, 13);

        rotuloR.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        painelMatriz.add(rotuloR);
        rotuloR.setBounds(450, 175, 10, 13);

        rotuloRI.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        painelMatriz.add(rotuloRI);
        rotuloRI.setBounds(230, 340, 30, 13);

        rotuloLinha_11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_11);
        rotuloLinha_11.setBounds(10, 315, 15, 13);

        rotuloLinha_10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_10);
        rotuloLinha_10.setBounds(10, 290, 15, 13);

        rotuloLinha_9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_9);
        rotuloLinha_9.setBounds(10, 265, 15, 13);

        rotuloLinha_8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_8);
        rotuloLinha_8.setBounds(10, 240, 15, 13);

        rotuloLinha_7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_7);
        rotuloLinha_7.setBounds(10, 215, 15, 13);

        rotuloLinha_6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_6);
        rotuloLinha_6.setBounds(10, 190, 15, 13);

        rotuloLinha_5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_5);
        rotuloLinha_5.setBounds(10, 165, 15, 13);

        rotuloLinha_4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_4);
        rotuloLinha_4.setBounds(10, 140, 15, 13);

        rotuloLinha_3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_3);
        rotuloLinha_3.setBounds(10, 115, 15, 13);

        rotuloLinha_2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_2);
        rotuloLinha_2.setBounds(10, 90, 15, 13);

        rotuloLinha_1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_1);
        rotuloLinha_1.setBounds(10, 65, 15, 13);

        rotuloLinha_0.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloLinha_0);
        rotuloLinha_0.setBounds(10, 40, 15, 13);

        rotuloColuna_0.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_0);
        rotuloColuna_0.setBounds(35, 20, 15, 13);

        rotuloColuna_1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_1);
        rotuloColuna_1.setBounds(70, 20, 15, 13);

        rotuloColuna_2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_2);
        rotuloColuna_2.setBounds(105, 20, 15, 13);

        rotuloColuna_3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_3);
        rotuloColuna_3.setBounds(140, 20, 15, 13);

        rotuloColuna_4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_4);
        rotuloColuna_4.setBounds(175, 20, 15, 13);

        rotuloColuna_5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_5);
        rotuloColuna_5.setBounds(210, 20, 15, 13);

        rotuloColuna_7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_7);
        rotuloColuna_7.setBounds(280, 20, 15, 13);

        rotuloColuna_8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_8);
        rotuloColuna_8.setBounds(315, 20, 15, 13);

        rotuloColuna_9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_9);
        rotuloColuna_9.setBounds(350, 20, 15, 13);

        rotuloColuna_10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_10);
        rotuloColuna_10.setBounds(385, 20, 15, 13);

        rotuloColuna_11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        painelMatriz.add(rotuloColuna_11);
        rotuloColuna_11.setBounds(420, 20, 15, 13);
        painelMatriz.add(rotuloI);
        rotuloI.setBounds(230, 10, 10, 13);

        jPanel2.add(painelMatriz);
        painelMatriz.setBounds(600, 10, 470, 360);

        areaTextoResultados.setColumns(20);
        areaTextoResultados.setEditable(false);
        areaTextoResultados.setRows(5);
        scrollPaneInvariancia.setViewportView(areaTextoResultados);

        jPanel2.add(scrollPaneInvariancia);
        scrollPaneInvariancia.setBounds(290, 17, 300, 353);

        painelEntrada.setBorder(javax.swing.BorderFactory.createTitledBorder("Entrada"));
        painelEntrada.setLayout(null);

        botaoValor9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor9.setText("9");
        painelEntrada.add(botaoValor9);
        botaoValor9.setBounds(20, 150, 50, 20);

        botaoValor10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor10.setText("10");
        painelEntrada.add(botaoValor10);
        botaoValor10.setBounds(110, 150, 50, 20);

        botaoValor11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor11.setText("11");
        painelEntrada.add(botaoValor11);
        botaoValor11.setBounds(200, 150, 50, 20);

        botaoValor8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor8.setText("8");
        painelEntrada.add(botaoValor8);
        botaoValor8.setBounds(200, 190, 50, 20);

        botaoValor7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor7.setText("7");
        painelEntrada.add(botaoValor7);
        botaoValor7.setBounds(110, 190, 50, 20);

        botaoValor6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor6.setText("6");
        painelEntrada.add(botaoValor6);
        botaoValor6.setBounds(20, 190, 50, 20);

        botaoValor3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor3.setText("3");
        painelEntrada.add(botaoValor3);
        botaoValor3.setBounds(20, 230, 50, 20);

        botaoValor4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor4.setText("4");
        painelEntrada.add(botaoValor4);
        botaoValor4.setBounds(110, 230, 50, 20);

        botaoValor5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor5.setText("5");
        painelEntrada.add(botaoValor5);
        botaoValor5.setBounds(200, 230, 50, 20);

        botaoValor2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor2.setText("2");
        painelEntrada.add(botaoValor2);
        botaoValor2.setBounds(200, 270, 50, 20);

        botaoValor1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor1.setText("1");
        painelEntrada.add(botaoValor1);
        botaoValor1.setBounds(110, 270, 50, 20);

        botaoValor0.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoValor0.setText("0");
        painelEntrada.add(botaoValor0);
        botaoValor0.setBounds(20, 270, 50, 20);

        botaoRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SetaBackspace.png"))); // NOI18N
        botaoRemover.setToolTipText("Remover Elemento");
        botaoRemover.setEnabled(false);
        botaoRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRemoverActionPerformed(evt);
            }
        });
        painelEntrada.add(botaoRemover);
        botaoRemover.setBounds(20, 310, 50, 20);

        botaoSubstituirEntrada.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoSubstituirEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SetaCima.png"))); // NOI18N
        botaoSubstituirEntrada.setToolTipText("Substitui entrada atual pelo resultado");
        botaoSubstituirEntrada.setEnabled(false);
        botaoSubstituirEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSubstituirEntradaActionPerformed(evt);
            }
        });
        painelEntrada.add(botaoSubstituirEntrada);
        botaoSubstituirEntrada.setBounds(200, 310, 50, 20);

        scrollPaneEntrada.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneEntrada.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        areaTextoEntrada.setColumns(20);
        areaTextoEntrada.setEditable(false);
        areaTextoEntrada.setRows(5);
        scrollPaneEntrada.setViewportView(areaTextoEntrada);

        painelEntrada.add(scrollPaneEntrada);
        scrollPaneEntrada.setBounds(20, 35, 230, 70);

        botaoReproduzir.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoReproduzir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ClaveFaTransparente.png"))); // NOI18N
        botaoReproduzir.setToolTipText("Reproduzir conjunto da entrada");
        botaoReproduzir.setEnabled(false);
        botaoReproduzir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoReproduzirActionPerformed(evt);
            }
        });
        painelEntrada.add(botaoReproduzir);
        botaoReproduzir.setBounds(110, 310, 50, 20);

        jPanel2.add(painelEntrada);
        painelEntrada.setBounds(10, 10, 270, 360);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 30, 1080, 390);

        barraDeFerramentas.setRollover(true);

        botaoLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lixopequeno.png"))); // NOI18N
        botaoLimpar.setToolTipText("Limpar Tela");
        botaoLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLimparActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoLimpar);
        barraDeFerramentas.add(jSeparator3);

        botaoAlternarNumeroNotas.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoAlternarNumeroNotas.setText("C#");
        botaoAlternarNumeroNotas.setToolTipText("Selecionar modo Notas");
        botaoAlternarNumeroNotas.setFocusable(false);
        botaoAlternarNumeroNotas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoAlternarNumeroNotas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoAlternarNumeroNotas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                botaoAlternarNumeroNotasItemStateChanged(evt);
            }
        });
        barraDeFerramentas.add(botaoAlternarNumeroNotas);
        barraDeFerramentas.add(jSeparator8);

        botaoGerarFormaNormalStraus.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarFormaNormalStraus.setText("FNS");
        botaoGerarFormaNormalStraus.setToolTipText("Forma Normal segundo Straus");
        botaoGerarFormaNormalStraus.setEnabled(false);
        botaoGerarFormaNormalStraus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarFormaNormalStrausActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarFormaNormalStraus);

        botaoGerarFormaNormalForte.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarFormaNormalForte.setText("FNF");
        botaoGerarFormaNormalForte.setToolTipText("Forma Normal segundo Forte");
        botaoGerarFormaNormalForte.setEnabled(false);
        botaoGerarFormaNormalForte.setFocusable(false);
        botaoGerarFormaNormalForte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoGerarFormaNormalForte.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoGerarFormaNormalForte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarFormaNormalForteActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarFormaNormalForte);

        botaoGerarFormaPrimaStraus.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarFormaPrimaStraus.setText("FPS");
        botaoGerarFormaPrimaStraus.setToolTipText("Forma Prima segundo Straus");
        botaoGerarFormaPrimaStraus.setEnabled(false);
        botaoGerarFormaPrimaStraus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarFormaPrimaStrausActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarFormaPrimaStraus);

        botaoGerarFormaPrimaForte.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarFormaPrimaForte.setText("FPF");
        botaoGerarFormaPrimaForte.setToolTipText("Forma Prima segundo Forte");
        botaoGerarFormaPrimaForte.setEnabled(false);
        botaoGerarFormaPrimaForte.setFocusable(false);
        botaoGerarFormaPrimaForte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoGerarFormaPrimaForte.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoGerarFormaPrimaForte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarFormaPrimaForteActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarFormaPrimaForte);

        botaoGerarSubconjuntos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarSubconjuntos.setText("sn");
        botaoGerarSubconjuntos.setToolTipText("Subconjuntos de n classes");
        botaoGerarSubconjuntos.setEnabled(false);
        botaoGerarSubconjuntos.setFocusable(false);
        botaoGerarSubconjuntos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoGerarSubconjuntos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoGerarSubconjuntos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarSubconjuntosActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarSubconjuntos);

        botaoGerarRotacaoStravinskyana.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarRotacaoStravinskyana.setText("RS");
        botaoGerarRotacaoStravinskyana.setToolTipText("Rotação Stravinskyana");
        botaoGerarRotacaoStravinskyana.setEnabled(false);
        botaoGerarRotacaoStravinskyana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarRotacaoStravinskyanaActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarRotacaoStravinskyana);

        botaoGerarTabelaAdicao.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarTabelaAdicao.setText("TA");
        botaoGerarTabelaAdicao.setToolTipText("Tabela de Adição");
        botaoGerarTabelaAdicao.setEnabled(false);
        botaoGerarTabelaAdicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarTabelaAdicaoActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarTabelaAdicao);

        botaoGerarVetorIntervalar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarVetorIntervalar.setText("VI");
        botaoGerarVetorIntervalar.setToolTipText("Vetor Intervalar");
        botaoGerarVetorIntervalar.setEnabled(false);
        botaoGerarVetorIntervalar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarVetorIntervalarActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarVetorIntervalar);

        botaoInvarianciaTranspositiva.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoInvarianciaTranspositiva.setText("IT");
        botaoInvarianciaTranspositiva.setToolTipText("Invariância Transpositiva");
        botaoInvarianciaTranspositiva.setEnabled(false);
        botaoInvarianciaTranspositiva.setFocusable(false);
        botaoInvarianciaTranspositiva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoInvarianciaTranspositiva.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoInvarianciaTranspositiva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoInvarianciaTranspositivaActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoInvarianciaTranspositiva);

        botaoInvarianciaInversiva.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoInvarianciaInversiva.setText("II");
        botaoInvarianciaInversiva.setToolTipText("Invariância Inversiva");
        botaoInvarianciaInversiva.setEnabled(false);
        botaoInvarianciaInversiva.setFocusable(false);
        botaoInvarianciaInversiva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoInvarianciaInversiva.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoInvarianciaInversiva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoInvarianciaInversivaActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoInvarianciaInversiva);

        botaoPaleta.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoPaleta.setText("Pal");
        botaoPaleta.setToolTipText("Paleta de classes de conjuntos");
        botaoPaleta.setEnabled(false);
        botaoPaleta.setFocusable(false);
        botaoPaleta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoPaleta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoPaleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPaletaActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoPaleta);
        barraDeFerramentas.add(jSeparator7);

        botaoInserirSegundoFator.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoInserirSegundoFator.setText("y");
        botaoInserirSegundoFator.setToolTipText("Manipular segundo conjunto");
        botaoInserirSegundoFator.setEnabled(false);
        botaoInserirSegundoFator.setFocusable(false);
        botaoInserirSegundoFator.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoInserirSegundoFator.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoInserirSegundoFator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                botaoInserirSegundoFatorItemStateChanged(evt);
            }
        });
        barraDeFerramentas.add(botaoInserirSegundoFator);

        botaoMultiplicacaoBoulez.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoMultiplicacaoBoulez.setText("Boulez");
        botaoMultiplicacaoBoulez.setToolTipText("Multiplicação segundo Boulez");
        botaoMultiplicacaoBoulez.setEnabled(false);
        botaoMultiplicacaoBoulez.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMultiplicacaoBoulezActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoMultiplicacaoBoulez);

        botaoMultiplicacaoRahn.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoMultiplicacaoRahn.setText("Rahn 1");
        botaoMultiplicacaoRahn.setToolTipText("Multiplicação segundo Rahn");
        botaoMultiplicacaoRahn.setEnabled(false);
        botaoMultiplicacaoRahn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMultiplicacaoRahnActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoMultiplicacaoRahn);

        botaoMultiplicacaoRahnEstendido.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoMultiplicacaoRahnEstendido.setText("Rahn 2");
        botaoMultiplicacaoRahnEstendido.setToolTipText("Multiplicação Rahn expandida");
        botaoMultiplicacaoRahnEstendido.setEnabled(false);
        botaoMultiplicacaoRahnEstendido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMultiplicacaoRahnEstendidoActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoMultiplicacaoRahnEstendido);

        botaoGerarSimilaridade.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarSimilaridade.setText("Si");
        botaoGerarSimilaridade.setToolTipText("Similaridade");
        botaoGerarSimilaridade.setEnabled(false);
        botaoGerarSimilaridade.setFocusable(false);
        botaoGerarSimilaridade.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoGerarSimilaridade.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoGerarSimilaridade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarSimilaridadeActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarSimilaridade);
        barraDeFerramentas.add(jSeparator6);

        botaoGerarDerivacaoSerial.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarDerivacaoSerial.setText("DS");
        botaoGerarDerivacaoSerial.setToolTipText("Derivação Serial");
        botaoGerarDerivacaoSerial.setEnabled(false);
        botaoGerarDerivacaoSerial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarDerivacaoSerialActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarDerivacaoSerial);

        botaoGerarMatrizDodecafonica.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoGerarMatrizDodecafonica.setText("MD");
        botaoGerarMatrizDodecafonica.setToolTipText("Matriz Dodecafônica");
        botaoGerarMatrizDodecafonica.setEnabled(false);
        botaoGerarMatrizDodecafonica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoGerarMatrizDodecafonicaActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoGerarMatrizDodecafonica);

        botaoCombinatoriedade.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoCombinatoriedade.setText("Comb.");
        botaoCombinatoriedade.setToolTipText("Combinatoriedade");
        botaoCombinatoriedade.setEnabled(false);
        botaoCombinatoriedade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCombinatoriedadeActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoCombinatoriedade);

        botaoInvariancia.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoInvariancia.setText("Inv.");
        botaoInvariancia.setToolTipText("Invariância");
        botaoInvariancia.setEnabled(false);
        botaoInvariancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoInvarianciaActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoInvariancia);
        barraDeFerramentas.add(jSeparator1);

        botaoExportarHTML.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoExportarHTML.setText("Ex");
        botaoExportarHTML.setToolTipText("Exportar para HTML");
        botaoExportarHTML.setEnabled(false);
        botaoExportarHTML.setFocusable(false);
        botaoExportarHTML.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoExportarHTML.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoExportarHTML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExportarHTMLActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoExportarHTML);
        barraDeFerramentas.add(jSeparator4);

        botaoTabelasCordais.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botaoTabelasCordais.setText("Tab.");
        botaoTabelasCordais.setToolTipText("Tabelas Cordais");
        botaoTabelasCordais.setFocusable(false);
        botaoTabelasCordais.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoTabelasCordais.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoTabelasCordais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoTabelasCordaisActionPerformed(evt);
            }
        });
        barraDeFerramentas.add(botaoTabelasCordais);

        getContentPane().add(barraDeFerramentas);
        barraDeFerramentas.setBounds(0, 0, 1090, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoGerarMatrizDodecafonicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarMatrizDodecafonicaActionPerformed
        limpaMatriz();

        try {
            controlador.geraMatrizDodecafonica();

            rotuloP.setText("P");
            rotuloI.setText("I");
            rotuloR.setText("R");
            rotuloRI.setText("RI");

            ((TitledBorder)painelMatriz.getBorder()).setTitle("Matriz Dodecafônica");
            painelMatriz.repaint();

            preencheMatrizInterfaceGrafica(12);

            botaoCombinatoriedade.setEnabled(true);
            botaoInvariancia.setEnabled(true);
            botaoExportarHTML.setEnabled(true);

            modosAtuais[1] = MATRIZ_DODECAFONICA;
        }
        catch(NumberFormatException dp) {
            JOptionPane.showMessageDialog(null, "H\u00e1 dados n\u00e3o permitidos. Preencha corretamente");
        }
}//GEN-LAST:event_botaoGerarMatrizDodecafonicaActionPerformed

    private void botaoLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparActionPerformed
        areaTextoResultados.setText("");
        botaoExportarHTML.setEnabled(false);
        botaoCombinatoriedade.setEnabled(false);
        botaoInvariancia.setEnabled(false);
        botaoInserirSegundoFator.setSelected(false);

        controlador.limparDados();
        limpaMatriz();
        atualizaEntrada();

        modosAtuais[0] = -1;
        atualizaHabilitacaoBotoesFuncionalidades();
        for (JButton botao : botoesEntrada) {
            substituiModo1Fator(botao, botao.getActionListeners()[0]);
        }
}//GEN-LAST:event_botaoLimparActionPerformed

    private void botaoRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRemoverActionPerformed
        if (!botaoInserirSegundoFator.isSelected()) {
            controlador.removerConjuntoPrincipal();
        }
        else if (!controlador.removerSegundoConjunto()) {
            botaoInserirSegundoFator.setSelected(false);
        }

        atualizaEntrada();
        atualizaHabilitacaoBotoesFuncionalidades();
}//GEN-LAST:event_botaoRemoverActionPerformed

    private void botaoGerarDerivacaoSerialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarDerivacaoSerialActionPerformed
        try {
            controlador.geraDerivacaoSerial();

            botaoGerarMatrizDodecafonica.setEnabled(true);
            botaoExportarHTML.setEnabled(true);
            botaoSubstituirEntrada.setEnabled(true);

            modosAtuais[0] = SAIDA_DERIVACAO_SERIAL;
            atualizaSaida();
            atualizaHabilitacaoBotoesFuncionalidades();
        }
        catch(DadosProibidos dp) {
            JOptionPane.showMessageDialog(null, dp.getMessage());
        }
}//GEN-LAST:event_botaoGerarDerivacaoSerialActionPerformed

    private void botaoInvarianciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoInvarianciaActionPerformed
        controlador.geraInvariancia();

        modosAtuais[0] = SAIDA_INVARIANCIA;
        atualizaSaida();

        botaoSubstituirEntrada.setEnabled(false);
}//GEN-LAST:event_botaoInvarianciaActionPerformed

    private void botaoCombinatoriedadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCombinatoriedadeActionPerformed
        for (Point p : controlador.geraCombinatoriedade()) {
            camposMatriz[p.x][p.y].setBackground(Color.yellow);
        }
}//GEN-LAST:event_botaoCombinatoriedadeActionPerformed

    private void botaoExportarHTMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExportarHTMLActionPerformed
        String nomeArquivo = "saida.html";
        try {
            PrintWriter pagina = new PrintWriter(nomeArquivo);
            pagina.append("<html><head><title>Resultado</title></head><body>");
            JTextField campoAtual;
            if (modosAtuais[0] == SAIDA_PALETA) {
                ArrayList<ArrayList<ClasseDeAltura[]>> resultadoPaleta = controlador.getPaleta();
                int quantLinhas = resultadoPaleta.get(0).size();
                pagina.append("<table border = 3>");
                for (int i = 0; i < quantLinhas; i++) {
                    pagina.append("<tr>");
                    for (ClasseDeAltura c : resultadoPaleta.get(0).get(i)) {
                        pagina.append("<td>" + c.representacao(formatoRepresentacao) + "</td>");
                    }
                    pagina.append("<td>    </td>"); //TODO: corrigir esse elemento de separação
                    try {
                        for (ClasseDeAltura c : resultadoPaleta.get(1).get(i)) {
                            pagina.append("<td>" + c.representacao(formatoRepresentacao) + "</td>");
                        }
                    }
                    catch(IndexOutOfBoundsException aioobe) {}
                    pagina.append("</tr>");
                }
                pagina.append("</table>");
            }
            if (modosAtuais[1] != -1) {
                pagina.append("<table border = 3>");
                for (int i = 0; i < 12; i++) {
                    pagina.append("<tr>");
                    for (int j = 0; j < 12; j++) {
                        campoAtual = camposMatriz[i][j];
                        if (!campoAtual.getBackground().equals(Color.YELLOW)) {
                            pagina.append("<td>" + campoAtual.getText() + "</td>");
                        }
                        else {
                            pagina.append("<td bgcolor=\"#FFFF00\">" + campoAtual.getText() + "</td>");
                        }
                    }
                    pagina.append("</tr>");
                }
                pagina.append("</table>");
            }

            pagina.append("<p></p><p></p><p></p><p></p>");

            String texto = areaTextoResultados.getText();
            switch(modosAtuais[0]) {
                case InterfaceGrafica.SAIDA_ROTACAO_STRAVINSKYANA:
                    texto = texto.replace("\u03B1", "A");
                    texto = texto.replace("\u03B2", "B");
                case InterfaceGrafica.SAIDA_INVARIANCIA:
                case InterfaceGrafica.SAIDA_DERIVACAO_SERIAL:
                case InterfaceGrafica.SAIDA_SUBCONJUNTOS:
                    texto = texto.replace("\n\n", "<br>");
                    break;
            }

            if (modosAtuais[0] != SAIDA_PALETA) {
                pagina.append(texto.replace("\n\n", "<br>") + "</body></html>");
            }

            pagina.close();
        }
        catch(Exception e) {}

        abrirNavegadorPadrao(nomeArquivo);
    }//GEN-LAST:event_botaoExportarHTMLActionPerformed

    public static void abrirNavegadorPadrao(String nomeArquivo) {
        try {
            String osName = System.getProperty("os.name");
            Process abrirBrowser;
            if (osName.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL",
                    new Class[] {String.class});
                openURL.invoke(null, new Object[] {nomeArquivo});
            }
            else if (osName.startsWith("Windows")) {
                abrirBrowser = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + nomeArquivo);
                abrirBrowser.waitFor();
            }
            else { //assume Unix or Linux
                String[] browsers = {
                    "google-chrome", "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(
                    new String[] {"which", browsers[count]}).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null) {
                    throw new Exception("Navegador não encontrado!");
                }
                else  {
                    abrirBrowser = Runtime.getRuntime().exec(new String[] {browser, nomeArquivo});
                    abrirBrowser.waitFor();
                }
            }
        }
        catch(Exception e) {}
    }

    private void botaoGerarFormaPrimaStrausActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarFormaPrimaStrausActionPerformed
        controlador.geraFormaPrimaStraus();

        modosAtuais[0] = SAIDA_FORMA_COMPACTA;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);
    }//GEN-LAST:event_botaoGerarFormaPrimaStrausActionPerformed

    private void botaoGerarFormaNormalStrausActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarFormaNormalStrausActionPerformed
        controlador.geraFormaNormalStraus();

        modosAtuais[0] = SAIDA_FORMA_COMPACTA;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);

    }//GEN-LAST:event_botaoGerarFormaNormalStrausActionPerformed

    private void botaoGerarRotacaoStravinskyanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarRotacaoStravinskyanaActionPerformed
        controlador.geraRotacaoStravinskyana();

        modosAtuais[0] = SAIDA_ROTACAO_STRAVINSKYANA;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(false);
    }//GEN-LAST:event_botaoGerarRotacaoStravinskyanaActionPerformed

    private void botaoMultiplicacaoBoulezActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMultiplicacaoBoulezActionPerformed
        controlador.geraMultiplicacaoBoulez();

        modosAtuais[0] = SAIDA_MULTIPLICACAO;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);
    }//GEN-LAST:event_botaoMultiplicacaoBoulezActionPerformed

    private void botaoMultiplicacaoRahnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMultiplicacaoRahnActionPerformed
        controlador.geraMultiplicacaoRahn1();

        modosAtuais[0] = SAIDA_MULTIPLICACAO;
        atualizaSaida();
        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);
    }//GEN-LAST:event_botaoMultiplicacaoRahnActionPerformed

    private void botaoMultiplicacaoRahnEstendidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMultiplicacaoRahnEstendidoActionPerformed
        controlador.geraMultiplicacaoRahn2();

        modosAtuais[0] = SAIDA_MULTIPLICACAO;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);
    }//GEN-LAST:event_botaoMultiplicacaoRahnEstendidoActionPerformed

    private void botaoGerarTabelaAdicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarTabelaAdicaoActionPerformed
        limpaMatriz();

        ArrayList<ClasseDeAltura> numeros = controlador.getConjuntoPrincipal();
        int tamanho = numeros.size(), i = 0, j;
        String repr;

        for (; i < tamanho; i++) {
            repr = numeros.get(i).representacao(formatoRepresentacao);
            rotulosDasLinhas[i].setText(repr);
            rotulosDasColunas[i].setText(repr);
        }

        for (; i < 12; i++) {
            rotulosDasLinhas[i].setText("");
            rotulosDasColunas[i].setText("");
        }

        TitledBorder borda = (TitledBorder)painelMatriz.getBorder();
        borda.setTitle("Tabela de Adiçao");
        painelMatriz.repaint();

        controlador.geraTabelaAdicao();
        modosAtuais[1] = MATRIZ_TABELA_ADICAO;

        preencheMatrizInterfaceGrafica(tamanho);
        botaoExportarHTML.setEnabled(true);
    }//GEN-LAST:event_botaoGerarTabelaAdicaoActionPerformed

    private void botaoGerarVetorIntervalarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarVetorIntervalarActionPerformed
        controlador.gerarVetorIntervalar();

        modosAtuais[0] = SAIDA_VETOR_INTERVALAR;
        atualizaSaida();
        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(false);
    }//GEN-LAST:event_botaoGerarVetorIntervalarActionPerformed

    private void botaoGerarSimilaridadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarSimilaridadeActionPerformed
        controlador.geraSimilaridade();

        modosAtuais[0] = SAIDA_SIMILARIDADE;
        atualizaSaida();
        botaoExportarHTML.setEnabled(false);
        botaoSubstituirEntrada.setEnabled(false);
    }//GEN-LAST:event_botaoGerarSimilaridadeActionPerformed

    private void botaoGerarSubconjuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarSubconjuntosActionPerformed
        controlador.geraSubconjuntos();

        modosAtuais[0] = SAIDA_SUBCONJUNTOS;
        atualizaSaida();
        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(false);
    }//GEN-LAST:event_botaoGerarSubconjuntosActionPerformed

    private void botaoTabelasCordaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoTabelasCordaisActionPerformed
        guiEscolherTabela.setVisible(true);
    }//GEN-LAST:event_botaoTabelasCordaisActionPerformed

    private void botaoInvarianciaTranspositivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoInvarianciaTranspositivaActionPerformed
        controlador.geraInvarianciaTranspositiva();

        modosAtuais[0] = SAIDA_INVARIANCIA_DERIVATIVA;
        atualizaSaida();
        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(false);
    }//GEN-LAST:event_botaoInvarianciaTranspositivaActionPerformed

    private void botaoAlternarNumeroNotasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_botaoAlternarNumeroNotasItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            formatoRepresentacao = ClasseDeAltura.TipoRepresentacao.NomeSustenido;
            for (int i = 0; i < 12; i++) {
                botoesEntrada.get(i).setText(ClasseDeAltura.criar(i).nome());
            }

            if (modosAtuais[1] != -1) {
                for (JTextField[] array : camposMatriz) {
                    for (JTextField campo : array) {
                        try {
                            campo.setText(ClasseDeAltura.criar(Integer.parseInt(campo.getText())).toString());
                        }
                        catch(NumberFormatException nfe) {}
                    }
                }

                if (modosAtuais[1] == MATRIZ_TABELA_ADICAO) {
                    ArrayList<ClasseDeAltura> entrada = controlador.getConjuntoPrincipal();
                    for (int i = 0; i < entrada.size(); i++) {
                        rotulosDasLinhas[i].setText(entrada.get(i).nome());
                        rotulosDasColunas[i].setText(entrada.get(i).nome());
                    }
                }
            }

            atualizaSaida();
            atualizaEntrada();
        }
        else {
            formatoRepresentacao = ClasseDeAltura.TipoRepresentacao.Inteiro;
            try {
                for (int i = 0; i < 12; i++) {
                    botoesEntrada.get(i).setText(String.valueOf(notaToNumero(botoesEntrada.get(i).getText())));
                }
            }
            catch(DadosProibidos dp) {}

            if (modosAtuais[1] != -1) {
                for (JTextField[] array : camposMatriz) {
                    for (JTextField campo : array) {
                        try {
                            campo.setText(String.valueOf(notaToNumero(campo.getText())));
                        }
                        catch(DadosProibidos dp) {}
                    }
                }

                if (modosAtuais[1] == MATRIZ_TABELA_ADICAO) {
                    ArrayList<ClasseDeAltura> entrada = controlador.getConjuntoPrincipal();
                    try {
                        for (int i = 0; i < entrada.size(); i++) {
                            rotulosDasLinhas[i].setText(String.valueOf(notaToNumero(rotulosDasLinhas[i].getText())));
                            rotulosDasColunas[i].setText(String.valueOf(notaToNumero(rotulosDasColunas[i].getText())));
                        }
                    }
                    catch(DadosProibidos dp) {}
                }
            }

            atualizaSaida();
            atualizaEntrada();
        }
    }//GEN-LAST:event_botaoAlternarNumeroNotasItemStateChanged

    private void botaoSubstituirEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSubstituirEntradaActionPerformed
        switch(modosAtuais[0]) {
            case SAIDA_FORMA_COMPACTA:
                controlador.substitui_FormaCompacta();
                break;
            case SAIDA_MULTIPLICACAO:
                controlador.substituir_Multiplicacao();
                break;
            case SAIDA_DERIVACAO_SERIAL:
                controlador.substitui_DerivacaoSerial();
                botaoGerarMatrizDodecafonica.setEnabled(true);
                break;
            case SAIDA_PALETA:
                controlador.substitui_Paleta();
                break;
        }

        atualizaEntrada();
        atualizaHabilitacaoBotoesFuncionalidades();
    }//GEN-LAST:event_botaoSubstituirEntradaActionPerformed

    private void botaoInserirSegundoFatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_botaoInserirSegundoFatorItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            for (JButton botao : botoesEntrada) {
                substituiModo2Fatores(botao, buttonListenerEntradaSegundoConjunto);
            }
        }
        else {
            for (JButton botao : botoesEntrada) {
                substituiModo1Fator(botao, botao.getActionListeners()[0]);
            }
        }

        atualizaEntrada();
        atualizaHabilitacaoBotoesFuncionalidades();
    }//GEN-LAST:event_botaoInserirSegundoFatorItemStateChanged

    private void botaoReproduzirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoReproduzirActionPerformed
        ArrayList<ClasseDeAltura> escolhido;

        if (!botaoInserirSegundoFator.isSelected() || controlador.getSegundoConjunto().isEmpty()) {
            escolhido = controlador.getConjuntoPrincipal();
        }
        else {
            escolhido = controlador.getSegundoConjunto();
        }

        try {
            Sequence sequencia = new Sequence(Sequence.PPQ, 5040, 1);
            Track trilha = sequencia.getTracks()[0];
            ShortMessage mensagem = new ShortMessage();
            mensagem.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0);
            trilha.add(new MidiEvent(mensagem, 0));

            mensagem = new ShortMessage();
            mensagem.setMessage(ShortMessage.CONTROL_CHANGE, 0, 7, 64);
            trilha.add(new MidiEvent(mensagem, 0));

            int tickAtual = 1;
            for (ClasseDeAltura valor : escolhido) {
                mensagem = new ShortMessage();
                mensagem.setMessage(ShortMessage.NOTE_ON, 0, valor.inteiro() + 48, 127);
                trilha.add(new MidiEvent(mensagem, (tickAtual += 200)));

                mensagem = new ShortMessage();
                mensagem.setMessage(ShortMessage.NOTE_OFF, 0, valor.inteiro() + 48, 127);
                trilha.add(new MidiEvent(mensagem, (tickAtual += 3780)));
            }

            sequenciador.setSequence(sequencia);
            sequenciador.open();
            sintetizador.open();

            sequenciador.getTransmitter().setReceiver(sintetizador.getReceiver());
            sequenciador.start();
        }
        catch(MidiUnavailableException mue) {}
        catch(InvalidMidiDataException imde) {}
    }//GEN-LAST:event_botaoReproduzirActionPerformed

    private void botaoPaletaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPaletaActionPerformed
        controlador.geraPaleta();

        modosAtuais[0] = SAIDA_PALETA;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);
    }//GEN-LAST:event_botaoPaletaActionPerformed

    private void botaoInvarianciaInversivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoInvarianciaInversivaActionPerformed
        controlador.geraInvarianciaInversiva();

        modosAtuais[0] = SAIDA_INVARIANCIA_DERIVATIVA;
        atualizaSaida();
        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(false);
    }//GEN-LAST:event_botaoInvarianciaInversivaActionPerformed

    private void botaoGerarFormaPrimaForteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarFormaPrimaForteActionPerformed
        controlador.geraFormaPrimaForte();

        modosAtuais[0] = SAIDA_FORMA_COMPACTA;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);
    }//GEN-LAST:event_botaoGerarFormaPrimaForteActionPerformed

    private void botaoGerarFormaNormalForteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoGerarFormaNormalForteActionPerformed
        controlador.geraFormaNormalForte();

        modosAtuais[0] = SAIDA_FORMA_COMPACTA;
        atualizaSaida();

        botaoExportarHTML.setEnabled(true);
        botaoSubstituirEntrada.setEnabled(true);
    }//GEN-LAST:event_botaoGerarFormaNormalForteActionPerformed

    private void substituiModo2Fatores(JButton botao, ActionListener listener) {
        botao.removeActionListener(buttonListenerEntrada);
        botao.addActionListener(listener);
    }

    private void substituiModo1Fator(JButton botao, ActionListener listener) {
        botao.removeActionListener(listener);
        botao.addActionListener(buttonListenerEntrada);
    }

    private void preencheMatrizInterfaceGrafica(int tamanho) {
        int valor;
        MatrizDodecafonica matriz = controlador.getMatriz();
        for (int i = 0; i < tamanho; ++i) {
            for (int j = 0; j < tamanho; ++j) {
                camposMatriz[i][j].setText(matriz.getValor(i, j).representacao(formatoRepresentacao));
            }
        }
        botaoExportarHTML.setEnabled(true);
    }

    private int notaToNumero(String simboloNota) throws DadosProibidos {
        if (simboloNota.equals("C")) {
            return 0;
        }
        else if (simboloNota.equals("C#") || simboloNota.equals("Db")) {
            return 1;
        }
        else if (simboloNota.equals("D")) {
            return 2;
        }
        else if (simboloNota.equals("Eb") || simboloNota.equals("D#")) {
            return 3;
        }
        else if (simboloNota.equals("E")) {
            return 4;
        }
        else if (simboloNota.equals("F")) {
            return 5;
        }
        else if (simboloNota.equals("Gb") || simboloNota.equals("F#")) {
            return 6;
        }
        else if (simboloNota.equals("G")) {
            return 7;
        }
        else if (simboloNota.equals("Ab") || simboloNota.equals("G#")) {
            return 8;
        }
        else if (simboloNota.equals("A")) {
            return 9;
        }
        else if (simboloNota.equals("Bb") || simboloNota.equals("A#")) {
            return 10;
        }
        else if (simboloNota.equals("B")) {
            return 11;
        }

        throw new DadosProibidos("Nota inexistente");
    }

    public void fechaMIDI() {
        if (sintetizador != null) {
            sintetizador.close();
            sequenciador.close();
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final InterfaceGrafica ig = new InterfaceGrafica();
                ig.setSize(1090, 440);
                if (!Toolkit.getDefaultToolkit().getScreenSize().equals(new Dimension(800,600))) {
                    ig.setLocation(125, 125);
                }
                else {
                    ig.setLocation(0, 50);
                }
                ig.setTitle("Lewin");
                ig.setVisible(true);

                ig.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        ig.fechaMIDI();
                    }
                });
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaTextoEntrada;
    private javax.swing.JTextArea areaTextoResultados;
    private javax.swing.JToolBar barraDeFerramentas;
    private javax.swing.JToggleButton botaoAlternarNumeroNotas;
    protected javax.swing.JButton botaoCombinatoriedade;
    private javax.swing.JButton botaoExportarHTML;
    protected javax.swing.JButton botaoGerarDerivacaoSerial;
    private javax.swing.JButton botaoGerarFormaNormalForte;
    private javax.swing.JButton botaoGerarFormaNormalStraus;
    private javax.swing.JButton botaoGerarFormaPrimaForte;
    private javax.swing.JButton botaoGerarFormaPrimaStraus;
    protected javax.swing.JButton botaoGerarMatrizDodecafonica;
    private javax.swing.JButton botaoGerarRotacaoStravinskyana;
    private javax.swing.JButton botaoGerarSimilaridade;
    private javax.swing.JButton botaoGerarSubconjuntos;
    private javax.swing.JButton botaoGerarTabelaAdicao;
    private javax.swing.JButton botaoGerarVetorIntervalar;
    private javax.swing.JToggleButton botaoInserirSegundoFator;
    protected javax.swing.JButton botaoInvariancia;
    private javax.swing.JButton botaoInvarianciaInversiva;
    private javax.swing.JButton botaoInvarianciaTranspositiva;
    protected javax.swing.JButton botaoLimpar;
    private javax.swing.JButton botaoMultiplicacaoBoulez;
    private javax.swing.JButton botaoMultiplicacaoRahn;
    private javax.swing.JButton botaoMultiplicacaoRahnEstendido;
    private javax.swing.JButton botaoPaleta;
    protected javax.swing.JButton botaoRemover;
    private javax.swing.JButton botaoReproduzir;
    private javax.swing.JButton botaoSubstituirEntrada;
    private javax.swing.JButton botaoTabelasCordais;
    protected javax.swing.JButton botaoValor0;
    protected javax.swing.JButton botaoValor1;
    protected javax.swing.JButton botaoValor10;
    protected javax.swing.JButton botaoValor11;
    protected javax.swing.JButton botaoValor2;
    protected javax.swing.JButton botaoValor3;
    protected javax.swing.JButton botaoValor4;
    protected javax.swing.JButton botaoValor5;
    protected javax.swing.JButton botaoValor6;
    protected javax.swing.JButton botaoValor7;
    protected javax.swing.JButton botaoValor8;
    protected javax.swing.JButton botaoValor9;
    protected javax.swing.JTextField campoTexto_0_0_Matriz;
    protected javax.swing.JTextField campoTexto_0_10_Matriz;
    protected javax.swing.JTextField campoTexto_0_11_Matriz;
    protected javax.swing.JTextField campoTexto_0_1_Matriz;
    protected javax.swing.JTextField campoTexto_0_2_Matriz;
    protected javax.swing.JTextField campoTexto_0_3_Matriz;
    protected javax.swing.JTextField campoTexto_0_4_Matriz;
    protected javax.swing.JTextField campoTexto_0_5_Matriz;
    protected javax.swing.JTextField campoTexto_0_6_Matriz;
    protected javax.swing.JTextField campoTexto_0_7_Matriz;
    protected javax.swing.JTextField campoTexto_0_8_Matriz;
    protected javax.swing.JTextField campoTexto_0_9_Matriz;
    protected javax.swing.JTextField campoTexto_10_0_Matriz;
    protected javax.swing.JTextField campoTexto_10_10_Matriz;
    protected javax.swing.JTextField campoTexto_10_11_Matriz;
    protected javax.swing.JTextField campoTexto_10_1_Matriz;
    protected javax.swing.JTextField campoTexto_10_2_Matriz;
    protected javax.swing.JTextField campoTexto_10_3_Matriz;
    protected javax.swing.JTextField campoTexto_10_4_Matriz;
    protected javax.swing.JTextField campoTexto_10_5_Matriz;
    protected javax.swing.JTextField campoTexto_10_6_Matriz;
    protected javax.swing.JTextField campoTexto_10_7_Matriz;
    protected javax.swing.JTextField campoTexto_10_8_Matriz;
    protected javax.swing.JTextField campoTexto_10_9_Matriz;
    protected javax.swing.JTextField campoTexto_11_0_Matriz;
    protected javax.swing.JTextField campoTexto_11_10_Matriz;
    protected javax.swing.JTextField campoTexto_11_11_Matriz;
    protected javax.swing.JTextField campoTexto_11_1_Matriz;
    protected javax.swing.JTextField campoTexto_11_2_Matriz;
    protected javax.swing.JTextField campoTexto_11_3_Matriz;
    protected javax.swing.JTextField campoTexto_11_4_Matriz;
    protected javax.swing.JTextField campoTexto_11_5_Matriz;
    protected javax.swing.JTextField campoTexto_11_6_Matriz;
    protected javax.swing.JTextField campoTexto_11_7_Matriz;
    protected javax.swing.JTextField campoTexto_11_8_Matriz;
    protected javax.swing.JTextField campoTexto_11_9_Matriz;
    protected javax.swing.JTextField campoTexto_1_0_Matriz;
    protected javax.swing.JTextField campoTexto_1_10_Matriz;
    protected javax.swing.JTextField campoTexto_1_11_Matriz;
    protected javax.swing.JTextField campoTexto_1_1_Matriz;
    protected javax.swing.JTextField campoTexto_1_2_Matriz;
    protected javax.swing.JTextField campoTexto_1_3_Matriz;
    protected javax.swing.JTextField campoTexto_1_4_Matriz;
    protected javax.swing.JTextField campoTexto_1_5_Matriz;
    protected javax.swing.JTextField campoTexto_1_6_Matriz;
    protected javax.swing.JTextField campoTexto_1_7_Matriz;
    protected javax.swing.JTextField campoTexto_1_8_Matriz;
    protected javax.swing.JTextField campoTexto_1_9_Matriz;
    protected javax.swing.JTextField campoTexto_2_0_Matriz;
    protected javax.swing.JTextField campoTexto_2_10_Matriz;
    protected javax.swing.JTextField campoTexto_2_11_Matriz;
    protected javax.swing.JTextField campoTexto_2_1_Matriz;
    protected javax.swing.JTextField campoTexto_2_2_Matriz;
    protected javax.swing.JTextField campoTexto_2_3_Matriz;
    protected javax.swing.JTextField campoTexto_2_4_Matriz;
    protected javax.swing.JTextField campoTexto_2_5_Matriz;
    protected javax.swing.JTextField campoTexto_2_6_Matriz;
    protected javax.swing.JTextField campoTexto_2_7_Matriz;
    protected javax.swing.JTextField campoTexto_2_8_Matriz;
    protected javax.swing.JTextField campoTexto_2_9_Matriz;
    protected javax.swing.JTextField campoTexto_3_0_Matriz;
    protected javax.swing.JTextField campoTexto_3_10_Matriz;
    protected javax.swing.JTextField campoTexto_3_11_Matriz;
    protected javax.swing.JTextField campoTexto_3_1_Matriz;
    protected javax.swing.JTextField campoTexto_3_2_Matriz;
    protected javax.swing.JTextField campoTexto_3_3_Matriz;
    protected javax.swing.JTextField campoTexto_3_4_Matriz;
    protected javax.swing.JTextField campoTexto_3_5_Matriz;
    protected javax.swing.JTextField campoTexto_3_6_Matriz;
    protected javax.swing.JTextField campoTexto_3_7_Matriz;
    protected javax.swing.JTextField campoTexto_3_8_Matriz;
    protected javax.swing.JTextField campoTexto_3_9_Matriz;
    protected javax.swing.JTextField campoTexto_4_0_Matriz;
    protected javax.swing.JTextField campoTexto_4_10_Matriz;
    protected javax.swing.JTextField campoTexto_4_11_Matriz;
    protected javax.swing.JTextField campoTexto_4_1_Matriz;
    protected javax.swing.JTextField campoTexto_4_2_Matriz;
    protected javax.swing.JTextField campoTexto_4_3_Matriz;
    protected javax.swing.JTextField campoTexto_4_4_Matriz;
    protected javax.swing.JTextField campoTexto_4_5_Matriz;
    protected javax.swing.JTextField campoTexto_4_6_Matriz;
    protected javax.swing.JTextField campoTexto_4_7_Matriz;
    protected javax.swing.JTextField campoTexto_4_8_Matriz;
    protected javax.swing.JTextField campoTexto_4_9_Matriz;
    protected javax.swing.JTextField campoTexto_5_0_Matriz;
    protected javax.swing.JTextField campoTexto_5_10_Matriz;
    protected javax.swing.JTextField campoTexto_5_11_Matriz;
    protected javax.swing.JTextField campoTexto_5_1_Matriz;
    protected javax.swing.JTextField campoTexto_5_2_Matriz;
    protected javax.swing.JTextField campoTexto_5_3_Matriz;
    protected javax.swing.JTextField campoTexto_5_4_Matriz;
    protected javax.swing.JTextField campoTexto_5_5_Matriz;
    protected javax.swing.JTextField campoTexto_5_6_Matriz;
    protected javax.swing.JTextField campoTexto_5_7_Matriz;
    protected javax.swing.JTextField campoTexto_5_8_Matriz;
    protected javax.swing.JTextField campoTexto_5_9_Matriz;
    protected javax.swing.JTextField campoTexto_6_0_Matriz;
    protected javax.swing.JTextField campoTexto_6_10_Matriz;
    protected javax.swing.JTextField campoTexto_6_11_Matriz;
    protected javax.swing.JTextField campoTexto_6_1_Matriz;
    protected javax.swing.JTextField campoTexto_6_2_Matriz;
    protected javax.swing.JTextField campoTexto_6_3_Matriz;
    protected javax.swing.JTextField campoTexto_6_4_Matriz;
    protected javax.swing.JTextField campoTexto_6_5_Matriz;
    protected javax.swing.JTextField campoTexto_6_6_Matriz;
    protected javax.swing.JTextField campoTexto_6_7_Matriz;
    protected javax.swing.JTextField campoTexto_6_8_Matriz;
    protected javax.swing.JTextField campoTexto_6_9_Matriz;
    protected javax.swing.JTextField campoTexto_7_0_Matriz;
    protected javax.swing.JTextField campoTexto_7_10_Matriz;
    protected javax.swing.JTextField campoTexto_7_11_Matriz;
    protected javax.swing.JTextField campoTexto_7_1_Matriz;
    protected javax.swing.JTextField campoTexto_7_2_Matriz;
    protected javax.swing.JTextField campoTexto_7_3_Matriz;
    protected javax.swing.JTextField campoTexto_7_4_Matriz;
    protected javax.swing.JTextField campoTexto_7_5_Matriz;
    protected javax.swing.JTextField campoTexto_7_6_Matriz;
    protected javax.swing.JTextField campoTexto_7_7_Matriz;
    protected javax.swing.JTextField campoTexto_7_8_Matriz;
    protected javax.swing.JTextField campoTexto_7_9_Matriz;
    protected javax.swing.JTextField campoTexto_8_0_Matriz;
    protected javax.swing.JTextField campoTexto_8_10_Matriz;
    protected javax.swing.JTextField campoTexto_8_11_Matriz;
    protected javax.swing.JTextField campoTexto_8_1_Matriz;
    protected javax.swing.JTextField campoTexto_8_2_Matriz;
    protected javax.swing.JTextField campoTexto_8_3_Matriz;
    protected javax.swing.JTextField campoTexto_8_4_Matriz;
    protected javax.swing.JTextField campoTexto_8_5_Matriz;
    protected javax.swing.JTextField campoTexto_8_6_Matriz;
    protected javax.swing.JTextField campoTexto_8_7_Matriz;
    protected javax.swing.JTextField campoTexto_8_8_Matriz;
    protected javax.swing.JTextField campoTexto_8_9_Matriz;
    protected javax.swing.JTextField campoTexto_9_0_Matriz;
    protected javax.swing.JTextField campoTexto_9_10_Matriz;
    protected javax.swing.JTextField campoTexto_9_11_Matriz;
    protected javax.swing.JTextField campoTexto_9_1_Matriz;
    protected javax.swing.JTextField campoTexto_9_2_Matriz;
    protected javax.swing.JTextField campoTexto_9_3_Matriz;
    protected javax.swing.JTextField campoTexto_9_4_Matriz;
    protected javax.swing.JTextField campoTexto_9_5_Matriz;
    protected javax.swing.JTextField campoTexto_9_6_Matriz;
    protected javax.swing.JTextField campoTexto_9_7_Matriz;
    protected javax.swing.JTextField campoTexto_9_8_Matriz;
    protected javax.swing.JTextField campoTexto_9_9_Matriz;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JPanel painelEntrada;
    private javax.swing.JPanel painelMatriz;
    private javax.swing.JLabel rotuloColuna_0;
    private javax.swing.JLabel rotuloColuna_1;
    private javax.swing.JLabel rotuloColuna_10;
    private javax.swing.JLabel rotuloColuna_11;
    private javax.swing.JLabel rotuloColuna_2;
    private javax.swing.JLabel rotuloColuna_3;
    private javax.swing.JLabel rotuloColuna_4;
    private javax.swing.JLabel rotuloColuna_5;
    private javax.swing.JLabel rotuloColuna_6;
    private javax.swing.JLabel rotuloColuna_7;
    private javax.swing.JLabel rotuloColuna_8;
    private javax.swing.JLabel rotuloColuna_9;
    private javax.swing.JLabel rotuloI;
    private javax.swing.JLabel rotuloLinha_0;
    private javax.swing.JLabel rotuloLinha_1;
    private javax.swing.JLabel rotuloLinha_10;
    private javax.swing.JLabel rotuloLinha_11;
    private javax.swing.JLabel rotuloLinha_2;
    private javax.swing.JLabel rotuloLinha_3;
    private javax.swing.JLabel rotuloLinha_4;
    private javax.swing.JLabel rotuloLinha_5;
    private javax.swing.JLabel rotuloLinha_6;
    private javax.swing.JLabel rotuloLinha_7;
    private javax.swing.JLabel rotuloLinha_8;
    private javax.swing.JLabel rotuloLinha_9;
    private javax.swing.JLabel rotuloP;
    private javax.swing.JLabel rotuloR;
    private javax.swing.JLabel rotuloRI;
    private javax.swing.JScrollPane scrollPaneEntrada;
    protected javax.swing.JScrollPane scrollPaneInvariancia;
    // End of variables declaration//GEN-END:variables
}
