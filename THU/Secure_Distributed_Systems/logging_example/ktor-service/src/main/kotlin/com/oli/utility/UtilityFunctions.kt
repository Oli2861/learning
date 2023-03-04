package com.oli.utility

import org.apache.commons.text.StringEscapeUtils
import com.oli.utility.MaskingPatternLayout

fun sanitizeLogEntry(string: String): String? {
    // Escape special string characters such as \n
    val sanitizedString = StringEscapeUtils.escapeJava(string)
    return sanitizedString
}