package mainFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import mainDB.ClassDB;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class Controller {

    private Connection connection;
    private static Statement stmt;
    private static ArrayList<TableColumn> column = new ArrayList<>();
    private static String currentTable;
    private static TableView currentTableViewStatic;
    public static String selectedID;

    @FXML
    ImageView imageViewGreen;

    @FXML
    ComboBox comboTableDB;

    @FXML
    HBox boxTableDB, mainTableBox;

    @FXML
    Button newTableBtn, connectBtn;

    @FXML
    Label tableNameInfo;

    @FXML
    VBox currentTableBox;

    @FXML
    TableView currentTableView;


    public void connect() {
        try {
            ClassDB.connect();
            connection = ClassDB.getConnection();
            stmt = ClassDB.getStmt();
            imageViewGreen.setVisible(true);
            imageViewGreen.setManaged(true);
            connectBtn.setDisable(true);
            isConnect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void isConnect() throws SQLException {
        boxTableDB.setVisible(true);
        boxTableDB.setManaged(true);
        ResultSet rs = ClassDB.returnSQL("SELECT * FROM sqlite_master WHERE type = 'table'", this.stmt);
        rs.next(); //Первая запись - системная таблица
        while (rs.next()) {
            comboTableDB.getItems().add(rs.getString(2));
        }
        comboTableDB.setPromptText("Выберите таблицу ...");

       comboTableDB.setOnAction(event -> {
           try {
               currentTable = comboTableDB.getSelectionModel().getSelectedItem().toString();
               choiceTable();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       });
    }

    private void choiceTable() throws SQLException {
        mainTableBox.setVisible(true);
        mainTableBox.setManaged(true);
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        ResultSet rs = ClassDB.returnSQL("SELECT * FROM " + currentTable, this.stmt);

        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                column.add(new TableColumn(rs.getMetaData().getColumnName(i + 1)));
                column.get(i).setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                        ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                currentTableView.getColumns().add(column.get(i));
        }
            tableNameInfo.setText("Таблица: " + currentTable);

        addTableLine(data, rs, currentTableView);
        currentTableViewStatic = currentTableView;

            currentTableView.setOnContextMenuRequested(event -> contentMenuCurrentTable(event));
        }

    private static void addTableLine(ObservableList<ObservableList> data, ResultSet rs, TableView currentTableView) throws SQLException {
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }
        currentTableView.setItems(data);
    }

    private void contentMenuCurrentTable(ContextMenuEvent event) {
        if (currentTableView.getSelectionModel().getSelectedIndex() > -1){
            ContextMenu cm = new ContextMenu();
            MenuItem itemCreate = new MenuItem("Добавить запись");
            MenuItem itemUpdate = new MenuItem("Изменить запись");
            MenuItem itemDelete = new MenuItem("Удалить запись");
            cm.getItems().addAll(itemCreate, itemUpdate, itemDelete);
            cm.show(currentTableView, event.getScreenX(), event.getScreenY());
            itemCreate.setOnAction(event1 -> {
                try {
                    createLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            itemDelete.setOnAction(event1 -> {
                try {
                    selectedID = currentTableView.getSelectionModel().getSelectedItems().get(0).toString().substring(1).split(",",2)[0];
                    deleteLine();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            itemUpdate.setOnAction(event1 -> {
                try {
                    selectedID = currentTableView.getSelectionModel().getSelectedItems().get(0).toString().substring(1).split(",",2)[0];
                    updateLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        }
    }

    private void deleteLine() throws Exception{
        String sql = "DELETE FROM " + currentTable + " WHERE id = " + Controller.selectedID;
        ClassDB.voidSQL(sql);
        refreshTable();
    }

    private void createLine() throws Exception{
        newWindow("Добавить запись", "new");
    }

    private void updateLine() throws IOException {
        newWindow("Редактировать запись", "update");
    }

    private void newWindow(String title, String type) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("modal.fxml"));
        Scene secondScene = new Scene(root, 300, 100);
        Stage newWindow = new Stage();
        ControllerModal.currentStage = newWindow;
        ControllerModal.setTypeWindow(type);
        newWindow.setTitle(title);
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    public static ArrayList<TableColumn> getColumn() {
        return column;
    }

    public static void setCurrentTable(String nameTable){
        currentTable = nameTable;
    }

    public static String getCurrentTable(){
        return currentTable;
    }

   public static void refreshTable() throws SQLException {
       ObservableList<ObservableList> data = FXCollections.observableArrayList();
       ResultSet rs = ClassDB.returnSQL("SELECT * FROM " + currentTable, stmt);
       addTableLine(data, rs, currentTableViewStatic);
   }

   public static ResultSet getLineByID() throws SQLException {
       ResultSet rs = ClassDB.returnSQL("SELECT * FROM " + currentTable + " WHERE id = " + selectedID, stmt);
        return rs;
   }

}
