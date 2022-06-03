package tables;
/**
 * Класс Норматив со свойствами <b>id</b>, <b>name</b> и <b>value</b>.
 * <p>
 * Данный класс позволяет описать экземпляр норматива.
 * Каждое из полей можно заполнить и изменить в дальнейшем. Класс предназначен для вывода
 * списка нормативов в таблицу, а также для работы с таблицей нормативов в базе данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class Normative {
    /** Поле уникальный идентификатор норматива*/
    private int id;
    /** Поле название норматива*/
    private String name;
    /** Поле значение норматива*/
    private float value;
    /**
     * Конструктор – создание нового экземпляра норматива
     * @see Normative#Normative()
     */
    public Normative(){ }
    /**
     * Функция получения значения поля {@link Normative#id}
     * @return возвращает уникальный идентификатор норматива
     */
    public int getId() {
        return id;
    }
    /**
     * Функция изменения уникального идентификатора норматива {@link Normative#id}
     * @param id уникальный идентификатор норматива
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Функция получения значения поля {@link Normative#name}
     * @return возвращает название норматива
     */
    public String getName() {
        return name;
    }
    /**
     * Функция изменения названия норматива{@link Normative#name}
     * @param name название норматива
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Функция получения значения поля {@link Normative#value}
     * @return возвращает значение норматива
     */
    public float getValue() {
        return value;
    }
    /**
     * Функция изменения значения норматива{@link Normative#value}
     * @param value значение норматива
     */
    public void setValue(float value) {
        this.value = value;
    }
}
