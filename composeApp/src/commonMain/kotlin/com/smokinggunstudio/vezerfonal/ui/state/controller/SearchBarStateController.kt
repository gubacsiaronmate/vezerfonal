package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.ui.state.model.SearchBarStateModel

class SearchBarStateController(initial: SearchBarStateModel) {
    private val _query = mutableStateOf(initial.query)
    val query: String get() = _query.value
    
    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }
    
    fun snapshot() = SearchBarStateModel(query)
}