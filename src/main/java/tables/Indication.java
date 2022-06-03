package tables;
/**
 * Класс Показания со свойствами <b>id</b>, <b>period</b>, <b>value</b>, <b>idCounter</b>, <b>type</b>,
 * <b>numberCounter</b>б <b>numberFlat</b> и <b>idFlat</b>.
 * <p>
 * Данный класс позволяет описать экземпляр показания.
 * Каждое из полей можно заполнить и изменить в дальнейшем. Класс предназначен для вывода
 * списка показаний в таблицу, а также для работы с таблицей показаний в базе данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class Indication {
    /** Поле уникальный идентификатор показания*/
    private int id;
    /** Поле период передачи*/
    private String period;
    /** Поле значение показания*/
    private int value;
    /** Поле уникальный идентификатор счетчика данного показания*/
    private int idCounter;
    /** Поле тип счетчика данного показания*/
    private String type;
    /** Поле номер счетчика данного показания*/
    private String numberCounter;
    /** Поле номер квартиры данного показания*/
    private int numberFlat;
    /** Поле уникальный идентификатор квартиры данного показания*/
    private int idFlat;
    /**
     * Конструктор – создание нового экземпляра показания
     * @see Indication#Indication()
     */
    public Indication () { }
    /**
     * Функция получения значения поля {@link Indication#id}
     * @return возвращает уникальный идентификатор показания
     */
    public int getId() {
        return id;
    }
    /**
     * Функция изменения уникального идентификатора показания {@link Indication#id}
     * @param id уникальный идентификатор показания
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Функция получения значения поля {@link Indication#period}
     * @return возвращает период передачи показания
     */
    public String getPeriod() {
        return period;
    }
    /**
     * Функция изменения периода передачи показания {@link Indication#period}
     * @param period период передачи показания
     */
    public void setPeriod(String period) {
        this.period = period;
    }
    /**
     * Функция получения значения поля {@link Indication#value}
     * @return возвращает значение показания
     */
    public int getValue() {
        return value;
    }
    /**
     * Функция изменения значения показания {@link Indication#value}
     * @param value значение показания
     */
    public void setValue(int value) {
        this.value = value;
    }
    /**
     * Функция получения значения поля {@link Indication#idCounter}
     * @return возвращает уникальный идентификатор счетчика
     */
    public int getIdCounter() {
        return idCounter;
    }
    /**
     * Функция изменения уникального идентификатора счетчика {@link Indication#idCounter}
     * @param idCounter уникальный идентификатор счетчика
     */
    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }
    /**
     * Функция получения значения поля {@link Indication#numberCounter}
     * @return возвращает номер счетчика
     */
    public String getNumberCounter() {
        return numberCounter;
    }
    /**
     * Функция изменения номера счетчика{@link Indication#numberCounter}
     * @param numberCounter номер счетчика
     */
    public void setNumberCounter(String numberCounter) {
        this.numberCounter = numberCounter;
    }
    /**
     * Функция получения значения поля {@link Indication#numberFlat}
     * @return возвращает номер квартиры
     */
    public int getNumberFlat() {
        return numberFlat;
    }
    /**
     * Функция изменения номера квартиры{@link Indication#numberFlat}
     * @param numberFlat номер квартиры
     */
    public void setNumberFlat(int numberFlat) { this.numberFlat = numberFlat;}
    /**
     * Функция получения значения поля {@link Indication#idFlat}
     * @return возвращает уникальный идентификатор квартиры
     */
    public int getIdFlat() {
        return idFlat;
    }
    /**
     * Функция изменения уникального идентификатора квартиры{@link Indication#idFlat}
     * @param idFlat уникальный идентификатор квартиры
     */
    public void setIdFlat(int idFlat) {
        this.idFlat = idFlat;
    }
    /**
     * Функция получения значения поля {@link Indication#type}
     * @return возвращает тип счетчика
     */
    public String getType() { return type; }
    /**
     * Функция изменения типа счетчика {@link Indication#type}
     * @param type тип счетчика
     */
    public void setType(String type) { this.type = type; }
}
