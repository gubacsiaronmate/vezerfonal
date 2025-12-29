package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.ui.state.model.SelectionStateModel
import kotlin.collections.plus

abstract class SelectionStateController<T>(initial: SelectionStateModel<T>) {
    private val _allItems = mutableStateOf(initial.allItems)
    val allItems: List<T> get() = _allItems.value
    
    private val _visibleItems: MutableState<List<T>> = mutableStateOf(allItems)
    val visibleItems: List<T> get() = _visibleItems.value
    
    private val _selectedItems = mutableStateOf(initial.selectedItems)
    val selectedItems: Set<T> get() = _selectedItems.value
    
    fun loadAllItems(items: List<T>) {
        _allItems.value = items
        _visibleItems.value = _visibleItems.value.toSet().intersect(items.toSet()).toList()
    }
    
    fun addItem(item: T) {
        _selectedItems.value += item
    }
    
    fun removeItem(item: T) {
        _selectedItems.value = _selectedItems.value.filter { it != item }.toSet()
    }
    
    fun search(query: String) {
        _visibleItems.value = allItems.filter { it.toString().contains(query, ignoreCase = true) }
    }
    
    fun clear() {
        _visibleItems.value = allItems
        _selectedItems.value = emptySet()
    }
    
    abstract fun snapshot(): SelectionStateModel<T>
}