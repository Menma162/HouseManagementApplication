package com.housemanagement;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tables.Counter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
/**
 * Класс Контроллер передачи показания с выбором счетчика со свойствами <b>comboBoxTypesCounterFilter</b>, <b>comboBoxFlatsFilter</b>, <b>btnThen</b>, <b>tableCounters</b>,
 * <b>columnIdCounter</b>, <b>columnTypeCounter</b>, <b>columnNumberCounter</b>, <b>columnUsedCounter</b>, <b>columnNumberFlatCounter</b> и <b>alert</b>.
 * <p>
 * Данный класс передает выбранный счетчик при передаче показаний на следующую сцену.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class TransferIndicationChangeCounterController {
    /** Поле ComboBox, которое содержит список типов счетчиков для фильтрации*/
    @FXML
    private ComboBox comboBoxTypesCounterFilter;
    /** Поле ComboBox, которое содержит список квартир для фильтрации*/
    @FXML
    private ComboBox comboBoxFlatsFilter;
    /** Поле Button - кнопки фильтрации таблицы*/
    @FXML
    private Button btnThen;
    /** Поле таблицы счетчиков на сцене*/
    @FXML
    private TableView<Counter> tableCounters;
    /** Поле столбца уникального идентификатора счетчика на сцене*/
    @FXML
    private TableColumn<Counter, Integer> columnIdCounter;
    /** Поле столбца типа счетчика на сцене*/
    @FXML
    private TableColumn<Counter, String> columnTypeCounter;
    /** Поле столбца номера счетчика на сцене*/
    @FXML
    private TableColumn<Counter, String> columnNumberCounter;
    /** Поле столбца статуса использования счетчика на сцене*/
    @FXML
    private TableColumn<Counter, CheckBox> columnUsedCounter;
    /** Поле столбца номера счетчика на сцене*/
    @FXML
    private TableColumn<Counter, Integer> columnNumberFlatCounter;
    /**
     * Функция загрузки данных в поля
     */
    void initialize() {
        columnIdCounter.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        columnTypeCounter.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType()));
        columnNumberCounter.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNumber()));
        columnUsedCounter.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getUsedC()));
        columnNumberFlatCounter.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getFlatNumber()));
        fillCounters();

        fillFilters();
    }
    /**
     * Функция заполнения полей для фильтров
     */
    private void fillFilters(){
        //Квартиры
        comboBoxFlatsFilter.getItems().clear();
        ObservableList<Integer> flat = FXCollections.observableArrayList();
        flat.add(null);
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select flat_number from flat");
            while (resultSet.next()) {
                flat.add(Integer.valueOf(resultSet.getString("flat_number")));
            }
            comboBoxFlatsFilter.setItems(flat);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        comboBoxFlatsFilter.getSelectionModel().select(0);
        comboBoxFlatsFilter.getSelectionModel().select(0);
        //Типы счетчиков
        comboBoxTypesCounterFilter.getItems().clear();
        ObservableList<String> type = FXCollections.observableArrayList();
        type.addAll(null,"Газовый счетчик", "Счетчик горячей воды", "Счетчик холодной воды",
                "Счетчик электрической энергии", "Счетчик отопления");
        comboBoxTypesCounterFilter.setItems(type);
        comboBoxTypesCounterFilter.getSelectionModel().select(0);
    }
    /**
     * Функция данных в таблицу счетчиков
     */
    private void fillCounters(){
        tableCounters.getItems().clear();
        Counter counter = new Counter();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from counter where used = '1'");
            while (resultSet.next()) {
                counter = new Counter();
                counter.setId(resultSet.getInt("id_counter"));
                counter.setType(resultSet.getString("type"));
                counter.setNumber(resultSet.getString("number"));
                counter.setUsed(resultSet.getBoolean("used"));
                counter.setIdFlat(resultSet.getInt("id_flat"));

                statement =  HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet2 = statement.executeQuery("select flat_number from flat where id_flat = '" + counter.getIdFlat() + "'");
                while (resultSet2.next()){
                    counter.setFlatNumber(resultSet2.getInt("flat_number"));
                }
                tableCounters.getItems().add(counter);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        if(tableCounters.getItems().size() != 0){
            tableCounters.getSelectionModel().select(0);
        }
        else btnThen.setDisable(false);
    }
    /**
     * Функция обработки нажатия перехода на следующую форму при передаче показания с выбором счетчика
     */
    public void onThen(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) btnThen.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("transferIndication.fxml"));
        Parent root2 = (Parent) fxmlLoader.load();
        TransferIndicationAddInfoController controller = fxmlLoader.getController();
        controller.initialize();
        controller.fillCounter(tableCounters.getSelectionModel().selectedItemProperty().getValue().getId());
        Stage stage2 = new Stage();
        stage1.close();
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.setScene(new Scene(root2));
        stage2.setTitle("Передача показания: ввод информации");
        stage2.show();
    }
    /**
     * Функция обработки нажатия при фильтрации
     */
    public void onFilter(ActionEvent actionEvent) {
        String type = (String)comboBoxTypesCounterFilter.getSelectionModel().getSelectedItem();
        Integer flatNumber = (Integer)comboBoxFlatsFilter.getSelectionModel().getSelectedItem();
        ArrayList<Counter> counters = new ArrayList<Counter>();
        Counter counter = new Counter();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("select * from counter");
            while (resultSet.next()) {
                counter = new Counter();
                counter.setId(resultSet.getInt("id_counter"));
                counter.setType(resultSet.getString("type"));
                counter.setNumber(resultSet.getString("number"));
                counter.setUsed(resultSet.getBoolean("used"));
                counter.setIdFlat(resultSet.getInt("id_flat"));

                statement = HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet2 = statement.executeQuery("select flat_number from flat where id_flat = " + counter.getIdFlat() + "");
                while (resultSet2.next()){
                    counter.setFlatNumber(resultSet2.getInt("flat_number"));
                }

                counters.add(counter);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        if (type != null) counters = (ArrayList<Counter>)counters.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
        if (flatNumber != null) counters = (ArrayList<Counter>)counters.stream().filter(x -> x.getFlatNumber() == flatNumber).collect(Collectors.toList());
        tableCounters.getItems().clear();
        tableCounters.getItems().addAll(counters);
        tableCounters.getSelectionModel().select(0);
    }
}
