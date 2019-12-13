/*
 * @(#)SettingMenuItem.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.util

import java.awt.event.ActionListener

/**
 * Класс для предварительного заполнения параметров для создания меню.
 * @author VLaskin
 * @version 1.0
 * Date: 14.01.2005
 * Time: 15:19:18
 * @param text текст меню
 * @param image иконка меню
 * @param toolTip всплывающая подсказка
 * @param action действие на кнопку
 * @param items SettingMenuItem[] список подменю для данного меню
 */
class SettingMenuItem(
    var text: String? = null,
    var image: String? = null,
    var toolTip: String? = null,
    var action: ActionListener? = null,
    var items: Array<SettingMenuItem>? = null
)