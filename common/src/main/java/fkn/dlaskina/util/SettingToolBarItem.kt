/*
 * @(#)SettingToolBarItem.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.util

import java.awt.event.ActionListener

/**
 * Класс для предварительного заполнения параметров для создания панели
 * инструментов.
 * @author VLaskin
 * @version 1.0
 * Date: 17.01.2005
 * Time: 11:55:05
 */
/**
 * Создаём новую кнопку.
 * @param text текст на кнопке
 * @param image изображение на кнопке
 * @param toolTip всплывающая подсказка
 * @param width ширина кнопки
 * @param height высота кнопки
 * @param action some aAction events
 */
class SettingToolBarItem(
    val text: String? = null,
    val image: String? = null,
    val toolTip: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val action: ActionListener? = null
)