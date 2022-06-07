package jogo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class PainelJogo extends JPanel implements ActionListener{
	
	int larguraTela = 600; //DEFINE LARGURA (X) DA TELA
	int alturaTela = 600; //DEFINE ALTURA (Y) DA TELA
	int tamanhoUnidade = 25; //TAMANHO DE TODAS AS UNIDADES E DIMENS�ES DA GRADE DO JOGO
	int totalUnidade = (larguraTela*alturaTela)/tamanhoUnidade; //DEFINE AS UNIDADES DO JOGO
	int delay = 75; //VELOCIDADE DO JOGO/DA COBRA
	int x[] = new int[larguraTela+alturaTela+1]; //LARGURA DAS UNIDADES
	int y[] = new int[larguraTela+alturaTela+1]; //ALTURA DAS UNIDADES
	int partesCobra = 6; //QUANTIDADE INICIAL DAS PARTES DA COBRA
	int pontuacao; //CONTA QUANTOS ELEMENTOS FORAM COMIDOS
	int highscore=0; //ARMAZENA �LTIMA PONTUA��O M�XIMA
	int alimentoX; //POSI��O X DO ALIMENTO
	int alimentoY; //POSI��O Y DO ALIMENTO
	char direcao = 'D'; //DIRE��O QUE A COBRA COME�A A SE MEXER (direita)
	boolean rodando = false; //CHECA SE O JOGO AINDA EST� RODANDO (true) OU PAROU (false)
	Random random; //CLASSE RANDOM
	Timer timer; //CLASSE TIMER/TEMPORIZADOR
	
	public PainelJogo() { //PAINEL QUE RODA O JOGO
		random = new Random();
		this.setPreferredSize(new Dimension(larguraTela, alturaTela)); //TAMANHO DA JANELA
		this.setBackground(Color.black); //FUNDO PRETO
		this.setFocusable(true); //FOCADO
		this.addKeyListener(new MyKeyAdapter()); //ADICIONA O SISTEMA QUE OUVE O TECLADO
		iniciarJogo();
	}
	public void iniciarJogo() {
		novoAlimento(); //CRIA UM NOVO ALIMENTO NA TELA
		rodando = true; //FAZ O JOGO COME�AR A RODAR
		timer = new Timer(delay,this); //CRIA UM TEMPORIZADOR PRA CONTINUAR CHECANDO SE O JOGO AINDA RODA
		timer.start(); //COME�A O TIMER
	}
	
	public void paintComponent(Graphics g) { //COMPONENTE PARA DESENHO
		super.paintComponent(g);
		desenhar(g);
	}
	
	public void desenhar(Graphics g) { //DESENHO DOS ELEMENTOS
		if(rodando) { //SE O JOGO AINDA ESTIVER RODANDO, OS ELEMENTOS SER�O DESENHADOS NA TELA
			for(int i=0;i<alturaTela/tamanhoUnidade;i++) { //DESENHO DA GRADE DO JOGO
				g.drawLine(i*tamanhoUnidade, 0, i*tamanhoUnidade, alturaTela);
				g.drawLine(0, i*tamanhoUnidade, larguraTela, i*tamanhoUnidade);
			}
			g.setColor(Color.red); //COR DO ALIMENTO
			//Posi��o (j� definida) e Dimens�es do Alimento ABAIXO
			g.fillOval(alimentoX, alimentoY, tamanhoUnidade, tamanhoUnidade);
			
			for(int i = 0; i < partesCobra; i++) { //DESENHO DA COBRA, CONFORME A QUANTIDADE DE PARTES
				if(i == 0) { //CABE�A
					g.setColor(Color.green); //COR VERDE
					g.fillRect(x[i], y[i], tamanhoUnidade, tamanhoUnidade); //DIMENS�ES PADR�O DA UNIDADE
				}
				else { //OUTRAS PARTES DO CORPO
					g.setColor(new Color(45, 100, 0)); //COR VERDE MAIS ESCURO
					g.fillRect(x[i], y[i], tamanhoUnidade, tamanhoUnidade); //DIMENS�ES PADR�O DAS UNIDADES
				}
			}
			//PONTUA��O ATUAL
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUA��O (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 45)); //FONTE DO TEXTO
			FontMetrics metricas1 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, M�TRICA E TAMANHO PADR�O DA FONTE
			g.drawString("PONTUA��O: " + pontuacao, (larguraTela - metricas1.stringWidth("PONTUA��O: " + pontuacao))/2, g.getFont().getSize());
			
			//PONTUA��O M�XIMA
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUA��O (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 45)); //FONTE DO TEXTO
			FontMetrics metricas2 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, M�TRICA E TAMANHO PADR�O DA FONTE
			g.drawString("HIGHSCORE: " + highscore, (larguraTela - metricas2.stringWidth("HIGHSCORE: " + highscore))/2, (alturaTela - 35));
		} //IF(RODANDO)
		else {
			fimDeJogo(g); //SE O JOGO N�O ESTIVER RODANDO, D� FIM DE JOGO
		}
	} //DRAW
	
	public void novoAlimento() {
		alimentoX = random.nextInt((int)(larguraTela/tamanhoUnidade))*tamanhoUnidade; //Posi��o X ALEAT�RIA do alimento
		alimentoY = random.nextInt((int)(alturaTela/tamanhoUnidade))*tamanhoUnidade; //Posi��o Y ALEAT�RIA do alimento
		while((alimentoX > x[partesCobra]) && (alimentoX < x[0]) && (alimentoY > y[partesCobra]) && (alimentoY < y[0])) {
			//SE CASO O ALIMENTO ESTIVER CONTIDO NA COBRA, CRIA UMA NOVA POSI��O PARA ELE
			alimentoX = random.nextInt((int)(larguraTela/tamanhoUnidade))*tamanhoUnidade;
			alimentoY = random.nextInt((int)(alturaTela/tamanhoUnidade))*tamanhoUnidade;
		}
	}
	
	public void mover() { //MOVIMENTO DA COBRA
		//CONTINUA A RODAR ENQUANTO PARTES DA COBRA MAIOR QUE ZERO e CHECA CADA PARTE DA COBRA (i--)
		for(int i = partesCobra; i > 0; i--) {
			x[i] = x[i-1]; //PEGA PARTE X DA COBRA E MOVE 1, QUANDO SE MOVE
			y[i] = y[i-1]; //PEGA PARTE Y DA COBRA E MOVE 1, QUANDO SE MOVE
		}
		
		switch(direcao) { //SWITCH DE ACORDO COM A DIRE��O DO MOVIMENTO DA COBRA
		case 'C': //Apertou CIMA
			y[0] = y[0] - tamanhoUnidade; //Posi��o da cabe�a vai uma casa pra cima, decrescendo no plano cartesiano
			break;
		case 'B': //Apertou BAIXO
			y[0] = y[0] + tamanhoUnidade; //Posi��o da cabe�a vai uma casa pra baixo, crescendo no plano cartesiano
			break;
		case 'E': //Apertou ESQUERDA
			x[0] = x[0] - tamanhoUnidade; //Posi��o da cabe�a vai uma casa pra esquerda, decrescendo no plano cartesiano
			break;
		case 'D': //Apertou DIREITA
			x[0] = x[0] + tamanhoUnidade; //Posi��o da cabe�a vai uma casa pra direita, crescendo no plano cartesiano
			break;
		}
	}
	
	public void checarAlimento() {
		if((x[0] == alimentoX) && (y[0] == alimentoY)) { //"SE A CABE�A DA COBRA E O ALIMENTO OCUPAREM O MESMO ESPA�O.."
			partesCobra++; //ADICIONA 1 A QUANTIDADE DE PARTES DA COBRA
			pontuacao++; //AUMENTA A PONTUA��O
			novoAlimento(); //CRIA UM NOVO ALIMENTO
		}
	}
	
	public void checarColisao() { //COLIS�O DA COBRA NELA MESMA E NAS PAREDES
		//CABE�A COLIDE COM CORPO
		//CONTINUA A RODAR ENQUANTO PARTES DA COBRA MAIOR QUE DE ZERO e CHECA CADA PARTE DA COBRA (i--)
		for(int i = partesCobra; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) { //SE CABE�A TOCA EM ALGUMA PARTE DO CORPO, O JOGO PARA DE RODAR
				rodando = false;
			}
		}
		//CABE�A COLIDE COM PAREDE ESQUERDA
		if(x[0] < 0) { //SE CABE�A TOCA NO IN�CIO HORIZONTAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		//CABE�A COLIDE COM PAREDE DIREITA
		if(x[0] > larguraTela-tamanhoUnidade) { //SE CABE�A TOCA NO FIM HORIZONTAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		//CABE�A COLIDE COM TOPO
		if(y[0] < 0) { //SE CABE�A TOCA NO IN�CIO VERTICAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		//CABE�A COLIDE ABAIXO
		if(y[0] > alturaTela-tamanhoUnidade) { //SE CABE�A TOCA NO FIM VERTICAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		
		if(!rodando) { //SE 'RODANDO' � FALSO, O TIMER PARA (ASSIM COMO O PR�PRIO JOGO)
			timer.stop();
		}
	}
	
	public void fimDeJogo(Graphics g) {
		if(!rodando) {
			if(pontuacao>highscore) { //SE PONTUA��O FINAL FOR MAIOR QUE A PONTUA��O M�XIMA..
				highscore = pontuacao; //PONTUA��O M�XIMA SER� PONTUA��O FINAL
			}
			//PONTUA��O FINAL
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUA��O (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO
			FontMetrics metricas1 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, M�TRICA E TAMANHO PADR�O DA FONTE
			g.drawString("PONTUA��O: " + pontuacao, (larguraTela - metricas1.stringWidth("PONTUA��O: " + pontuacao))/2, g.getFont().getSize());
			
			//PONTUA��O M�XIMA
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUA��O (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO
			FontMetrics metricas2 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, M�TRICA E TAMANHO PADR�O DA FONTE
			g.drawString("HIGHSCORE: " + highscore, (larguraTela - metricas2.stringWidth("HIGHSCORE: " + highscore))/2, g.getFont().getSize()+75);
			
			
			if(partesCobra == larguraTela+alturaTela) { //SE A COBRA OCUPAR TODOS OS ESPA�OS
				g.setColor(Color.green); //COR DA FONTE DO TEXTO PONTUA��O M�XIMA (VERDE)
				g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO
				FontMetrics metricas3 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
				//DESENHA O TEXTO, POSI��O HORIZONTAL NA TELA E POSI��O VERTICAL (METADE DA ALTURA DA TELA)
				g.drawString("Pontua��o", (larguraTela - metricas3.stringWidth("Pontua��o"))/2, alturaTela/2);
				g.drawString("M�XIMA!", (larguraTela - metricas3.stringWidth("M�XIMA"))/2, (alturaTela/2)+50);
				
				//TEXTO DE REINICIAR
				g.setColor(Color.white); //COR DA FONTE DO TEXTO DE REINICIAR (BRANCO)
				g.setFont(new Font("Monospaced", Font.BOLD, 50)); //FONTE DO TEXTO (TAMANHO 50)
				FontMetrics metricas4 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
				g.drawString("Aperte ESPA�O", (larguraTela - metricas4.stringWidth("Aperte ESPA�O"))/2, (alturaTela/2)+100);
				g.drawString("para Reiniciar", (larguraTela - metricas4.stringWidth("para Reiniciar"))/2, (alturaTela/2)+150);
			}
			else { //TELA DE FIM DE JOGO PADR�O
				//TEXTO DE FIM DE JOGO
				g.setColor(Color.red); //COR DA FONTE DO TEXTO FIM DE JOGO (VERMELHO)
				g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO (TAMANHO 75)
				FontMetrics metricas3 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
				//DESENHA O TEXTO, POSI��O HORIZONTAL NA TELA E POSI��O VERTICAL (METADE DA ALTURA DA TELA)
				g.drawString("Fim de Jogo", (larguraTela - metricas3.stringWidth("Fim de Jogo"))/2, alturaTela/2);
				
				//TEXTO DE REINICIAR
				g.setColor(Color.white); //COR DA FONTE DO TEXTO DE REINICIAR (BRANCO)
				g.setFont(new Font("Monospaced", Font.BOLD, 50)); //FONTE DO TEXTO (TAMANHO 50)
				FontMetrics metricas4 = getFontMetrics(g.getFont()); //M�TRICAS DA FONTE
				g.drawString("Aperte ESPA�O", (larguraTela - metricas4.stringWidth("Aperte ESPA�O"))/2, (alturaTela/2)+100);
				g.drawString("para Reiniciar", (larguraTela - metricas4.stringWidth("para Reiniciar"))/2, (alturaTela/2)+150);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { //SE ALGUMA A��O � OMITIDA, CHAMA ESSE M�TODO
		if(rodando) { //SE O JOGO ESTIVER RODANDO: O JOGADOR PODE SE MOVER, O JOGO CHECA O ALIMENTO E A COLIS�O
			mover();
			checarAlimento();
			checarColisao();
		}
		repaint(); //"RE-PINTA" OS ELEMENTOS NA TELA 
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) { //SE ALGUMA TECLA � PRESSIONADA, CHAMA ESTE M�TODO
			switch(e.getKeyCode()) { //SWITCH QUE CHECA SE ALGUMA TECLA FOI PRESSIONADA
			case KeyEvent.VK_UP: //APERTOU SETA CIMA
				if(direcao != 'B') { //SE N�O ESTIVER DIRECIONADO PARA BAIXO, PODE IR PARA CIMA
					direcao = 'C';
				}
				break;
			case KeyEvent.VK_DOWN: //APERTOU SETA BAIXO
				if(direcao != 'C') { //SE N�O ESTIVER DIRECIONADO PARA CIMA, PODE IR PARA BAIXO
					direcao = 'B';
				}
				break;
			case KeyEvent.VK_LEFT: //APERTOU SETA ESQUERDA
				if(direcao != 'D') { //SE N�O ESTIVER DIRECIONADO PARA DIREITA, PODE IR PARA ESQUERDA
					direcao = 'E';
				}
				break;
			case KeyEvent.VK_RIGHT: //APERTOU SETA DIREITA
				if(direcao != 'E') { //SE N�O ESTIVER DIRECIONADO PARA ESQUERDA, PODE IR PARA DIREITA
					direcao = 'D';
				}
				break;
			case KeyEvent.VK_SPACE: //APERTOU BARRA DE ESPA�O
				if(!rodando) {
					//REINICIA AS VARI�VEIS DO JOGO, DESDE PARTES DA COBRA AT� POSI��O DO ALIMENTO
					partesCobra = 6;
					direcao = 'D';
					pontuacao = 0;
					alimentoX = 0;
					alimentoY = 0;
					novoAlimento();
					//REINICIA A POSI��O DA COBRA
					x[0] = 0;
					y[0] = 0;
					//REINICIA PARTES RESTANTES DA COBRA
					for(int i = 0; i < partesCobra; i++) { //DESENHO DA COBRA, CONFORME A QUANTIDADE DE PARTES
						if(i!=0) { //PARTES DO CORPO, EXCETO A CABE�A
							//RESETA SUAS POSI��ES X E Y
							x[i] = 0;
							y[i] = 0;
						}
					}
					//REINICIA O TIMER
					timer.restart();
					//RETORNA O JOGO
					rodando = true;
				}
			} //SWITCH
		} //KEYPRESSED
	} //MY KEY ADAPTER
} //FRAME