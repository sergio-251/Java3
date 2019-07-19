package client;

import player.Player;

import java.io.*;
import java.net.Socket;

public class MainClient {
    static Socket socket;
    static DataOutputStream out;
    static DataInputStream in;
    final static String IP_ADRESS = "localhost";
    final static int PORT = 8183;


    public static void main(String[] args) {

        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Дополнительное задание
        System.out.println("Дополнительное Задание");
        Player player = new Player("John", "Murray");
        player.addScore(10);
        System.out.println("Количество очков игрока до сериализации: " + player.getScore());

            try {
                // Сериализуем объект
                System.out.println("Объект сериализован");
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(player);
                oos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void connect() throws IOException {
        socket = new Socket(IP_ADRESS, PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

    }


}
