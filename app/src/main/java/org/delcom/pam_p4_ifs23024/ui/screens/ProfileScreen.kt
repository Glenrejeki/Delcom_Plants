package org.delcom.pam_p4_ifs23024.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23024.R
import org.delcom.pam_p4_ifs23024.helper.ToolsHelper
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseProfile
import org.delcom.pam_p4_ifs23024.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23024.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23024.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyViewModel
import org.delcom.pam_p4_ifs23024.ui.viewmodels.ProfileUIState

@Composable
fun ProfileScreen(
    navController: NavHostController,
    celestialBodyViewModel: CelestialBodyViewModel
) {
    val uiState by celestialBodyViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        celestialBodyViewModel.getProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Profil Pengembang", showBackButton = false)
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = uiState.profile) {
                is ProfileUIState.Loading -> LoadingUI()
                is ProfileUIState.Success -> ProfileUI(profile = state.data)
                is ProfileUIState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
        
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun ProfileUI(profile: ResponseProfile) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = ToolsHelper.getProfilePhotoUrl(),
            contentDescription = profile.nama,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder),
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = profile.nama,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "@${profile.username}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tentang Saya",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.tentang,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
