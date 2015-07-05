package GUI;


import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.Raster;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author paulino
 */
public class Imagem {
    private BufferedImage imagem;
    private WritableRaster acessoAPixels;
    
    public Imagem(int largura, int altura) {
        imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_4BYTE_ABGR);
        acessoAPixels = imagem.getRaster();
    }
    
    public Imagem(String endereco) {
        lerImagem(endereco);
    }
    
    public Imagem(BufferedImage image) {
        imagem = image;
        acessoAPixels = image.getRaster();
    }

    public Imagem(Imagem im) {
        imagem = im.getBufferedImage();
        acessoAPixels = imagem.getRaster();
    }

    public final void lerImagem(String endereco) {
        try {
            BufferedImage imagemTemporaria = ImageIO.read(new File(endereco));
            WritableRaster rasterTemporario = imagemTemporaria.getRaster();
            
            int largura = imagemTemporaria.getWidth(), altura = imagemTemporaria.getHeight();
            imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_4BYTE_ABGR);
            acessoAPixels = imagem.getRaster();
            int[] valores = new int[4];
            valores[3] = 255;
            
            for (int i = 0; i < largura; i++) {
                for (int j = 0; j < altura; j++) {
                    rasterTemporario.getPixel(i, j, valores);
//                    if ((valores[0] != 0 && valores[0] != 255) || (valores[1] != 0 && valores[1] != 255) || 
//                        (valores[2] != 0 && valores[2] != 255)) {
//                        System.out.println("Valor diferente: (" + i + "," + j + ") " + Arrays.toString(valores));
//                    }
                    acessoAPixels.setPixel(i, j, valores);
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "O endereco da imagem est\u00e1 "
                                    + "incorreto.\nA leitura n\u00e3o foi feita.");
        }
    }
    
    public void setR(int x, int y, int valor) throws ArrayIndexOutOfBoundsException {
        try {
            int [] valores = new int[5];
            acessoAPixels.getPixel(x, y, valores);
            valores[0] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }
    
    public void setG(int x, int y, int valor) throws ArrayIndexOutOfBoundsException {
        try {
            int [] valores = new int[5];
            acessoAPixels.getPixel(x, y, valores);
            valores[1] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }
    
    public void setB(int x, int y, int valor) throws ArrayIndexOutOfBoundsException {
        try {    
            int [] valores = new int[5];
            acessoAPixels.getPixel(x, y, valores);
            valores[2] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }
    
    public void setAlpha(int x, int y, int valor) {
        try {    
            int [] valores = new int[5];
            acessoAPixels.getPixel(x, y, valores);
            valores[3] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }

    public void setRG(int x, int y, int valor) throws ArrayIndexOutOfBoundsException {
        try {
            int [] valores = new int[5];
            acessoAPixels.getPixel(x, y, valores);
            valores[0] = valor;
            valores[1] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }

    public void setRB(int x, int y, int valor) throws ArrayIndexOutOfBoundsException {
        try {
            int [] valores = new int[5];
            acessoAPixels.getPixel(x, y, valores);
            valores[0] = valor;
            valores[2] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }

    public void setGB(int x, int y, int valor) throws ArrayIndexOutOfBoundsException {
        try {
            int [] valores = new int[5];
            acessoAPixels.getPixel(x, y, valores);
            valores[1] = valor;
            valores[2] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }
    
    public void setRGB(int x, int y, int valor) throws ArrayIndexOutOfBoundsException {
        try {    
            int [] valores = new int[5];
            valores[0] = valor;
            valores[1] = valor;
            valores[2] = valor;
            acessoAPixels.setPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(x, y) = (" + x + ", " + y + ")");
        }
    }
    
    public int getR(int x, int y) {
        int[] valores = new int[5];
        
        try {
            acessoAPixels.getPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("(x, y) = (" + x + ", " + y + ")");
            aioobe.printStackTrace();
        }
        return valores[0];
    }
    
    public int getG(int x, int y) {
        int[] valores = new int[5];
        
        try {
            acessoAPixels.getPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("(x, y) = (" + x + ", " + y + ")");
            aioobe.printStackTrace();
        }
        return valores[1];
    }
    
    public int getB(int x, int y) {
        int[] valores = new int[5];
        
        try {
            acessoAPixels.getPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("(x, y) = (" + x + ", " + y + ")");
            aioobe.printStackTrace();
        }
        return valores[2];
    }
    
    public int getRGB(int x, int y) {
        int valor = 0;
        
        try {
            valor = imagem.getRGB(x, y);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("(x, y) = (" + x + ", " + y + ")");
            aioobe.printStackTrace();
        }
        return valor;
    }
    
    public int getAlpha(int x, int y) {
        int[] valores = new int[5];
        
        try {
            acessoAPixels.getPixel(x, y, valores);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("(x, y) = (" + x + ", " + y + ")");
            aioobe.printStackTrace();
        }
        return valores[3];
    }
    
    public void grava(String nome) {
        try {
            ImageIO.write(imagem, nome.substring(nome.lastIndexOf(".") + 1), new File(nome));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro na escrita da imagem.");
            ex.printStackTrace();
        }
    }
    
    public int getWidth() {
        return imagem.getWidth();
    }
    
    public int getHeight() {
        return imagem.getHeight();
    }
    
    public BufferedImage getBufferedImage() {
        return imagem.getSubimage(0, 0, imagem.getWidth(), imagem.getHeight());
    }
    
    public void setImage(Imagem i) {
        imagem = i.getBufferedImage();
    }
    
    public Imagem subImagem(int x, int y, int largura, int altura) {
        return new Imagem(imagem.getSubimage(x, y, largura, altura));
    }
}
