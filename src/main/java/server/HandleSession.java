package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class HandleSession implements Runnable{

    private static ArrayList<HandleSession> sessions = new ArrayList<>();
    private Socket socket;
    private String userName;
    private BufferedReader fromClient;
    private BufferedWriter toClient;


    public HandleSession(Socket socket){
        this.socket = socket;
        try {
            this.fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.toClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = fromClient.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            closeSession();
        }
        sessions.add(this);
        broadcast(userName + " has joined the chat");
    }

    @Override
    public void run() {
        String messageFromClient;
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                messageFromClient = fromClient.readLine();
                for (HandleSession session:sessions) {
                    System.out.println(session.toString());
                }
                if (messageFromClient!=null) {
                    broadcast(messageFromClient);
                }else break;
            }
        }catch (Exception e) {
            e.printStackTrace();
            closeSession();
        }
    }

    public void broadcast(String message){
        try {
            for (HandleSession session : sessions) {
                if (session.userName != this.userName) {
                        session.toClient.write(message);
                        session.toClient.newLine();
                        session.toClient.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeSession();
        }
    }

    public void closeSession(){
        try {
            System.out.println(this.userName + " has left");
            sessions.remove(this);
            if (fromClient!=null) fromClient.close();
            if (toClient!=null) toClient.close();
            if (!socket.isClosed())socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
