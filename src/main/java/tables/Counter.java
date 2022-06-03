package tables;

import javafx.scene.control.CheckBox;
/**
 * Класс Счетчик со свойствами <b>id</b>, <b>type</b>, <b>number</b>, <b>used</b>, <b>idFlat</b>,
 * <b>flatNumber</b> и <b>valueIndication</b>.
 * <p>
 * Данный класс позволяет описать экземпляр счетчика.
 * Каждое из полей можно заполнить и изменить в дальнейшем. Класс предназначен для вывода
 * списка счетчиков в таблицу, а также для работы с таблицей счетчиков в базе данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class Counter {
    /** Поле уникальный идентификатор счетчика*/
    private Integer id;
    /** Поле тип счетчика */
    private String type;
    /** Поле номер счетчика */
    private String number;
    /** Поле статус использования */
    private boolean used;
    /** Поле уникальный идентификатор квартиры*/
    private int idFlat;
    /** Поле номер квартиры */
    private int flatNumber;
    /** Поле значение показания */
    private int valueIndication;
    /**
     * Конструктор – создание нового экземпляра счетчика
     * @see Counter#Counter()
     */
    public Counter () { }
    /**
     * Функция получения значения поля {@link Counter#id}
     * @return возвращает уникальный идентификатор счетчика
     */
    public Integer getId() {
        return id;
    }
    /**
     * Функция изменения уникального идентификатора счетчика {@link Counter#id}
     * @param id уникальный идентификатор счетчика
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * Функция получения значения поля {@link Counter#type}
     * @return возвращает тип счетчика
     */
    public String getType() {
        return type;
    }
    /**
     * Функция изменения типа счетчика {@link Counter#type}
     * @param type тип счетчика
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Функция получения значения поля {@link Counter#number}
     * @return возвращает номер счетчика
     */
    public String getNumber() {
        return number;
    }
    /**
     * Функция изменения номера счетчика {@link Counter#number}
     * @param number номер счетчика
     */
    public void setNumber(String number) {
        this.number = number;
    }
    /**
     * Функция получения значения поля {@link Counter#used}
     * @return возвращает статус использования счетчика
     */
    public boolean getUsed() {
        return used;
    }
    /**
     * Функция статуса использования счетчика {@link Counter#used}
     * @param used статус использования счетчика
     */
    public void setUsed(boolean used) {
        this.used = used;
    }
    /**
     * Функция получения значения поля {@link Counter#flatNumber}
     * @return возвращает номер квартиры счетчика
     */
    public int getFlatNumber() {
        return flatNumber;
    }
    /**
     * Функция изменения номера квартиры счетчика {@link Counter#flatNumber}
     * @param flatNumber номера квартиры счетчика
     */
    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }
    /**
     * Функция получения значения поля {@link Counter#idFlat}
     * @return возвращает уникальный идентификатор квартиры счетчика
     */
    public int getIdFlat() {
        return idFlat;
    }
    /**
     * Функция изменения уникального идентификатора квартиры счетчика {@link Counter#idFlat}
     * @param idFlat уникальный идентификатор квартиры счетчика
     */
    public void setIdFlat(int idFlat) {
        this.idFlat = idFlat;
    }
    /**
     * Функция получения значения поля статуса использования с типом CheckBox
     * @return возвращает статус использования счетчика с типом CheckBox
     */
    public CheckBox getUsedC(){
        CheckBox usedC = new CheckBox();
        usedC.setSelected(used);
        usedC.setDisable(true);
        return usedC;
    }
    /**
     * Функция получения значения поля {@link Counter#valueIndication}
     * @return возвращает значение показания счетчика
     */
    public int getValueIndication() {
        return valueIndication;
    }
    /**
     * Функция изменения значения показания счетчика {@link Counter#valueIndication}
     * @param valueIndication значение показания счетчика
     */
    public void setValueIndication(int valueIndication) {
        this.valueIndication = valueIndication;
    }
}
