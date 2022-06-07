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
	int tamanhoUnidade = 25; //TAMANHO DE TODAS AS UNIDADES E DIMENSÕES DA GRADE DO JOGO
	int totalUnidade = (larguraTela*alturaTela)/tamanhoUnidade; //DEFINE AS UNIDADES DO JOGO
	int delay = 75; //VELOCIDADE DO JOGO/DA COBRA
	int x[] = new int[larguraTela+alturaTela+1]; //LARGURA DAS UNIDADES
	int y[] = new int[larguraTela+alturaTela+1]; //ALTURA DAS UNIDADES
	int partesCobra = 6; //QUANTIDADE INICIAL DAS PARTES DA COBRA
	int pontuacao; //CONTA QUANTOS ELEMENTOS FORAM COMIDOS
	int highscore=0; //ARMAZENA ÚLTIMA PONTUAÇÃO MÁXIMA
	int alimentoX; //POSIÇÃO X DO ALIMENTO
	int alimentoY; //POSIÇÃO Y DO ALIMENTO
	char direcao = 'D'; //DIREÇÃO QUE A COBRA COMEÇA A SE MEXER (direita)
	boolean rodando = false; //CHECA SE O JOGO AINDA ESTÁ RODANDO (true) OU PAROU (false)
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
		rodando = true; //FAZ O JOGO COMEÇAR A RODAR
		timer = new Timer(delay,this); //CRIA UM TEMPORIZADOR PRA CONTINUAR CHECANDO SE O JOGO AINDA RODA
		timer.start(); //COMEÇA O TIMER
	}
	
	public void paintComponent(Graphics g) { //COMPONENTE PARA DESENHO
		super.paintComponent(g);
		desenhar(g);
	}
	
	public void desenhar(Graphics g) { //DESENHO DOS ELEMENTOS
		if(rodando) { //SE O JOGO AINDA ESTIVER RODANDO, OS ELEMENTOS SERÃO DESENHADOS NA TELA
			for(int i=0;i<alturaTela/tamanhoUnidade;i++) { //DESENHO DA GRADE DO JOGO
				g.drawLine(i*tamanhoUnidade, 0, i*tamanhoUnidade, alturaTela);
				g.drawLine(0, i*tamanhoUnidade, larguraTela, i*tamanhoUnidade);
			}
			g.setColor(Color.red); //COR DO ALIMENTO
			//Posição (já definida) e Dimensões do Alimento ABAIXO
			g.fillOval(alimentoX, alimentoY, tamanhoUnidade, tamanhoUnidade);
			
			for(int i = 0; i < partesCobra; i++) { //DESENHO DA COBRA, CONFORME A QUANTIDADE DE PARTES
				if(i == 0) { //CABEÇA
					g.setColor(Color.green); //COR VERDE
					g.fillRect(x[i], y[i], tamanhoUnidade, tamanhoUnidade); //DIMENSÕES PADRÃO DA UNIDADE
				}
				else { //OUTRAS PARTES DO CORPO
					g.setColor(new Color(45, 100, 0)); //COR VERDE MAIS ESCURO
					g.fillRect(x[i], y[i], tamanhoUnidade, tamanhoUnidade); //DIMENSÕES PADRÃO DAS UNIDADES
				}
			}
			//PONTUAÇÃO ATUAL
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUAÇÃO (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 45)); //FONTE DO TEXTO
			FontMetrics metricas1 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, MÉTRICA E TAMANHO PADRÃO DA FONTE
			g.drawString("PONTUAÇÃO: " + pontuacao, (larguraTela - metricas1.stringWidth("PONTUAÇÃO: " + pontuacao))/2, g.getFont().getSize());
			
			//PONTUAÇÃO MÁXIMA
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUAÇÃO (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 45)); //FONTE DO TEXTO
			FontMetrics metricas2 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, MÉTRICA E TAMANHO PADRÃO DA FONTE
			g.drawString("HIGHSCORE: " + highscore, (larguraTela - metricas2.stringWidth("HIGHSCORE: " + highscore))/2, (alturaTela - 35));
		} //IF(RODANDO)
		else {
			fimDeJogo(g); //SE O JOGO NÃO ESTIVER RODANDO, DÁ FIM DE JOGO
		}
	} //DRAW
	
	public void novoAlimento() {
		alimentoX = random.nextInt((int)(larguraTela/tamanhoUnidade))*tamanhoUnidade; //Posição X ALEATÓRIA do alimento
		alimentoY = random.nextInt((int)(alturaTela/tamanhoUnidade))*tamanhoUnidade; //Posição Y ALEATÓRIA do alimento
		while((alimentoX > x[partesCobra]) && (alimentoX < x[0]) && (alimentoY > y[partesCobra]) && (alimentoY < y[0])) {
			//SE CASO O ALIMENTO ESTIVER CONTIDO NA COBRA, CRIA UMA NOVA POSIÇÃO PARA ELE
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
		
		switch(direcao) { //SWITCH DE ACORDO COM A DIREÇÃO DO MOVIMENTO DA COBRA
		case 'C': //Apertou CIMA
			y[0] = y[0] - tamanhoUnidade; //Posição da cabeça vai uma casa pra cima, decrescendo no plano cartesiano
			break;
		case 'B': //Apertou BAIXO
			y[0] = y[0] + tamanhoUnidade; //Posição da cabeça vai uma casa pra baixo, crescendo no plano cartesiano
			break;
		case 'E': //Apertou ESQUERDA
			x[0] = x[0] - tamanhoUnidade; //Posição da cabeça vai uma casa pra esquerda, decrescendo no plano cartesiano
			break;
		case 'D': //Apertou DIREITA
			x[0] = x[0] + tamanhoUnidade; //Posição da cabeça vai uma casa pra direita, crescendo no plano cartesiano
			break;
		}
	}
	
	public void checarAlimento() {
		if((x[0] == alimentoX) && (y[0] == alimentoY)) { //"SE A CABEÇA DA COBRA E O ALIMENTO OCUPAREM O MESMO ESPAÇO.."
			partesCobra++; //ADICIONA 1 A QUANTIDADE DE PARTES DA COBRA
			pontuacao++; //AUMENTA A PONTUAÇÃO
			novoAlimento(); //CRIA UM NOVO ALIMENTO
		}
	}
	
	public void checarColisao() { //COLISÃO DA COBRA NELA MESMA E NAS PAREDES
		//CABEÇA COLIDE COM CORPO
		//CONTINUA A RODAR ENQUANTO PARTES DA COBRA MAIOR QUE DE ZERO e CHECA CADA PARTE DA COBRA (i--)
		for(int i = partesCobra; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) { //SE CABEÇA TOCA EM ALGUMA PARTE DO CORPO, O JOGO PARA DE RODAR
				rodando = false;
			}
		}
		//CABEÇA COLIDE COM PAREDE ESQUERDA
		if(x[0] < 0) { //SE CABEÇA TOCA NO INÍCIO HORIZONTAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		//CABEÇA COLIDE COM PAREDE DIREITA
		if(x[0] > larguraTela-tamanhoUnidade) { //SE CABEÇA TOCA NO FIM HORIZONTAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		//CABEÇA COLIDE COM TOPO
		if(y[0] < 0) { //SE CABEÇA TOCA NO INÍCIO VERTICAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		//CABEÇA COLIDE ABAIXO
		if(y[0] > alturaTela-tamanhoUnidade) { //SE CABEÇA TOCA NO FIM VERTICAL DA TELA: PARA DE RODAR
			rodando = false;
		}
		
		if(!rodando) { //SE 'RODANDO' É FALSO, O TIMER PARA (ASSIM COMO O PRÓPRIO JOGO)
			timer.stop();
		}
	}
	
	public void fimDeJogo(Graphics g) {
		if(!rodando) {
			if(pontuacao>highscore) { //SE PONTUAÇÃO FINAL FOR MAIOR QUE A PONTUAÇÃO MÁXIMA..
				highscore = pontuacao; //PONTUAÇÃO MÁXIMA SERÁ PONTUAÇÃO FINAL
			}
			//PONTUAÇÃO FINAL
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUAÇÃO (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO
			FontMetrics metricas1 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, MÉTRICA E TAMANHO PADRÃO DA FONTE
			g.drawString("PONTUAÇÃO: " + pontuacao, (larguraTela - metricas1.stringWidth("PONTUAÇÃO: " + pontuacao))/2, g.getFont().getSize());
			
			//PONTUAÇÃO MÁXIMA
			g.setColor(Color.GRAY); //COR DA FONTE DA PONTUAÇÃO (CINZA)
			g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO
			FontMetrics metricas2 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
			//DESENHA O TEXTO, COM TAL COR, MÉTRICA E TAMANHO PADRÃO DA FONTE
			g.drawString("HIGHSCORE: " + highscore, (larguraTela - metricas2.stringWidth("HIGHSCORE: " + highscore))/2, g.getFont().getSize()+75);
			
			
			if(partesCobra == larguraTela+alturaTela) { //SE A COBRA OCUPAR TODOS OS ESPAÇOS
				g.setColor(Color.green); //COR DA FONTE DO TEXTO PONTUAÇÃO MÁXIMA (VERDE)
				g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO
				FontMetrics metricas3 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
				//DESENHA O TEXTO, POSIÇÃO HORIZONTAL NA TELA E POSIÇÃO VERTICAL (METADE DA ALTURA DA TELA)
				g.drawString("Pontuação", (larguraTela - metricas3.stringWidth("Pontuação"))/2, alturaTela/2);
				g.drawString("MÁXIMA!", (larguraTela - metricas3.stringWidth("MÁXIMA"))/2, (alturaTela/2)+50);
				
				//TEXTO DE REINICIAR
				g.setColor(Color.white); //COR DA FONTE DO TEXTO DE REINICIAR (BRANCO)
				g.setFont(new Font("Monospaced", Font.BOLD, 50)); //FONTE DO TEXTO (TAMANHO 50)
				FontMetrics metricas4 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
				g.drawString("Aperte ESPAÇO", (larguraTela - metricas4.stringWidth("Aperte ESPAÇO"))/2, (alturaTela/2)+100);
				g.drawString("para Reiniciar", (larguraTela - metricas4.stringWidth("para Reiniciar"))/2, (alturaTela/2)+150);
			}
			else { //TELA DE FIM DE JOGO PADRÃO
				//TEXTO DE FIM DE JOGO
				g.setColor(Color.red); //COR DA FONTE DO TEXTO FIM DE JOGO (VERMELHO)
				g.setFont(new Font("Monospaced", Font.BOLD, 75)); //FONTE DO TEXTO (TAMANHO 75)
				FontMetrics metricas3 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
				//DESENHA O TEXTO, POSIÇÃO HORIZONTAL NA TELA E POSIÇÃO VERTICAL (METADE DA ALTURA DA TELA)
				g.drawString("Fim de Jogo", (larguraTela - metricas3.stringWidth("Fim de Jogo"))/2, alturaTela/2);
				
				//TEXTO DE REINICIAR
				g.setColor(Color.white); //COR DA FONTE DO TEXTO DE REINICIAR (BRANCO)
				g.setFont(new Font("Monospaced", Font.BOLD, 50)); //FONTE DO TEXTO (TAMANHO 50)
				FontMetrics metricas4 = getFontMetrics(g.getFont()); //MÉTRICAS DA FONTE
				g.drawString("Aperte ESPAÇO", (larguraTela - metricas4.stringWidth("Aperte ESPAÇO"))/2, (alturaTela/2)+100);
				g.drawString("para Reiniciar", (larguraTela - metricas4.stringWidth("para Reiniciar"))/2, (alturaTela/2)+150);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { //SE ALGUMA AÇÃO É OMITIDA, CHAMA ESSE MÉTODO
		if(rodando) { //SE O JOGO ESTIVER RODANDO: O JOGADOR PODE SE MOVER, O JOGO CHECA O ALIMENTO E A COLISÃO
			mover();
			checarAlimento();
			checarColisao();
		}
		repaint(); //"RE-PINTA" OS ELEMENTOS NA TELA 
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) { //SE ALGUMA TECLA É PRESSIONADA, CHAMA ESTE MÉTODO
			switch(e.getKeyCode()) { //SWITCH QUE CHECA SE ALGUMA TECLA FOI PRESSIONADA
			case KeyEvent.VK_UP: //APERTOU SETA CIMA
				if(direcao != 'B') { //SE NÃO ESTIVER DIRECIONADO PARA BAIXO, PODE IR PARA CIMA
					direcao = 'C';
				}
				break;
			case KeyEvent.VK_DOWN: //APERTOU SETA BAIXO
				if(direcao != 'C') { //SE NÃO ESTIVER DIRECIONADO PARA CIMA, PODE IR PARA BAIXO
					direcao = 'B';
				}
				break;
			case KeyEvent.VK_LEFT: //APERTOU SETA ESQUERDA
				if(direcao != 'D') { //SE NÃO ESTIVER DIRECIONADO PARA DIREITA, PODE IR PARA ESQUERDA
					direcao = 'E';
				}
				break;
			case KeyEvent.VK_RIGHT: //APERTOU SETA DIREITA
				if(direcao != 'E') { //SE NÃO ESTIVER DIRECIONADO PARA ESQUERDA, PODE IR PARA DIREITA
					direcao = 'D';
				}
				break;
			case KeyEvent.VK_SPACE: //APERTOU BARRA DE ESPAÇO
				if(!rodando) {
					//REINICIA AS VARIÁVEIS DO JOGO, DESDE PARTES DA COBRA ATÉ POSIÇÃO DO ALIMENTO
					partesCobra = 6;
					direcao = 'D';
					pontuacao = 0;
					alimentoX = 0;
					alimentoY = 0;
					novoAlimento();
					//REINICIA A POSIÇÃO DA COBRA
					x[0] = 0;
					y[0] = 0;
					//REINICIA PARTES RESTANTES DA COBRA
					for(int i = 0; i < partesCobra; i++) { //DESENHO DA COBRA, CONFORME A QUANTIDADE DE PARTES
						if(i!=0) { //PARTES DO CORPO, EXCETO A CABEÇA
							//RESETA SUAS POSIÇÕES X E Y
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