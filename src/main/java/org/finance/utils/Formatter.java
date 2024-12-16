package org.finance.utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatter {
    public static String doubleToReal(double valor) {
        Locale brasil = Locale.of("pt", "BR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(brasil);
        return currencyFormatter.format(valor);
    }

    public static String doubleToPorcento(double valor){
        valor = (valor * 100);
        String formatted = String.format("%.2f", valor);
        formatted = formatted.replace('.', ',');
        return formatted + "%";
    }

    public static LocalDateTime stringToLocalDateTime(String data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(data, formatter).atStartOfDay();
    }
}
