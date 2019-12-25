/*
 * @(#)LGFilenameFilter.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.util

import java.io.File
import java.io.FilenameFilter

/**
 * Created by IntelliJ IDEA.
 * @author vlaskin
 * @version 1.0
 * @since 14.09.2005 17:23:43
 */
class LGFilenameFilter(aMask: String) : FilenameFilter {
    /** расширение файла.  */
    private var ext: String? = null

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param   dir    the directory in which the file was found.
     * @param   name   the name of the file.
     * @return  `true` if and only if the name should be
     * included in the file list; `false` otherwise.
     */
    override fun accept(dir: File, name: String): Boolean {
        if (dir.isDirectory) {
            return false
        }
        if (ext != null) {
            val fileName = dir.name
            var relExt: String? = null
            val lastPnt = fileName.lastIndexOf('.')
            if (lastPnt > 0) {
                relExt = fileName.substring(lastPnt)
            }
            if (relExt != null && relExt.equals(ext, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Constructor.
     * @param aMask маска файла
     */
    init {
        val lastPnt = aMask.lastIndexOf('.')
        if (lastPnt > 0) {
            ext = aMask.substring(lastPnt)
        }
    }
}