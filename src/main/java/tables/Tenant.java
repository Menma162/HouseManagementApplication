package tables;

import java.sql.Date;

/**
 * Класс Квартиросъемщик со свойствами <b>id_tenant</b>, <b>full_name</b>, <b>date_of_registration</b>, <b>number_of_family_members</b> и <b>phone_number</b>.
 * <p>
 * Данный класс позволяет описать экземпляр квартиросъемщика.
 * Каждое из полей можно заполнить и изменить в дальнейшем. Класс предназначен для вывода
 * списка квартиросъемщиков, а также для работы с таблицей квартиросъемщиков в базе данных.
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class Tenant{
    /** Поле уникальный идентификатор квартиросъемщика*/
    private Integer id_tenant;
    /** Поле ФИО квартиросъемщика*/
    private String full_name;
    /** Поле дата регистрации квартиросъемщика*/
    private Date date_of_registration;
    /** Поле количество членов семьи квартиросъемщика*/
    private Integer number_of_family_members;
    /** Поле номера телефона квартиросъемщика*/
    private String phone_number;
    /**
     * Конструктор – создание нового экземпляра квартиросъемщика
     * @see Tenant#Tenant()
     */
    public Tenant(){ }
    /**
     * Функция получения значения поля {@link Tenant#full_name}
     * @return возвращает ФИО квартиросъемщика
     */
    public String getFull_name() {
        return full_name;
    }
    /**
     * Функция изменения ФИО квартиросъемщика {@link Tenant#full_name}
     * @param full_name ФИО квартиросъемщика
     */
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    /**
     * Функция получения значения поля {@link Tenant#date_of_registration}
     * @return возвращает дату регистрации квартиросъемщика
     */
    public Date getDate_of_registration() {
        return date_of_registration;
    }
    /**
     * Функция изменения даты регистрации квартиросъемщика {@link Tenant#date_of_registration}
     * @param date_of_registration дату регистрации квартиросъемщика
     */
    public void setDate_of_registration(Date date_of_registration) {this.date_of_registration = date_of_registration;}
    /**
     * Функция получения значения поля {@link Tenant#number_of_family_members}
     * @return возвращает количество членов семьи квартиросъемщика
     */
    public Integer getNumber_of_family_members() {return number_of_family_members;}
    /**
     * Функция изменения количества членов семьи квартиросъемщика {@link Tenant#number_of_family_members}
     * @param number_of_family_members количество членов семьи квартиросъемщика
     */
    public void setNumber_of_family_members(Integer number_of_family_members) {this.number_of_family_members = number_of_family_members;}
    /**
     * Функция получения значения поля {@link Tenant#phone_number}
     * @return возвращает номер телефона квартиросъемщика
     */
    public String getPhone_number() {
        return phone_number;
    }
    /**
     * Функция изменения номера телефона квартиросъемщика {@link Tenant#phone_number}
     * @param phone_number номер телефона квартиросъемщика
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    /**
     * Функция получения значения поля {@link Tenant#id_tenant}
     * @return возвращает уникальный идентификатор квартиросъемщика
     */
    public Integer getId_tenant() {
        return id_tenant;
    }
    /**
     * Функция изменения уникального идентификатора квартиросъемщика {@link Tenant#id_tenant}
     * @param id_tenant уникальный идентификатор квартиросъемщика
     */
    public void setId_tenant(Integer id_tenant) {
        this.id_tenant = id_tenant;
    }
}
