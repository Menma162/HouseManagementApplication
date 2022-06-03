package com.housemanagement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Класс Проверка периода без свойств
 * Данный класс используется для проверки возможности передачи показания или начисления платежей
 * @author Автор Тюрина П.П.
 * @version 1.3
 */
public class CheckPeriod {
    /**
     * Функция получения значения возможности передачи показания или начисления платежей
     * @param period период
     * @return значение возможности передачи показания или начисления платежей
     */
    public static boolean check(String period){
        boolean can = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date dateF = new Date();
        String date = dateFormat.format(dateF);
        StringBuilder monthNowString = new StringBuilder();
        StringBuilder yearNow = new StringBuilder();
        StringBuilder yearPeriod = new StringBuilder();
        StringBuilder monthPeriod = new StringBuilder();
        int i = 0, monthPeriodInt = 0;
        while (i < date.length())
        {
            if (Character.isDigit(date.charAt(i)))
            {
                while (Character.isDigit(date.charAt(i)))
                {
                    yearNow.append(date.charAt(i));
                    i++;
                }
            }
            if (date.charAt(i) == '/') i++;
            if (Character.isDigit(date.charAt(i)))
            {
                while (Character.isDigit(date.charAt(i)))
                {
                    monthNowString.append(date.charAt(i));
                    i++;
                }
            }
            if (date.charAt(i) == '/') break;
        }
        int q = 0;
        while (q < period.length())
        {
            if (Character.isLetter(period.charAt(q)))
            {
                monthPeriod.append(period.charAt(q));
                q++;
            }
            else if (period.charAt(q) == ' ') q++;
            else if (Character.isDigit(period.charAt(q)))
            {
                yearPeriod.append(period.charAt(q));
                q++;
            }
        }
        int monthNow = Integer.parseInt(monthNowString.toString()), yearNowInt = Integer.parseInt(yearNow.toString()),
                yearPeriodInt = Integer.parseInt(yearPeriod.toString());
        switch (monthPeriod.toString()) {
            case "Январь" -> monthPeriodInt = 1;
            case "Февраль" -> monthPeriodInt = 2;
            case "Март" -> monthPeriodInt = 3;
            case "Апрель" -> monthPeriodInt = 4;
            case "Май" -> monthPeriodInt = 5;
            case "Июнь" -> monthPeriodInt = 6;
            case "Июль" -> monthPeriodInt = 7;
            case "Август" -> monthPeriodInt = 8;
            case "Сентябрь" -> monthPeriodInt = 9;
            case "Октябрь" -> monthPeriodInt = 10;
            case "Ноябрь" -> monthPeriodInt = 11;
            case "Декабрь" -> monthPeriodInt = 12;
        }
        if (monthPeriodInt == (monthNow - 1) && yearPeriod.toString().equals(yearNow.toString())) can = true;
        else if (monthNow == 1 && monthPeriodInt == 12 && yearPeriodInt == yearNowInt - 1) can =
                true;
        return !can;
    }
}
