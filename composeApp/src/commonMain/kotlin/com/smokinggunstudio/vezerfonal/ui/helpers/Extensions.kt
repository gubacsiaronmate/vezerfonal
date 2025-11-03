package com.smokinggunstudio.vezerfonal.ui.helpers

import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder

fun Navigator.go(node: NavTree) = navigate(node.route)

val NavTree.route: String
    get() = when (hasParameters) {
        true -> "/${this::class.simpleName!!.lowercase()}"
        false -> "/${this::class.simpleName!!.lowercase()}/${getParameter()}"
    }

fun RouteBuilder.screen(route: NavTree, content: ComposableContent) {
    scene(route.route) { content() }
}