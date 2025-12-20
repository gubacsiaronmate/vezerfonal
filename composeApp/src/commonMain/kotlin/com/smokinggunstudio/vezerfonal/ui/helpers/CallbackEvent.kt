package com.smokinggunstudio.vezerfonal.ui.helpers

import kotlinx.serialization.Serializable

@Serializable
fun interface CallbackEvent<T> { operator fun invoke(returns: T) }