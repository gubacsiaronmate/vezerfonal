package com.smokinggunstudio.vezerfonal.ui.helpers

interface SuspendCallbackClickEvent<T> { suspend operator fun invoke(returns: T) }