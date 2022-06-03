package com.housemanagement;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tables.Flat;
import tables.Tenant;
import java.sql.*;
import java.util.Date;
import java.util.Objects;
/**
 * Класс Контроллер списка квартиросъемщиков со свойствами <b>btnChangeTenant</b>, <b>tableTenants</b>, <b>columnPhoneNumber</b>, <b>columnIdTenant</b>,
 * <b>columnFullName</b>, <b>columnDateOfRegister</b>, <b>columnNumberOfFamilyMembers</b>, <b>flat</b> и <b>message </b>.
 * <p>
 * Данный класс выгружает список всех квартиросъемщиков в таблицу и добавляет новую запись квартиры в базу данных или изменяет ее.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class ListTenantsController {
    /** Поле Button - кнопка добавления/изменения при работе с квартирами*/
    @FXML
    private Button btnChangeTenant;
    /** Поле таблицы квартир на сцене*/
    @FXML
    private TableView<Tenant> tableTenants;
    /** Поле столбца номера телефона квартиросъемщика на сцене*/
    @FXML
    private TableColumn<Tenant, String> columnPhoneNumber;
    /** Поле столбца уникального идентификатора квартиросъемщика на сцене*/
    @FXML
    private TableColumn<Tenant, Integer> columnIdTenant;
    /** Поле столбца ФИО квартиросъемщика на сцене*/
    @FXML
    private TableColumn<Tenant, String> columnFullName;
    /** Поле столбца даты регистрации квартиросъемщика на сцене*/
    @FXML
    private TableColumn<Tenant, Date> columnDateOfRegister;
    /** Поле столбца количества членов семьи квартиросъемщика на сцене*/
    @FXML
    private TableColumn<Tenant, Integer> columnNumberOfFamilyMembers;
    /** Поле квартиры квартиросъемщика*/
    private Flat flat;
    /** Поле выводимого сообщения*/
    private String message = "";
    /**
     * Функция загрузки данных в поля
     */
    @FXML
    void initialize(){;
        columnFullName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFull_name()));
        columnIdTenant.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId_tenant()));
        columnDateOfRegister.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getDate_of_registration()));
        columnPhoneNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPhone_number()));
        columnNumberOfFamilyMembers.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getNumber_of_family_members()));

        btnChangeTenant.visibleProperty().setValue(false);
        fillTable();
    }
    /**
     * Функция выделения строки с квартиросъемщиком при работе с квартирами
     */
    public void select(Flat flatItem){
        flat = flatItem;
        btnChangeTenant.visibleProperty().setValue(true);
        fillTable();
        if (flatItem.getId() == 0 )
        {
            btnChangeTenant.setText("Добавить квартиру");
            message = "Квартира успешно добавлена";
        }
        else {
            btnChangeTenant.setText("Изменить квартиру");
            message = "Квартира успешно изменена";
        }
        if(flat.getId_tenant() == null){
            tableTenants.getSelectionModel().select(0);
        }
        else {
            FilteredList<Tenant> tenantItem = tableTenants.getItems().filtered(item -> Objects.equals(item.getId_tenant(), flat.getId_tenant()));
            tableTenants.getSelectionModel().select(tenantItem.get(0));
        }
    }
    /**
     * Функция заполнения таблицы
     */
    private void fillTable(){
        tableTenants.getItems().clear();
        Tenant tenant = new Tenant();
        tenant.setId_tenant(null);
        tenant.setFull_name(null);
        tenant.setPhone_number(null);
        tenant.setNumber_of_family_members(null);
        tenant.setDate_of_registration(null);tableTenants.getItems().add(tenant);
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from tenant");
            while (resultSet.next()) {
                tenant = new Tenant();
                tenant.setId_tenant(Integer.parseInt(resultSet.getString("id_tenant")));
                tenant.setFull_name(resultSet.getString("full_name"));
                tenant.setDate_of_registration((resultSet.getDate("date_of_registration")));
                tenant.setPhone_number(resultSet.getString("phone_number"));
                tenant.setNumber_of_family_members(Integer.parseInt(resultSet.getString("number_of_family_members")));
                tableTenants.getItems().add(tenant);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
    }
    /**
     * Функция обработки нажатия кнопки при работе с квартирами (добавление/изменение квартиры в базу данных)
     */
    public void onChangeTenant(ActionEvent actionEvent) {
        Integer idTenant = tableTenants.getSelectionModel().selectedItemProperty().getValue().getId_tenant();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            int row = 0;
            if(flat.getId() == 0) row = statement.executeUpdate("INSERT INTO flat (personal_account, flat_number, " +
                    "total_area, usable_area, entrance_number, number_of_rooms,  number_of_registered_residents, number_of_owners, id_tenant) VALUES " +
                    "('" + flat.getPersonal_account() + "', '" + flat.getFlat_number() + "', '" + flat.getTotal_area() + "', '" + flat.getUsable_area() + "'," +
                    " '" + flat.getEntrance_number() + "', '" + flat.getNumber_of_rooms() + "', '" + flat.getNumber_of_registered_residents() + "', " +
                    "'" + flat.getNumber_of_owners() + "', " + idTenant + ")");
            else row = statement.executeUpdate("Update flat set personal_account = '" + flat.getPersonal_account() + "', flat_number = '" + flat.getFlat_number() + "'" +
                    ", total_area = '" + flat.getTotal_area() + "', usable_area = '" + flat.getUsable_area() + "', entrance_number =  '" + flat.getEntrance_number() + "', " +
                    "number_of_rooms = '" + flat.getNumber_of_rooms() + "',  number_of_registered_residents = '" + flat.getNumber_of_registered_residents() + "'," +
                    " number_of_owners = '" + flat.getNumber_of_owners() + "', id_tenant = " + idTenant + " where id_flat = '" + flat.getId() + "'");
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешно");
            alert.setHeaderText(message);
            alert.show();
            MainController controller = HouseManagementApplication.mainController;
            controller.updateFlats();
            controller.fillFilters();
            Stage stage1 = (Stage) btnChangeTenant.getScene().getWindow();
            stage1.close();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
    }
}
