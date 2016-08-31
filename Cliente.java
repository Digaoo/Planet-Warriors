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
  private boolean estado_jogo_adv = true;

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
  private boolean estado_jogador = false;
  private boolean pronto_para_inicio = false;
  private boolean estado_jogo = true;

  // variaveis de conexao cliente/servidor
  Socket cliente = null;
  static DataInputStream recebe_servidor = null;
  static DataOutputStream saida = null;

  /////////////// Funcoes de aquisição de dados do adversario ///////////////////
  public boolean get_estado_jogo_adv(){
    return estado_jogo_adv;
  }

  public boolean get_pronto_para_inicio(){
		return pronto_para_inicio;
	}

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
  public void put_estado_jogo(boolean estado){
    estado_jogo = estado;
  }

  public void put_estado_jogador(boolean estado){
		estado_jogador = estado;
	}

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
      do{ // segura o cliente enquanto ele nao esta pronto.
        saida.writeBoolean(estado_jogador); // envia o estado do jogador ao servidor.
      } while (!estado_jogador); // segura o cliente enquanto ele nao esta pronto.
      do { // segura o cliente que ja esta pronto enquanto o adversario nao esta pronto.
        pronto_para_inicio = recebe_servidor.readBoolean(); // receeb do servidor se o adversario esta ou nao pronto.
      } while (!pronto_para_inicio); // segura o cliente que ja esta pronto enquanto o adversario nao esta pronto.
      pronto_para_inicio=true;
    }catch(IOException e){};
    do{
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
        saida.writeBoolean(estado_jogo);
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
        estado_jogo_adv = recebe_servidor.readBoolean();
      }catch(IOException e){
        System.out.println(e);
      }
      try{
        sleep(30);
      }catch(InterruptedException e){
        System.out.println(e);
      }
    }while(estado_jogo && estado_jogo_adv);
    // finaliza o cliente
    try{
      saida.close();
      recebe_servidor.close();
      cliente.close();
    }catch(IOException e){
      System.out.println(e);
    }
  }
 }
