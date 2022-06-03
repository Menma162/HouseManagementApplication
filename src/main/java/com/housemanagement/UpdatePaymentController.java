package com.housemanagement;

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
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/**
 * Класс Контроллер обновления записи о начислении со свойствами <b>lbNamePhoto</b>, <b>lbPeriod</b>, <b>lbService</b>, <b>lbFlatNumber</b>,
 * <b>textFAmount</b>, <b>checkBoxStatus</b>, <b>buttonAddCheck</b>, <b>payment</b>, <b>src</b>, <b>stage</b> и <b>alert</b>.
 * <p>
 * Данный класс обновляет запись в БД о начислении.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class UpdatePaymentController {
    /** Поле Label, которое содержит название изображения*/
    @FXML
    private Label lbNamePhoto;
    /** Поле Label, которое содержит период начисления*/
    @FXML
    private Label lbPeriod;
    /** Поле Label, которое содержит услугу начисления*/
    @FXML
    private Label lbService;
    /** Поле Label, которое содержит номер квартиры*/
    @FXML
    private Label lbFlatNumber;
    /** Поле TextField, которое содержит сумму начисления*/
    @FXML
    private TextField textFAmount;
    /** Поле CheckBox, которое содержит статус оплаты начисления*/
    @FXML
    private CheckBox checkBoxStatus;
    /** Поле Button - кнопка добавления изображения*/
    @FXML
    private Button buttonAddCheck;
    /** Поле текущего начисления класса Payment*/
    private Payment payment = new Payment();
    /** Поле путь до изображения, которое загрузили*/
    private Path src = null;
    /** Поле диалога с выводимыми сообщениями*/
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    /** Поле главного окна*/
    public static Stage stage;
    /**
     * Функция загрузки данных в поля
     */
    void initialize(){
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setRoundingMode(RoundingMode.HALF_UP);
    }
    /**
     * Функция заполнения полей начисления на сцене
     * @param id уникальный идентификатор начисления
     */
    public void fillPayment(Integer id){
        payment.setId(id);
        if (id != null){
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery("select * from payment where id_payment = '" + payment.getId() + " '");
                while (resultSet.next()) {
                    lbPeriod.setText(resultSet.getString("period"));
                    payment.setIdRate(resultSet.getInt("id_rate"));
                    payment.setIdFlat(resultSet.getInt("id_flat"));
                    textFAmount.setText(resultSet.getString("amount"));
                    checkBoxStatus.setSelected(resultSet.getBoolean("payment_status"));

                    statement = HouseManagementApplication.getConnection().createStatement();
                    resultSet = statement.executeQuery("select service from rate where id_rate = '" + payment.getIdRate() + "'");
                    while (resultSet.next()){
                        lbService.setText(resultSet.getString("service"));
                    }

                    resultSet = statement.executeQuery("select flat_number from flat where id_flat = '" + payment.getIdFlat() + "'");
                    while (resultSet.next()){
                        lbFlatNumber.setText(resultSet.getString("flat_number"));
                    }
                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
    }
    /**
     * Функция обработки нажатия при загрузке изображения
     */
    public void onAddCheck(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter filter1 =
                new FileChooser.ExtensionFilter("All image files", "*.png",
                        "*.jpg", "*.gif");
        fileChooser.getExtensionFilters()
                .addAll(filter1);
        File file = fileChooser.showOpenDialog(UpdatePaymentController.stage);
        if (file != null) {
            src = file.toPath();
            payment.setCheque(payment.getId() + "_" + file.getName());
        }
        lbNamePhoto.setText(payment.getCheque());
    }
    /**
     * Функция обработки нажатия загрузки при изменении записи о платеже в БД
     */
    public void onUpdatePayment(ActionEvent actionEvent) {
        try {
            payment.setAmount(Float.valueOf(textFAmount.getText()));
        }
        catch (Exception e){
            payment.setAmount((float) -1);
        }
        payment.setStatus(checkBoxStatus.isSelected());
        if (payment.getAmount() == -1){
            alert.setTitle("Ошибка изменения начисления!");
            alert.setHeaderText("Некорректный ввод данных.");
            alert.show();
        }
        else{
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement();
                int row = statement.executeUpdate("UPDATE payment SET amount = '" + payment.getAmount() + "', " +
                        "payment_status = '" + payment.isStatus() + "', cheque = '" + payment.getCheque() + "' WHERE (id_payment = '" + payment.getId() +"');");
                if (src!= null) {
                    Path dst = Files.createDirectories(Path.of(String.valueOf(Paths.get("./images/" + payment.getCheque()))));
                    Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
                }
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Успешно");
                alert.setHeaderText("Показание успешно обновлено.");
                alert.show();
                MainController controller = HouseManagementApplication.mainController;
                controller.fillPayments();
                Stage stage1 = (Stage) buttonAddCheck.getScene().getWindow();
                stage1.close();
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
