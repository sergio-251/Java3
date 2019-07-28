package server;

import java.sql.*;

public class DBService {

    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws SQLException {
        try {
            // обращение к драйверу
            Class.forName("org.sqlite.JDBC");
            // установка подключения
            connection = DriverManager.getConnection("jdbc:sqlite:clientsDB.db");
            // создание Statement для возможности оправки запросов
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public static String getNickByLoginAndPass(String login, String pass) {
        // формирование запроса
        String sql = String.format("SELECT nickname FROM main where login = '%s' and password = '%s'", login, pass);


        try {
            // оправка запроса и получение ответа
            ResultSet rs = stmt.executeQuery(sql);
            // если есть строка возвращаем результат если нет то вернеться null
            if(rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getIdByNick(String nick) {
        // формирование запроса
        String sql = String.format("SELECT id FROM main WHERE nickname = '%s'", nick);
        try {
            // оправка запроса и получение ответа
            ResultSet rs = stmt.executeQuery(sql);
            // если есть строка возвращаем результат если нет то вернеться null
            if(rs.next()) {
                return Integer.parseInt(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getNickById(int id) {
        // формирование запроса
        String sql = String.format("SELECT nickname FROM main WHERE id = %d", id);
        try {
            // оправка запроса и получение ответа
            ResultSet rs = stmt.executeQuery(sql);
            // если есть строка возвращаем результат если нет то вернеться null
            if(rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getBlackListByIdClient(int idClient){
        StringBuilder result = new StringBuilder();
        // формирование запроса

        String sql = String.format("SELECT idBlackClient FROM blackList WHERE idClient = %d", idClient);


        try {
            // оправка запроса и получение ответа
            ResultSet rs = stmt.executeQuery(sql);
            // если есть строка возвращаем результат если нет то вернеться null

            while(rs.next()) {
                result.append(getNickById(rs.getInt(1)) + " ");
            }
            String resultAll = result.toString();
            return resultAll;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isLoginAndNick(String login, String nick) {
        // формирование запроса
        String sql1 = String.format("SELECT login FROM main WHERE login = '%s'", login);
        String sql2 = String.format("SELECT nickname FROM main WHERE nickname = '%s'", nick);
        try {
            // оправка запроса и получение ответа
            ResultSet isLogin = stmt.executeQuery(sql1);
            ResultSet isNick = stmt.executeQuery(sql2);
            // если есть строка возвращаем результат если нет то вернеться null
            if(!isLogin.next() || !isNick.next()) {
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean register(String login, String pass, String nick) {
        // формирование запроса
        String sql = String.format("INSERT INTO main (login, password, nickname) VALUES ('%s', '%s', '%s')", login, pass, nick);

        try {
            // оправка запроса на добавление нового пользователя
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    /*
    public static String[] getBlackList(String clieint){

    }
         */

    public static Boolean addBlackListClient(String client, String blackClient){
        int idClient = getIdByNick(client);
        int idBlackClient = getIdByNick(blackClient);
        String blackClientsBeforeInsert = getBlackListByIdClient(getIdByNick(client));
        String[] tokens = blackClientsBeforeInsert.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (blackClient.equals(tokens[i])){
               return false;
            }
        }

        // формирование запроса
        String sql = String.format("INSERT INTO blackList (idClient, idBlackClient) VALUES (%d, %d)", idClient, idBlackClient);

        try {
            // оправка запроса на добавление нового пользователя
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void removeBlackClient(String client, String blackClient) {
        // формирование запроса
        int idClient = getIdByNick(client);
        int idBlackClient = getIdByNick(blackClient);
        String sql = String.format("DELETE FROM blackList WHERE idClient = %d AND idBlackClient = %d", idClient, idBlackClient);
        try {
            // оправка запроса и получение ответа
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void addHistoryTable(String message) {
        // формирование запроса
        String sql = String.format("INSERT INTO history (message) VALUES ('%s')", message);
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void disconnect() {
        try {
            // закрываем соединение
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getHistory(){
        StringBuilder result = new StringBuilder();
        // формирование запроса

        String sql = "SELECT message FROM history";

        try {
            // оправка запроса и получение ответа
            ResultSet rs = stmt.executeQuery(sql);
            // если есть строка возвращаем результат если нет то вернеться null

            while(rs.next()) {
                result.append(rs.getInt(1) + " ");
            }
            String resultAll = result.toString();
            return resultAll;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



}
