package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private int choice;

    public ClientHandler(Socket socket) {
        try {
            this.clientSocket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.choice = Integer.parseInt(bufferedReader.readLine());         
            clientHandlers.add(this);            
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
   

    @Override
    public void run() {
        
        while (clientSocket.isConnected()) {
            try {
             
                decideWinner();
                if(clientHandlers.size() == 2)
                    break;

            } catch (Exception e) {
                closeEverything(clientSocket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
       
            for (ClientHandler clientHandler : clientHandlers) {
                try {        
                        clientHandler.bufferedWriter.write("Servidor: " + messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();         
              
                } catch (IOException e) {
                    closeEverything(clientSocket, bufferedReader, bufferedWriter);
                }
            }       
    }


    private void decideWinner(){
        if(clientHandlers.size() == 2){       
            ClientHandler player1 = clientHandlers.get(0);
            ClientHandler player2 = clientHandlers.get(1);;      
            broadcastMessage(printChoise(player1)+printChoise(player2));      
            
            if (player1.choice == player2.choice){
                broadcastMessage("EMPATE!");
            } else {
                if((player1.choice == 1 && player2.choice == 3)
                || (player1.choice == 2 && player2.choice == 1 || (player1.choice == 3 && player2.choice == 2))) {
                    broadcastMessage(player1.clientUsername+"  VENCEU!");
                } else {
                    broadcastMessage(player2.clientUsername+"  VENCEU!");
                }
            }
        }
    }

    private String printChoise(ClientHandler player){
        String message;
        switch (player.choice) {
            case 1:
                message =player.clientUsername +" escolheu: PEDRA \t";
                break;
            case 2:
                message =player.clientUsername +" escolheu: PAPEL  \t";
                break;        
            case 3:
                message =player.clientUsername +" escolheu: TESOURA  \t";
                break;     
            default:
                message = "";
        }
        return message;
    }


    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("Servidor: " + clientUsername + " saiu da partida!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            removeClientHandler();
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


}  