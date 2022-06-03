package com.housemanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tables.Counter;
import java.sql.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tables.Payment;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/**
 * Класс Контроллер добавления/изменения записи о счетчике со свойствами <b>checkBoxStatus</b>, <b>labelNameFrom</b>, <b>comboBoxFlats</b>, <b>comboBoxTypes</b>,
 * <b>textFNumberCounter</b>, <b>buttonWorkCounter</b>, <b>counter</b> и <b>alert</b>.
 * <p>
 * Данный класс добавляет/изменяет запись о счетчике в БД.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class WorkCounterController {
    /** Поле CheckBox, которое содержит статус использования*/
    @FXML
    private CheckBox checkBoxStatus;
    /** Поле Label, которое содержит название окна*/
    @FXML
    private Label labelNameFrom;
    /** Поле ComboBox, которое содержит список квартир*/
    @FXML
    private ComboBox comboBoxFlats;
    /** Поле ComboBox, которое содержит список типов счетчиков*/
    @FXML
    private ComboBox comboBoxTypes;
    /** Поле TextField, которое содержит номер счетчика*/
    @FXML
    private TextField textFNumberCounter;
    /** Поле Button - кнопки работы со счетчиком*/
    @FXML
    private Button buttonWorkCounter;
    /** Поле счетчик класса counter*/
    private Counter counter = new Counter();
    /** Поле диалога с выводимыми сообщениями*/
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    /**
     * Функция загрузки данных в поля
     */
    void initialize(){
        fillFlats();
        fillTypes();

        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,20}").matcher(change.getControlNewText()).matches() ? change : null);
        textFNumberCounter.setTextFormatter(formatter);
    }
    /**
     * Функция заполнения списка квартир
     */
    private void fillFlats(){
        comboBoxFlats.getItems().clear();
        ObservableList<Integer> flat = FXCollections.observableArrayList();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select flat_number from flat");
            while (resultSet.next()) {
                flat.add(Integer.valueOf(resultSet.getString("flat_number")));
            }
            comboBoxFlats.setItems(flat);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        comboBoxFlats.getSelectionModel().select(0);
    }
    /**
     * Функция загрузки списка типов счетчика
     */
    private void fillTypes(){
        comboBoxTypes.getItems().clear();
        ObservableList<String> type = FXCollections.observableArrayList();
        type.addAll("Газовый счетчик", "Счетчик горячей воды", "Счетчик холодной воды",
                "Счетчик электрической энергии", "Счетчик отопления");
        comboBoxTypes.setItems(type);
        comboBoxTypes.getSelectionModel().select(0);
    }
    /**
     * Функция загрузки данных о текущем счетчике
     * @param id уникальный идентификатор счетчика
     */
    public void fill(Integer id){
        counter.setId(id);
        if(id != null){
            labelNameFrom.setText("Изменение счетчика");
            buttonWorkCounter.setText("Изменить");
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = resultSet = statement.executeQuery("select * from counter where id_counter = " + counter.getId() + "");
                while (resultSet.next()) {
                    int idFlat = resultSet.getInt("id_flat");
                    comboBoxTypes.getSelectionModel().select(resultSet.getString("type"));
                    textFNumberCounter.setText(resultSet.getString("number"));
                    checkBoxStatus.setSelected(resultSet.getBoolean("used"));

                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet2 = statement.executeQuery("select flat_number from flat where id_flat = '" + idFlat + "'");
                    while (resultSet2.next()){
                        comboBoxFlats.getSelectionModel().select(resultSet2.getString("flat_number"));
                    }
                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
        else {
            labelNameFrom.setText("Добавление счетчика");
            buttonWorkCounter.setText("Добавить");
        }
    }
    /**
     * Функция обработки нажатия при добавлении/изменении записи о счетчике в БД
     */
    public void onWorkCounter(ActionEvent actionEvent) {
        counter.setFlatNumber(Integer.parseInt(comboBoxFlats.getSelectionModel().getSelectedItem().toString()));
        counter.setNumber(textFNumberCounter.getText());
        counter.setType(comboBoxTypes.getSelectionModel().getSelectedItem().toString());
        counter.setUsed(checkBoxStatus.isSelected());
        if((counter.getNumber().length() < 8)){
            alert.setTitle("Некорректный ввод данных!");
            alert.setHeaderText("Некорректный ввод данных. Длина поля номера счетчика должна \nбыть не меньше 8 и не больше 20.");
            alert.show();
        }
        else {
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = null;
                if (counter.getId() == null) resultSet = statement.executeQuery("select * from counter where type = '" + counter.getType() + "' and " +
                        "number = '" + counter.getNumber() + "'");
                else resultSet = statement.executeQuery("select * from counter where (type = '" + counter.getType() + "'  and " +
                        "number = '" + counter.getNumber() + "' and id_counter != '" + counter.getId() + "')");
                resultSet.last();
                int rows = resultSet.getRow();
                if (rows == 0) {

                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet2 = statement.executeQuery("select id_flat from flat where flat_number = '" + counter.getFlatNumber() + "'");
                    while (resultSet2.next()){
                        counter.setIdFlat(resultSet2.getInt("id_flat"));
                    }

                    if(counter.getId() == null) {
                        rows = statement.executeUpdate("INSERT INTO counter (type, used, " +
                                "number, id_flat) VALUES ('" + counter.getType() + "', '" + (counter.getUsed() ? 1 : 0) + "', " +
                                "'" + counter.getNumber() + "', '" + counter.getIdFlat() + "')");
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успешно");
                        alert.setHeaderText("Счетчик успешно добавлен.");
                        alert.show();
                    }
                    else {
                        rows = statement.executeUpdate("UPDATE counter SET type = '" + counter.getType()  + "', used = " +
                                "'" + (counter.getUsed() ? 1 : 0) + "', number = '" + counter.getNumber() + "', " +
                                "id_flat = " + counter.getIdFlat() + " where id_counter = '" + counter.getId() + "'");
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успешно");
                        alert.setHeaderText("Счетчик успешно изменен.");
                        alert.show();
                    }
                    MainController controller = HouseManagementApplication.mainController;
                    controller.fillСounters();
                    Stage stage1 = (Stage) buttonWorkCounter.getScene().getWindow();
                    stage1.close();
                } else {
                    alert.setTitle("Ошибка работы со счетчиком!");
                    alert.setHeaderText("Счетчик такого типа с таким номером уже существует.");
                    alert.show();
                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
    }
}
