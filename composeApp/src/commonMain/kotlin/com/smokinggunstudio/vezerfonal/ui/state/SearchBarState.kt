package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

@Deprecated(
    message = "Redone",
    level = DeprecationLevel.ERROR
)
class SearchBarState {
    private val _query = mutableStateOf("")
    val query: String get() = _query.value
    
    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }
}