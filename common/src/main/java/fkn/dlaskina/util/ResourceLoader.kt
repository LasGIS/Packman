/**
 * @(#)ResourceLoader.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */
package fkn.dlaskina.util

import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.*

/**
 * The class for loading text resources from file.
 * @author VLaskin
 * @version 1.0
 */
object ResourceLoader {
    private val LOG = LoggerFactory.getLogger(ResourceLoader::class.java)

    /** Уровень вложенности.  */
    private const val DEPTH = 4

    /** признак чтения системных свойств.  */
    const val EXTEND_SYS_PROPERTIES = true
    /**
     * Вернуть уже загруженные глобальные свойства.
     * @return глобальные свойства
     */
    /** Global Properties.  */
    private val properties = loadProperties()

    /**
     * Loads properties.
     * @return properties
     */
    private fun loadProperties(): Properties {
        val props = if (EXTEND_SYS_PROPERTIES) {
            Properties(System.getProperties())
        } else {
            Properties()
        }
        for (resource in ResourceStrings.resources) {
            loadProperties(props, resource)
        }
        checkIntegrity(props)
        checkDepth(props)
        checkRecursion(props)
        return props
    }

    /**
     * Проверка на существование набора свойств.
     * @param props набор свойств
     */
    private fun checkIntegrity(props: Properties) {
        val stack: MutableList<String> = ArrayList()
        val keys: Enumeration<*> = props.keys()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement() as String
            if (!isResolved(props, key)) {
                stack.add(key)
            }
        }
        if (stack.size > 0) {
            throw MissingResourceException(
                "The definition(s) can't be recognized: $stack",
                "",
                ""
            )
        }
    }

    /**
     * Проверка рекурсии.
     * @param props набор свойств
     */
    private fun checkRecursion(props: Properties) {
        val stack: MutableList<String> = ArrayList()
        val keys: Enumeration<*> = props.keys()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement() as String
            if (hasRecursion(props, key)) {
                stack.add(key)
            }
        }
        if (stack.size > 0) {
            throw MissingResourceException(
                "Infinite loop for the definition(s): $stack", "", ""
            )
        }
    }

    /**
     *
     * @param props набор свойств
     */
    private fun checkDepth(props: Properties) {
        val stack: MutableList<String> = ArrayList()
        val keys: Enumeration<*> = props.keys()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement() as String
            if (exceedsDepth(props, key)) {
                stack.add(key)
            }
        }
        if (stack.size > 0) {
            throw MissingResourceException(
                "Depth exceeding for the definition(s): $stack", "", ""
            )
        }
    }

    /**
     * Проверка на существование уже загруженных свойств.
     * @param def уже загруженные свойства
     * @param over вновь загружаемые свойства
     */
    private fun checkOverriding(def: Properties, over: Properties) {
        val keys: Enumeration<*> = def.keys()
        val stack: MutableList<String> = ArrayList()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement() as String
            if (over.containsKey(key)) {
                if (stack.size < 4) {
                    stack.add(key)
                } else {
                    stack.add("...")
                    break
                }
            }
        }
        if (stack.size > 0) {
            throw MissingResourceException(
                "Overridden definitions: $stack",
                "",
                ""
            )
        }
    }

    /**
     * Читаем свойства в объект Properties def из файла под именем "resName".
     * @param def Properties объект, в который закачиваются свойства
     * @param resName имя файла со свойствами
     */
    private fun loadProperties(def: Properties, resName: String) {
        val ldr = ResourceLoader::class.java.classLoader
        val `in` = ldr.getResourceAsStream(resName)
        val props = Properties()
        try {
            props.load(`in`)
            `in`.close()
        } catch (e: Exception) {
            throw MissingResourceException(
                "Can't find bundle for $resName", "", ""
            )
        }
        checkOverriding(def, props)
        val keys: Enumeration<*> = props.keys()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement() as String
            def.setProperty(key, props.getProperty(key))
        }
    }

    /**
     * Function returns value of given property from file of properties.
     * @param key Property name
     * @return Property value
     */
    fun getResource(key: String): String {
        val res = replaceProperties(properties.getProperty(key))
        return res
            ?: throw MissingResourceException(
                "Can't find resources for bundle: $key",
                ResourceLoader::class.java.javaClass.name,
                key
            )
    }

    /**
     * Function returns value of given property from file of properties.
     * @param key Property name
     * @return Property value
     */
    fun getInteger(key: String): Int {
        return Integer.valueOf(getResource(key))
    }

    /**
     * Function returns value of given property from file of properties or null if property is not found.
     * @param key Property key
     * @return Property value
     */
    fun getResourceOrNull(key: String): String? {
        return try {
            getResource(key)
        } catch (ex: MissingResourceException) {
            null
        }
    } // getResourceOrNull(String): String

    /**
     * Replaces `${xxx}` style constructions in the given value
     * with the string value of the corresponding data types.
     *
     * @param value The string to be scanned for property references.
     * May be `null`, in which case this
     * method returns immediately with no effect.
     * @return the original string with the properties replaced, or
     * `null` if the original string is `null`.
     */
    private fun replaceProperties(value: String?): String? {
        if (value == null) {
            return null
        }
        val fragments = Vector<String?>()
        val refs = Vector<String>()
        parsePropertyString(value, fragments, refs)
        val sb = StringBuilder()
        val i: Enumeration<*> = fragments.elements()
        val j: Enumeration<*> = refs.elements()
        while (i.hasMoreElements()) {
            var fragment = i.nextElement() as String
            if (fragment == null) {
                val prop = j.nextElement() as String
                var replacement: Any?
                replacement = getResourceOrNull(prop)
                if (replacement == null) {
                    LOG.debug(
                        "Property \${$prop} has not been set"
                    )
                }
                fragment = replacement?.toString() ?: "\${$prop}"
            }
            sb.append(fragment)
        }
        return sb.toString()
    }

    /**
     * Parses a string containing `${xxx}` style property
     * references into two lists. The first list is a collection
     * of text fragments, while the other is a set of string property names.
     * `null` entries in the first list indicate a property
     * reference from the second list.
     *
     * It can be overridden with a more efficient or customized version.
     *
     * @param value     Text to parse. Must not be `null`.
     * @param fragments List to add text fragments to.
     * Must not be `null`.
     * @param refs List to add property names to.
     * Must not be `null`.
     */
    private fun parsePropertyString(
        value: String, fragments: Vector<String?>, refs: Vector<String>
    ) {
        var prev = 0
        var pos: Int
        while (value.indexOf("$", prev).also { pos = it } >= 0) {
            if (pos > 0) {
                fragments.addElement(value.substring(prev, pos))
            }
            prev = if (pos == value.length - 1) {
                fragments.addElement("$")
                pos + 1
            } else if (value[pos + 1] != '{') {
                if (value[pos + 1] == '$') {
                    fragments.addElement("$")
                    pos + 2
                } else {
                    fragments.addElement(value.substring(pos, pos + 2))
                    pos + 2
                }
            } else { //property found, extract its name or bail on a typo
                val endName = value.indexOf('}', pos)
                if (endName < 0) {
                    throw MissingResourceException(
                        "Syntax error in property: ",
                        ResourceLoader::class.java.name,
                        value
                    )
                }
                val propertyName = value.substring(pos + 2, endName)
                fragments.addElement(null)
                refs.addElement(propertyName)
                endName + 1
            }
        }
        //no more $ signs found
//if there is any tail to the file, append it
        if (prev < value.length) {
            fragments.addElement(value.substring(prev))
        }
    }
    /**
     *
     * @param msg
     */
