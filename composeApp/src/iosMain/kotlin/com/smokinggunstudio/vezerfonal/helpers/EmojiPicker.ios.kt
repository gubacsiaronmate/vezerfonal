package com.smokinggunstudio.vezerfonal.helpers

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSRange
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual object EmojiPicker {
    @OptIn(ExperimentalForeignApi::class)
    @Composable
    actual fun Show(
        isVisible: Boolean,
        onEmojiSelected: (String) -> Unit,
        onRemove: () -> Unit,
        onDismiss: () -> Unit,
    ) {
        if (!isVisible) return
        
        val tfRef = remember { mutableStateOf<UITextField?>(null) }
        val delegateRef = remember { mutableStateOf<NSObject?>(null) }
        
        fun hideImeAndDismiss() {
            tfRef.value?.resignFirstResponder()
            tfRef.value?.setDelegate(null)
            delegateRef.value = null
            tfRef.value = null
            onDismiss()
        }
        
        AlertDialog(
            onDismissRequest = { hideImeAndDismiss() },
            title = { Text("Pick emoji") },
            text = {
                UIKitView(
                    modifier = Modifier,
                    factory = {
                        val textField = UITextField().apply {
                            placeholder = "Tap here and choose emoji from your keyboard"
                            borderStyle = platform.UIKit.UITextBorderStyle.UITextBorderStyleRoundedRect
                        }
                        val delegate = object : NSObject(), UITextFieldDelegateProtocol {
                            override fun textField(
                                textField: UITextField,
                                shouldChangeCharactersInRange: CValue<NSRange>,
                                replacementString: String
                            ): Boolean {
                                if (replacementString.isEmpty()) {
                                    onRemove()
                                    textField.text = ""
                                } else {
                                    // Better emoji validation
                                    val emojiOnly = replacementString.all { char ->
                                        char.isSupplementaryCodePoint() || 
                                        char.code in 0x1F300..0x1F9FF ||
                                        char.code in 0x2600..0x26FF
                                    }
                                    if (emojiOnly || replacementString.length <= 10) {
                                        onEmojiSelected(replacementString)
                                        textField.text = replacementString
                                    }
                                }
                                return false
                            }
                        }
                        delegateRef.value = delegate
                        textField.setDelegate(delegate)
                        tfRef.value = textField
                        
                        // Safely open keyboard with null check
                        dispatch_async(dispatch_get_main_queue()) {
                            if (tfRef.value != null) {
                                textField.becomeFirstResponder()
                            }
                        }
                        
                        textField as UIView
                    },
                    update = { view ->
                        // Ensure keyboard shows if view is reused
                        (view as? UITextField)?.becomeFirstResponder()
                    },
                    onRelease = { view ->
                        // Proper cleanup when the view is released
                        (view as? UITextField)?.let { tf ->
                            tf.resignFirstResponder()
                            tf.setDelegate(null)
                        }
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRemove()
                    hideImeAndDismiss()
                }) { Text("Remove") }
            },
            dismissButton = {
                TextButton(onClick = { hideImeAndDismiss() }) { Text("Close") }
            }
        )
    }
}

private fun Char.isSupplementaryCodePoint(): Boolean {
    return this.code > 0xFFFF
}
