package vista;

import javax.swing.JFrame;

public class FrameJogo extends JFrame{
	FrameJogo() {
		this.add(new PainelJogo()); //NOVO PAINEL
		this.setTitle("Jogo da Cobrinha"); //NOME DA JANELA
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //FECHA AO CLICAR NO X
		this.setResizable(false); //NÃO PODE SER REDIMENSIONADO
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}