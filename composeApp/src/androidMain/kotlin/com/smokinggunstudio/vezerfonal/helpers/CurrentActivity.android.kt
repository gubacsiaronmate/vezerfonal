package com.smokinggunstudio.vezerfonal.helpers

import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

internal object CurrentActivityProvider {
    private var ref: WeakReference<ComponentActivity>? = null

    var current: ComponentActivity?
        get() = ref?.get()
        set(value) {
            ref = if (value != null) WeakReference(value) else null
        }
}
