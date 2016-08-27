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
  private double ang_Nave_elipse_Adv;
  private int Pos_Disparo1_X_Adv = -50;
  private int Pos_Disparo1_Y_Adv;
  private int Pos_Disparo2_X_Adv = -50;
  private int Pos_Disparo2_Y_Adv;
  private int Pos_Disparo3_X_Adv = -50;
  private int Pos_Disparo3_Y_Adv;

  // posicao da nave atual.
  private double ang_Nave_elipse;
  private int Pos_Disparo1_X = -50;
  private int Pos_Disparo1_Y;
  private int Pos_Disparo2_X = -50;
  private int Pos_Disparo2_Y;
  private int Pos_Disparo3_X = -50;
  private int Pos_Disparo3_Y;

  Socket cliente = null;
  static DataInputStream recebe_servidor = null;
  static DataOutputStream saida = null;

  /////////////// Funcoes de aquisição de dados do adversario ///////////////////
  public int get_identificador_jogador() {
	return identificador_jogador;  
  }
  
  public double get_ang_Nave_elipse_Adv(){
    return ang_Nave_elipse_Adv;
  }

  public int get_Pos_Disparo1_X_Adv(){
    return Pos_Disparo1_X_Adv;
  }

  public int get_Pos_Disparo1_Y_Adv(){
    return Pos_Disparo1_Y_Adv;
  }
  
  public int get_Pos_Disparo2_X_Adv(){
    return Pos_Disparo2_X_Adv;
  }

  public int get_Pos_Disparo2_Y_Adv(){
    return Pos_Disparo2_Y_Adv;
  }
  
  public int get_Pos_Disparo3_X_Adv(){
    return Pos_Disparo3_X_Adv;
  }

  public int get_Pos_Disparo3_Y_Adv(){
    return Pos_Disparo3_Y_Adv;
  }

  /////////// Funcoes de envio de dados da nave para o servidor ///////////
  public void put_ang_Nave_elipse(double ang){
     ang_Nave_elipse = ang;
  }

  public void put_Pos_Disparo1_X(int posX){
    Pos_Disparo1_X = posX;
  }
  public void put_Pos_Disparo1_Y(int posY){
    Pos_Disparo1_Y = posY;
  }
  
  public void put_Pos_Disparo2_X(int posX){
    Pos_Disparo2_X = posX;
  }
  
  public void put_Pos_Disparo2_Y(int posY){
    Pos_Disparo2_Y = posY;
  }
  
  public void put_Pos_Disparo3_X(int posX){
    Pos_Disparo3_X = posX;
  }
  
  public void put_Pos_Disparo3_Y(int posY){
    Pos_Disparo3_Y = posY;
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

      // DEBUG
      /*if(identificador_jogador == 0){
        Pos_Nave_X = 0;
        Pos_Nave_Y = 0;
        Pos_Disparo_X = 0;
        Pos_Disparo_Y = 0;
      }
      else{
        Pos_Nave_X = 1;
        Pos_Nave_Y = 1;
        Pos_Disparo_X = 1;
        Pos_Disparo_Y = 1;
      }*/

    while(true){ // talvez podemos fazer parar assim que o jogo acaba, mas fazermos isso depois.
      try{
        // envia os dados ao servidor.
        saida.writeDouble(ang_Nave_elipse);
        saida.writeInt(Pos_Disparo1_X);
        saida.writeInt(Pos_Disparo1_Y);
        saida.writeInt(Pos_Disparo2_X);
        saida.writeInt(Pos_Disparo2_Y);
        saida.writeInt(Pos_Disparo3_X);
        saida.writeInt(Pos_Disparo3_Y);
      }catch(IOException e){
        System.out.println(e);
      }
      try{
        // recebe os dados da outra nave do servidor.
        ang_Nave_elipse_Adv = recebe_servidor.readDouble();
        Pos_Disparo1_X_Adv = recebe_servidor.readInt();
        Pos_Disparo1_Y_Adv = recebe_servidor.readInt();
        Pos_Disparo2_X_Adv = recebe_servidor.readInt();
        Pos_Disparo2_Y_Adv = recebe_servidor.readInt();
        Pos_Disparo3_X_Adv = recebe_servidor.readInt();
        Pos_Disparo3_Y_Adv = recebe_servidor.readInt();
      }catch(IOException e){
        System.out.println(e);
      }
      // DEBUG
      /*System.out.println("Pos_Nave_X_Adv:  " + Pos_Nave_X_Adv + " I: " + identificador_jogador);
      System.out.println("Pos_Nave_Y_Adv:  " + Pos_Nave_Y_Adv + " I: " + identificador_jogador);
      System.out.println("Pos_Disparo_X_Adv:  " + Pos_Disparo_X_Adv + " I: " + identificador_jogador);
      System.out.println("Pos_Disparo_Y_Adv:  " + Pos_Disparo_Y_Adv + " I: " + identificador_jogador);
      System.out.println(" ");*/
      try{
        sleep(30);
      }catch(InterruptedException e){
        System.out.println(e);
      }
    }
  }
 }

