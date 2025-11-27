package com.smokinggunstudio.vezerfonal.helpers

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

interface Modifiable<T> {
    val table: Table
    val id: Column<T>
}