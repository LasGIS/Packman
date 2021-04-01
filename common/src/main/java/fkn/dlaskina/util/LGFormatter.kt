/*
 * @(#)LGFormatter.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * The Class LGFormatter. Преобразование десятичного числа или даты в строку.
 *
 * @author Laskin
 * @version 1.0
 * @since 02.01.13 23:48
 */
object LGFormatter {
    /**
     * Полное число с секундами.
     */
    private val DATE_FORMAT = SimpleDateFormat("DD HH:mm:ss")
    /**
     * Формат даты, используемый для логгирования.
     */
    private val LOG_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SS")
    /**
     * Вывод десятичного числа.
     */
    private val DECIMAL_FORMAT: NumberFormat = DecimalFormat(
        "###0.000", DecimalFormatSymbols(Locale.ENGLISH)
    )

    /**
     * Форматированный вывод десятичного числа.
     * @param val значение для вывода
     * @return строка
     */
    fun format(`val`: Double): String {
        var ret = DECIMAL_FORMAT.format(`val`)
        val ind = ret.lastIndexOf(".000")
        if (ind > -1) {
            ret = ret.substring(0, ind)
        }
        return ret
    }

    /**
     * Форматированный вывод десятичного числа.
     * @param val значение для вывода
     * @return строка
     */
    fun formatLog(`val`: Double): String {
        return DECIMAL_FORMAT.format(`val`)
    }

    init {
        DATE_FORMAT.timeZone = TimeZone.getTimeZone("GMT")
    }
}
