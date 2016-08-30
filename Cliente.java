import java.net.*;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

// COLOCA "Cliente cliente = new Cliente();" "cliente.start();" PARA RODAR O CLIENTE. PODE COLOCAR NO COMEÇO DO PROGRAMA.
// PRA VOCE PEGAR AS VARIAVEIS VOCE USA AS FUNCOES TIPO: "cliente.get_Pos_Nave_X_Adv();". Da uma olhada que elas estao aí em baixo.
// Pra colocar os valores das variaveis da nave voce usa as funcoes tipo: "cliente.put_Pos_Nave_Y(int coordenadaxy)".

public class Cliente extends Thread{
  private int identificador_jogador;

  // Posicoes do adversario.
  private double ang_nave_elipse_adv;
  private int pos_disparo1_x_adv = -50;
  private int pos_disparo1_y_adv;
  private int pos_disparo2_x_adv = -50;
  private int pos_disparo2_y_adv;
  private int pos_disparo3_x_adv = -50;
  private int pos_disparo3_y_adv;
  private int vida_planeta_adv;
  private int vida_nave_adv;

  // posicao da nave atual.
  private double ang_nave_elipse;
  private int pos_disparo1_x = -50;
  private int pos_disparo1_y;
  private int pos_disparo2_x = -50;
  private int pos_disparo2_y;
  private int pos_disparo3_x = -50;
  private int pos_disparo3_y;
  private int vida_planeta;
  private int vida_nave;

  // variaveis de conexao cliente/servidor
  Socket cliente = null;
  static DataInputStream recebe_servidor = null;
  static DataOutputStream saida = null;

  /////////////// Funcoes de aquisição de dados do adversario ///////////////////
  public int get_vida_nave_adv(){
		return vida_nave_adv;
	}

  public int get_vida_planeta_adv(){
		return vida_planeta_adv;
	}

  public int get_identificador_jogador(){
		return identificador_jogador;
	}

  public double get_ang_nave_elipse_adv(){
    return ang_nave_elipse_adv;
  }

  public int get_pos_disparo1_x_adv(){
    return pos_disparo1_x_adv;
  }

  public int get_pos_disparo1_y_adv(){
    return pos_disparo1_y_adv;
  }

  public int get_pos_disparo2_x_adv(){
    return pos_disparo2_x_adv;
  }

  public int get_pos_disparo2_y_adv(){
    return pos_disparo2_y_adv;
  }

  public int get_pos_disparo3_x_adv(){
    return pos_disparo3_x_adv;
  }

  public int get_pos_disparo3_y_adv(){
    return pos_disparo3_y_adv;
  }

  /////////// Funcoes de envio de dados da nave para o servidor ///////////
  public void put_vida_nave_adv(int vida){
    vida_nave = vida;
  }

  public void out_vida_planeta_adv(int vida){
    vida_planeta = vida;
  }

  public void put_ang_nave_elipse(double ang){
     ang_nave_elipse = ang;
  }

  public void put_pos_disparo1_x(int posX){
    pos_disparo1_x = posX;
  }
  public void put_pos_disparo1_y(int posY){
    pos_disparo1_y = posY;
  }

  public void put_pos_disparo2_x(int posX){
    pos_disparo2_x = posX;
  }

  public void put_pos_disparo2_y(int posY){
    pos_disparo2_y = posY;
  }

  public void put_pos_disparo3_x(int posX){
    pos_disparo3_x = posX;
  }

  public void put_pos_disparo3_y(int posY){
    pos_disparo3_y = posY;
  }

  // thread de inicio do cliente
  public void run(){
    try{
      cliente = new Socket("127.0.0.1", 12345); // Estabelece conexão com o servidor
      System.out.println("O cliente se conectou ao servidor!");
      recebe_servidor = new DataInputStream(cliente.getInputStream()); // Estabelece a entrada de dados vindos do servidor.
      saida = new DataOutputStream(cliente.getOutputStream()); // Estabelece a saida de dados para o servidor.
      identificador_jogador = recebe_servidor.readInt(); // Recebe o numero de jogador respectivo a cada cliente (vem do servidor).
      System.out.println("identificador_jogador:  " + identificador_jogador);
    }catch(IOException e){};
    while(true){ // talvez podemos fazer parar assim que o jogo acaba, mas fazermos isso depois.
      try{
        // envia os dados ao servidor.
        saida.writeDouble(ang_nave_elipse);
        saida.writeInt(pos_disparo1_x);
        saida.writeInt(pos_disparo1_y);
        saida.writeInt(pos_disparo2_x);
        saida.writeInt(pos_disparo2_y);
        saida.writeInt(pos_disparo3_x);
        saida.writeInt(pos_disparo3_y);
        saida.writeInt(vida_nave);
        saida.writeInt(vida_planeta);
      }catch(IOException e){
        System.out.println(e);
      }
      try{
        // recebe os dados da outra nave do servidor.
        ang_nave_elipse_adv = recebe_servidor.readDouble();
        pos_disparo1_x_adv = recebe_servidor.readInt();
        pos_disparo1_y_adv = recebe_servidor.readInt();
        pos_disparo2_x_adv = recebe_servidor.readInt();
        pos_disparo2_y_adv = recebe_servidor.readInt();
        pos_disparo3_x_adv = recebe_servidor.readInt();
        pos_disparo3_y_adv = recebe_servidor.readInt();
        vida_planeta_adv = recebe_servidor.readInt();
        vida_nave_adv = recebe_servidor.readInt();
      }catch(IOException e){
        System.out.println(e);
      }
      try{
        sleep(30);
      }catch(InterruptedException e){
        System.out.println(e);
      }
    }
  }
 }
