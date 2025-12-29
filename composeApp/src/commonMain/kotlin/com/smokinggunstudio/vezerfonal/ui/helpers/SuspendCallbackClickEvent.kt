package com.smokinggunstudio.vezerfonal.ui.helpers

fun interface SuspendCallbackClickEvent<T> { suspend operator fun invoke(returns: T) }