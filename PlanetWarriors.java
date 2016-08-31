import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.geom.Ellipse2D;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Shape;

class Base {
	
    int x, y; // x e y originais da imagem
    int cx,cy; // x e y do centro da imagem
    Image img; // Imagem mostrada
    Draw draw; // Instancia da classe que faz o desenho
    Shape hitbox=null;
    
    Base(int a,int b,Image img,Draw d) {
      
      x=a;
      y=b;
      cx=a-(img.getWidth(null))/2;
      cy=b-(img.getHeight(null))/2;
      this.img=img;
      draw=d;
      
    }
    
}

class Character implements ActionListener {
  
  Timer timer = new Timer(40, this);
  int x,y;
  int cx, cy;
  Image img;
  Draw draw;
  Base planeta;
  int c1,c2;
  int rw,rh;
  int add;
  int cont=0;
  double t=0;
  double troca=150;
  boolean chamado=true;
  boolean sentido=false; // true = subindo, false = descendo
  Rectangle2D.Float hitbox;
  
  Character (int a,int b,Image img,Draw d,int planet) {
	  
	draw=d;
    this.img=img;
	x=a;
	y=b;
	cx=a-(img.getWidth(null))/2;
    cy=b-(img.getHeight(null))/2;
    planeta = draw.base.get(planet);
    c1=planeta.x;
    c2=planeta.y;
    rw=(int)draw.orbitas_um[0].elipse.width/2;
    rh=(int)draw.orbitas_um[0].elipse.height/2;
    add=50;
	  
  }
  
  public void actionPerformed(ActionEvent e) {
    
    if (Math.random()*100>98&&Math.abs(troca)>150) {
	  
	  troca=0;
	  sentido = !sentido;
	  
	}
    
    if (sentido) t-=0.001/(cont+1);
    
    else if (!sentido) t+=0.001/(cont+1);
    
    troca++;
    
    coord();
    
    if (chamado)
    
      draw.repaint();
    
    chamado=false;
    
  }
  
  public void go () {
	
	timer.start();
	  
  }
  
  public void coord () {
	
	x=(int)(rw*Math.cos(Math.toDegrees(t))+c1-(img.getWidth(null))/2)+10;
	y=(int)(rh*Math.sin(Math.toDegrees(t))+c2-(img.getHeight(null))/2);
	
	hitbox.x=x;
	hitbox.y=y;
	
	chamado=true;
	  
  }
  	
}

class Tiro implements ActionListener {
  
  Timer timer = new Timer(40, this);
  int x,y;
  int cx, cy;
  int vel=5;
  Image img;
  Draw draw;
  Rectangle2D.Float hitbox;
  
  Tiro (int a,int b,Image im,Draw d) {
	  
	draw=d;
	x=a;
	y=b;
	img=im;
	cx=a-(img.getWidth(null))/2;
    cy=b-(img.getHeight(null))/2;
	timer.start();
	  
  }
  
  public void actionPerformed(ActionEvent e) {
    
    boolean parar=false;
    
    x+=vel;
    draw.repaint();
    hitbox.x=x;
    
    if ((draw.base.get(2)).hitbox.intersects(hitbox)) {
	  
	  draw.planeta2_life-=5;
	  draw.repaint(0,0,1000,10);
	  
	  if (draw.planeta2_life==0) draw.end=true;
	  
	  parar=true;
	   
	}
	
	else if ((draw.base.get(1)).hitbox.contains(hitbox)) {
	  
	  draw.planeta1_life-=5;
	  draw.repaint(0,0,1000,10);
	  
	  if (draw.planeta1_life==0) draw.end=true;
	  
	  parar=true;
	   
	}
	
	else if (draw.player2.hitbox.intersects(hitbox)) {
	  
	  draw.nave2_life-=5;
	  
	  parar=true;
	  	
	}
	
	else if (x>1000) {
	  
	  parar=true;
	  	
    }
    
    if (parar) {
	  
	  timer.stop();
	  
	  if(draw.tiro1[0]!=null&&draw.tiro1[0].equals(this)) draw.tiro1[0]=null;
	  
	  else if(draw.tiro1[1]!=null&&draw.tiro1[1].equals(this)) draw.tiro1[1]=null;
	  
	  else if(draw.tiro1[2]!=null&&draw.tiro1[2].equals(this)) draw.tiro1[2]=null;
	  	
	}
    
  }
  	
}

