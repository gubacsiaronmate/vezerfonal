package com.smokinggunstudio.vezerfonal.ui.state.model

interface SelectionStateModel<out T> {
    val allItems: List<T>
    
    val visibleItems: List<T>
    
    val selectedItems: Set<T>
}