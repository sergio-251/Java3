package mainFX;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainDB.ClassDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerModal {

    public static Stage currentStage;
    private static String typeWindow;

    public static void setTypeWindow(String _typeWindow) {
        typeWindow = _typeWindow;
    }

    @FXML
    VBox mainBox;

    @FXML
    HBox addField;

    @FXML
    Button addBtn;

    public void init() throws SQLException {
        if (typeWindow.equals("new")) {
            VBox[] arrBox = new VBox[Controller.getColumn().size()];
            TextField[] attTF = new TextField[Controller.getColumn().size()];
            for (int i = 0; i < Controller.getColumn().size(); i++) {
                arrBox[i] = new VBox();
                arrBox[i].setId(i + "column");
                attTF[i] = new TextField();
                if (i == 0){
                    attTF[i].setDisable(true);
                }
                arrBox[i].getChildren().addAll(new Label(Controller.getColumn().get(i).getText()), attTF[i]);
                addField.getChildren().add(arrBox[i]);
            }
            Button saveBtn = new Button("Сохранить");
            mainBox.getChildren().add(saveBtn);
            saveBtn.setOnAction(event -> {
                try {
                    ClassDB.voidSQL("INSERT INTO " + Controller.getCurrentTable() + "(id, name) VALUES (" + attTF[0].getText() +
                            ", '" + attTF[1].getText() + "');");
                    Controller.refreshTable();
                    closeWindows();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            VBox[] arrBox = new VBox[Controller.getColumn().size()];
            TextField[] attTF = new TextField[Controller.getColumn().size()];
            ResultSet line = Controller.getLineByID();
            for (int i = 0; i < Controller.getColumn().size(); i++) {
                arrBox[i] = new VBox();
                arrBox[i].setId(i + "column");
                attTF[i] = new TextField();
                attTF[i].setText(line.getString(i + 1));
                if (i == 0){
                    attTF[i].setDisable(true);
                }
                arrBox[i].getChildren().addAll(new Label(Controller.getColumn().get(i).getText()), attTF[i]);
                addField.getChildren().add(arrBox[i]);
            }
            Button saveBtn = new Button("Сохранить");
            mainBox.getChildren().add(saveBtn);
            saveBtn.setOnAction(event -> {
                try {
                    ClassDB.voidSQL("UPDATE " + Controller.getCurrentTable() + " SET name = '" + attTF[1].getText() +
                            "' WHERE id = " + Controller.selectedID);
                    Controller.refreshTable();
                    closeWindows();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void closeWindows(){
        currentStage.close();
    }

}
