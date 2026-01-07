package com.smokinggunstudio.vezerfonal.ui.helpers

fun interface CallbackEvent<T> { operator fun invoke(returns: T) }