class Draw extends JPanel {
  
  Graphics2D g2;
  Image fundo = null;
  Image planeta_um = null;
  Image planeta_dois = null;
  Image nave_um = null;
  Image nave_dois = null;
  Image laser1=null;
  Image laser2=null;
  Elipse[] orbitas_um = new Elipse[2];
  Elipse[] orbitas_dois = new Elipse[2];
  ArrayList <Base> base = new ArrayList<>();
  Character player1;
  Character player2;
  Tiro[] tiro1 = new Tiro[3];
  Tiro[] tiro2 = new Tiro[3];
  int planeta1_life=200;
  int nave1_life=20;
  int planeta2_life=200;
  int nave2_life=20;
  boolean inicio=true;
  boolean go=false;
  int contador=3;
  Timer contagem = new Timer (1000,new ActionListener() {
	
	@Override
	public void actionPerformed(ActionEvent e) {
	  
	  contador--;
	  
	  if (contador==0) {
		
		inicio=false;
		contagem.stop();
		player1.go();
		  
	  }
	  
	  repaint();
	  	
	}
	 
  });
  boolean end=false;
  Cliente cliente=null;
  Timer check = new Timer (30,new ActionListener() {
	
	@Override
	public void actionPerformed(ActionEvent e) {
	  	  
	  repaint();
	  	
	}
	 
  });
  
  Draw () {
	
	super(true);
	
	try {
        
      fundo = ImageIO.read(new File("Estrelas.jpg"));
      planeta_um = ImageIO.read(new File("Marte.png"));
      planeta_dois = ImageIO.read(new File("Planeta.png"));
      nave_um = ImageIO.read(new File("Nave_1.png"));
      nave_dois = ImageIO.read(new File("Nave_2.png"));
      laser1 = ImageIO.read(new File("Laser1.png"));
      laser2 = ImageIO.read(new File("Laser2.png"));
        
    } catch (IOException e) {
    
      System.out.println("Erro ao carregar uma imagem!");
      System.exit(1);
     
    }
    
    base.add(new Base (0,0,fundo,this));
    Base aux = new Base (200,325,planeta_um,this);
    aux.hitbox = new Ellipse2D.Float(aux.cx,aux.cy,aux.img.getWidth(null),aux.img.getHeight(null));
    orbitas_um[0] = new Elipse(aux.x,aux.y,110,150);
    orbitas_um[1] = new Elipse(aux.x,aux.y,160,200);
    base.add(aux);
    Base aux2 = new Base (780,325,planeta_dois,this);
    aux2.hitbox = new Ellipse2D.Float(aux2.cx+8,aux2.cy+8,aux2.img.getWidth(null)-16,aux2.img.getHeight(null)-16);
    orbitas_dois[0] = new Elipse(aux2.x,aux2.y,110,150);
    orbitas_dois[1] = new Elipse(aux2.x,aux2.y,160,200);
    base.add(aux2);
    
    player1 = new Character(aux.x+(int)(orbitas_um[0].elipse.width/2)-nave_um.getWidth(null)/2+10,aux.y-nave_um.getHeight(null)/2,nave_um,this,1);
	player1.hitbox = new Rectangle2D.Float(player1.x,player1.y,player1.img.getWidth(null),player1.img.getHeight(null));
	
	player2 = new Character(aux2.x-(int)(orbitas_dois[0].elipse.width/2)-nave_dois.getWidth(null)/2-10,aux2.y-nave_dois.getHeight(null)/2,nave_dois,this,2);
	player2.hitbox = new Rectangle2D.Float(player2.x,player2.y,player2.img.getWidth(null),player2.img.getHeight(null));
	
	cliente = new Cliente();
	check.start();
	cliente.start();
	  
  }
  
