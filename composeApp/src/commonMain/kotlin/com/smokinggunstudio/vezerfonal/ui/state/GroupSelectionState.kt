package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.GroupData

class GroupSelectionState : SelectionState<GroupData> {
    private var _allItems: List<GroupData> = emptyList()
    override val allItems: List<GroupData>
        get() = _allItems
    
    override var visibleItems: List<GroupData> = allItems
    
    private val _groups = mutableStateOf<List<GroupData>>(emptyList())
    override val selectedItems: List<GroupData>
        get() = _groups.value
    
    override fun loadAllItems(items: List<GroupData>) {
        _allItems = items
    }
    
    override fun addItem(item: GroupData) {
        _groups.value += item
    }
    
    override fun removeItem(item: GroupData) {
        _groups.value = _groups.value.filter { it != item }
    }
}