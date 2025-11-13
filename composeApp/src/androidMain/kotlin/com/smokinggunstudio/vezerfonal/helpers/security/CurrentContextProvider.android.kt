package com.smokinggunstudio.vezerfonal.helpers.security

import android.content.Context
import java.lang.ref.WeakReference

internal object CurrentContextProvider {
    private var context: WeakReference<Context>? = null
    
    var current: Context?
        get() = context?.get()
        set(value) {
            context = if (value != null) WeakReference(value) else null
        }
}