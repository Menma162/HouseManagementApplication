package com.housemanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tables.Indication;
import java.sql.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
/**
 * Класс Контроллер передачи показания со свойствами <b>lbFlatNumber</b>, <b>lbType</b>, <b>lbCounterNumber</b>, <b>comboBoxMonth</b>,
 * <b>comboBoxYear</b>, <b>textFValue</b>, <b>indication</b> и <b>alert</b>.
 * <p>
 * Данный класс добавляет новую запись показания в базу данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class TransferIndicationAddInfoController {
    /** Поле Label, которое содержит номер квартиры*/
    @FXML
    private Label lbFlatNumber;
    /** Поле Label, которое содержит тип счетчика*/
    @FXML
    private Label lbType;
    /** Поле Label, которое содержит номер счетчика*/
    @FXML
    private Label lbCounterNumber;
    /** Поле ComboBox, которое содержит список месяцев*/
    @FXML
    private ComboBox comboBoxMonth;
    /** Поле ComboBox, которое содержит список годов*/
    @FXML
    private ComboBox comboBoxYear;
    /** Поле TextField, которое содержит значение показания*/
    @FXML
    private TextField textFValue;
    /** Поле показания класса Indication*/
    private Indication indication = new Indication();
    /** Поле диалога с выводимыми сообщениями*/
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    /**
     * Функция загрузки данных в поля
     */
    @FXML
    void initialize(){
        fillPeriod();
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,3}").matcher(change.getControlNewText()).matches() ? change : null);
        textFValue.setTextFormatter(formatter);
    }
    /**
     * Функция заполнения счетчика и квартиры на сцене
     */
    public void fillCounter(int idCounter){
        indication.setIdCounter(idCounter);
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("select * from counter where id_counter = " + indication.getIdCounter() + "");
            while (resultSet.next()){
                lbCounterNumber.setText(resultSet.getString("number"));
                lbType.setText(resultSet.getString("type"));
                indication.setIdFlat(resultSet.getInt("id_flat"));

                statement = HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet2 = statement.executeQuery("select flat_number from flat where id_flat = " + indication.getIdFlat() + "");
                while(resultSet2.next()){
                    lbFlatNumber.setText(resultSet2.getString("flat_number"));
                }
            }
        } catch (SQLException ex) {
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
    }
    /**
     * Функция заполнения выпадающих списков с месяцами и годами
     */
    private void fillPeriod(){
        comboBoxMonth.getItems().clear();
        comboBoxYear.getItems().clear();
        ObservableList<String> months = FXCollections.observableArrayList();
        ObservableList<String> years = FXCollections.observableArrayList();
        months.addAll("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
        years.addAll("2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036",
                "2037", "2038", "2039", "2040", "2041", "2042","2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050", "2051", "2052","2053", "2054", "2055", "2056",
                "2057", "2058", "2059", "2060", "2061", "2062","2063", "2064", "2065", "2066", "2067", "2068", "2069", "2070");
        comboBoxMonth.setItems(months);
        comboBoxYear.setItems(years);
        comboBoxMonth.getSelectionModel().select(0);
        comboBoxYear.getSelectionModel().select(0);
    }
    /**
     * Функция обработки нажатия передачи показания
     */
    public void onTransfer(ActionEvent actionEvent) {
        indication.setPeriod(comboBoxMonth.getSelectionModel().getSelectedItem() + " " + (comboBoxYear.getSelectionModel().getSelectedItem()));
        try {
            indication.setValue(Integer.parseInt(textFValue.getText()));
        }
        catch (Exception e){
            indication.setValue(-1);
        }
        if (indication.getValue() == -1){
            alert.setTitle("Некорректный ввод данных!");
            alert.setHeaderText("Некорректный ввод данных. Проверьте поле со значением показания.");
            alert.show();
        }
        else {
            if(CheckPeriod.check(indication.getPeriod())){
                alert.setTitle("Ошибка передачи показания!");
                alert.setHeaderText("Показания можно передать только за предыдущий период");
                alert.show();
            }
            else {
                Statement statement = null;
                try {
                    statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet resultSet = statement.executeQuery("select * from indication where id_counter = " + indication.getIdCounter() + " and period = '" + indication.getPeriod() + "'");
                    resultSet.last();
                    int row = resultSet.getRow();
                    if (row!=0){
                        alert.setTitle("Ошибка передачи показания!");
                        alert.setHeaderText("Показания за данный период по этому счетчику уже переданы.");
                        alert.show();
                    }
                    else {
                        statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet resultSet3 = statement.executeQuery("select * from payment where period = '" + indication.getPeriod() + "'");
                        resultSet3.last();
                        int count = resultSet3.getRow();
                        if(count == 0){
                            HouseManagementApplication.getConnection().createStatement();
                            int rows = statement.executeUpdate("INSERT INTO indication (period, indication, id_counter) VALUES ('" + indication.getPeriod() + "' ,  '" + indication.getValue() + "' , '" + indication.getIdCounter() + "')");
                            alert.setAlertType(Alert.AlertType.INFORMATION);
                            alert.setTitle("Успешно");
                            alert.setHeaderText("Показание успешно передано.");
                            alert.show();
                            MainController controller = HouseManagementApplication.mainController;
                            controller.fillIndications();
                            Stage stage1 = (Stage) lbFlatNumber.getScene().getWindow();
                            stage1.close();
                        }
                        else {
                            alert.setTitle("Ошибка передачи показания!");
                            alert.setHeaderText("Показания передать невозможно, так как за этот период начисления уже произведены.");
                            alert.show();
                        }
                    }
                } catch (SQLException ex) {
                    alert.setTitle("Ошибка в базе данных!");
                    alert.setHeaderText(ex.getMessage());
                    alert.show();
                }
            }
        }
    }
}
