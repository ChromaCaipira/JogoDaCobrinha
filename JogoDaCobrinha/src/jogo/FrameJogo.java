package jogo;

import javax.swing.JFrame;

public class FrameJogo extends JFrame{
	FrameJogo() {
		this.add(new PainelJogo()); //NOVO PAINEL
		this.setTitle("Jogo da Cobrinha"); //NOME DA JANELA
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //FECHA AO CLICAR NO X
		this.setResizable(false); //N?O PODE SER REDIMENSIONADO
		this.pack(); //DESTE PACOTE
		this.setVisible(true); //APARECER EM PRIMEIRO PLANO
		this.setLocationRelativeTo(null); //APARECER NO MEIO DA TELA
	}
}