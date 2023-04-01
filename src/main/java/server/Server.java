package server;

import javafx.application.Platform;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    private ServerSocket serverSocket;

    public Server(){
            try {
                serverSocket = new ServerSocket(8000);

                while (!serverSocket.isClosed()) {
                    //listen connection
                    Socket socket = serverSocket.accept();
                    System.out.println("New client has joined at: " + new Date());

                    new Thread(new HandleSession(socket)).start();
                }
            } catch (Exception e) {
                closeServer();
            }
    }
    public void closeServer(){
        try {
            if (!serverSocket.isClosed()){
                serverSocket.close();
            }
        }catch (IOException e ){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