  public void reset() {
	
	planeta1_life=200;
    nave1_life=20;
    planeta2_life=200;
    nave2_life=20;
    inicio=true;
    go=false;
    contador=3;
    end=false;
    player1 = player1 = new Character(base.get(1).x+(int)(orbitas_um[0].elipse.width/2)-nave_um.getWidth(null)/2+10,base.get(1).y-nave_um.getHeight(null)/2,nave_um,this,1);
	player1.hitbox = new Rectangle2D.Float(player1.x,player1.y,player1.img.getWidth(null),player1.img.getHeight(null));
	 
  }
  
  @Override
  public void paintComponent (Graphics g) { update(g); }
  
  @Override
  public void update (Graphics g) {
	 
	g2 = (Graphics2D) g;
	
	fundo(g2);
	personagem(g2);
	tiros(g2);
	dados(g2);
	if (inicio) entry(g2);
	if (end) fim(g2);
	
  }
  
  public void entry (Graphics2D g2) {
	
	g2.setColor (new Color(0,0,0,150));
	g2.fillRect (0,0,1000,600);
	g2.setColor (new Color(255,255,255));
	
	if (!go) {
	
	  g2.setFont(new Font(Font.SANS_SERIF, Font.HANGING_BASELINE, 50));
	  g2.drawString("Aperte Enter Para Continuar",130,340);
	  cliente.put_estado_jogador(true);
	
    }
    
    else if (!cliente.get_pronto_para_inicio()) {
	  
	  g2.setFont(new Font(Font.SANS_SERIF, Font.HANGING_BASELINE, 50));
	  g2.drawString("Aguardando Adversário",190,340);
	  	
	}
	
	else {
	  
	  contagem.start();
	  g2.setFont(new Font(Font.SANS_SERIF, Font.HANGING_BASELINE, 100));
	  g2.drawString(""+contador,460,340);
	  	
	}
	
  }
  
  public void fundo (Graphics2D g2) {

	Base b = base.get(0);
	g2.drawImage(b.img,b.x,b.y,null);
	
	b = base.get(1);
	g2.drawImage(b.img,b.cx,b.cy,null);
	
	b = base.get(2);
	g2.drawImage(b.img,b.cx,b.cy,null);
	
	Color c = g2.getColor();
	g2.setColor (new Color(255,255,255));
	g2.draw(orbitas_um[0].elipse);
	g2.draw(orbitas_um[1].elipse);
	g2.draw(orbitas_dois[0].elipse);
	g2.draw(orbitas_dois[1].elipse);
	  
  }
  
  public void personagem (Graphics2D g2) {
	
	g2.drawImage(player1.img,player1.x,player1.y,null);
	cliente.put_ang_nave_elipse(player1.t);
	
	player2.t = cliente.get_ang_nave_elipse_adv() + Math.PI;
	player2.coord();
	g2.drawImage(player2.img,player2.x,player2.y,null);
	  
  }
  
