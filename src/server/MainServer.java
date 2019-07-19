package server;

import player.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static ServerSocket serverSocket;
    public static Socket socket;
    static private DataInputStream in;
    static private DataOutputStream out;


    public static void main(String[] args) {
        MainServer.startServer();

        while (true){
            try {
                socket = getServerSocket().accept();
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                System.out.println("Клиент подключился");

                // Десериализация объекта из потока
                ObjectInputStream ois = new ObjectInputStream(in);
                Player player2 = (Player) ois.readObject();
                System.out.println("Количество очков игрока после десериализации: " + player2.getScore());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public static void startServer() {
        try {
            serverSocket = new ServerSocket(8183);
            System.out.println("Сервер запущен!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }
}
