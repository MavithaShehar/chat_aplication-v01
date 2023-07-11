package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    public static final List<ClientHandler> clientHandlerList = new ArrayList<>();

    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final String clientName;

    public ClientHandler(Socket socket) throws IOException {

        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        clientName = inputStream.readUTF();
        System.out.println("client handler class constroctor befor clientHandlerList.add(this) ");
        clientHandlerList.add(this);

    }

    public static void broadcastMessage(String message) throws IOException {
        for (ClientHandler handler : clientHandlerList) {
                handler.sendMessage("SERVER", message);

            }
    }

    public static void notifyJoins(String message) throws IOException {
     for (ClientHandler handler : clientHandlerList) {
           handler.sendMessage("SERVER", message);
        }
    }


    @Override
    public void run() {

        System.out.println("client handeler class run ()  ");
        while (socket.isConnected()) {
            try {
                String utf = inputStream.readUTF();
                if (utf.equals("*image*")) {
                    receiveImage();
                } else {
                    for (ClientHandler handler : clientHandlerList) {

                        if (!handler.clientName.equals(clientName)) {
                            handler.sendMessage(clientName, utf);
                        }
                    }
                }
            } catch (IOException e) {

                clientHandlerList.remove(this);
                break;
            }
        }
    }
    private void receiveImage() throws IOException {
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);
        for (ClientHandler handler : clientHandlerList) {
            if (!handler.clientName.equals(clientName)) {
                handler.sendImage(clientName, bytes);

            }
        }
    }
    private void sendImage(String sender, byte[] bytes) throws IOException {
        outputStream.writeUTF("*image*");
        outputStream.writeUTF(sender);
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();

    }
    public void sendMessage(String sender, String msg) throws IOException {

        outputStream.writeUTF(sender + " : " + msg);
        outputStream.flush();

    }


}
