package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.UserData

class UserSelectionState : SelectionState<UserData> {
    private var _allItems: List<UserData> = emptyList()
    override val allItems: List<UserData>
        get() = _allItems
    
    override var visibleItems: List<UserData> = allItems
    
    private val _users = mutableStateOf<List<UserData>>(emptyList())
    override val selectedItems: List<UserData>
        get() = _users.value
    
    override fun loadAllItems(items: List<UserData>) {
        _allItems = items
    }
    
    override fun addItem(item: UserData) {
        _users.value += item
    }
    
    override fun removeItem(item: UserData) {
        _users.value = _users.value.filter { it != item }
    }
}