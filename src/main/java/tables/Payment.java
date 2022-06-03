package tables;

import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
/**
 * Класс Начисление со свойствами <b>id</b>, <b>period</b>, <b>idFlat</b>, <b>numberFlat</b>,<b>idRate</b>,
 *<b>service</b>, <b>amount</b>, <b>status</b> и <b>cheque</b>.
 * <p>
 * Данный класс позволяет описать экземпляр начисления.
 * Каждое из полей можно заполнить и изменить в дальнейшем. Класс предназначен для вывода
 * списка начислений, а также для работы с таблицей начислений в базе данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class Payment {
    /** Поле уникальный идентификатор начисления*/
    private int id;
    /** Поле период начисления*/
    private String period;
    /** Поле уникальный идентификатор квартиры данного начисления*/
    private int idFlat;
    /** Поле номер квартиры данного начисления*/
    private int numberFlat;
    /** Поле уникальный идентификатор тарифа данного начисления*/
    private int idRate;
    /** Поле название тарифа данного начисления*/
    private String service;
    /** Поле уникальный идентификатор норматива данного начисления*/
    private Integer idNormative;
    /** Поле сумма начисления*/
    private Float amount;
    /** Поле статус оплаты начисления*/
    private boolean status;
    /** Поле название картинки с чеком начисления*/
    private String cheque;
    /**
     * Конструктор – создание нового экземпляра начисления
     * @see Payment#Payment()
     */
    public Payment() { }
    /**
     * Функция получения значения поля {@link Payment#id}
     * @return возвращает уникальный идентификатор начисления
     */
    public int getId() {
        return id;
    }
    /**
     * Функция изменения уникального идентификатора начисления {@link Payment#id}
     * @param id уникальный идентификатор начисления
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Функция получения значения поля {@link Payment#period}
     * @return возвращает период начисления
     */
    public String getPeriod() {
        return period;
    }
    /**
     * Функция изменения периода начисления {@link Payment#period}
     * @param period период начисления
     */
    public void setPeriod(String period) {
        this.period = period;
    }
    /**
     * Функция получения значения поля {@link Payment#idFlat}
     * @return возвращает уникальный идентификатор квартиры
     */
    public int getIdFlat() {
        return idFlat;
    }
    /**
     * Функция изменения уникального идентификатора квартиры {@link Payment#idFlat}
     * @param idFlat уникальный идентификатор квартиры
     */
    public void setIdFlat(int idFlat) {
        this.idFlat = idFlat;
    }
    /**
     * Функция получения значения поля {@link Payment#idRate}
     * @return возвращает уникальный идентификатор тарифа
     */
    public int getIdRate() {
        return idRate;
    }
    /**
     * Функция изменения уникального идентификатора тарифа {@link Payment#idRate}
     * @param idRate уникальный идентификатор тарифа
     */
    public void setIdRate(int idRate) {
        this.idRate = idRate;
    }
    /**
     * Функция получения значения поля {@link Payment#idNormative}
     * @return возвращает уникальный идентификатор норматива
     */
    public Integer getIdNormative() {
        return idNormative;
    }
    /**
     * Функция изменения уникального идентификатора норматива {@link Payment#idNormative}
     * @param idNormative уникальный идентификатор норматива
     */
    public void setIdNormative(Integer idNormative) {
        this.idNormative = idNormative;
    }
    /**
     * Функция получения значения поля {@link Payment#amount}
     * @return возвращает сумму начисления
     */
    public Float getAmount() {
        return amount;
    }
    /**
     * Функция изменения суммы начисления {@link Payment#amount}
     * @param amount уникальный идентификатор норматива
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }
    /**
     * Функция получения значения поля {@link Payment#status}
     * @return возвращает статус оплаты
     */
    public boolean isStatus() {
        return status;
    }
    /**
     * Функция изменения статуса оплаты {@link Payment#status}
     * @param status статус оплаты
     */
    public void setStatus(boolean status) {
        this.status = status;
    }
    /**
     * Функция получения значения поля {@link Payment#status}
     * @return возвращает статус оплаты с типом CheckBox
     */
    public CheckBox getStatusC() {
        CheckBox statusC = new CheckBox();
        statusC.setSelected(status);
        statusC.setDisable(true);
        return statusC;
    }
    /**
     * Функция получения значения поля {@link Payment#cheque}
     * @return возвращает название чека
     */
    public String getCheque() {
        return cheque;
    }
    /**
     * Функция изменения названия чека {@link Payment#cheque}
     * @param cheque название чека
     */
    public void setCheque(String cheque) {
        this.cheque = cheque;
    }
    /**
     * Функция получения значения поля {@link Payment#cheque}
     * @return возвращает чек с типом Image
     */
    public ImageView getChequeI() {
        ImageView chequeI = new ImageView();
        Image image = null;
        try {
            image = new Image(new File("src/main/resources/images/" + cheque).toURI().toString());
        }
        catch (Exception ignored){}
        chequeI.setFitHeight(300);
        chequeI.setFitHeight(100);
        chequeI.setImage(image);
        return chequeI;
    }
    /**
     * Функция получения значения поля {@link Payment#numberFlat}
     * @return возвращает номер квартиры
     */
    public int getNumberFlat() {
        return numberFlat;
    }
    /**
     * Функция изменения номера квартиры {@link Payment#numberFlat}
     * @param numberFlat номер квартиры
     */
    public void setNumberFlat(int numberFlat) {
        this.numberFlat = numberFlat;
    }
    /**
     * Функция получения значения поля {@link Payment#service}
     * @return возвращает название тарифа
     */
    public String getService() {
        return service;
    }
    /**
     * Функция изменения значения тарифа {@link Payment#service}
     * @param service значение тарифа
     */
    public void setService(String service) {
        this.service = service;
    }
}
