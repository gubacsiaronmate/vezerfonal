package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.TagData

@Deprecated(
    message = "Redone",
    level = DeprecationLevel.ERROR
)
class TagSelectionState : SelectionState<TagData> {
    private var _allItems = mutableStateOf<List<TagData>>(emptyList())
    override val allItems: List<TagData> get() = _allItems.value
    
    override var visibleItems: List<TagData> = allItems
    
    private val _tags = mutableStateOf<List<TagData>>(emptyList())
    override val selectedItems: List<TagData> get() = _tags.value
    
    override fun loadAllItems(items: List<TagData>) {
        _allItems.value = items
        visibleItems = items
    }
    
    override fun addItem(item: TagData) {
        _tags.value += item
    }
    
    override fun removeItem(item: TagData) {
        _tags.value = _tags.value.filter { it != item }
    }
    
    fun clear() {
        visibleItems = allItems
        _tags.value = emptyList()
    }
}