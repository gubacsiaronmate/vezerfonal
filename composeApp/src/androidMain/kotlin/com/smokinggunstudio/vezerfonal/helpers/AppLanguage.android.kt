package com.smokinggunstudio.vezerfonal.helpers

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.smokinggunstudio.vezerfonal.helpers.security.CurrentContextProvider

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
actual fun applyLanguage(tag: String) {
    val context = CurrentContextProvider.current ?: return
    context.getSystemService(LocaleManager::class.java)
        .applicationLocales = LocaleList.forLanguageTags(tag)
}