//    private static void debug(String msg) {
//        System.out.println(msg);
//    }
//
    /**
     * Проверка рекурсии.
     * @param props набор свойств
     * @param key Property name
     * @return признак установленной рекурсии
     */
    private fun hasRecursion(props: Properties, key: String): Boolean {
        val stack = Stack<String>()
        return hasRecursion(props, stack, key)
    }

    /**
     * .
     * @param props набор свойств
     * @param key Property name
     * @return .
     */
    private fun exceedsDepth(props: Properties, key: String): Boolean {
        val stack = Stack<String>()
        return exceedsDepth(props, stack, key)
    }

    /**
     *
     * @param props набор свойств
     * @param key Property name
     * @return признак установленной рекурсии
     */
    private fun isResolved(props: Properties, key: String): Boolean {
        val fragments = Vector<String?>()
        val refs = Vector<String>()
        val `val` = props.getProperty(key)
        var res = true
        parsePropertyString(`val`, fragments, refs)
        val j: Enumeration<*> = refs.elements()
        while (res && j.hasMoreElements()) {
            val ref = j.nextElement() as String
            if (props.getProperty(ref) == null) {
                res = false
            }
        }
        return res
    }

    /**
     *
     * @param props набор свойств
     * @param stack вложенный стек свойств
     * @param key Property name
     * @return признак установленной рекурсии
     */
    private fun exceedsDepth(props: Properties, stack: Stack<String>, key: String): Boolean {
        if (stack.size > DEPTH) {
            return true
        }
        stack.push(key)
        val fragments = Vector<String?>()
        val refs = Vector<String>()
        val `val` = props.getProperty(key)
        var res = false
        parsePropertyString(`val`, fragments, refs)
        val j: Enumeration<*> = refs.elements()
        while (!res && j.hasMoreElements()) {
            val ref = j.nextElement() as String
            res = exceedsDepth(props, stack, ref)
            stack.pop()
        }
        return res
    }

    /**
     *
     * @param props набор свойств
     * @param stack стек рекурсивного вызова
     * @param key Property name
     * @return признак установленной рекурсии
     */
    private fun hasRecursion(props: Properties, stack: Stack<String>, key: String): Boolean {
        stack.push(key)
        val fragments = Vector<String?>()
        val refs = Vector<String>()
        val `val` = props.getProperty(key)
        var res = false
        parsePropertyString(`val`, fragments, refs)
        val j: Enumeration<*> = refs.elements()
        while (!res && j.hasMoreElements()) {
            val ref = j.nextElement() as String
            if (stack.contains(ref)) {
                res = true
            } else {
                res = hasRecursion(props, stack, ref)
                stack.pop()
            }
        }
        return res
    }

    /**
     * Читаем содержимое файла.
     * @param name Имя файла
     * @return содержимое файла как массив байт
     * @throws java.io.IOException IOException
     */
    @Throws(IOException::class)
    fun loadFile(name: String): ByteArray {
        val ldr = ResourceLoader::class.java.classLoader
        val `in` = ldr.getResourceAsStream(name)
        return if (`in` != null) {
            try {
                val sizefile = `in`.available()
                val imageData = ByteArray(sizefile)
                val sizeLoaded = `in`.read(imageData)
                if (sizeLoaded != sizefile) {
                    throw IOException(
                        "Число прочитанных байт(" + sizeLoaded
                                + ") не соответствует размеру файла \"" + name
                                + "\"(" + sizeLoaded
                                + ")!"
                    )
                }
                imageData
            } finally {
                `in`.close()
            }
        } else {
            throw IOException("Файл \"$name\" не найден!")
        }
    }
}
