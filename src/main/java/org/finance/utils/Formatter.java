package org.finance.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {
    public static String doubleToReal(double valor) {
        Locale brasil = Locale.of("pt", "BR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(brasil);
        return currencyFormatter.format(valor);
    }
}
