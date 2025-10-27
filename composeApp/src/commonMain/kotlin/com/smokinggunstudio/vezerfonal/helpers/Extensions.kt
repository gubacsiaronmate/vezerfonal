package com.smokinggunstudio.vezerfonal.helpers

import moe.tlaster.precompose.navigation.Navigator

fun Navigator.goTo(node: NavTree) = navigate(node.route)