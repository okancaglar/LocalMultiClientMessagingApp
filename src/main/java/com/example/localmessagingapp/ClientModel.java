package com.example.localmessagingapp;

import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientModel {

    private Socket socket;
    private String userName;
    private String messageFromServer="";
    private BufferedReader fromServer;
    private BufferedWriter toServer;

    public ClientModel(String userName){
        try {
            this.socket = new Socket("localhost",8000);
            this.userName = userName;
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            toServer.write(userName);
            toServer.newLine();
            toServer.flush();

        } catch (IOException e) {
            e.printStackTrace();
            terminateConnection(this);
        }
    }
    public void sendDataToServer(String message){
        try {
            if (socket.isConnected()){
                toServer.write(message);
                toServer.newLine();
                toServer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            terminateConnection(this);
        }
    }
    public void getMessageFromServer(VBox chatBox){
        try {
            new Thread(()->{
                    while (socket.isConnected()) {
                        try {
                            messageFromServer = fromServer.readLine();
                             ExecutorService executors = Executors.newCachedThreadPool();
                             executors.execute(()->{
                                 System.out.println(messageFromServer);
                             });
                            ChatViewController.addMessageToChatBox(
                                    ChatViewController.createMessageBoxFromServer(messageFromServer),chatBox);
                        }catch (IOException e){
                            e.printStackTrace();
                            terminateConnection(this);
                            break;
                        }
                    }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
            terminateConnection(this);
        }



    }

    public static void terminateConnection(ClientModel model){
        try {
            if (model.toServer!=null)model.toServer.close();
            if (model.fromServer!=null)model.fromServer.close();
            if (!model.socket.isConnected())model.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
