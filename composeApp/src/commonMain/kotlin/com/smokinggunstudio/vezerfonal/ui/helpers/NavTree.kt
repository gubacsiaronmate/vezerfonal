package com.smokinggunstudio.vezerfonal.ui.helpers

sealed class NavTree(val hasParameters: Boolean) {
    abstract fun getParameter(): String
    
    data object Landing : NavTree(false) { override fun getParameter(): String = "" }
    data object Home : NavTree(false) { override fun getParameter(): String = "" }
    data object Register1 : NavTree(false) { override fun getParameter(): String = "" }
    data object Register2 : NavTree(false) { override fun getParameter(): String = "" }
    data object Register3 : NavTree(false) { override fun getParameter(): String = "" }
    data object CreateOrg : NavTree(false) { override fun getParameter(): String = "" }
    
    data class Test(val test: String) : NavTree(true) { override fun getParameter(): String = "/$test" }
}