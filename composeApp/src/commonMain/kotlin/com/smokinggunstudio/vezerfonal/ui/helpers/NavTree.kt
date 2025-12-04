package com.smokinggunstudio.vezerfonal.ui.helpers

sealed class NavTree(val hasParameters: Boolean) {
    abstract fun getParameter(): String
    
    data object Landing : NavTree(false) { override fun getParameter(): String = "" }
    data object Home : NavTree(false) { override fun getParameter(): String = "" }
    data class Register(val page: Int) : NavTree(true) { override fun getParameter(): String = "$page" }
    data object CreateOrg : NavTree(false) { override fun getParameter(): String = "" }
    data object Login : NavTree(false) { override fun getParameter(): String = "" }
    data object AccountSettings : NavTree(false) { override fun getParameter(): String = "" }
    data object AdminTools : NavTree(false) { override fun getParameter(): String = "" }
    data object RegCodeManagement : NavTree(false) { override fun getParameter(): String = "" }
    data object TagManagement : NavTree(false) { override fun getParameter(): String = "" }
    data object UserManagement : NavTree(false) { override fun getParameter(): String = "" }
    data object Test : NavTree(false) { override fun getParameter(): String = "" }
    data object ChangePassword : NavTree(false) { override fun getParameter(): String = "" }
}