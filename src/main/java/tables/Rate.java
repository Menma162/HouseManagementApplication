package tables;
/**
 * Класс Норматив со свойствами <b>id</b>, <b>name</b> и <b>value</b>.
 * <p>
 * Данный класс позволяет описать экземпляр тарифа.
 * Каждое из полей можно заполнить и изменить в дальнейшем. Класс предназначен для вывода
 * списка тарифов в таблицу, а также для работы с таблицей тарифов в базе данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class Rate {
    /** Поле уникальный идентификатор тарифа*/
    private Integer id;
    /** Поле название тарифа*/
    private String name;
    /** Поле значение тарифа*/
    private float value;
    /**
     * Конструктор – создание нового экземпляра тарифа
     * @see Rate#Rate()
     */
    public Rate(){ }
    /**
     * Функция получения значения поля {@link Rate#id}
     * @return возвращает уникальный идентификатор тарифа
     */
    public Integer getId() {
        return id;
    }
    /**
     * Функция изменения уникального идентификатора тарифа {@link Rate#id}
     * @param id уникальный идентификатор тарифа
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * Функция получения значения поля {@link Rate#name}
     * @return возвращает название тарифа
     */
    public String getName() {
        return name;
    }
    /**
     * Функция изменения названия тарифа {@link Rate#name}
     * @param name название тарифа
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Функция получения значения поля {@link Rate#value}
     * @return возвращает значение тарифа
     */
    public float getValue() {
        return value;
    }
    /**
     * Функция изменения значения тарифа {@link Rate#value}
     * @param value значение тарифа
     */
    public void setValue(float value) {
        this.value = value;
    }
}
