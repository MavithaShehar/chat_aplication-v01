package client;


import Server.ClientHandler;
import controller.ChatClientController;
import controller.ChatServerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Runnable, Serializable {
    private final String name;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private ChatClientController clientChatFormController;

    public Client(String name) throws IOException {
          this.name = name;

        socket = new Socket("localhost", 3002);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        ChatServerController.receiveMessage(name+ " has Joined!");
        ClientHandler.notifyJoins(name+ " has Joined!");
        outputStream.writeUTF(name);
        outputStream.flush();

        try {
            loadScene();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        Thread.interrupted();
        inputStream.close();
        outputStream.close();
        socket.close();
    }

    @Override
    public void run() {
        String message = "";
        while (!message.equals("exit")) {
            try {
                message = inputStream.readUTF();
                if (message.equals("*image*")) {
                    receiveImage();
                } else {
                    clientChatFormController.writeMessage(message);
                }

            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ex) {

                }
            }
        }
    }

    public void sendMessage(String msg) throws IOException {

        outputStream.writeUTF(msg);
        outputStream.flush();
    }

    public void sendImage(byte[] bytes) throws IOException {
        outputStream.writeUTF("*image*");
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }

    private void loadScene() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatClient.fxml"));
        Parent parent = loader.load();
        clientChatFormController = loader.getController();
        clientChatFormController.setClient(this);
        stage.setResizable(false);
        stage.setScene(new Scene(parent));
        stage.setTitle(name + "'s Chat");
        stage.show();

        stage.setOnCloseRequest(event -> {
            try {
                shutdown(name+ " left");
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        });

    }

    private void shutdown(String s) throws IOException {

        ChatServerController.receiveMessage(name+ " has left");
        ClientHandler.notifyJoins(name+ " has left");
    }

    public String getName() {
        return name;
    }

    private void receiveImage() throws IOException {
        String utf = inputStream.readUTF();
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);
        System.out.println(name + "- Image received: from " + utf);
        clientChatFormController.setImage(bytes, utf);
        // Handle the received image bytes as needed
    }
}
