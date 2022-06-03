package com.housemanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс Домоуправление со свойством <b>mainController</b>.
 * Данный класс запускает приложения.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class HouseManagementApplication extends Application {
    /** Поле основного контроллера*/
    public static MainController mainController;
    /** Поле подключения к БД*/
    private static Connection connection;
    /** Поле строка подключения*/
    static final String DB_URL = "jdbc:mysql://localhost:3306/housemanagement";
    /** Поле логина для подключения*/
    static final String LOGIN = "root";
    /** Поле пароля для подключения*/
    static final String PASSWORD = "root root";
    /**
     * Функция получения значения поля {@link HouseManagementApplication#connection}
     * @return возвращает подключение
     */
    public static Connection getConnection() {
        return connection;
    }
    /**
     * Загрузка основного контроллера и сцены
     */
    @Override
    public void start(Stage stage) throws IOException {
        try{
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось подключиться к базе данных. ", ButtonType.OK);
            alert.showAndWait();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HouseManagementApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1143, 741);
        mainController = fxmlLoader.getController();
        stage.setScene(scene);
        stage.setTitle("Домоуправление");
        stage.show();
    }
    /**
     * Функция запуска приложения
     */
    public static void main(String[] args) {
        launch();
    }
}