  public void tiros (Graphics2D g2) {
	
	g2.setColor (new Color(255,0,0));
	
	if (tiro1[0]!=null) g2.drawImage(tiro1[0].img,(int)tiro1[0].x,tiro1[0].y,null);
	
	if (tiro1[1]!=null) g2.drawImage(tiro1[1].img,(int)tiro1[1].x,tiro1[1].y,null);
	
	if (tiro1[2]!=null) g2.drawImage(tiro1[2].img,(int)tiro1[2].x,tiro1[2].y,null);
	
	if (tiro2[0]==null) {
		
		tiro2[0] = new Tiro(cliente.get_pos_disparo1_x_adv(),cliente.get_pos_disparo1_y_adv(),laser2,this); 
		tiro2[0].vel=-5;
		tiro2[0].hitbox = new Rectangle2D.Float(tiro2[0].x+10,tiro2[0].y,tiro2[0].img.getWidth(null),tiro2[0].img.getHeight(null));
		
	  }
	  
	else if (tiro2[1]==null) {
		  
	  tiro2[1] = new Tiro(cliente.get_pos_disparo2_x_adv(),cliente.get_pos_disparo2_y_adv(),laser2,this); 
	  tiro2[1].vel=-5;
	  tiro2[1].hitbox = new Rectangle2D.Float(tiro2[1].x+10,tiro2[1].y,tiro2[1].img.getWidth(null),tiro2[1].img.getHeight(null));
		
	}
	  
	else if (tiro2[2]==null) {
		  
	  tiro2[2] = new Tiro(cliente.get_pos_disparo3_x_adv(),cliente.get_pos_disparo3_y_adv(),laser2,this); 
	  tiro2[2].vel=-5;
	  tiro2[2].hitbox = new Rectangle2D.Float(tiro2[2].x+10,tiro2[2].y,tiro2[2].img.getWidth(null),tiro2[2].img.getHeight(null));
		
	}
	
	if (tiro2[0]!=null) g2.drawImage(tiro2[0].img,(int)tiro2[0].x,tiro2[0].y,null);
	
	if (tiro2[1]!=null) g2.drawImage(tiro2[1].img,(int)tiro2[1].x,tiro2[1].y,null);
	
	if (tiro2[2]!=null) g2.drawImage(tiro2[2].img,(int)tiro2[2].x,tiro2[2].y,null);
	  
  }
  
  public void dados (Graphics2D g2) {
		
	Color c = g2.getColor();
	g2.setColor (new Color(255,255,255));
	g2.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
	
	g2.drawString("Vida do Planeta: "+planeta1_life,10,30);
	g2.drawString("Vida do Planeta: "+planeta2_life,670,30);
	
	g2.setFont(new Font("TimesRoman", Font.PLAIN, 25));
	
	g2.drawString("Vida da Nave: "+nave1_life,10,60);
	g2.drawString("Vida da Nave: "+nave2_life,670,60);
	
	g2.setColor (c);
	  
  }
  
  public void fim (Graphics2D g2) {
	
	player1.timer.stop();
	
	g2.setColor (new Color(0,0,0,150));
	g2.fillRect (0,0,1000,600);
	g2.setColor (new Color(255,255,255));
	
	g2.setColor (new Color(0,255,0));
	g2.setFont(new Font(Font.SANS_SERIF, Font.HANGING_BASELINE, 50));
	
	g2.drawString("Fim de Jogo !",325,310);
	
	if (planeta1_life==0) g2.drawString("Player 2 WINS !",130,370);
	
	else g2.drawString("Player 1 WINS !",295,370);
	
	g2.setFont(new Font(Font.SANS_SERIF, Font.HANGING_BASELINE, 30));
	g2.drawString("Aperte Enter Para Jogar Novamente ",220,560);
	  
  }
  
}

class Elipse {
	
  Ellipse2D.Float elipse;
  
  Elipse (int x,int y,int w,int h) {
	
	elipse = new Ellipse2D.Float(x-w,y-h,w*2,h*2);
	  
  }
  	
} 

class Teclado extends KeyAdapter {
  
  Draw draw;
  JFrame jan;
  boolean sobe=true;
  boolean desce=true;
  
  Teclado (Draw aux,JFrame j) {
	  
	draw = aux;
	jan=j;
	  
  }

