package org.delcom.pam_p4_ifs23024.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23024.helper.ConstHelper
import org.delcom.pam_p4_ifs23024.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23024.ui.screens.CelestialBodiesScreen
import org.delcom.pam_p4_ifs23024.ui.screens.CelestialBodyAddScreen
import org.delcom.pam_p4_ifs23024.ui.screens.CelestialBodyDetailScreen
import org.delcom.pam_p4_ifs23024.ui.screens.CelestialBodyEditScreen
import org.delcom.pam_p4_ifs23024.ui.screens.HomeScreen
import org.delcom.pam_p4_ifs23024.ui.screens.ProfileScreen
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    celestialBodyViewModel: CelestialBodyViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                CustomSnackbar(snackbarData, onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() })
            }
        },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
        ) {
            // Home
            composable(route = ConstHelper.RouteNames.Home.path) {
                HomeScreen(navController = navController)
            }

            // Profile
            composable(route = ConstHelper.RouteNames.Profile.path) {
                ProfileScreen(
                    navController = navController,
                    celestialBodyViewModel = celestialBodyViewModel
                )
            }

            // Celestial Bodies List
            composable(route = ConstHelper.RouteNames.CelestialBodies.path) {
                CelestialBodiesScreen(
                    navController = navController,
                    celestialBodyViewModel = celestialBodyViewModel
                )
            }

            // Celestial Body Add
            composable(route = ConstHelper.RouteNames.CelestialBodyAdd.path) {
                CelestialBodyAddScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    celestialBodyViewModel = celestialBodyViewModel
                )
            }

            // Celestial Body Detail
            composable(
                route = ConstHelper.RouteNames.CelestialBodyDetail.path,
                arguments = listOf(navArgument("celestialBodyId") { type = NavType.StringType })
            ) { backStackEntry ->
                val celestialBodyId = backStackEntry.arguments?.getString("celestialBodyId") ?: ""
                CelestialBodyDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    celestialBodyViewModel = celestialBodyViewModel,
                    celestialBodyId = celestialBodyId
                )
            }

            // Celestial Body Edit
            composable(
                route = ConstHelper.RouteNames.CelestialBodyEdit.path,
                arguments = listOf(navArgument("celestialBodyId") { type = NavType.StringType })
            ) { backStackEntry ->
                val celestialBodyId = backStackEntry.arguments?.getString("celestialBodyId") ?: ""
                CelestialBodyEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    celestialBodyViewModel = celestialBodyViewModel,
                    celestialBodyId = celestialBodyId
                )
            }
        }
    }
}
