package me.BlockDynasty.Economy.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;



public class DecimalUtils {

    /**
     * Redondea un valor decimal al número de lugares especificado.
     *
     * @param value  el número a redondear.
     * @param places el número de lugares decimales a mantener.
     * @return el valor redondeado.
     * @throws IllegalArgumentException si el número de lugares es negativo.
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException("Decimal places must be non-negative.");
        double factor = Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }

//para aumento de precision usar este caso
    /*public static double round(double value, int places) {
        //if (places < 0) throw new IllegalArgumentException("Decimal places must be non-negative.");
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }*/
}