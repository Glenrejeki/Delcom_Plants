package org.delcom.pam_p4_ifs23024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.delcom.pam_p4_ifs23024.ui.UIApp
import org.delcom.pam_p4_ifs23024.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val celestialBodyViewModel: CelestialBodyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelcomTheme {
                UIApp(celestialBodyViewModel = celestialBodyViewModel)
            }
        }
    }
}