package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import server.StartServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;


public class Controller {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;


    @FXML
    MenuBar menuBar;

    @FXML
    ListView<String> blackListArea, onlineArea;

    @FXML
    TextField textField;

    @FXML
    Button btn1;

    @FXML
    HBox bottomPanel;

    @FXML
    HBox upperPanel, upperPanel2, mainField;

    @FXML
    VBox regForm, authForm, messageArea, rightVBox;

    @FXML
    TextField loginFiled, loginFiled2, nickField;

    @FXML
    PasswordField passwordField, passwordField2;

    @FXML
    CheckMenuItem style1, style2, auth, reg;

    @FXML
    Menu menu, menu2;

    @FXML
    Label serviceMsg, nickAuth;


    private boolean isAuthorized;
    public boolean isHistoryMessage;

    final String IP_ADRESS = "localhost";
    final int PORT = 8182;



    public void themePretty(){
        style1.setSelected(false);
        style2.setSelected(true);
        menuBar.setStyle("-fx-background-color: white;  -fx-text-fill: #2b2b2b");
    }

    public void themeCode(){
        style1.setSelected(true);
        style2.setSelected(false);
        menuBar.setStyle("-fx-background-color: #3c3f41;  -fx-text-fill: #bbbbbb");
    }
    // метод для показа нижней панели или верхней
    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;

