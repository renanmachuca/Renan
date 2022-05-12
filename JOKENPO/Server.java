package src.server;

import java.net.*;
import java.io.*;

public class Server {

    private ServerSocket serverSocket;

    public void start(int port)  {
        try {
            System.out.println("Servidor iniciado na porta " + port);
            serverSocket = new ServerSocket(port);   
            while(!serverSocket.isClosed()){                          
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());   
                Thread thread = new Thread(clientHandler);
                thread.start();                                  
            }                         
        } catch (IOException e) {
            e.printStackTrace();
        }          
    }

    public void stop() {
        try {
            if(serverSocket != null){
                serverSocket.close();
                System.out.println("Servidor Parado!");
            }                
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(6666);
    }
    
}
