package com.smokinggunstudio.vezerfonal.ui.state

@Deprecated(
    message = "Redone",
    level = DeprecationLevel.ERROR
)
interface SelectionState<T> {
    val allItems: List<T>
    
    var visibleItems: List<T>
    
    val selectedItems: List<T>

    fun loadAllItems(items: List<T>)
    
    fun addItem(item: T)
    
    fun removeItem(item: T)
}