  @Override
  public void keyPressed (KeyEvent e) {
	  
	int cod = e.getKeyCode();
    
    if ((cod==KeyEvent.VK_LEFT||cod==KeyEvent.VK_A)&&desce&&!draw.inicio&&!draw.end) { 
	  
	  
	 if (draw.player1.cont==0) {
		
		draw.player1.rw+=draw.player1.add; 
		draw.player1.rh+=draw.player1.add; 
		draw.player1.cont++; 
		
	  }
	  
	  sobe=true;
	  desce=false;
	  	
	}
    
    else if ((cod==KeyEvent.VK_RIGHT||cod==KeyEvent.VK_D)&&sobe&&!draw.inicio&&!draw.end) { 
		  
	  if (draw.player1.cont==1) { 
		
		draw.player1.rw-=draw.player1.add;
		draw.player1.rh-=draw.player1.add;
		draw.player1.cont--; 
		
	  }
	  
	  desce=true;
	  sobe=false;
	
	}
    
    else if (cod==KeyEvent.VK_SPACE&&!draw.inicio&&!draw.end) {
	  
	  if (draw.tiro1[0]==null) {
		
		draw.tiro1[0] = new Tiro(draw.player1.x+draw.player1.img.getWidth(null)+10,draw.player1.y+draw.player1.img.getHeight(null)/2-8,draw.laser1,draw); 
		draw.tiro1[0].hitbox = new Rectangle2D.Float(draw.tiro1[0].x,draw.tiro1[0].y,draw.tiro1[0].img.getWidth(null)-10,draw.tiro1[0].img.getHeight(null));
		draw.cliente.put_pos_disparo1_x(draw.tiro1[0].x);
		draw.cliente.put_pos_disparo1_y(draw.tiro1[0].y);
		
	  }
	  
	  else if (draw.tiro1[1]==null) {
		  
		draw.tiro1[1] = new Tiro(draw.player1.x+draw.player1.img.getWidth(null)+10,draw.player1.y+draw.player1.img.getHeight(null)/2-8,draw.laser1,draw);
		draw.tiro1[1].hitbox = new Rectangle2D.Float(draw.tiro1[1].x,draw.tiro1[1].y,draw.tiro1[1].img.getWidth(null)-10,draw.tiro1[1].img.getHeight(null));
		draw.cliente.put_pos_disparo2_x(draw.tiro1[1].x);
		draw.cliente.put_pos_disparo2_y(draw.tiro1[1].y);
		
	  }
	  
	  else if (draw.tiro1[2]==null) {
		  
		draw.tiro1[2] = new Tiro(draw.player1.x+draw.player1.img.getWidth(null)+10,draw.player1.y+draw.player1.img.getHeight(null)/2-8,draw.laser1,draw);
		draw.tiro1[2].hitbox = new Rectangle2D.Float(draw.tiro1[2].x,draw.tiro1[2].y,draw.tiro1[2].img.getWidth(null)-10,draw.tiro1[2].img.getHeight(null));
		draw.cliente.put_pos_disparo3_x(draw.tiro1[2].x);
		draw.cliente.put_pos_disparo3_y(draw.tiro1[2].y);
		
	  }
	  	
	}
    
    else if (cod==KeyEvent.VK_ENTER) {
	  
	  if (draw.inicio) {
	  
	    draw.go=true;
	    draw.repaint();
	    
	  }
	  
	  else if (draw.end) {
		
		draw.reset();
		draw.repaint();
		  
	  }
	  
	  else return;
	  
	}
    
    else return;
    
    draw.player1.coord();
    
  }
  
}

class PlanetWarriors {
	
  JFrame jan = new JFrame ("Planet Warriors !");
  
  public static void main (String[] args) {
	
	PlanetWarriors pw = new PlanetWarriors();
	Draw draw = new Draw();
	
	pw.jan.setSize(new Dimension(1000,600));
	pw.jan.setResizable(false);
	pw.jan.add(draw);
	pw.jan.addKeyListener(new Teclado(draw,pw.jan));
	pw.jan.setVisible(true);
	  
  }
  	
}
