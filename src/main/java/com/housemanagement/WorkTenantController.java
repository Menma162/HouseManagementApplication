package com.housemanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tables.Tenant;
import java.sql.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
/**
 * Класс Контроллер добавления/изменения записи о квартиросъемщике со свойствами <b>labelNameFrom</b>, <b>textFFullName</b>, <b>datePickerDateOfRegister</b>,
 * <b>spinnerFamilyMembers</b>, <b>textFPhoneNumber</b>, <b>btnNameButton</b>, <b>tenant</b> и <b>alert</b>.
 * <p>
 * Данный класс добавляет/изменяет запись о квартиросъемщике в БД.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class WorkTenantController {
    /** Поле Label, которое содержит название окна*/
    @FXML
    private Label labelNameFrom;
    /** Поле TextField, которое содержит ФИО квартиросъемщика*/
    @FXML
    private TextField textFFullName;
    /** Поле DatePicker, которое содержит дату регистрации квартиросъемщика*/
    @FXML
    private DatePicker datePickerDateOfRegister;
    /** Поле Spinner, позволяющее выбрать количество членов семьи*/
    @FXML
    private Spinner spinnerFamilyMembers;
    /** Поле TextField, которое содержит номер телефона квартиросъемщика*/
    @FXML
    private TextField textFPhoneNumber;
    /** Поле Button - кнопки работы с квартиросъемщиком*/
    @FXML
    private Button btnNameButton;
    /** Поле текущего квартиросъемщика класса Tenant*/
    private Tenant tenant = new Tenant();
    /** Поле диалога с выводимыми сообщениями*/
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    /**
     * Функция загрузки данных в поля
     */
    void initialize(){
        final int initialValue = 1;
        SpinnerValueFactory<Integer> valueFactoryNumberEntrance = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, initialValue);
        spinnerFamilyMembers.setValueFactory(valueFactoryNumberEntrance);

        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\D{0,50}").matcher(change.getControlNewText()).matches() ? change : null);
        textFFullName.setTextFormatter(formatter);
        TextFormatter formatter2 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,11}").matcher(change.getControlNewText()).matches() ? change : null);
        textFPhoneNumber.setTextFormatter(formatter2);
    }
    /**
     * Функция заполнения полей квартиросъемщика на сцене
     * @param phoneNumber номер телефона квартиросъемщика
     */
    public void fill(String phoneNumber){
        tenant.setPhone_number(phoneNumber);
        if (phoneNumber == ""){
            labelNameFrom.setText("Добавление квартиросъемщика");
            btnNameButton.setText("Добавить");
        }
        else {
            labelNameFrom.setText("Изменение квартиросъемщика");
            btnNameButton.setText("Изменить");
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery("select * from tenant where phone_number = " + tenant.getPhone_number() + "");
                while (resultSet.next()) {
                    textFFullName.setText(resultSet.getString("full_name"));
                    textFPhoneNumber.setText(resultSet.getString("phone_number"));
                    datePickerDateOfRegister.setValue(resultSet.getDate("date_of_registration").toLocalDate());
                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100, Integer.parseInt(resultSet.getString("number_of_family_members")));
                    spinnerFamilyMembers.setValueFactory(valueFactory);
                    tenant.setId_tenant(resultSet.getInt("id_tenant"));
                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
    }
    /**
     * Функция обработки нажатия при добавлении/изменении записи о квартиросъемщике в БД
     */
    public void onNameButton(ActionEvent actionEvent) {
        tenant.setFull_name(textFFullName.getText());
        tenant.setNumber_of_family_members((Integer) spinnerFamilyMembers.getValue());
        try {
            tenant.setDate_of_registration(Date.valueOf((datePickerDateOfRegister.getValue())));
        } catch (Exception e) {

        }
        tenant.setPhone_number(textFPhoneNumber.getText());

        if (tenant.getFull_name().length() < 10 || !tenant.getPhone_number().matches("[0-9]*") || tenant.getPhone_number().length() != 11 || tenant.getDate_of_registration() == null) {
            alert.setTitle("Некорректный ввод данных!");
            alert.setHeaderText("Некорректный ввод данных. Номер телефона: длина строки 11, \nзначением могут быть только цифры.");
            alert.show();
        } else {
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = null;
                if (tenant.getId_tenant() == null) resultSet = statement.executeQuery("select * from tenant where phone_number = '" + tenant.getPhone_number() + "'");
                else resultSet = statement.executeQuery("select * from tenant where phone_number = '" + tenant.getPhone_number() + "' and id_tenant != '" + tenant.getId_tenant() + "'");
                resultSet.last();
                int rows = resultSet.getRow();
                if (rows == 0) {
                    if(tenant.getId_tenant() == null) {
                        rows = statement.executeUpdate("INSERT INTO tenant (full_name, date_of_registration, " +
                                "number_of_family_members, phone_number) VALUES ('" + tenant.getFull_name() + "', '" + tenant.getDate_of_registration() + "', " +
                                "'" + tenant.getNumber_of_family_members() + "', '" + tenant.getPhone_number() + "')");
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успешно");
                        alert.setHeaderText("Квартиросъемщик успешно добавлен.");
                        alert.show();
                    }
                     else {
                        rows = statement.executeUpdate("UPDATE tenant SET full_name = '" + tenant.getFull_name() + "', date_of_registration = " +
                                "'" + tenant.getDate_of_registration() + "', number_of_family_members = '" + tenant.getNumber_of_family_members() + "', " +
                                "phone_number = " + tenant.getPhone_number() + " where id_tenant = '" + tenant.getId_tenant() + "'");
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("Успешно");
                        alert.setHeaderText("Квартиросъемщик успешно изменен.");
                        alert.show();
                    }
                    MainController controller = HouseManagementApplication.mainController;
                    controller.updateTenants();
                    Stage stage1 = (Stage) btnNameButton.getScene().getWindow();
                    stage1.close();
                } else {
                    alert.setTitle("Ошибка работы с квартриросъемщиком!");
                    alert.setHeaderText("Квартиросъемщик с таким номером телефона уже существует.");
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
