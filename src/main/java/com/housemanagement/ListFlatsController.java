package com.housemanagement;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tables.Flat;
import java.sql.*;
/**
 * Класс Контроллер списка квартир со свойствами <b>tableFlats</b>, <b>columnIdFlat</b>, <b>columnFlatNumber</b>, <b>columnPersonalAccount</b>, <b>columnTotalArea</b>,
 * <b>columnUsableArea</b>, <b>columnNumberEntrance</b>, <b>columnNumberOfRooms</b>, <b>columnNumberOfResidence</b>, <b>columnNumberOwners</b> и <b>columnFullNameTenant</b>.
 * <p>
 * Данный класс выгружает список всех квартир в таблицу.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class ListFlatsController {
    /** Поле таблицы квартир на сцене*/
    @FXML
    private TableView<Flat> tableFlats;
    /** Поле столбца уникального идентификатора квартиры на сцене*/
    @FXML
    private TableColumn<Flat, Integer> columnIdFlat;
    /** Поле столбца номера квартиры на сцене*/
    @FXML
    private TableColumn<Flat, Integer> columnFlatNumber;
    /** Поле столбца лицевого счета квартиры на сцене*/
    @FXML
    private TableColumn<Flat, String> columnPersonalAccount;
    /** Поле столбца общей площади квартиры на сцене*/
    @FXML
    private TableColumn<Flat, Float> columnTotalArea;
    /** Поле столбца полезной площади квартиры на сцене*/
    @FXML
    private TableColumn<Flat, Float> columnUsableArea;
    /** Поле столбца номера подъезда квартиры на сцене*/
    @FXML
    private TableColumn<Flat, Integer> columnNumberEntrance;
    /** Поле столбца количества комнат квартиры на сцене*/
    @FXML
    private TableColumn<Flat, Integer> columnNumberOfRooms;
    /** Поле столбца количества зарегистрированных жителей квартиры на сцене*/
    @FXML
    private TableColumn<Flat, Integer> columnNumberOfResidence;
    /** Поле столбца количества владельцев жилья на сцене*/
    @FXML
    private TableColumn<Flat, Integer> columnNumberOwners;
    /** Поле столбца ФИО квартиросъемщика квартиры на сцене*/
    @FXML
    private TableColumn<Flat, String> columnFullNameTenant;
    /**
     * Функция загрузки данных в поля
     */
    @FXML
    void initialize(){
        columnIdFlat.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        columnFlatNumber.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getFlat_number()));
        columnPersonalAccount.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPersonal_account()));
        columnTotalArea.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getTotal_area()));
        columnUsableArea.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getUsable_area()));
        columnNumberEntrance.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getEntrance_number()));
        columnNumberOfRooms.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getNumber_of_rooms()));
        columnNumberOfResidence.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getNumber_of_registered_residents()));
        columnNumberOwners.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getNumber_of_owners()));
        columnFullNameTenant.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFull_name()));

        fill();
    }
    /**
     * Функция заполнения таблицы
     */
    private void fill(){
        Flat flat = null;
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from flat");
            while (resultSet.next()) {
                flat = new Flat();
                flat.setId(resultSet.getInt("id_tenant"));
                flat.setFlat_number(resultSet.getInt("flat_number"));
                flat.setPersonal_account(resultSet.getString("personal_account"));
                flat.setEntrance_number(resultSet.getInt("entrance_number"));
                flat.setTotal_area(resultSet.getFloat("total_area"));
                flat.setUsable_area(resultSet.getFloat("usable_area"));
                flat.setNumber_of_rooms(resultSet.getInt("number_of_rooms"));
                flat.setNumber_of_registered_residents(resultSet.getInt("number_of_registered_residents"));
                flat.setNumber_of_owners(resultSet.getInt("number_of_owners"));
                flat.setId_tenant(resultSet.getInt("id_tenant"));

                statement = HouseManagementApplication.getConnection().createStatement();
                if (flat.getId_tenant() != 0){
                    ResultSet resultSet2 = statement.executeQuery("select full_name from tenant where id_tenant = '" + flat.getId_tenant() + "'");
                    while (resultSet2.next()) {
                        flat.setFull_name(resultSet2.getString("full_name"));
                    }
                }

                tableFlats.getItems().add(flat);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
    }
}
