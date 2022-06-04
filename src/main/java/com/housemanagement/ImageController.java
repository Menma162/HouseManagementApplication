package com.housemanagement;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс Изображения со свойством <b>image</b>.
 * <p>
 * Данный класс позволяет открыть изображение в отдельном окне.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class ImageController {
    /** Поле изображение*/
    @FXML
    private ImageView image;
    /**
     * Функция загрузки изображения
     * @param file файл изображения
     */
    public void fill (String path){
        Image imageFile = new Image(path);
        image.setFitHeight(747);
        image.setFitWidth(967);
        image.setImage(imageFile);
    }
}
