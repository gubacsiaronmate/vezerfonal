package com.smokinggunstudio.vezerfonal.helpers

import java.lang.Byte
import java.lang.Double
import java.lang.Float
import java.lang.Long
import java.lang.Short
import kotlin.Any
import kotlin.Boolean
import kotlin.String
import kotlin.Suppress
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun Class<*>.isPrimitiveType(): Boolean =
    this.isPrimitive || this in setOf(
        String::class.java,
        java.lang.Boolean::class,
        Byte::class.java,
        Short::class.java,
        Integer::class.java,
        Long::class.java,
        Float::class.java,
        Double::class.java,
        Character::class.java
    )

fun <T: Any> T.toFlatString(): String =
    this::class.memberProperties.joinToString(" ") { property ->
        @Suppress("UNCHECKED_CAST")
        val prop = (property as KProperty1<T, *>).get(this)
        when (prop) {
            null -> ""
            is List<*> -> prop.joinToString(" ") {
                if (it == null) ""
                else if (it::class.java.isPrimitiveType()) it.toString()
                else it.toFlatString()
            }
            else -> if (prop::class.java.isPrimitiveType()) prop.toString() else prop.toFlatString()
        }
    }

fun <T: Any> T.containsInParameters(substring: String, ignoreCase: Boolean = true): Boolean {
    val searchableString = this.toFlatString()
    val searchQueries = substring.trim().split(" ")
    var result = 0
    for (query in searchQueries) {
        if (searchableString.contains(query, ignoreCase)) {
            if (searchQueries.last() == query) result++ else continue
        } else continue
    }
    
    return result > 0
}