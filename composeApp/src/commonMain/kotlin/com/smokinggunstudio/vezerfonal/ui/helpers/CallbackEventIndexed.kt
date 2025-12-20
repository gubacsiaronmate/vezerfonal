package com.smokinggunstudio.vezerfonal.ui.helpers

fun interface CallbackEventIndexed<T> { operator fun invoke(index: Int, returns: T) }