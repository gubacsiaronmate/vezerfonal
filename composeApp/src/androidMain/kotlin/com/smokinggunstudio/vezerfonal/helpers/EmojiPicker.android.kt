package com.smokinggunstudio.vezerfonal.helpers

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.text.BreakIterator

actual object EmojiPicker {
    @Composable
    actual fun Show(
        isVisible: Boolean,
        onEmojiSelected: (String) -> Unit,
        onRemove: () -> Unit,
        onDismiss: () -> Unit,
    ) {
        if (!isVisible) return
        
        val editTextRef = remember { mutableStateOf<EditText?>(null) }
        
        fun hideImeAndDismiss() {
            editTextRef.value?.let { et ->
                val imm = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(et.windowToken, 0)
                et.clearFocus()
            }
            onDismiss()
        }
    
        Dialog(
            onDismissRequest = { hideImeAndDismiss() }
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Pick emoji",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    @Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { ctx ->
                            EditText(ctx).apply {
                                hint = "Tap here and choose emoji from your keyboard"
                                isSingleLine = true
                                post {
                                    if (isAttachedToWindow) {
                                        requestFocus()
                                        val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                                    }
                                }
                            }.also { editTextRef.value = it }
                        },
                        update = { editText ->
                            editText.addTextChangedListener(object : TextWatcher {
                                var previous = ""
                                var isProcessing = false
                            
                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                    previous = s?.toString() ?: ""
                                }
                            
                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                    if (isProcessing) return
                                    isProcessing = true
                                
                                    val now = s?.toString() ?: ""
                                    if (now.length > previous.length) {
                                        val lastCluster = run {
                                            if (now.isEmpty()) return@run ""
                                        
                                            val it = BreakIterator.getCharacterInstance()
                                            it.setText(now)
                                            val end = it.last()
                                            val startIdx = it.previous()
                                            if (startIdx != BreakIterator.DONE && end != BreakIterator.DONE &&
                                                startIdx >= 0 && end <= now.length && startIdx < end
                                            ) {
                                                now.substring(startIdx, end)
                                            } else {
                                                if (now.isNotEmpty()) now.last().toString() else ""
                                            }
                                        }
                                        if (lastCluster.isNotEmpty()) {
                                            onEmojiSelected(lastCluster)
                                        }
                                        editText.windowToken?.let { token ->
                                            val imm =
                                                editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                            imm.hideSoftInputFromWindow(token, 0)
                                        }
                                    } else if (now.length < previous.length) {
                                        onRemove()
                                        editText.windowToken?.let { token ->
                                            val imm =
                                                editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                            imm.hideSoftInputFromWindow(token, 0)
                                        }
                                    }
                                
                                    isProcessing = false
                                }
                            
                                override fun afterTextChanged(s: Editable?) { /* no-op */ }
                            })
                        }
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { hideImeAndDismiss() }) {
                            Text("Close")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            onRemove()
                            hideImeAndDismiss()
                        }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}
