package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.TagData

class TagSelectionState : SelectionState<TagData> {
    private var _allItems = emptyList<TagData>()
    override val allItems: List<TagData>
        get() = _allItems
    
    override var visibleItems: List<TagData> = allItems
    
    private val _tags = mutableStateOf<List<TagData>>(emptyList())
    override val selectedItems: List<TagData>
        get() = _tags.value
    
    override fun loadAllItems(items: List<TagData>) {
        _allItems = items
    }
    
    override fun addItem(item: TagData) {
        _tags.value += item
    }
    
    override fun removeItem(item: TagData) {
        _tags.value = _tags.value.filter { it != item }
    }
}