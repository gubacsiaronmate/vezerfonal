package com.smokinggunstudio.vezerfonal

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.smokinggunstudio.vezerfonal.helpers.CurrentActivityProvider
import com.smokinggunstudio.vezerfonal.helpers.security.CurrentContextProvider
import com.smokinggunstudio.vezerfonal.ui.theme.Black
import com.smokinggunstudio.vezerfonal.ui.theme.White
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.close
import vezerfonal.composeapp.generated.resources.leave
import vezerfonal.composeapp.generated.resources.perm_not_granted
import kotlin.properties.Delegates
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        CurrentActivityProvider.current = this
        CurrentContextProvider.current = this
        
        val perm = checkSelfPermission(POST_NOTIFICATIONS)
        val permissionGranted = perm == PackageManager.PERMISSION_GRANTED
        
        if (!permissionGranted)
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(POST_NOTIFICATIONS),
                    1
                )
        
        setContent {
            var permGranted by remember { mutableStateOf(permissionGranted) }
            
            ActivityCompat.OnRequestPermissionsResultCallback { requestCode, permissions, grantResults ->
                if (requestCode != 1 && !permissions.contains(POST_NOTIFICATIONS))
                    return@OnRequestPermissionsResultCallback
                
                permGranted = with(grantResults) {
                    val notifPerm = this[permissions.indexOf(POST_NOTIFICATIONS)]
                    notifPerm == PackageManager.PERMISSION_GRANTED
                }
            }
            
            if (permGranted) App()
            else Surface(Modifier.fillMaxSize().background(Black)) {
                Dialog({ exitProcess(0) }) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(Res.string.perm_not_granted),
                            color = White,
                            textAlign = TextAlign.Center
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton({
                                exitProcess(0)
                            }) { Text(stringResource(Res.string.leave), color = White) }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (CurrentActivityProvider.current === this)
            CurrentActivityProvider.current = null
        
        super.onDestroy()
    }
}