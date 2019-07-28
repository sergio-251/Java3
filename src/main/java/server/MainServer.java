package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

public class MainServer {

    private Vector<ClientHandler> clients;
    private int currentNumberClient = 0;

    static  final Logger logger = LogManager.getRootLogger();

    public MainServer() throws SQLException {

        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();


        try {
            DBService.connect();

            server = new ServerSocket(8182);
            System.out.println("Сервер запущен");
            logger.info("Сервер успешно запущен!");

            while (true) {
                socket = server.accept();
                new ClientHandler(socket, this);
                this.currentNumberClient++;
                System.out.println("Клиент № " + this.currentNumberClient + " подключился");
                logger.info("Клиент № " + this.currentNumberClient + " успешно подключился");

            }

        } catch (IOException e) {
            logger.error("Ошибка при подключении: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Ошибка при закрытии сокета: " + e.getMessage());
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DBService.disconnect();
        }
    }

    // подписываем клиента на рассылку
    public void subscribe(ClientHandler client) {
        clients.add(client);
        broadcastOnline();
    }

    // отписываем клиента от рассылки сообщений
    public void unsubscribe(ClientHandler client){
        clients.remove(client);
        broadcastOnline();
    }


    // общая рассылка
    public void broadcastMsg(String msg) {
        String currentClient = msg.split(" ", 2)[0];
        for (ClientHandler o: clients) {
            if (o.getNick().equals(currentClient)) {
                o.sendMsg("/personal " + msg);
                addHistory("/personal " + msg);

            } else if (!isClientInBlackList(o.getNick(), currentClient)  && !isClientInBlackList(currentClient, o.getNick())){
                o.sendMsg(msg);
                addHistory(o.getNick() + " " + msg);
            }
        }
    }

    // личные сообщения
    public void personalMsg(String msg, ClientHandler fromClient, String toClient){
        fromClient.sendMsg("/w " + fromClient.getNick() + " : " + msg);
        addHistory("/w " + fromClient.getNick() + " : " + msg);
        for (ClientHandler o: clients) {
            if(o.getNick().equals(toClient)){
               if(!fromClient.getNick().equals(o.getNick()))
                o.sendMsg(fromClient.getNick() + " : " + msg);
                addHistory(fromClient.getNick() + " : " + msg);
            }
        }
    }

    public boolean isAuthClient(String nick){
        for (ClientHandler o: clients){
            if(o.getNick().equals(nick)){
                return true;
            }
        }
        return false;
    }

    public void broadcastOnline(){
        StringBuilder onlineList = new StringBuilder();
        onlineList.append("/online ");
        for (ClientHandler o: clients){
            onlineList.append(o.getNick()+ " ");
        }
        String result = onlineList.toString();
        for (ClientHandler o: clients){
            o.sendMsg(result);
        }
    }

    public void upgradeBlackList(ClientHandler client, String allBlackClients){
        client.sendMsg("/blacklist " + allBlackClients);
    }

    public void removeBlackClient(ClientHandler client, String blackClient) {

        client.sendMsg("/remove " + client.getNick() + " " + blackClient);
    }

    public boolean isClientInBlackList(String currentClient, String BlackClient){
        String[] allBlackClients = DBService.getBlackListByIdClient(DBService.getIdByNick(currentClient)).split(" ");
        for (int i = 0; i < allBlackClients.length; i++) {
            if (allBlackClients[i].equals(BlackClient)){
                return true;
            }
        }
        return false;
    }

    public void addHistory(String message){
        DBService.addHistoryTable(message);
    }

}
