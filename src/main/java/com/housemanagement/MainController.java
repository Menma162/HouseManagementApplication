package com.housemanagement;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tables.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Класс Контроллер загрузки основной сцены со свойствами.
 * <p>
 * Данный класс загружает основную сцену приложения, а также осуществляет основную работу с данными из БД.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class MainController {
    /**
     * Функция загрузки данных в поля
     */
    @FXML
    void initialize() {
        fillFilters();
        //Tab Flats------------------------------------
        comboBoxFlats.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer>
                                        changed, Integer oldValue, Integer newValue) {
                if (newValue != null) flatNumber = newValue;

                Statement statement = null;
                try {
                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from flat where flat_number = '" + flatNumber + "'");
                    while (resultSet.next()) {
                        labelIdTenant.setText(resultSet.getString("id_tenant"));
                        labelPersonalAccount.setText(resultSet.getString("personal_account"));
                        labelNumberEntrance.setText(resultSet.getString("entrance_number"));
                        labelTotalArea.setText(resultSet.getString("total_area"));
                        labelUsableArea.setText(resultSet.getString("usable_area"));
                        labelNumberRooms.setText(resultSet.getString("number_of_rooms"));
                        labelResidents.setText(resultSet.getString("number_of_registered_residents"));
                        labelOwners.setText(resultSet.getString("number_of_owners"));
                    }

                    labelTenant.setText("-");

                    resultSet = statement.executeQuery("select full_name from tenant where id_tenant = '" + labelIdTenant.getText() + "'");
                    while (resultSet.next()) {
                        labelTenant.setText(resultSet.getString("full_name"));
                    }
                } catch (SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка в базе данных!");
                    alert.setHeaderText(ex.getMessage());
                    alert.show();
                }
            }
        });
        fillComboBoxFlats();
        comboBoxFlats.getSelectionModel().select(0);

        //Tab Tenants----------------------------------------------------
        comboBoxTenants.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null)  phoneNumber = t1.toString().substring(t1.toString().length() - 11);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                Statement statement = null;
                try {
                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from tenant where phone_number = '" + phoneNumber + "'");
                    while (resultSet.next()) {
                        labelFullName.setText(resultSet.getString("full_name"));
                        labelDateOfRegister.setText(resultSet.getDate("date_of_registration").toLocalDate().format(formatter));
                        labelNumberFamilyMembers.setText(resultSet.getString("number_of_family_members"));
                        labeLPhoneNumber.setText(resultSet.getString("phone_number"));
                        idTenant = Integer.parseInt(resultSet.getString("id_tenant"));
                    }

                    fillComboBoxFlatsOnTenant(idTenant);
                    comboBoxFlatsTenant.getSelectionModel().select(0);
                } catch (SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка в базе данных!");
                    alert.setHeaderText(ex.getMessage());
                    alert.show();
                }
            }
        });
        fillComboBoxTenants();
        comboBoxTenants.getSelectionModel().select(0);

        //Тарифы и нормативы
        columnIdRate.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        columnNameRate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        columnValueRate.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getValue()));

        columnIdNormative.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        columnNameNormative.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        columnValueNormative.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getValue()));
        
        onSelectRate();
        onSelectNormative();
        fillRates();
        fillNormatives();
        tableRates.getSelectionModel().select(0);
        tableNormative.getSelectionModel().select(0);


        //Счетчики
        columnIdCounter.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        columnTypeCounter.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType()));
        columnNumberCounter.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNumber()));
        columnUsedCounter.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getUsedC()));
        columnNumberFlatCounter.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getFlatNumber()));

        fillСounters();

        //Показания
        columnIdIndication.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        columnPeriod.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPeriod()));
        columnType.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType()));
        columnNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNumberCounter()));
        columnValue.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getValue()));
        columnNumberFlat.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getNumberFlat()));

        fillIndications();

        //Платежи
        columnIdPayment.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        columnPeriodPayment.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPeriod()));
        columnStatusPayment.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getStatusC()));
        columnNumberFlatPayment.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getNumberFlat()));
        columnAmount.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getAmount()));
        columnCheque.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getChequeI()));
        columnService.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getService()));

        fillPayments();
        fillPeriod();
    }

    //Tab Flats--------------------------------------------------------------
    /** Поле Label - уникальный идентификатор квартиросъемщика (Tab Квартиры)*/
    @FXML
    private Label labelIdTenant;
    /** Поле ComboBox, которое содержит список всех квартир (Tab Квартиры)*/
    @FXML
    private ComboBox<Integer> comboBoxFlats;
    /** Поле Label - лицевой счет квартиры (Tab Квартиры)*/
    @FXML
    private Label labelPersonalAccount;
    /** Поле Label - общая площадь квартиры (Tab Квартиры)*/
    @FXML
    private Label labelTotalArea;
    /** Поле Label - полезная площадь квартиры (Tab Квартиры)*/
    @FXML
    private Label labelUsableArea;
    /** Поле Label - общая номер подъезда (Tab Квартиры)*/
    @FXML
    private Label labelNumberEntrance;
    /** Поле Label - количество комнат квартиры (Tab Квартиры)*/
    @FXML
    private Label labelNumberRooms;
    /** Поле Label - количество прописанных жильцов квартиры (Tab Квартиры)*/
    @FXML
    private Label labelResidents;
    /** Поле Label - количество собственников квартиры (Tab Квартиры)*/
    @FXML
    private Label labelOwners;
    /** Поле Label - ФИО квартиросъемщика квартиры (Tab Квартиры)*/
    @FXML
    private Label labelTenant;
    /** Поле номер квартиры (Tab Квартиры)*/
    private int flatNumber = 0;
    /**
     * Функция заполнения списка квартир на сцене (Tab Квартиры)
     */
    private void fillComboBoxFlats() {
        comboBoxFlats.getItems().clear();
        ObservableList<Integer> flatNumbers = FXCollections.observableArrayList();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select flat_number from flat");
            while (resultSet.next()) {
                flatNumbers.add(Integer.valueOf(resultSet.getString("flat_number")));
            }
            comboBoxFlats.setItems(flatNumbers);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        comboBoxFlats.getSelectionModel().select(0);
    }
    /**
     * Функция обработки нажатия при добавлении квартиры - открытие новой сцены (Tab Квартиры)
     */
    public void onAddFlat(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("workFlat.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        WorkFlatController controller = fxmlLoader.getController();
        controller.initialize();
        controller.fill(0);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Добавление квартиры: ввод информации");
        stage.show();
    }
    /**
     * Функция обработки нажатия при изменении квартиры - открытие новой сцены (Tab Квартиры)
     */
    public void onUpdateFlat(ActionEvent actionEvent) throws IOException {
        int idFlat = 0;
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select id_flat from flat where flat_number = '" + flatNumber + "'");
            while (resultSet.next()) {
                idFlat = Integer.parseInt(resultSet.getString("id_flat"));
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("workFlat.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        WorkFlatController controller = fxmlLoader.getController();
        controller.fill(idFlat);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Изменение квартиры: ввод информации");
        stage.show();
    }
    /**
     * Функция обработки нажатия при удалении записи о квартире в БД (Tab Квартиры)
     */
    public void onDeleteFlat(ActionEvent actionEvent) {
        if(flatNumber != 0){
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery("select id_flat from flat where flat_number = '" + flatNumber + "'");
                int id = 0;
                while (resultSet.next()){
                    id = resultSet.getInt("id_flat");
                }
                resultSet = statement.executeQuery("select * from counter where id_flat = '" + id + "'");
                resultSet.last();
                int rows = resultSet.getRow();
                resultSet = statement.executeQuery("select * from payment where id_flat = '" + id + "'");
                resultSet.last();
                rows += resultSet.getRow();
                if(rows == 0){
                    alert.setTitle("Удаление квартиры");
                    alert.setHeaderText("Вы уверены, что хотите удалить квартиру № " + flatNumber + "?");
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.OK) {
                        int row = statement.executeUpdate("DELETE FROM flat WHERE flat_number = '" + flatNumber + "'");
                        alert.setHeaderText("Квартира  № " + flatNumber + " успешно удалена.");
                        alert.show();
                        updateFlats();
                    }
                }
                else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка удаления!");
                    alert.setHeaderText("Эта квартира уже задействована либо в счетчиках, либо в начислениях.");
                    alert.show();
                }
            } catch (SQLException ex) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
    }
    /**
     * Функция обработки нажатия на список квартир - открытие новой сцены (Tab Квартиры)
     */
    public void onListFlats(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("listFlats.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Список квартир");
        stage.show();
    }
    /**
     * Функция обновления списка квартир (Tab Квартиры)
     */
    public void updateFlats(){
        labelIdTenant.setText("-");
        labelPersonalAccount.setText("-");
        labelNumberEntrance.setText("-");
        labelTotalArea.setText("-");
        labelUsableArea.setText("-");
        labelNumberRooms.setText("-");
        labelResidents.setText("-");
        labelOwners.setText("-");
        fillComboBoxFlats();
        comboBoxFlats.getSelectionModel().select(0);
    }

    //Tab Tenants
    /** Поле ComboBox, которое содержит список всех квартиросъемщиков (Tab Квартиросъемщики)*/
    @FXML
    private ComboBox<String> comboBoxTenants;
    /** Поле Label - ФИО квартиросъемщика (Tab Квартиросъемщики)*/
    @FXML
    private Label labelFullName;
    /** Поле Label - дата регистрации квартиросъемщика (Tab Квартиросъемщики)*/
    @FXML
    private Label labelDateOfRegister;
    /** Поле Label - количество членов семьи квартиросъемщика (Tab Квартиросъемщики)*/
    @FXML
    private Label labelNumberFamilyMembers;
    /** Поле Label - номер телефона квартиросъемщика (Tab Квартиросъемщики)*/
    @FXML
    private Label labeLPhoneNumber;
    /** Поле ComboBox, которое содержит список всех квартир квартиросъемщика (Tab Квартиросъемщики)*/
    @FXML
    private ComboBox<Integer> comboBoxFlatsTenant;
    /** Поле уникальный идентификатор квартиросъемщика (Tab Квартиросъемщики)*/
    private int idTenant = 0;
    /** Поле номер телефона квартиросъемщика (Tab Квартиросъемщики)*/
    private String phoneNumber = "";
    /**
     * Функция заполнения списка квартиросъемщиков на сцене (Tab Квартиросъемщики)
     */
    private void fillComboBoxTenants(){
        comboBoxTenants.getItems().clear();
        ObservableList<String> tenant = FXCollections.observableArrayList();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select full_name, phone_number from tenant");
            while (resultSet.next()) {
                tenant.add(resultSet.getString("full_name") + " - " + resultSet.getString("phone_number"));
            }
            comboBoxTenants.setItems(tenant);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        comboBoxTenants.getSelectionModel().select(0);
    }
    /**
     * Функция заполнения списка квартиросъемщиков на сцене (Tab Квартиросъемщики)
     */
    private void  fillComboBoxFlatsOnTenant(int id_tenant){
        comboBoxFlatsTenant.getItems().clear();
        ObservableList<Integer> flat = FXCollections.observableArrayList();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select flat_number from flat where id_tenant = '" + id_tenant + "'");
            while (resultSet.next()) {
                flat.add(Integer.valueOf(resultSet.getString("flat_number")));
            }
            comboBoxFlatsTenant.setItems(flat);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
    }
    /**
     * Функция обработки нажатия при добавлении квартиросъемщика - открытие новой сцены (Tab Квартиросъемщики)
     */
    public void onAddTenant(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("workTenant.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        WorkTenantController controller = fxmlLoader.getController();
        controller.initialize();
        controller.fill("");
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Добавление квартиросъемщика");
        stage.show();
    }
    /**
     * Функция обработки нажатия при изменении квартиросъемщика - открытие новой сцены (Tab Квартиросъемщики)
     */
    public void onUpdateTenant(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("workTenant.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        WorkTenantController controller = fxmlLoader.getController();
        controller.initialize();
        controller.fill(phoneNumber);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Изменение квартиросъемщика");
        stage.show();
    }
    /**
     * Функция обработки нажатия при удалении записи о квартиросъемщике в БД (Tab Квартиросъемщики)
     */
    public void onDeleteTenant(ActionEvent actionEvent) {
        if(phoneNumber != ""){
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                int id = 0;
                ResultSet resultSet = statement.executeQuery("select id_tenant from tenant where phone_number = " + phoneNumber + "");
                while (resultSet.next()){
                    id = resultSet.getInt("id_tenant");
                }
                resultSet = statement.executeQuery("select * from flat where id_tenant = " + id + "");
                resultSet.last();
                int rows = resultSet.getRow();
                if (rows == 0){
                    alert.setTitle("Удаление квартиросъемщика");
                    alert.setHeaderText("Вы уверены, что хотите удалить квартиросъемщика " + labelFullName.getText() + "?");
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.OK)
                    {
                        int row = statement.executeUpdate("DELETE FROM tenant WHERE phone_number = " + phoneNumber + "");
                        alert.setHeaderText("Квартиросъемщик " + labelFullName.getText() + " успешно удален.");
                        alert.show();
                    }
                }
                else{
                    alert.setTitle("Ошибка удаления!");
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setHeaderText("Этот квартиросъемщик уже задействован в квартирах.");
                    alert.show();
                }
            } catch (SQLException ex) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
            updateTenants();
        }
    }
    /**
     * Функция обновления списка квартиросъемщиков (Tab Квартиросъемщики)
     */
    public void updateTenants(){
        labelFullName.setText("-");
        labelDateOfRegister.setText("-");
        labeLPhoneNumber.setText("-");
        labelNumberFamilyMembers.setText("-");
        fillComboBoxTenants();
        comboBoxTenants.getSelectionModel().select(0);
    }
    /**
     * Функция обработки нажатия на список квартиросъемщиков - открытие новой сцены (Tab Квартиросъемщики)
     */
    public void onListTenants(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("listTenants.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ListTenantsController controller = fxmlLoader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Список квартиросъемщиков");
        stage.show();
    }

    //Teb Тарифы и нормативы------------------------------------------
    /** Поле таблицы тарифов на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableView<Rate> tableRates;
    /** Поле столбца уникального идентификатора тарифа на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableColumn<Rate, Integer> columnIdRate;
    /** Поле столбца названия тарифа на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableColumn<Rate, String> columnNameRate;
    /** Поле столбца значения тарифа на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableColumn<Rate, Float> columnValueRate;
    /** Поле Label - название тарифа (Tab Тарифы и нормативы)*/
    @FXML
    private Label labelRateName;
    /** Поле TextField, которое содержит значение тарифа на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TextField txtFieldValueRate;
    /** Поле таблицы нормативов на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableView<Normative> tableNormative;
    /** Поле уникального идентификатора норматива на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableColumn<Normative, Integer> columnIdNormative;
    /** Поле столбца названия норматива на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableColumn<Normative, String> columnNameNormative;
    /** Поле столбца значения норматива на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TableColumn<Normative, Float> columnValueNormative;
    /** Поле Label - название норматива (Tab Тарифы и нормативы)*/
    @FXML
    private Label labelNormativeName;
    /** Поле TextField, которое содержит значение норматива на сцене (Tab Тарифы и нормативы)*/
    @FXML
    private TextField txrFieldValueNormative;
    /** Поле Label - единица измерения норматива (Tab Тарифы и нормативы)*/
    @FXML
    private Label labelUnitNormative;
    /** Поле уникального идентификатора тарифа (Tab Тарифы и нормативы)*/
    private int idRateItem = 0;
    /** Поле уникального идентификатора норматива (Tab Тарифы и нормативы)*/
    private int idNormativeItem = 0;
    /**
     * Функция заполнения таблицы с тарифами (Tab Тарифы и нормативы)
     */
    private void fillRates(){
        tableRates.getItems().clear();
        Rate rate = new Rate();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from rate");
            while (resultSet.next()) {
                rate = new Rate();
                rate.setId(resultSet.getInt("id_rate"));
                rate.setName(resultSet.getString("service"));
                rate.setValue(resultSet.getFloat("value"));

                tableRates.getItems().add(rate);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        tableRates.getSelectionModel().select(0);
    }
    /**
     * Функция заполнения таблицы с нормативами (Tab Тарифы и нормативы)
     */
    private void fillNormatives(){
        tableNormative.getItems().clear();
        Normative normative = new Normative();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from normative");
            while (resultSet.next()) {
                normative = new Normative();
                normative.setId(resultSet.getInt("id_normative"));
                normative.setName(resultSet.getString("normative"));
                normative.setValue(resultSet.getFloat("value"));

                tableNormative.getItems().add(normative);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        tableNormative.getSelectionModel().select(0);
    }
    /**
     * Функция считывания выделения строки таблицы с тарифами (Tab Тарифы и нормативы)
     */
    private void onSelectRate(){
        tableRates.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rate>() {
            @Override
            public void changed(ObservableValue<? extends Rate> observableValue, Rate rate, Rate t1) {
                if (t1 != null) {
                    idRateItem = t1.getId();
                    labelRateName.setText(t1.getName());
                    txtFieldValueRate.setText(String.valueOf(t1.getValue()));
                }
            }
        });
    }
    /**
     * Функция считывания выделения строки таблицы с нормативами (Tab Тарифы и нормативы)
     */
    private void onSelectNormative(){
        tableNormative.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Normative>() {
            @Override
            public void changed(ObservableValue<? extends Normative> observableValue, Normative normative, Normative t1) {
                if (t1 != null){
                    idNormativeItem = t1.getId();
                    labelNormativeName.setText(t1.getName());
                    txrFieldValueNormative.setText(String.valueOf(t1.getValue()));
                    if(Objects.equals(t1.getName(), "Горячая вода") || Objects.equals(t1.getName(), "Холодная вода") || Objects.equals(t1.getName(), "Газ")) labelUnitNormative.setText("м³");
                    else if(Objects.equals(t1.getName(), "Электроэнергия")) labelUnitNormative.setText("кВт-ч");
                    else labelUnitNormative.setText("Гкал");
                }
            }
        });
    }
    /**
     * Функция обработки нажатия изменения записи о тарифе в БД (Tab Тарифы и нормативы)
     */
    public void onUpdateRate(ActionEvent actionEvent) {
        float value = 0;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            value = Float.parseFloat(txtFieldValueRate.getText());
        }
        catch (Exception e){}
        if(value != 0){
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement();
                int row = statement.executeUpdate("update rate set value = '" + value + "' where id_rate = '" + idRateItem + "'");
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Успешно");
                alert.setHeaderText("Значение тарифа успешно изменено.");
                alert.show();
            } catch (SQLException ex) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
        else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Некорректный ввод данных");
            alert.show();
        }
        fillRates();
        tableNormative.getSelectionModel().select(0);
    }
    /**
     * Функция обработки нажатия изменения записи о нормативе в БД (Tab Тарифы и нормативы)
     */
    public void onUpdateNormative(ActionEvent actionEvent) {
        float value = 0;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            value = Float.parseFloat(txrFieldValueNormative.getText());
        }
        catch (Exception e){}
        if(value != 0){
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement();
                int row = statement.executeUpdate("update normative set value = '" + value + "' where id_normative = '" + idNormativeItem + "'");
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Успешно");
                alert.setHeaderText("Значение норматива успешно изменено.");
                alert.show();
            } catch (SQLException ex) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
        else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Некорректный ввод данных");
            alert.show();
        }
        fillNormatives();
        tableNormative.getSelectionModel().select(0);
    }

    //Счетчики
    /** Поле таблицы счетчиков на сцене (Tab счетчики)*/
    @FXML
    private TableView<Counter> tableCounters;
    /** Поле столбца уникального идентификатора счетчика на сцене (Tab Счетчики)*/
    @FXML
    private TableColumn<Counter, Integer> columnIdCounter;
    /** Поле столбца типа счетчика на сцене (Tab Счетчики)*/
    @FXML
    private TableColumn<Counter, String> columnTypeCounter;
    /** Поле столбца номера счетчика на сцене (Tab Счетчики)*/
    @FXML
    private TableColumn<Counter, String> columnNumberCounter;
    /** Поле столбца статуса использования счетчика на сцене (Tab Счетчики)*/
    @FXML
    private TableColumn<Counter, CheckBox> columnUsedCounter;
    /** Поле столбца номера квартиры счетчика на сцене (Tab Счетчики)*/
    @FXML
    private TableColumn<Counter, Integer> columnNumberFlatCounter;
    /** Поле ComboBox, которое содержит список типов счетчиков для фильтрации (Tab Счетчики)*/
    @FXML
    private ComboBox comboBoxTypesCounterFilter;
    /** Поле ComboBox, которое содержит список квартир для фильтрации (Tab Счетчики)*/
    @FXML
    private ComboBox comboBoxFlatsCounterFilter;
    /**
     * Функция заполнения таблицы со счетчиками (Tab счетчики)
     */
    public void fillСounters(){
        tableCounters.getItems().clear();
        Counter counter = new Counter();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
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

                tableCounters.getItems().add(counter);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        tableCounters.getSelectionModel().select(0);
    }
    /**
     * Функция заполнения всех фильтров на основной сцене
     */
    public void fillFilters(){
        //Квартиры
        comboBoxFlatsCounterFilter.getItems().clear();
        comboBoxFlatsFilterIndication.getItems().clear();
        comboBoxFlatsFilterPayment.getItems().clear();
        ObservableList<Integer> flat = FXCollections.observableArrayList();
        flat.add(null);
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select flat_number from flat");
            while (resultSet.next()) {
                flat.add(Integer.valueOf(resultSet.getString("flat_number")));
            }
            comboBoxFlatsCounterFilter.setItems(flat);
            comboBoxFlatsFilterIndication.setItems(flat);
            comboBoxFlatsFilterPayment.setItems(flat);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        comboBoxFlatsFilterIndication.getSelectionModel().select(0);
        comboBoxFlatsCounterFilter.getSelectionModel().select(0);
        comboBoxFlatsFilterPayment.getSelectionModel().select(0);
        //Типы счетчиков
        comboBoxTypesCounterFilter.getItems().clear();
        comboBoxTypesCounterFilterIndication.getItems().clear();
        ObservableList<String> type = FXCollections.observableArrayList();
        type.addAll(null,"Газовый счетчик", "Счетчик горячей воды", "Счетчик холодной воды",
                "Счетчик электрической энергии", "Счетчик отопления");
        comboBoxTypesCounterFilter.setItems(type);
        comboBoxTypesCounterFilterIndication.setItems(type);
        comboBoxTypesCounterFilterIndication.getSelectionModel().select(0);
        comboBoxTypesCounterFilter.getSelectionModel().select(0);
        //Периоды
        comboBoxMonthFilterIndication.getItems().clear();
        comboBoxYearFilterIndication.getItems().clear();
        comboBoxMonthFilterPayment.getItems().clear();
        comboBoxYearFilterPayment.getItems().clear();
        ObservableList<String> months = FXCollections.observableArrayList();
        ObservableList<String> years = FXCollections.observableArrayList();
        months.addAll(null, "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
        years.addAll(null, "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036",
                "2037", "2038", "2039", "2040", "2041", "2042","2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050", "2051", "2052","2053", "2054", "2055", "2056",
                "2057", "2058", "2059", "2060", "2061", "2062","2063", "2064", "2065", "2066", "2067", "2068", "2069", "2070");
        comboBoxMonthFilterIndication.setItems(months);
        comboBoxYearFilterIndication.setItems(years);
        comboBoxMonthFilterPayment.setItems(months);
        comboBoxYearFilterPayment.setItems(years);
        comboBoxMonth.getSelectionModel().select(0);
        comboBoxYear.getSelectionModel().select(0);
        comboBoxMonthFilterPayment.getSelectionModel().select(0);
        comboBoxYearFilterPayment.getSelectionModel().select(0);
        //Платежи
        comboBoxServiceFilterPayment.getItems().clear();
        ObservableList<String> services = FXCollections.observableArrayList();
        services.addAll(null, "Холодная вода", "Горячая вода", "Газ", "Электроэнергия", "Тепловая энергия");
        comboBoxServiceFilterPayment.setItems(services);
        comboBoxServiceFilterPayment.getSelectionModel().select(0);
        comboBoxStatusFilterPayment.getItems().clear();
        ObservableList<String> statuses = FXCollections.observableArrayList();
        statuses.addAll(null, "Оплачено", "Не оплачено");
        comboBoxStatusFilterPayment.setItems(statuses);
        comboBoxStatusFilterPayment.getSelectionModel().select(0);
    }
    /**
     * Функция обработки нажатия при добавлении счетчика - открытие новой сцены (Tab Счетчики)
     */
    public void onAddCounter(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("workCounter.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        WorkCounterController controller = fxmlLoader.getController();
        controller.initialize();
        controller.fill(null);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Добавление счетчика");
        stage.show();
    }
    /**
     * Функция обработки нажатия при изменении счетчика - открытие новой сцены (Tab Счетчики)
     */
    public void onUpdateCounter(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("workCounter.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        WorkCounterController controller = fxmlLoader.getController();
        controller.initialize();
        var counters = tableCounters.getSelectionModel().getSelectedItems();
        controller.fill(tableCounters.getSelectionModel().selectedItemProperty().getValue().getId());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Изменение счетчика");
        stage.show();
    }
    /**
     * Функция обработки нажатия при удалении записи о счетчике в БД (Tab Счетчики)
     */
    public void onDeleteCounter(ActionEvent actionEvent) {
        if(tableCounters.getItems().size() != 0){
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                int id = 0;
                ResultSet resultSet = statement.executeQuery("select * from indication where id_counter = " +
                        tableCounters.getSelectionModel().selectedItemProperty().getValue().getId() + "");
                resultSet.last();
                int rows = resultSet.getRow();
                if (rows == 0){
                    alert.setTitle("Удаление счетчика");
                    alert.setHeaderText("Вы уверены, что хотите удалить " + tableCounters.getSelectionModel().selectedItemProperty().getValue().getType() + " " +
                            "с номером " + tableCounters.getSelectionModel().selectedItemProperty().getValue().getNumber() + " ?");
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.OK)
                    {
                        int row = statement.executeUpdate("DELETE FROM counter WHERE id_counter = " + tableCounters.getSelectionModel().selectedItemProperty().getValue().getId() + "");
                        alert.setHeaderText(tableCounters.getSelectionModel().selectedItemProperty().getValue().getType() + " с " +
                                "номером " + tableCounters.getSelectionModel().selectedItemProperty().getValue().getNumber() + " успешно удален.");
                        alert.show();
                    }
                }
                else{
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка удаления!");
                    alert.setHeaderText("Этот счетчик уже задействован в показаниях.");
                    alert.show();
                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
            updateTenants();
        }
    }
    /**
     * Функция обработки нажатия при фильтрации таблицы счетчиков (Tab Счетчики)
     */
    public void onFilterCounters(ActionEvent actionEvent) {
        String type = (String) comboBoxTypesCounterFilter.getSelectionModel().getSelectedItem();
        Integer flatNumber = (Integer) comboBoxFlatsCounterFilter.getSelectionModel().getSelectedItem();
        ArrayList<Counter> counters = new ArrayList<Counter>();
        Counter counter = new Counter();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
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

    //Показания-----------------------------------
    /** Поле таблицы показаний на сцене (Tab Показания)*/
    @FXML
    private TableView<Indication> tableIndications;
    /** Поле столбца индивидуального идентификатора показания на сцене (Tab Показания)*/
    @FXML
    private TableColumn<Indication, Integer> columnIdIndication;
    /** Поле столбца периода показания на сцене (Tab Показания)*/
    @FXML
    private TableColumn<Indication, String> columnPeriod;
    /** Поле столбца типа счетчика по переданному показанию на сцене (Tab Показания)*/
    @FXML
    private TableColumn<Indication, String> columnType;
    /** Поле столбца номера счетчика по переданному показанию на сцене (Tab Показания)*/
    @FXML
    private TableColumn<Indication, String> columnNumber;
    /** Поле столбца значения показания на сцене (Tab Показания)*/
    @FXML
    private TableColumn<Indication, Integer> columnValue;
    /** Поле столбца номера квартиры по переданному показанию на сцене (Tab Показания)*/
    @FXML
    private TableColumn<Indication, Integer> columnNumberFlat;
    /** Поле ComboBox, которое содержит типы счетчиков для фильтрации (Tab Показания)*/
    @FXML
    private ComboBox comboBoxTypesCounterFilterIndication;
    /** Поле ComboBox, которое содержит список квартир для фильтрации (Tab Показания)*/
    @FXML
    private ComboBox comboBoxFlatsFilterIndication;
    /** Поле ComboBox, которое содержит список месяцев для фильтрации (Tab Показания)*/
    @FXML
    private ComboBox comboBoxMonthFilterIndication;
    /** Поле ComboBox, которое содержит список годов для фильтрации (Tab Показания)*/
    @FXML
    private ComboBox comboBoxYearFilterIndication;
    /**
     * Функция заполнения таблицы с показаниями (Tab Показания)
     */
    public void fillIndications(){
        tableIndications.getItems().clear();
        Indication indication = new Indication();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from indication");
            while (resultSet.next()) {
                indication = new Indication();
                indication.setId(resultSet.getInt("id_indications"));
                indication.setPeriod(resultSet.getString("period"));
                indication.setIdCounter(resultSet.getInt("id_counter"));
                indication.setValue(resultSet.getInt("indication"));

                statement = HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet2 = statement.executeQuery("select type, number, id_flat from counter where id_counter = " + indication.getIdCounter() + "");
                while (resultSet2.next()){
                    indication.setNumberCounter(resultSet2.getString("number"));
                    indication.setType(resultSet2.getString("type"));
                    indication.setIdFlat(resultSet2.getInt("id_flat"));

                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet3 = statement.executeQuery("select flat_number from flat where id_flat = " + indication.getIdFlat() +"");
                    while (resultSet3.next()){
                        indication.setNumberFlat(resultSet3.getInt("flat_number"));
                    }
                }

                tableIndications.getItems().add(indication);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        tableIndications.getSelectionModel().select(0);
    }
    /**
     * Функция обработки нажатия при передаче показания - открытие новой сцены (Tab Показания)
     */
    public void onTransferIndication(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("selectCounterTransfer.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        TransferIndicationChangeCounterController controller = fxmlLoader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Передача показаний: выбор счетчика");
        stage.show();
    }
    /**
     * Функция обработки нажатия при удалении записи о показании в БД (Tab Показания)
     */
    public void onDeleteIndication(ActionEvent actionEvent) {
        if(tableIndications.getItems().size() != 0){
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                int id = 0;
                ResultSet resultSet = statement.executeQuery("select * from payment where period = '" +
                        tableIndications.getSelectionModel().selectedItemProperty().getValue().getPeriod() + "'");
                resultSet.last();
                int rows = resultSet.getRow();
                if (rows == 0){
                    alert.setTitle("Удаление показания");
                    alert.setHeaderText("Вы уверены, что хотите удалить показание за период " + tableIndications.getSelectionModel().selectedItemProperty().getValue().getPeriod() + " " +
                            "" + tableIndications.getSelectionModel().selectedItemProperty().getValue().getType() + " "
                            + tableIndications.getSelectionModel().selectedItemProperty().getValue().getNumberCounter() + " с значением " +
                            "" + tableIndications.getSelectionModel().selectedItemProperty().getValue().getValue()+ " ?");
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.OK)
                    {
                        int row = statement.executeUpdate("DELETE FROM indication WHERE id_indications = " +
                                tableIndications.getSelectionModel().selectedItemProperty().getValue().getId() + "");
                        alert.setHeaderText("Показание успешн удалено.");
                        alert.show();
                    }
                }
                else{
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка удаления!");
                    alert.setHeaderText("Начисления по этому показанию уже произведены.");
                    alert.show();
                }
            } catch (SQLException ex) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
            fillIndications();
        }
    }
    /**
     * Функция обработки нажатия при фильтрации таблицы показаний (Tab Показания)
     */
    public void onFilterIndication(ActionEvent actionEvent) {
        String type = (String) comboBoxTypesCounterFilterIndication.getSelectionModel().getSelectedItem();
        Integer flatNumber = (Integer) comboBoxFlatsFilterIndication.getSelectionModel().getSelectedItem();
        String periodFilterIndication = "none";
        if (comboBoxMonthFilterIndication.getSelectionModel().getSelectedItem() != null && comboBoxYearFilterIndication.getSelectionModel().getSelectedItem() != null)
            periodFilterIndication = comboBoxMonthFilterIndication.getSelectionModel().getSelectedItem()
                + " " + comboBoxYearFilterIndication.getSelectionModel().getSelectedItem();
        ArrayList<Indication> indications = new ArrayList<Indication>();
        Indication indication = new Indication();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from indication");
            while (resultSet.next()) {
                indication = new Indication();
                indication.setId(resultSet.getInt("id_indications"));
                indication.setPeriod(resultSet.getString("period"));
                indication.setIdCounter(resultSet.getInt("id_counter"));
                indication.setValue(resultSet.getInt("indication"));

                statement = HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet2 = statement.executeQuery("select type, number, id_flat from counter where id_counter = " + indication.getIdCounter() + "");
                while (resultSet2.next()){
                    indication.setNumberCounter(resultSet2.getString("number"));
                    indication.setType(resultSet2.getString("type"));
                    indication.setIdFlat(resultSet2.getInt("id_flat"));

                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet3 = statement.executeQuery("select flat_number from flat where id_flat = " + indication.getIdFlat() +"");
                    while (resultSet3.next()){
                        indication.setNumberFlat(resultSet3.getInt("flat_number"));
                    }
                }

                indications.add(indication);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        if (type != null) indications = (ArrayList<Indication>)indications.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
        if (flatNumber != null) indications = (ArrayList<Indication>)indications.stream().filter(x -> x.getNumberFlat() == flatNumber).collect(Collectors.toList());
        if (!periodFilterIndication.equals("none")) {
            String finalPeriodFilterIndication = periodFilterIndication;
            indications = (ArrayList<Indication>)indications.stream().filter(x -> x.getPeriod().equals(finalPeriodFilterIndication)).collect(Collectors.toList());
        }
        tableIndications.getItems().clear();
        tableIndications.getItems().addAll(indications);
        tableIndications.getSelectionModel().select(0);
    }

    //Начисления-----------------------------------------------
    /** Поле таблицы начислений на сцене (Tab Начисления)*/
    @FXML
    private TableView<Payment> tablePayments;
    /** Поле столбца индивидуального идентификатора начисления на сцене (Tab Начисления)*/
    @FXML
    private TableColumn<Payment, Integer> columnIdPayment;
    /** Поле столбца периода начисления на сцене (Tab Начисления)*/
    @FXML
    private TableColumn<Payment, String> columnPeriodPayment;
    /** Поле столбца названия услуги начисления на сцене (Tab Начисления)*/
    @FXML
    private TableColumn<Payment, String> columnService;
    /** Поле столбца номера квартиры начисления на сцене (Tab Начисления)*/
    @FXML
    private TableColumn<Payment, Integer> columnNumberFlatPayment;
    /** Поле столбца суммы начисления на сцене (Tab Начисления)*/
    @FXML
    private TableColumn<Payment, Float> columnAmount;
    /** Поле столбца статуса оплаты начисления на сцене (Tab Начисления)*/
    @FXML
    private TableColumn<Payment, CheckBox>  columnStatusPayment;
    /** Поле столбца чека начисления на сцене (Tab Начисления)*/
    @FXML
    private TableColumn<Payment, ImageView>  columnCheque;
    /** Поле ComboBox, которое содержит список месяцев для добавления начислений (Tab Начисления)*/
    @FXML
    private ComboBox comboBoxMonth;
    /** Поле ComboBox, которое содержит список годов для добавления начислений (Tab Начисления)*/
    @FXML
    private ComboBox comboBoxYear;
    /** Поле ComboBox, которое содержит список названий тарифов для фильтрации таблицы начислений (Tab Начисления)*/
    @FXML
    private ComboBox comboBoxServiceFilterPayment;
    /** Поле ComboBox, которое содержит список номеров квартир для фильтрации таблицы начислений (Tab Начисления)*/
    @FXML
    private ComboBox comboBoxFlatsFilterPayment;
    /** Поле ComboBox, которое содержит список месяцев для фильтрации таблицы начислений (Tab Начисления)*/
    @FXML
    private ComboBox comboBoxMonthFilterPayment;
    /** Поле ComboBox, которое содержит список годов тарифов для фильтрации таблицы начислений (Tab Начисления)*/
    @FXML
    private ComboBox comboBoxYearFilterPayment;
    /** Поле ComboBox, которое содержит статусов оплаты для фильтрации таблицы начислений (Tab Начисления)*/
    @FXML
    private ComboBox comboBoxStatusFilterPayment;
    /**
     * Функция заполнения таблицы с начислениями (Tab Начисления)
     */
    public void fillPayments(){
        tablePayments.getItems().clear();
        Payment payment = new Payment();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from payment");
            while (resultSet.next()) {
                payment = new Payment();
                payment.setId(resultSet.getInt("id_payment"));
                payment.setPeriod(resultSet.getString("period"));
                payment.setIdFlat(resultSet.getInt("id_flat"));
                payment.setAmount(resultSet.getFloat("amount"));
                payment.setIdRate(resultSet.getInt("id_rate"));
                payment.setCheque(resultSet.getString("cheque"));
                payment.setStatus(resultSet.getBoolean("payment_status"));

                statement = HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet2 = statement.executeQuery("select flat_number from flat where id_flat = " + payment.getIdFlat() + "");
                while (resultSet2.next()) {
                    payment.setNumberFlat(resultSet2.getInt("flat_number"));
                }

                statement = HouseManagementApplication.getConnection().createStatement();
                resultSet2 = statement.executeQuery("select service from rate where id_rate = '" + payment.getIdRate() + "'");
                while (resultSet2.next()){
                    payment.setService(resultSet2.getString("service"));
                }

                tablePayments.getItems().add(payment);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        tablePayments.getSelectionModel().select(0);
    }
    /**
     * Функция заполнения списков месяцев и годов для добавления начислений (Tab Начисления)
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
     * Функция обработки нажатия при добавлении начислений (Tab Начисления)
     */
    public void onAddPayments(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String period = (comboBoxMonth.getSelectionModel().getSelectedItem()) + " " + (comboBoxYear.getSelectionModel().getSelectedItem());
        if(CheckPeriod.check(period)){
            alert.setTitle("Ошибка начисления!");
            alert.setHeaderText("Начислить возможно только за предыдущий период");
            alert.show();
        }
        else {
            Statement statement = null;
            try {
                statement = HouseManagementApplication.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery("select * from payment where period = '" + period + "'");
                resultSet.last();
                int count = resultSet.getRow();
                if(count == 0){
                    ArrayList<Flat> flats = new ArrayList<Flat>();
                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet2 = statement.executeQuery("select id_flat, number_of_registered_residents, number_of_owners, usable_area from Flat");
                    while (resultSet2.next()){
                        Flat flat = new Flat();
                        flat.setId(resultSet2.getInt("id_flat"));
                        flat.setNumber_of_registered_residents(resultSet2.getInt("number_of_registered_residents"));
                        flat.setNumber_of_owners(resultSet2.getInt("number_of_owners"));
                        flat.setUsable_area(resultSet2.getInt("usable_area"));

                        flats.add(flat);
                    }

                    ArrayList<Rate> rates = new ArrayList<>();
                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet5 = statement.executeQuery("select * from rate");
                    while (resultSet5.next()){
                        Rate rate = new Rate();
                        rate.setId(resultSet5.getInt("id_rate"));
                        rate.setName(resultSet5.getString("service"));
                        rate.setValue(resultSet5.getFloat("value"));

                        rates.add(rate);
                    }

                    ArrayList<Normative> normatives = new ArrayList<>();
                    statement = HouseManagementApplication.getConnection().createStatement();
                    ResultSet resultSet6 = statement.executeQuery("select * from normative");
                    while (resultSet6.next()){
                        Normative normative = new Normative();
                        normative.setId(resultSet6.getInt("id_normative"));
                        normative.setName(resultSet6.getString("normative"));
                        normative.setValue(resultSet6.getFloat("value"));

                        normatives.add(normative);
                    }

                    if(flats.size() != 0){
                        for (Flat flat: flats) {
                            ArrayList<Counter> counters = new ArrayList<Counter>();
                            statement = HouseManagementApplication.getConnection().createStatement();
                            ResultSet resultSet3 = statement.executeQuery("select id_counter, type from counter where id_flat = '" + flat.getId() + "' and used = '1'");
                            while (resultSet3.next()){
                                Counter counter = new Counter();
                                counter.setId(resultSet3.getInt("id_counter"));
                                counter.setType(resultSet3.getString("type"));

                                statement = HouseManagementApplication.getConnection().createStatement();
                                ResultSet resultSet4 = statement.executeQuery("select indication from indication where id_counter = '" + counter.getId() + "'");
                                if(!resultSet4.isBeforeFirst()){
                                    counter.setValueIndication(-1);
                                }
                                else {
                                    while (resultSet4.next()){
                                        counter.setValueIndication(resultSet4.getInt("indication"));
                                    }
                                }
                                counters.add(counter);
                            }

                            Payment payment = new Payment();
                            Payment payment_cw = new Payment();
                            Payment payment_hw = new Payment();
                            Payment payment_ee = new Payment();
                            Payment payment_g = new Payment();
                            Payment payment_te = new Payment();

                            int count_cw = 0, count_hw = 0, count_ee = 0, count_g = 0, count_te = 0;
                            float sum_ind_cw = 0;
                            float sum_noind_cw = 0;
                            float sum_ind_hw = 0;
                            float sum_noind_hw = 0;
                            float sum_ind_ee = 0;
                            float sum_noind_ee = 0;
                            float sum_ind_g = 0;
                            float sum_noind_g = 0;
                            float sum_ind_te = 0;
                            float sum_noind_te = 0;

                            for(Counter counter: counters){
                                String type = counter.getType();
                                switch (type)
                                {
                                    case "Счетчик холодной воды":
                                        Integer id_rate = rates.stream().filter(x -> x.getName().equals("Холодная вода")).map(Rate :: getId).findAny().orElse(null);
                                        Float value_rate = rates.stream().filter(x -> x.getName().equals("Холодная вода")).map(Rate :: getValue).findAny().orElse(null);
                                        Integer  id_normative = normatives.stream().filter(x -> x.getName().equals("Холодная вода")).map(Normative :: getId).findAny().orElse(null);
                                        Float  value_normative = normatives.stream().filter(x -> x.getName().equals("Холодная вода")).map(Normative :: getValue).findAny().orElse(null);
                                        int indication = counter.getValueIndication();
                                        payment_cw.setPeriod(period);
                                        payment_cw.setStatus(false);
                                        payment_cw.setCheque(null);
                                        payment_cw.setIdFlat(flat.getId());
                                        payment_cw.setIdRate(id_rate);
                                        payment_cw.setIdNormative(id_normative);
                                        if (counter.getValueIndication() != -1) {
                                            sum_ind_cw += indication * value_rate;
                                        }
                                        else {
                                            if (flat.getNumber_of_registered_residents()!= 0) sum_noind_cw = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                            else sum_noind_cw = value_rate * value_normative * flat.getNumber_of_owners();
                                            count_cw++;
                                        }
                                        break;
                                    case "Счетчик горячей воды":
                                        id_rate = rates.stream().filter(x -> x.getName().equals("Горячая вода")).map(Rate :: getId).findAny().orElse(null);
                                        value_rate = rates.stream().filter(x -> x.getName().equals("Горячая вода")).map(Rate :: getValue).findAny().orElse(null);
                                        id_normative = normatives.stream().filter(x -> x.getName().equals("Горячая вода")).map(Normative :: getId).findAny().orElse(null);
                                        value_normative = normatives.stream().filter(x -> x.getName().equals("Горячая вода")).map(Normative :: getValue).findAny().orElse(null);
                                        indication = counter.getValueIndication();
                                        payment_hw.setPeriod(period);
                                        payment_hw.setStatus(false);
                                        payment_hw.setCheque(null);
                                        payment_hw.setIdFlat(flat.getId());
                                        payment_hw.setIdRate(id_rate);
                                        payment_hw.setIdNormative(id_normative);
                                        if (counter.getValueIndication() != -1){
                                            sum_ind_hw += indication * value_rate;
                                        }
                                        else{
                                            if (flat.getNumber_of_registered_residents()!= 0) sum_noind_hw = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                            else sum_noind_hw = value_rate * value_normative * flat.getNumber_of_owners();
                                            count_hw++;
                                        }
                                        break;
                                    case "Счетчик электрической энергии":
                                        id_rate = rates.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Rate :: getId).findAny().orElse(null);
                                        value_rate = rates.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Rate :: getValue).findAny().orElse(null);
                                        id_normative = normatives.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Normative :: getId).findAny().orElse(null);
                                        value_normative = normatives.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Normative :: getValue).findAny().orElse(null);
                                        indication = counter.getValueIndication();
                                        payment_ee.setPeriod(period);
                                        payment_ee.setStatus(false);
                                        payment_ee.setCheque(null);
                                        payment_ee.setIdFlat(flat.getId());
                                        payment_ee.setIdRate(id_rate);
                                        payment_ee.setIdNormative(id_normative);
                                        if (counter.getValueIndication() != -1){
                                            sum_ind_ee += indication * value_rate;
                                        }
                                        else {
                                            if (flat.getNumber_of_registered_residents()!= 0) sum_noind_ee = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                            else sum_noind_ee = value_rate * value_normative * flat.getNumber_of_owners();
                                            count_ee++;
                                        }
                                        break;
                                    case "Газовый счетчик":
                                        id_rate = rates.stream().filter(x -> x.getName().equals("Газ")).map(Rate :: getId).findAny().orElse(null);
                                        value_rate = rates.stream().filter(x -> x.getName().equals("Газ")).map(Rate :: getValue).findAny().orElse(null);
                                        id_normative = normatives.stream().filter(x -> x.getName().equals("Газ")).map(Normative :: getId).findAny().orElse(null);
                                        value_normative = normatives.stream().filter(x -> x.getName().equals("Газ")).map(Normative :: getValue).findAny().orElse(null);
                                        indication = counter.getValueIndication();
                                        payment_g.setPeriod(period);
                                        payment_g.setStatus(false);
                                        payment_g.setCheque(null);
                                        payment_g.setIdFlat(flat.getId());
                                        payment_g.setIdRate(id_rate);
                                        payment_g.setIdNormative(id_normative);
                                        if (counter.getValueIndication() != -1){
                                            sum_ind_g += indication * value_rate;
                                        }
                                        else{
                                            if (flat.getNumber_of_registered_residents()!= 0) sum_noind_g = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                            else sum_noind_g = value_rate * value_normative * flat.getNumber_of_owners();
                                            count_g++;
                                        }
                                        break;
                                    case "Счетчик отопления":
                                        id_rate = rates.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Rate :: getId).findAny().orElse(null);
                                        value_rate = rates.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Rate :: getValue).findAny().orElse(null);
                                        id_normative = normatives.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Normative :: getId).findAny().orElse(null);
                                        value_normative = normatives.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Normative :: getValue).findAny().orElse(null);
                                        indication = counter.getValueIndication();
                                        payment_te.setPeriod(period);
                                        payment_te.setStatus(false);
                                        payment_te.setCheque(null);
                                        payment_te.setIdFlat(flat.getId());
                                        payment_te.setIdRate(id_rate);
                                        payment_te.setIdNormative(id_normative);
                                        if (counter.getValueIndication() != -1){
                                            sum_ind_te += indication * value_rate;
                                        }
                                        else{
                                            sum_ind_te = value_normative * flat.getUsable_area();
                                            count_te++;
                                        }
                                        break;
                                }
                            }
                            Integer found = 0;
                            found = Math.toIntExact(counters.stream().filter(x -> x.getType().equals("Счетчик холодной воды")).count());
                            statement = HouseManagementApplication.getConnection().createStatement();
                            if(found == 0){
                                Integer id_rate = rates.stream().filter(x -> x.getName().equals("Холодная вода")).map(Rate :: getId).findAny().orElse(null);
                                Float value_rate = rates.stream().filter(x -> x.getName().equals("Холодная вода")).map(Rate :: getValue).findAny().orElse(null);
                                Integer id_normative = normatives.stream().filter(x -> x.getName().equals("Холодная вода")).map(Normative :: getId).findAny().orElse(null);
                                Float value_normative = normatives.stream().filter(x -> x.getName().equals("Холодная вода")).map(Normative :: getValue).findAny().orElse(null);
                                Float amount = null;

                                if(flat.getNumber_of_registered_residents() != 0) amount = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                else amount = value_rate * value_normative * flat.getNumber_of_owners();
                                amount = new BigDecimal(amount).setScale(1, RoundingMode.HALF_EVEN ).floatValue();

                                payment.setPeriod(period);
                                payment.setStatus(false);
                                payment.setCheque(null);
                                payment.setIdFlat(flat.getId());
                                payment.setIdRate(id_rate);
                                payment.setIdNormative(id_normative);
                                payment.setAmount(amount);

                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment.getPeriod() + "', '" + payment.getIdFlat() + "', '" + payment.getIdRate() + "', " + payment.getIdNormative() + "," +
                                        " '"+ payment.getAmount() + "', '" + payment.isStatus() + "', " + payment.getCheque() + ");");
                            }
                            else{
                                if(count_cw != 0)  payment_cw.setAmount(new BigDecimal(sum_ind_cw + (sum_noind_cw / count_cw)).setScale(1, RoundingMode.HALF_EVEN ).floatValue());
                                else payment_cw.setAmount(new BigDecimal(sum_ind_cw).setScale(1, RoundingMode.HALF_EVEN ).floatValue());

                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment_cw.getPeriod() + "', '" + payment_cw.getIdFlat() + "', '" + payment_cw.getIdRate() + "', " + payment_cw.getIdNormative() + "," +
                                        " '"+ payment_cw.getAmount() + "', '" + payment_cw.isStatus() + "', " + payment_cw.getCheque() + ");");
                            }
                            found = 0;
                            statement = HouseManagementApplication.getConnection().createStatement();
                            found = Math.toIntExact(counters.stream().filter(x -> x.getType().equals("Счетчик горячей воды")).count());
                            if(found == 0){
                                Integer id_rate = rates.stream().filter(x -> x.getName().equals("Горячая вода")).map(Rate :: getId).findAny().orElse(null);
                                Float value_rate = rates.stream().filter(x -> x.getName().equals("Горячая вода")).map(Rate :: getValue).findAny().orElse(null);
                                Integer id_normative = normatives.stream().filter(x -> x.getName().equals("Горячая вода")).map(Normative :: getId).findAny().orElse(null);
                                Float value_normative = normatives.stream().filter(x -> x.getName().equals("Горячая вода")).map(Normative :: getValue).findAny().orElse(null);
                                Float amount = null;

                                if(flat.getNumber_of_registered_residents() != 0) amount = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                else amount = value_rate * value_normative * flat.getNumber_of_owners();
                                amount = new BigDecimal(amount).setScale(1, RoundingMode.HALF_EVEN ).floatValue();

                                payment.setPeriod(period);
                                payment.setStatus(false);
                                payment.setCheque(null);
                                payment.setIdFlat(flat.getId());
                                payment.setIdRate(id_rate);
                                payment.setIdNormative(id_normative);
                                payment.setAmount(amount);

                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment.getPeriod() + "', '" + payment.getIdFlat() + "', '" + payment.getIdRate() + "', " + payment.getIdNormative() + "," +
                                        " '"+ payment.getAmount() + "', '" + payment.isStatus() + "', " + payment.getCheque() + ");");
                            }
                            else{
                                if(count_hw != 0)  payment_hw.setAmount(new BigDecimal(sum_ind_hw + (sum_noind_hw / count_hw)).setScale(1, RoundingMode.HALF_EVEN ).floatValue());
                                else payment_hw.setAmount(new BigDecimal(sum_ind_hw).setScale(1, RoundingMode.HALF_EVEN ).floatValue());


                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment_hw.getPeriod() + "', '" + payment_hw.getIdFlat() + "', '" + payment_hw.getIdRate() + "', " + payment_hw.getIdNormative() + "," +
                                        " '"+ payment_hw.getAmount() + "', '" + payment_hw.isStatus() + "', " + payment_hw.getCheque() + ");");
                            }
                            found = 0;
                            statement = HouseManagementApplication.getConnection().createStatement();
                            found = Math.toIntExact(counters.stream().filter(x -> x.getType().equals("Счетчик электрической энергии")).count());
                            if(found == 0){
                                Integer id_rate = rates.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Rate :: getId).findAny().orElse(null);
                                Float value_rate = rates.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Rate :: getValue).findAny().orElse(null);
                                Integer id_normative = normatives.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Normative :: getId).findAny().orElse(null);
                                Float value_normative = normatives.stream().filter(x -> x.getName().equals("Электроэнергия")).map(Normative :: getValue).findAny().orElse(null);
                                Float amount = null;

                                if(flat.getNumber_of_registered_residents() != 0) amount = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                else amount = value_rate * value_normative * flat.getNumber_of_owners();
                                amount = new BigDecimal(amount).setScale(1, RoundingMode.HALF_EVEN ).floatValue();

                                payment.setPeriod(period);
                                payment.setStatus(false);
                                payment.setCheque(null);
                                payment.setIdFlat(flat.getId());
                                payment.setIdRate(id_rate);
                                payment.setIdNormative(id_normative);
                                payment.setAmount(amount);

                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment.getPeriod() + "', '" + payment.getIdFlat() + "', '" + payment.getIdRate() + "', " + payment.getIdNormative() + "," +
                                        " '"+ payment.getAmount() + "', '" + payment.isStatus() + "', " + payment.getCheque() + ");");
                            }
                            else{
                                if(count_ee != 0)  payment_ee.setAmount(new BigDecimal(sum_ind_ee + (sum_noind_ee / count_ee)).setScale(1, RoundingMode.HALF_EVEN ).floatValue());
                                else payment_ee.setAmount(new BigDecimal(sum_ind_ee).setScale(1, RoundingMode.HALF_EVEN ).floatValue());


                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment_ee.getPeriod() + "', '" + payment_ee.getIdFlat() + "', '" + payment_ee.getIdRate() + "', " + payment_ee.getIdNormative() + "," +
                                        " '"+ payment_ee.getAmount() + "', '" + payment_ee.isStatus() + "', " + payment_ee.getCheque() + ");");
                            }
                            found = 0;
                            statement = HouseManagementApplication.getConnection().createStatement();
                            found = Math.toIntExact(counters.stream().filter(x -> x.getType().equals("Газовый счетчик")).count());
                            if(found == 0){
                                Integer id_rate = rates.stream().filter(x -> x.getName().equals("Газ")).map(Rate :: getId).findAny().orElse(null);
                                Float value_rate = rates.stream().filter(x -> x.getName().equals("Газ")).map(Rate :: getValue).findAny().orElse(null);
                                Integer id_normative = normatives.stream().filter(x -> x.getName().equals("Газ")).map(Normative :: getId).findAny().orElse(null);
                                Float value_normative = normatives.stream().filter(x -> x.getName().equals("Газ")).map(Normative :: getValue).findAny().orElse(null);
                                Float amount = null;

                                if(flat.getNumber_of_registered_residents() != 0) amount = value_rate * value_normative * flat.getNumber_of_registered_residents();
                                else amount = value_rate * value_normative * flat.getNumber_of_owners();
                                amount = new BigDecimal(amount).setScale(1, RoundingMode.HALF_EVEN ).floatValue();

                                payment.setPeriod(period);
                                payment.setStatus(false);
                                payment.setCheque(null);
                                payment.setIdFlat(flat.getId());
                                payment.setIdRate(id_rate);
                                payment.setIdNormative(id_normative);
                                payment.setAmount(amount);

                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment.getPeriod() + "', '" + payment.getIdFlat() + "', '" + payment.getIdRate() + "', " + payment.getIdNormative() + "," +
                                        " '"+ payment.getAmount() + "', '" + payment.isStatus() + "', " + payment.getCheque() + ");");
                            }
                            else{
                                if(count_g != 0)  payment_g.setAmount(new BigDecimal(sum_ind_g + (sum_noind_g / count_g)).setScale(1, RoundingMode.HALF_EVEN ).floatValue());
                                else payment_g.setAmount(new BigDecimal(sum_ind_g).setScale(1, RoundingMode.HALF_EVEN ).floatValue());


                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment_g.getPeriod() + "', '" + payment_g.getIdFlat() + "', '" + payment_g.getIdRate() + "', " + payment_g.getIdNormative() + "," +
                                        " '"+ payment_g.getAmount() + "', '" + payment_g.isStatus() + "', " + payment_g.getCheque() + ");");
                            }
                            found = 0;
                            statement = HouseManagementApplication.getConnection().createStatement();
                            found = Math.toIntExact(counters.stream().filter(x -> x.getType().equals("Счетчик отопления")).count());
                            if(found == 0){
                                Integer id_rate = rates.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Rate :: getId).findAny().orElse(null);
                                Float value_rate = rates.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Rate :: getValue).findAny().orElse(null);
                                Integer id_normative = normatives.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Normative :: getId).findAny().orElse(null);
                                Float value_normative = normatives.stream().filter(x -> x.getName().equals("Тепловая энергия")).map(Normative :: getValue).findAny().orElse(null);
                                Float amount = value_normative * flat.getUsable_area();
                                amount = new BigDecimal(amount).setScale(1, RoundingMode.HALF_EVEN ).floatValue();

                                payment.setPeriod(period);
                                payment.setStatus(false);
                                payment.setCheque(null);
                                payment.setIdFlat(flat.getId());
                                payment.setIdRate(id_rate);
                                payment.setIdNormative(id_normative);
                                payment.setAmount(amount);

                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment.getPeriod() + "', '" + payment.getIdFlat() + "', '" + payment.getIdRate() + "', " + payment.getIdNormative() + "," +
                                        " '"+ payment.getAmount() + "', '" + payment.isStatus() + "', " + payment.getCheque() + ");");
                            }
                            else{
                                if(count_te != 0)  payment_te.setAmount(new BigDecimal(sum_ind_te + (sum_noind_te / count_te)).setScale(1, RoundingMode.HALF_EVEN ).floatValue());
                                else payment_te.setAmount(new BigDecimal(sum_ind_te).setScale(1, RoundingMode.HALF_EVEN ).floatValue());


                                int row = statement.executeUpdate("INSERT INTO payment (period, id_flat, id_rate, id_normative, amount, payment_status, cheque) " +
                                        "VALUES ('" + payment_te.getPeriod() + "', '" + payment_te.getIdFlat() + "', '" + payment_te.getIdRate() + "', " + payment_te.getIdNormative() + "," +
                                        " '"+ payment_te.getAmount() + "', '" + payment_te.isStatus() + "', " + payment_te.getCheque() + ");");
                            }
                        }
                    }
                    fillPayments();
                }
                else {
                    alert.setTitle("Ошибка начисления!");
                    alert.setHeaderText("Добавить новые начисления невозможно, \nтак как за этот период начисления уже произведены.");
                    alert.show();
                }
            } catch (SQLException ex) {
                alert.setTitle("Ошибка в базе данных!");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }
        }
    }
    /**
     * Функция обработки нажатия при изменении начисления - открытие новой сцены (Tab Начисления)
     */
    public void onUpdatePayment(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("updatePayment.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        UpdatePaymentController controller = fxmlLoader.getController();
        controller.initialize();
        controller.fillPayment(tablePayments.getSelectionModel().selectedItemProperty().getValue().getId());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Изменение начисления");
        stage.show();
    }
    /**
     * Функция обработки нажатия при просмотре чека - открытие новой сцены (Tab Начисления)
     */
    public void onOpenCheque(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("image.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ImageController controller = fxmlLoader.getController();
        File file = new File("src/main/resources/images/" + tablePayments.getSelectionModel().selectedItemProperty().getValue().getCheque());
        controller.fill(file);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Чек");
        stage.show();
    }
    /**
     * Функция обработки нажатия при фильтрации таблицы начислений (Tab Начисления)
     */
    public void onFilterPayment(ActionEvent actionEvent) {
        Integer flatNumber = (Integer) comboBoxFlatsFilterPayment.getSelectionModel().getSelectedItem();
        String periodFilterPayment = "none";
        if (comboBoxMonthFilterPayment.getSelectionModel().getSelectedItem() != null && comboBoxYearFilterPayment.getSelectionModel().getSelectedItem() != null)
            periodFilterPayment = comboBoxMonthFilterPayment.getSelectionModel().getSelectedItem()
                    + " " + comboBoxYearFilterPayment.getSelectionModel().getSelectedItem();
        String service = (String) comboBoxServiceFilterPayment.getSelectionModel().getSelectedItem();
        String statusS = (String) comboBoxStatusFilterPayment.getSelectionModel().getSelectedItem();
        Boolean status = false;
        if (statusS != null && statusS.equals("Оплачено")) status = true;
        ArrayList<Payment> payments = new ArrayList<Payment>();
        Payment payment = new Payment();
        Statement statement = null;
        try {
            statement = HouseManagementApplication.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from payment");
            while (resultSet.next()) {
                payment = new Payment();
                payment.setId(resultSet.getInt("id_payment"));
                payment.setPeriod(resultSet.getString("period"));
                payment.setIdFlat(resultSet.getInt("id_flat"));
                payment.setAmount(resultSet.getFloat("amount"));
                payment.setIdRate(resultSet.getInt("id_rate"));
                payment.setCheque(resultSet.getString("cheque"));
                payment.setStatus(resultSet.getBoolean("payment_status"));

                statement = HouseManagementApplication.getConnection().createStatement();
                ResultSet resultSet2 = statement.executeQuery("select flat_number from flat where id_flat = " + payment.getIdFlat() + "");
                while (resultSet2.next()) {
                    payment.setNumberFlat(resultSet2.getInt("flat_number"));
                }

                statement = HouseManagementApplication.getConnection().createStatement();
                resultSet2 = statement.executeQuery("select service from rate where id_rate = '" + payment.getIdRate() + "'");
                while (resultSet2.next()){
                    payment.setService(resultSet2.getString("service"));
                }

                payments.add(payment);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка в базе данных!");
            alert.setHeaderText(ex.getMessage());
            alert.show();
        }
        if (flatNumber != null) payments = (ArrayList<Payment>)payments.stream().filter(x -> x.getNumberFlat() == flatNumber).collect(Collectors.toList());
        if (!periodFilterPayment.equals("none")) {
            String finalPeriodFilterIndication = periodFilterPayment;
            payments = (ArrayList<Payment>)payments.stream().filter(x -> x.getPeriod().equals(finalPeriodFilterIndication)).collect(Collectors.toList());
        }
        if(service != null) {
            payments = (ArrayList<Payment>)payments.stream().filter(x -> x.getService().equals(service)).collect(Collectors.toList());
        }
        if (statusS != null) {
            Boolean finalStatus = status;
            payments = (ArrayList<Payment>)payments.stream().filter(x -> x.isStatus() == finalStatus).collect(Collectors.toList());
        }
        tablePayments.getItems().clear();
        tablePayments.getItems().addAll(payments);
        tablePayments.getSelectionModel().select(0);
    }
}