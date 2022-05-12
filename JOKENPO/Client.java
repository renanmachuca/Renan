package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket,String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
           e.getStackTrace();
        }
    }
  
    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            String messageToSend = scanner.nextLine();
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();           

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenToMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgToGame;                
                while (socket.isConnected()) {
                    try {
                        msgToGame = bufferedReader.readLine();                      
                        System.out.println(msgToGame);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }                                
            if(socket != null){
                socket.close();
            }
        } catch (Exception e) {
            e.getStackTrace();
        } 
    }


    public static void main(String[] args) throws IOException {
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Qual sera seu nome no jogo: ");
        String username = scanner.nextLine();                       
        greeting();                      
        int gameType = scanner.nextInt();                         
        
        if(gameType == 1 ){
            Socket socket = new Socket("localhost", 6666);    
            Client client = new Client(socket, username);   
            client.listenToMessage();                      
            gameMenu();
            client.sendMessage();           
        }else {
            gameMenu();
            int choice = scanner.nextInt();
            int cpuChoice = getChoiceCpuPlayer();
            printChoise(username,choice);
            printChoise("Computador",cpuChoice);
            decideWinner(choice,cpuChoice);  
        }      
    }

    private static void gameMenu(){
        System.out.println("");
        System.out.println("Essas sao suas opcoes: ");
        System.out.println("    1 - Pedra");
        System.out.println("    2 - Papel");
        System.out.println("    3 - Tesoura");
        System.out.print("Qual sera sua jogada: ");   
    }

    private static void  greeting(){        
        System.out.println("Seja bem vindo ao Jokenpo");
        System.out.println("Opcoes de escolhas sao: ");
        System.out.println("    1 - Para jogar com outro jogador ");
        System.out.println("    2 - Para jogar com o Computador  ");
        System.out.print("Qual e a sua escolha: ");    
    }   


    public static int getChoiceCpuPlayer(){
        return   (int)(Math.random()*(3-1)+1);
    }
    
    private static void printChoise(String username, int choice){
        switch (choice) {
            case 1:
            System.out.println (username +" escolheu: PEDRA");
            break;
            case 2:
            System.out.println ( username +" escolheu: PAPEL");
            break;        
            case 3:
            System.out.println ( username +" escolheu: TESOURA");
            break;        
        }
    }

    private static void decideWinner(int player1, int player2 ){
        if (player1 == player2){
            System.out.println("EMPATE!");
        } else {
            if((player1 == 1 && player2 == 3)
             || (player1 == 2 && player2 == 1 || (player1 == 3 && player2 == 2))) {
                System.out.println("Jogador 1  VENCEU!");
            } else {
                System.out.println("Jogador 2  VENCEU!");
            }
        }
    }
    
}
