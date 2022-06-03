package com.housemanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tables.Flat;
import java.io.IOException;
import java.sql.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
/**
 * Класс Контроллер добавления/изменения записи о квартире со свойствами <b>buttonWorkFlat</b>, <b>spinnerNumberEntrance</b>, <b>spinnerNumberRooms</b>,
 * <b>spinnerResidents</b>, <b>spinnerOwners</b>, <b>labelNameFrom</b>, <b>textFPersonalAccount</b>, <b>textFFlatNumber</b>, <b>textFTotalArea</b>
 * , <b>textFUsableArea</b>, <b>flat</b> и <b>alert</b>.
 * <p>
 * Данный класс добавляет/изменяет запись о квартире и передает эти значения на следующую сцену.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class WorkFlatController {
    /** Поле Button - кнопки работы с квартирой*/
    @FXML
    private Button buttonWorkFlat;
    /** Поле Spinner, позволяющее выбрать номер подъезда*/
    @FXML
    private Spinner spinnerNumberEntrance;
    /** Поле Spinner, позволяющее выбрать количество комнат*/
    @FXML
    private Spinner spinnerNumberRooms;
    /** Поле Spinner, позволяющее выбрать количество зарегистрированных жильцов*/
    @FXML
    private Spinner spinnerResidents;
    /** Поле Spinner, позволяющее выбрать количество собственников жилья*/
    @FXML
    private Spinner spinnerOwners;
    /** Поле Label, которое содержит название окна*/
    @FXML
    private Label labelNameFrom;
    /** Поле CheckBox, которое лицевой счет квартиры*/
    @FXML
    private TextField textFPersonalAccount;
    /** Поле CheckBox, которое лицевой номер квартиры*/
    @FXML
    private TextField textFFlatNumber;
    /** Поле CheckBox, которое лицевой общую площадь квартиры*/
    @FXML
    private TextField textFTotalArea;
    /** Поле CheckBox, которое полезную площадь квартиры*/
    @FXML
    private TextField textFUsableArea;
    /** Поле текущей квартиры класса Flat*/
    private Flat flat = new Flat();
    /** Поле диалога с выводимыми сообщениями*/
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    /**
     * Функция загрузки данных в поля
     */
    @FXML
    void initialize() {
        final int initialValue = 1;
        SpinnerValueFactory<Integer> valueFactoryNumberEntrance = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, initialValue);
        spinnerNumberEntrance.setValueFactory(valueFactoryNumberEntrance);
        SpinnerValueFactory<Integer> valueFactoryNumberRooms = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, initialValue);
        spinnerNumberRooms.setValueFactory(valueFactoryNumberRooms);
        SpinnerValueFactory<Integer> valueFactoryOwners = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, initialValue);
        spinnerOwners.setValueFactory(valueFactoryOwners);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        spinnerResidents.setValueFactory(valueFactory2);

        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,4}").matcher(change.getControlNewText()).matches() ? change : null);
        textFFlatNumber.setTextFormatter(formatter);
        TextFormatter formatter2 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,20}").matcher(change.getControlNewText()).matches() ? change : null);
        textFPersonalAccount.setTextFormatter(formatter2);
    }
    /**
     * Функция заполнения полей квартиры на сцене
     * @param id уникальный идентификатор квартиры
     */
    public void fill(Integer id){
        flat.setId(id);
        if (id == 0){
            labelNameFrom.setText("Добавление квартиры");
        }
        else {
            labelNameFrom.setText("Изменение квартиры");
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = resultSet = statement.executeQuery("select * from flat where id_flat = '" + flat.getId()+ " '");
                while (resultSet.next()) {
                    textFPersonalAccount.setText(resultSet.getString("personal_account"));
                    textFFlatNumber.setText(resultSet.getString("flat_number"));
                    textFTotalArea.setText(resultSet.getString("total_area"));
                    textFUsableArea.setText(resultSet.getString("usable_area"));
                    SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100, Integer.parseInt(resultSet.getString("number_of_registered_residents")));
                    spinnerResidents.setValueFactory(valueFactory3);
                    SpinnerValueFactory<Integer> valueFactory4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100, Integer.parseInt(resultSet.getString("number_of_rooms")));
                    spinnerNumberRooms.setValueFactory(valueFactory4);
                    SpinnerValueFactory<Integer> valueFactory5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100, Integer.parseInt(resultSet.getString("number_of_owners")));
                    spinnerOwners.setValueFactory(valueFactory5);
                    SpinnerValueFactory<Integer> valueFactory6 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                            100, Integer.parseInt(resultSet.getString("entrance_number")));
                    spinnerNumberEntrance.setValueFactory(valueFactory6);

                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
    }
    /**
     * Функция обработки нажатия перехода на следующую форму при добавлении/изменении квартиры с данными о квартире
     */
    public void onWorkFlat(ActionEvent actionEvent) {
        flat.setPersonal_account(textFPersonalAccount.getText());
        try {
            flat.setFlat_number(Integer.parseInt(textFFlatNumber.getText()));
        } catch (Exception e) {
            flat.setFlat_number(0);
        }
        try {
            flat.setTotal_area(Float.parseFloat(textFTotalArea.getText()));
        } catch (Exception e) {
            flat.setTotal_area(0);
        }
        try {
            flat.setUsable_area(Float.parseFloat(textFUsableArea.getText()));
        } catch (Exception e) {
            flat.setUsable_area(0);
        }
        flat.setEntrance_number((int) spinnerNumberEntrance.getValue());
        flat.setNumber_of_rooms((int) spinnerNumberRooms.getValue());
        flat.setNumber_of_registered_residents((int) spinnerResidents.getValue());
        flat.setNumber_of_owners((int)spinnerOwners.getValue());
        if (flat.getId() != null ) {
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery("select id_tenant from flat where id_flat = '" + flat.getId() + "'");
                if (resultSet.next()){
                    try {
                        flat.setId_tenant(Integer.parseInt(resultSet.getString("id_tenant")));
                    }
                    catch (Exception ex){
                        flat.setId_tenant(null);
                    }
                }
            }catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }

        if ((flat.getPersonal_account().length() < 10)
                || flat.getFlat_number() == 0 || flat.getTotal_area() == 0 || flat.getUsable_area() == 0
                || flat.getUsable_area() > flat.getTotal_area()) {
            alert.setTitle("Некорректный ввод данных!");
            alert.setHeaderText("Некорректный ввод данных. Лицевой счет: минимальная длина - 10, максимальная - 20, \nзначением могут быть только цифры; " +
                    "площадь полезной площади не должна превышать \nплощадь " +
                    "полной площади.");
            alert.show();
        } else {
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = null;
                if (flat.getId() == null) resultSet = statement.executeQuery("select * from flat where personal_account = '" + flat.getPersonal_account() + " 'or " +
                        "flat_number = '" + flat.getFlat_number() + "'");
                else resultSet = statement.executeQuery("select * from flat where (personal_account = '" + flat.getPersonal_account() + " 'or " +
                        "flat_number = '" + flat.getFlat_number() + "') and id_flat != '" + flat.getId() + "'");
                resultSet.last();
                int rows = resultSet.getRow();
                if (rows == 0) {
                    Stage stage1 = (Stage) buttonWorkFlat.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("listTenants.fxml"));
                    Parent root2 = (Parent) fxmlLoader.load();
                    ListTenantsController controller = fxmlLoader.getController();
                    controller.select(flat);
                    Stage stage2 = new Stage();
                    stage1.close();
                    stage2.initModality(Modality.APPLICATION_MODAL);
                    stage2.setScene(new Scene(root2));
                    stage2.setTitle("Работа с квартирой: выбор квартиросъемщика");
                    stage2.show();

                } else {
                    alert.setTitle("Ошибка работы с квартирой!");
                    alert.setHeaderText("Квартира с таким номером или лицевым счетом уже существует.");
                    alert.show();
                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
