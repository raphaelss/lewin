/*
 * This file is part of Lewin, a compositional calculator.
 * Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino@gmail.com
 * Copyright (C) 2013 Liduino José Pitombeira de Oliveira, http://www.pitombeira.com
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
import java.io.File;
import java.io.PrintWriter;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class TipoTabela extends javax.swing.JFrame {
    private JCheckBox[] checkBoxes;

    public TipoTabela() {
        initComponents();

        checkBoxes = new JCheckBox[] {checkBoxTricordes, checkBoxTetracordes, checkBoxPentacordes,
                                      checkBoxHexacordes, checkBoxModosMessiaen};

        checkBoxModosMessiaen.setVisible(false);
        setLocation(400, 200);
        setSize(305, 365);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        checkBoxTricordes = new javax.swing.JCheckBox();
        checkBoxTetracordes = new javax.swing.JCheckBox();
        checkBoxPentacordes = new javax.swing.JCheckBox();
        checkBoxHexacordes = new javax.swing.JCheckBox();
        checkBoxModosMessiaen = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        botaoExibirTabela = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setLayout(null);

        checkBoxTricordes.setText("Tricordes");
        jPanel1.add(checkBoxTricordes);
        checkBoxTricordes.setBounds(20, 60, 90, 23);

        checkBoxTetracordes.setText("Tetracordes");
        jPanel1.add(checkBoxTetracordes);
        checkBoxTetracordes.setBounds(20, 100, 100, 23);

        checkBoxPentacordes.setText("Pentacordes");
        jPanel1.add(checkBoxPentacordes);
        checkBoxPentacordes.setBounds(20, 140, 100, 23);

        checkBoxHexacordes.setText("Hexacordes");
        jPanel1.add(checkBoxHexacordes);
        checkBoxHexacordes.setBounds(20, 180, 100, 23);

        checkBoxModosMessiaen.setText("Modos de Messiaen");
        jPanel1.add(checkBoxModosMessiaen);
        checkBoxModosMessiaen.setBounds(20, 220, 140, 23);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Escolha o tipo de tabela");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(40, 20, 230, 22);

        botaoExibirTabela.setText("Exibir");
        botaoExibirTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExibirTabelaActionPerformed(evt);
            }
        });
        jPanel1.add(botaoExibirTabela);
        botaoExibirTabela.setBounds(80, 270, 73, 23);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 10, 270, 310);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoExibirTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExibirTabelaActionPerformed
        String nomeCorrente, textoCorrente, nomeArquivoCorrente;
        File arquivoCorrente;
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                textoCorrente = checkBox.getText();
                nomeCorrente = "Tabela" + textoCorrente;
                nomeArquivoCorrente = nomeCorrente + ".html";
                arquivoCorrente = new File(nomeArquivoCorrente);
                arquivoCorrente.delete();
                try {
                    PrintWriter pagina = new PrintWriter(arquivoCorrente);
                    pagina.append("<html><head><title>" + nomeCorrente + "</title></head><body>"
                            + "<img src=\"" + textoCorrente + ".png\" width=\"1000\" HEIGHT=\"1000\">"
                            + "</body></html>");
                    pagina.close();
                }
                catch(Exception e) {}

                InterfaceGrafica.abrirNavegadorPadrao(nomeArquivoCorrente);
            }
        }

        setVisible(false);
    }//GEN-LAST:event_botaoExibirTabelaActionPerformed

    private final static long serialVersionUID = 1L;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoExibirTabela;
    private javax.swing.JCheckBox checkBoxHexacordes;
    private javax.swing.JCheckBox checkBoxModosMessiaen;
    private javax.swing.JCheckBox checkBoxPentacordes;
    private javax.swing.JCheckBox checkBoxTetracordes;
    private javax.swing.JCheckBox checkBoxTricordes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
