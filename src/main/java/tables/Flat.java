package tables;
/**
 * Класс Квартира со свойствами <b>id</b>, <b>personal_account</b>, <b>flat_number</b>, <b>total_area</b>,<b>usable_area</b>,
 *<b>entrance_number</b>, <b>number_of_rooms</b>, <b>number_of_registered_residents</b>, <b>number_of_owners</b>,
 * <b>id_tenant</b> и <b>full_name</b>.
 * <p>
 * Данный класс позволяет описать экземпляр квартиры.
 * Каждое из полей можно заполнить и изменить в дальнейшем. Класс предназначен для вывода
 * списка квартир, а также для работы с таблицей квартир в базе данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class Flat {
    /** Поле уникальный идентификатор квартиры*/
    private Integer id;
    /** Поле лицевой счет*/
    private String personal_account;
    /** Поле номер квартиры*/
    private int flat_number;
    /** Поле общая площадь*/
    private float total_area;
    /** Поле полезная площадь*/
    private float usable_area;
    /** Поле номер подъезда*/
    private int entrance_number;
    /** Поле количество комнат*/
    private int number_of_rooms;
    /** Поле количество зарегистрированных жителей*/
    private int number_of_registered_residents;
    /** Поле количество собственников жилья*/
    private int number_of_owners;
    /** Поле уникальный идентификатор квартиросъемщика*/
    private Integer id_tenant;
    /** Поле ФИО квартиросъемщика*/
    private String full_name;
    /**
     * Конструктор – создание нового экземпляра квартиры
     * @see Flat#Flat()
     */
    public Flat() { }
    /**
     * Функция получения значения поля {@link Flat#id}
     * @return возвращает уникальный идентификатор квартиры
     */
    public Integer getId() {
        return id;
    }
    /**
     * Функция изменения уникального идентификатора квартиры {@link Flat#id}
     * @param id уникальный идентификатор квартиры
     */
    public void setId(Integer id) {this.id = id;}
    /**
     * Функция получения значения поля {@link Flat#personal_account}
     * @return возвращает лицевой счет квартиры
     */
    public String getPersonal_account() {
        return personal_account;
    }
    /**
     * Функция изменения лицевого счета квартиры {@link Flat#personal_account}
     * @param personal_account лицевой счет квартиры
     */
    public void setPersonal_account(String personal_account) {
        this.personal_account = personal_account;
    }
    /**
     * Функция получения значения поля {@link Flat#flat_number}
     * @return возвращает номер квартиры
     */
    public int getFlat_number() {
        return flat_number;
    }
    /**
     * Функция изменения номера квартиры {@link Flat#flat_number}
     * @param flat_number номер квартиры
     */
    public void setFlat_number(int flat_number) {
        this.flat_number = flat_number;
    }
    /**
     * Функция получения значения поля {@link Flat#total_area}
     * @return возвращает полную площадь квартиры
     */
    public float getTotal_area() {
        return total_area;
    }
    /**
     * Функция изменения полной площади квартиры {@link Flat#total_area}
     * @param total_area полная площадь квартиры
     */
    public void setTotal_area(float total_area) {this.total_area = total_area;}
    /**
     * Функция получения значения поля {@link Flat#usable_area}
     * @return возвращает полезную площадь квартиры
     */
    public float getUsable_area() {
        return usable_area;
    }
    /**
     * Функция изменения полезной площади квартиры {@link Flat#usable_area}
     * @param usable_area полезная площадь квартиры
     */
    public void setUsable_area(float usable_area) {
        this.usable_area = usable_area;
    }
    /**
     * Функция получения значения поля {@link Flat#entrance_number}
     * @return возвращает номер подъезда
     */
    public int getEntrance_number() {
        return entrance_number;
    }
    /**
     * Функция изменения номера подъезда {@link Flat#entrance_number}
     * @param entrance_number номер подъзда
     */
    public void setEntrance_number(int entrance_number) {
        this.entrance_number = entrance_number;
    }
    /**
     * Функция получения значения поля {@link Flat#number_of_rooms}
     * @return возвращает количество комнат
     */
    public int getNumber_of_rooms() {
        return number_of_rooms;
    }
    /**
     * Функция изменения количества комнат {@link Flat#number_of_rooms}
     * @param number_of_rooms количество комнат
     */
    public void setNumber_of_rooms(int number_of_rooms) {
        this.number_of_rooms = number_of_rooms;
    }
    /**
     * Функция получения значения поля {@link Flat#number_of_registered_residents}
     * @return возвращает количество харегистрированных жителей
     */
    public int getNumber_of_registered_residents() {
        return number_of_registered_residents;
    }
    /**
     * Функция изменения количества зарегистрированных жителей {@link Flat#number_of_registered_residents}
     * @param number_of_registered_residents количество зарегистрированных жителей
     */
    public void setNumber_of_registered_residents(int number_of_registered_residents) {this.number_of_registered_residents = number_of_registered_residents;}
    /**
     * Функция получения значения поля {@link Flat#number_of_owners}
     * @return возвращает количество собственников жилья
     */
    public int getNumber_of_owners() {
        return number_of_owners;
    }
    /**
     * Функция изменения количества собственников жилья {@link Flat#number_of_owners}
     * @param number_of_owners количество собственников жилья
     */
    public void setNumber_of_owners(int number_of_owners) {
        this.number_of_owners = number_of_owners;
    }
    /**
     * Функция получения значения поля {@link Flat#id_tenant}
     * @return возвращает уникальный идентификатор квартиросъемщика
     */
    public Integer getId_tenant() {
        return id_tenant;
    }
    /**
     * Функция изменения куникального идентификатор квартиросъемщика {@link Flat#id_tenant}
     * @param id_tenant уникальный идентификатор квартиросъемщика
     */
    public void setId_tenant(Integer id_tenant) {
        this.id_tenant = id_tenant;
    }
    /**
     * Функция получения значения поля {@link Flat#full_name}
     * @return возвращает ФИО квартиросъемщика
     */
    public String getFull_name() {return full_name;}
    /**
     * Функция изменения ФИО квартиросъемщика {@link Flat#full_name}
     * @param full_name ФИО квартиросъемщика
     */
    public void setFull_name(String full_name) {this.full_name = full_name;}
}
