package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientHandler {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private MainServer server;



    public String getNick() {
        return nick;
    }

    private String nick;

    public ClientHandler(Socket socket, MainServer server) {
        try {
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    // цикл для авторизации и регистрации
                    while (true) {
                        String str = in.readUTF();
                        // если сообщение начинается с /auth
                        if (str.startsWith("/auth")) {
                            String[] tokens = str.split(" ");
                            // Вытаскиваем данные из БД
                            String newNick = DBService.getNickByLoginAndPass(tokens[1], tokens[2]);
                            if(server.isAuthClient(newNick)){
                                sendMsg("Этот клиент уже вошел в чат!");
                            }
                            if (newNick != null && !server.isAuthClient(newNick)) {
                                // отправляем сообщение об успешной авторизации
                                nick = newNick;
                                sendMsg("/authok Вы вошли как " + nick);
                                server.subscribe(ClientHandler.this);
                                break;
                            }
                            if(!server.isAuthClient(newNick)) {
                                sendMsg("Неверный логин/пароль!");
                            }
                        }

                        // если сообщение начинается с /reg
                        if (str.startsWith("/reg")) {
                            String[] tokens = str.split(" ");
                            // Проверяем зяняты ди логин и ник
                            if (DBService.isLoginAndNick(tokens[1], tokens[3])) {
                                // отправляем сообщение о сущестовании записей
                                sendMsg("Такой логин/ник уже существует!");

                            } else {
                                DBService.register(tokens[1], tokens[2], tokens[3]);
                                nick = tokens[3];
                                sendMsg("/regok Вы успешно зарегистрированы как " + nick + "!");
                                nick = tokens[3];
                                server.subscribe(ClientHandler.this);
                                break;
                            }
                        }
                    }
                    // Заполняем Черный список при запуске приложения
                    String startBlackList = getStartBlackListInit(getNick());
                    String[] tokens = startBlackList.split(" ");
                    for (int i = 0; i < tokens.length; i++) {
                        server.upgradeBlackList(ClientHandler.this, startBlackList);
                    }

                    // Загрузка истории
                    String allHistory = DBService.getHistory();
                    String[] tokensH = allHistory.split(" ");
                    /*
                    if (!tokensH.equals("")) {
                        for (int i = 0; i < tokens.length; i++) {
                            String strHistory = tokensH[1];
                            if (strHistory.startsWith("personal") || strHistory.startsWith("w")){
                                server.personalMsg(tokens[2], ClientHandler.this, tokensH[1]);
                            } else if (!allHistory.equals("")) {
                                server.broadcastMsg(nick + " : " + tokensH[2]);
                            }
                        }
                    }

                     */

                    // блок для отправки сообщений
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/w")) {
                            String[] token = str.split(" ",3);
                            server.personalMsg(token[2], ClientHandler.this, token[1]);

                        } else
                        if (str.equals("/end")) {
                            out.writeUTF("/serverClosed");
                            break;
                        } else
                            if (str.startsWith("/blacklist")) {
                                String[] token = str.split(" ");
                                if(!DBService.addBlackListClient(token[1], token[2])) {
                                    server.personalMsg("Пользователь " + token[2] + " уже в \"Черном списке\"", ClientHandler.this, getNick());
                                } else {
                                    String result = DBService.getBlackListByIdClient(DBService.getIdByNick(token[1]));
                                    server.upgradeBlackList(ClientHandler.this, result);
                                }
                            } else
                            if (str.startsWith("/remove")) {
                                String[] token = str.split(" ",3);
                                DBService.removeBlackClient(token[1], token[2]);
                                server.removeBlackClient(ClientHandler.this, token[2]);

                            } else
                        server.broadcastMsg(nick + " : " + str);

                    }
                } catch (IOException e) {
                   e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    server.unsubscribe(ClientHandler.this);
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
                out.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static String getStartBlackListInit(String nick) {
        return DBService.getBlackListByIdClient(DBService.getIdByNick(nick));

    }

}