        if (!isAuthorized) {
            authForm.setVisible(true);
            authForm.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
            regForm.setVisible(true);
            regForm.setManaged(true);
            menu.setDisable(true);
            menu2.setDisable(false);
        } else {
            authForm.setVisible(false);
            authForm.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
            regForm.setVisible(false);
            regForm.setManaged(false);
            menu.setDisable(false);
            menu2.setDisable(true);
            mainField.setVisible(true);
            mainField.setManaged(true);
            messageArea.setPrefWidth(400);
            contextMenuCreate();

        }
    }



    private void contextMenuCreate() {
        ContextMenu cm = new ContextMenu();
        ContextMenu cmBlack = new ContextMenu();
        MenuItem personalMsgButn = new MenuItem();
        MenuItem blackAddButn = new MenuItem();
        MenuItem blackRemoveButn = new MenuItem();
        cm.getItems().addAll(personalMsgButn, blackAddButn);
        cmBlack.getItems().add(blackRemoveButn);
        personalMsgButn.setOnAction(event ->
                sendPersonalMsgFromContent(onlineArea.getSelectionModel().getSelectedItems().toString().substring(1, onlineArea.getSelectionModel().getSelectedItems().toString().length() - 1))
        );

        blackAddButn.setOnAction(event ->
                addBlackListClient(serviceMsg.getText().split(" ")[serviceMsg.getText().split(" ").length - 1], onlineArea.getSelectionModel().getSelectedItems().toString().substring(1, onlineArea.getSelectionModel().getSelectedItems().toString().length() - 1))
        );

        blackRemoveButn.setOnAction(event -> {

            removeBlackListClient(serviceMsg.getText().split(" ")[serviceMsg.getText().split(" ").length - 1], blackListArea.getSelectionModel().getSelectedItems().toString().substring(1, blackListArea.getSelectionModel().getSelectedItems().toString().length() - 1));
        });
        onlineArea.setOnContextMenuRequested(event -> {

            if(onlineArea.getSelectionModel().getSelectedIndices().get(0) > -1) {
                personalMsgButn.setText("Отправить личное сообщение " + onlineArea.getSelectionModel().getSelectedItems());
                blackAddButn.setText("Добавить " + onlineArea.getSelectionModel().getSelectedItems() + " в черный список");
                cm.show(onlineArea, event.getScreenX(), event.getScreenY());
                if(serviceMsg.getText().split(" ")[serviceMsg.getText().split(" ").length - 1].equals(onlineArea.getSelectionModel().getSelectedItems().toString().substring(1,
                        onlineArea.getSelectionModel().getSelectedItems().toString().length() - 1))) {

                    blackAddButn.setDisable(true);
                } else {
                    blackAddButn.setDisable(false);
                }
            }
        });

        blackListArea.setOnContextMenuRequested(event -> {
            if(blackListArea.getSelectionModel().getSelectedIndices().get(0) > -1) {
                blackRemoveButn.setText("Убрать" + blackListArea.getSelectionModel().getSelectedItems() +" из \"Черного списка\"");
                cmBlack.show(blackListArea, event.getScreenX(), event.getScreenY());
            }

        });




    }

    public void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    // блок для авторизации

                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/authok") || str.startsWith("/regok")) {
                            setAuthorized(true);
                            setServiceMsg(str.split(" ", 2)[1]);


                            break;
                        } else {
                            setServiceMsg(str);
                        }

                    }




                    // блок для разбора сообщений
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/serverClosed")) {
                            break;
                        }
                        if (str.startsWith("/online")){
                            String[] tokens2 = str.split(" ");
                            Platform.runLater(() -> {
                                onlineArea.getItems().clear();
                                for (int i = 1; i < tokens2.length; i++) {
                                    onlineArea.getItems().add(tokens2[i]);
                                }
                            });
                        } else
                        if (str.startsWith("/blacklist")){
                            String[] tokens3 = str.split(" ");
                            Platform.runLater(() -> {
                                blackListArea.getItems().clear();
                                for (int i = 1; i < tokens3.length ; i++) {
                                   blackListArea.getItems().add(tokens3[i]);
                                }
                            });


                        } else
                        if (str.startsWith("/remove")){
                            String[] tokens4 = str.split(" ");
                            Platform.runLater(() -> {
                                blackListArea.getItems().remove(tokens4[2]);
                            });
                        } else
                            if (str.startsWith("/personal") || str.startsWith("/w")){
                            sendPersonalMsg("Вы " + str.split(" ",3)[2]);
                        } else sendMessage(str);
                    }
                } catch (IOException e) {
                    String[] ar = new String[0];
                    try {
                        StartServer.main(ar);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setAuthorized(false);
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // метод для авторизации
    public void tryToAuth() {
        if (loginFiled.getText().equals("") || passwordField.getText().equals("")) {
            setServiceMsg("Введены не все данные для входа!");
            return;
        }
        else if (socket == null || socket.isClosed()) {
            // сначала подключаемся к серверу
            connect();
        }
            try {


                // отправка сообщений на сервер для авторизации
                out.writeUTF("/auth " + loginFiled.getText() + " " + passwordField.getText());
                loginFiled.clear();
                passwordField.clear();
            }
         catch(IOException e){
            e.printStackTrace();
            }
        }



    // метод для отправки сообщений
    public void sendMsg() {
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToReg() {
        if (loginFiled2.getText().equals("") || passwordField2.getText().equals("") || nickField.getText().equals("")) {
            setServiceMsg("Введены не все данные для регистрации!");
            return;
        }
        if (socket == null || socket.isClosed()) {
            // сначала подключаемся к серверу
            connect();
        }
        try {
            // отправка сообщений на сервер для регистрации
            out.writeUTF("/reg " + loginFiled2.getText() + " " + passwordField2.getText() + " " + nickField.getText());
            loginFiled.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRegForm() {
        reg.setSelected(true);
        auth.setSelected(false);
        upperPanel.setDisable(true);
        regForm.setDisable(false);
    }

    public void setAuthForm() {
        reg.setSelected(false);
        auth.setSelected(true);
        upperPanel.setDisable(false);
        regForm.setDisable(true);
    }

    public void setStyle1() {
        style1.setSelected(true);
        style2.setSelected(false);
    }


    public void setServiceMsg(String str){
        serviceMsg.setVisible(true);
        serviceMsg.setManaged(true);
        Platform.runLater(() -> serviceMsg.setText(str));

    }

    public void sendPersonalMsg(String str){
        Platform.runLater(() -> {
            Label labelText = new Label(str);
            VBox currentMsg = new VBox();

            currentMsg.setAlignment(Pos.CENTER_RIGHT);
            currentMsg.getChildren().add(labelText);
            labelText.setStyle("-fx-background-color:#2b2b2b; -fx-text-fill: #d4d6d9; -fx-font-size: 16px");
            messageArea.getChildren().add(new Separator());
            messageArea.getChildren().add(currentMsg);
        });
    }

    public void sendPersonalMsgFromContent(String toClientName){
        String res = "/w " + toClientName + " ";
        textField.setText(res);
        textField.requestFocus();
        textField.selectPositionCaret(res.length());
    }

    public void sendMessage(String str){
        Platform.runLater(() -> {
            Label labelText = new Label(str);
            VBox currentMsg = new VBox();
            currentMsg.setAlignment(Pos.CENTER_LEFT);
            currentMsg.getChildren().add(labelText);
            labelText.setStyle("-fx-background-color: #59595a; -fx-text-fill: #ada869;  -fx-font-size: 16px");
            messageArea.getChildren().add(new Separator());
            messageArea.getChildren().add(currentMsg);



        });

    }

    private void addBlackListClient(String client, String blackClient) {
        try {
            out.writeUTF("/blacklist " + client + " " + blackClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeBlackListClient(String client, String blackClient) {
        try {
            out.writeUTF("/remove " + client + " " + blackClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
