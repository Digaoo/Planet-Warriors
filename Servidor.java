import java.net.*;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class Servidor {
  public static void main(String args[]) throws IOException { // TRATAR O ERRO DE MANEIRA MAIS ADEQUADA!

    ServerSocket servidor = new ServerSocket(12345); // Definida a porta a ser aberta e usada pelo servidor.
    System.out.println("Porta 12345 aberta!"); // Mensagem de sucesso.
    Socket jogador1 = servidor.accept(); // Estabelece a conexão com o jogador 1.
    System.out.println("Nova conexão com o jogador 1 " + jogador1.getInetAddress().getHostAddress()); // imprime o ip do cliente
    Socket jogador2 = servidor.accept(); // Estabelece a conexão com o jogador 2.
    System.out.println("Nova conexão com o jogador 2 " + jogador2.getInetAddress().getHostAddress()); // imprime o ip do cliente
    new Servindo(jogador1, 0).start(); // Dá inicio à thread do jogador 1.
    new Servindo(jogador2, 1).start(); // Dá inicio à thread do jogador 2.
  }
}

class Servindo extends Thread {
  static DataOutputStream distribui_dados[] = new DataOutputStream[2];  // alocação do vetor de stream de saida de dados do servidor para os clientes;
  static DataInputStream recebe_dados_cliente[] = new DataInputStream[2];  // alocação do vetor de stream de saida de dados do servidor para os clientes;
  Socket cliente;
  int Pos_Nave_X, Pos_Nave_Y, Pos_Disparo_X, Pos_Disparo_Y;
  int identificador_jogador; // 0 para jogador1 e 1 para jogador2.

  Servindo(Socket cliente, int identificador_jogador){
    this.cliente = cliente;
    this.identificador_jogador = identificador_jogador;
  }
  public void run(){
    //System.out.println("identificador_jogador:  " + identificador_jogador);
    try{
      recebe_dados_cliente[identificador_jogador] = new DataInputStream(cliente.getInputStream()); // stream de dados que vem do cliente para o servidor.
      distribui_dados[identificador_jogador] = new DataOutputStream(cliente.getOutputStream());  // armazenda a stream de dados que vem do servidor para o cliente.
    }catch(IOException e){
      System.out.println(e);
    }
    try{ // no campo jogador = 0 envia 0, no campo jogador = 1 envia 1;
      distribui_dados[identificador_jogador].writeInt(identificador_jogador); // envia a identificacao ao jogador
    }catch(IOException e){
      System.out.println(e);
    }
    while(true){ // podemos fazer o cliente enviar que o jogo foi fechado e fechar o server. mas fazemos isso por ultimo.
      System.out.println("identificador_jogador:  " + identificador_jogador);
      try{
        // recebe o dado enviado pelo cliente e o armazena para envio ao outro cliente.
        Pos_Nave_X = (recebe_dados_cliente[identificador_jogador]).readInt();
        Pos_Nave_Y = recebe_dados_cliente[identificador_jogador].readInt();
        Pos_Disparo_X = recebe_dados_cliente[identificador_jogador].readInt();
        Pos_Disparo_Y = recebe_dados_cliente[identificador_jogador].readInt();
      }catch(IOException e){
        System.out.println(e);
      }
      try{
        // faz o envio dos dados aos respectivos clientes.
        if(identificador_jogador == 1){
          distribui_dados[0].writeInt(Pos_Nave_X);
          distribui_dados[0].writeInt(Pos_Nave_Y);
          distribui_dados[0].writeInt(Pos_Disparo_X);
          distribui_dados[0].writeInt(Pos_Disparo_Y);
        }
        else{
          distribui_dados[1].writeInt(Pos_Nave_X);
          distribui_dados[1].writeInt(Pos_Nave_Y);
          distribui_dados[1].writeInt(Pos_Disparo_X);
          distribui_dados[1].writeInt(Pos_Disparo_Y);
        }
      }catch(IOException e){
        System.out.println(e);
      }
      try{
        sleep(30);
      }catch(InterruptedException e){
        System.out.println(e);
      }
      // DEBUG
      /*System.out.println("Pos_Nave_X_:  " + Pos_Nave_X + " I: " + identificador_jogador);
      System.out.println("Pos_Nave_Y_:  " + Pos_Nave_Y + " I: " + identificador_jogador);
      System.out.println("Pos_Disparo_Y_:  " + Pos_Disparo_Y + " I: " + identificador_jogador);
      System.out.println("Pos_Disparo_X_:  " + Pos_Disparo_X + " I: " + identificador_jogador);
      System.out.println(" ");*/
    }
  }
}
