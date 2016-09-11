import java.net.*;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class Servidor {
  public static void main(String args[]) throws IOException {

    ServerSocket servidor = new ServerSocket(12345); // Definida a porta a ser aberta e usada pelo servidor.
    System.out.println("Porta 12345 aberta!"); // Mensagem de sucesso.
    Socket jogador1 = servidor.accept(); // Estabelece a conexão com o jogador 1.
    System.out.println("Nova conexão com o jogador 1 " + jogador1.getInetAddress().getHostAddress()); // imprime o ip do cliente
    new Servindo(jogador1, 0).start(); // Dá inicio à thread do jogador 1.
    Socket jogador2 = servidor.accept(); // Estabelece a conexão com o jogador 2.
    System.out.println("Nova conexão com o jogador 2 " + jogador2.getInetAddress().getHostAddress()); // imprime o ip do cliente
    new Servindo(jogador2, 1).start(); // Dá inicio à thread do jogador 2.
  }
}

class Servindo extends Thread {
  static DataOutputStream distribui_dados[] = new DataOutputStream[2];  // alocação do vetor de stream de saida de dados do servidor para os clientes;
  static DataInputStream recebe_dados_cliente[] = new DataInputStream[2];  // alocação do vetor de stream de saida de dados do servidor para os clientes;
  Socket cliente;
  private double ang_nave_elipse;
  private int largura_raio_nave;
  private int altura_raio_nave;
  private int pos_disparo1_x;
  private int pos_disparo1_y;
  private int pos_disparo2_x;
  private int pos_disparo2_y;
  private int pos_disparo3_x;
  private int pos_disparo3_y;
  private int vida_planeta;
  private int vida_nave;
  private boolean pronto_para_inicio = false; // indica se o jogador esta pronto para o iniciar o jogo.
  static int numero_jogadores_prontos = 0;
  static boolean estado_jogo = true;
  int identificador_jogador; // 0 para jogador1 e 1 para jogador2.

  Servindo(Socket cliente, int identificador_jogador){
    this.cliente = cliente;
    this.identificador_jogador = identificador_jogador;
  }
  public void run(){
    try{
      recebe_dados_cliente[identificador_jogador] = new DataInputStream(cliente.getInputStream()); // stream de dados que vem do cliente para o servidor.
      distribui_dados[identificador_jogador] = new DataOutputStream(cliente.getOutputStream());  // armazenda a stream de dados que vem do servidor para o cliente.
    }catch(IOException e){
      System.out.println(e);
    }
    try{
      distribui_dados[identificador_jogador].writeInt(identificador_jogador); // envia a identificacao ao jogador
      do{
        sleep(50); // aguardo de 50 ms para o envio de dados, evitando a sobrecarga da thread.
        pronto_para_inicio = recebe_dados_cliente[identificador_jogador].readBoolean(); // recebe do cliente se o jogador esta pronto para o inicio do jogo.
      }while(!pronto_para_inicio); // segura o servidor enquanto o cliente em questao nao esta pronto.
      numero_jogadores_prontos++; // quando um jogador informa que esta pronto, informa ao outro jogador quantos jogadores estao pronto (incluindo ele mesmo se este informou que estava pronto).
      do{
        sleep(50); // Aguarda 50ms para fazer o envio dos dados, nao sobrecarregando a thread.
        if(numero_jogadores_prontos == 2){ // não faz envio ao jogador que nao estiver pronto. Evitando, possivelmente, o broken pipe.
          if(identificador_jogador == 0)
            distribui_dados[1].writeBoolean(pronto_para_inicio);
          else
            distribui_dados[0].writeBoolean(pronto_para_inicio);
        }
      }while(numero_jogadores_prontos < 2); // segura o servidor enquanto ambos os clientes nao estao prontos.
    }catch(IOException e){
      System.out.println(e);
    }catch(InterruptedException e){
      System.out.println(e);
    }
    try{
      sleep(3000); // aguadar o cliente pois ele começa 3 segundos depois do inicio por causa da contagem.
      do{
        // recebe o dado enviado pelo cliente e o armazena para envio ao outro cliente.
        ang_nave_elipse = recebe_dados_cliente[identificador_jogador].readDouble();
        altura_raio_nave = recebe_dados_cliente[identificador_jogador].readInt();
        largura_raio_nave = recebe_dados_cliente[identificador_jogador].readInt();
        vida_nave = recebe_dados_cliente[identificador_jogador].readInt();
        vida_planeta = recebe_dados_cliente[identificador_jogador].readInt();
        pos_disparo1_x = recebe_dados_cliente[identificador_jogador].readInt();
        pos_disparo1_y = recebe_dados_cliente[identificador_jogador].readInt();
        pos_disparo2_x = recebe_dados_cliente[identificador_jogador].readInt();
        pos_disparo2_y = recebe_dados_cliente[identificador_jogador].readInt();
        pos_disparo3_x = recebe_dados_cliente[identificador_jogador].readInt();
        pos_disparo3_y = recebe_dados_cliente[identificador_jogador].readInt();
        estado_jogo = recebe_dados_cliente[identificador_jogador].readBoolean();

        // faz o envio dos dados aos respectivos clientes.
        if(identificador_jogador == 1){
          distribui_dados[0].writeDouble(ang_nave_elipse);
          distribui_dados[0].writeInt(altura_raio_nave);
          distribui_dados[0].writeInt(largura_raio_nave);
          distribui_dados[0].writeInt(vida_nave);
          distribui_dados[0].writeInt(vida_planeta);
          distribui_dados[0].writeInt(pos_disparo1_x);
          distribui_dados[0].writeInt(pos_disparo1_y);
          distribui_dados[0].writeInt(pos_disparo2_x);
          distribui_dados[0].writeInt(pos_disparo2_y);
          distribui_dados[0].writeInt(pos_disparo3_x);
          distribui_dados[0].writeInt(pos_disparo3_y);
          distribui_dados[0].writeBoolean(estado_jogo);
        }
        else{
          distribui_dados[1].writeDouble(ang_nave_elipse);
          distribui_dados[1].writeInt(altura_raio_nave);
          distribui_dados[1].writeInt(largura_raio_nave);
          distribui_dados[1].writeInt(vida_nave);
          distribui_dados[1].writeInt(vida_planeta);
          distribui_dados[1].writeInt(pos_disparo1_x);
          distribui_dados[1].writeInt(pos_disparo1_y);
          distribui_dados[1].writeInt(pos_disparo2_x);
          distribui_dados[1].writeInt(pos_disparo2_y);
          distribui_dados[1].writeInt(pos_disparo3_x);
          distribui_dados[1].writeInt(pos_disparo3_y);
          distribui_dados[1].writeBoolean(estado_jogo);
        }
      }while (estado_jogo);
      // finaliza o servidor.
      recebe_dados_cliente[identificador_jogador].close();
      distribui_dados[identificador_jogador].close();
      cliente.close();
    }catch(IOException e){
      System.out.println(e);
    }catch(InterruptedException e){
      System.out.println(e);
    }
  }
}
