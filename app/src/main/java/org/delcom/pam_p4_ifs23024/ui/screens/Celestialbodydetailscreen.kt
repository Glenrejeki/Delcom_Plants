package org.delcom.pam_p4_ifs23024.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23024.R
import org.delcom.pam_p4_ifs23024.helper.ConstHelper
import org.delcom.pam_p4_ifs23024.helper.RouteHelper
import org.delcom.pam_p4_ifs23024.helper.SuspendHelper
import org.delcom.pam_p4_ifs23024.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23024.helper.ToolsHelper
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodyData
import org.delcom.pam_p4_ifs23024.ui.components.BottomDialog
import org.delcom.pam_p4_ifs23024.ui.components.BottomDialogType
import org.delcom.pam_p4_ifs23024.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23024.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23024.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23024.ui.components.TopAppBarMenuItem
import org.delcom.pam_p4_ifs23024.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyActionUIState
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyUIState
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyViewModel

@Composable
fun CelestialBodyDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    celestialBodyViewModel: CelestialBodyViewModel,
    celestialBodyId: String
) {
    val uiState by celestialBodyViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }
    var celestialBody by remember { mutableStateOf<ResponseCelestialBodyData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        celestialBodyViewModel.getCelestialBodyById(celestialBodyId)
    }

    LaunchedEffect(uiState.celestialBody) {
        if (uiState.celestialBody !is CelestialBodyUIState.Loading) {
            if (uiState.celestialBody is CelestialBodyUIState.Success) {
                celestialBody = (uiState.celestialBody as CelestialBodyUIState.Success).data
                isLoading = false
            } else if (uiState.celestialBody is CelestialBodyUIState.Error) {
                isLoading = false
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.ERROR, (uiState.celestialBody as CelestialBodyUIState.Error).message)
                RouteHelper.back(navController)
            }
        }
    }

    fun onDelete() {
        isLoading = true
        celestialBodyViewModel.deleteCelestialBody(celestialBodyId)
    }

    LaunchedEffect(uiState.celestialBodyAction) {
        when (val state = uiState.celestialBodyAction) {
            is CelestialBodyActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.SUCCESS, "Berhasil menghapus data")
                RouteHelper.to(navController, ConstHelper.RouteNames.CelestialBodies.path, true)
                isLoading = false
            }
            is CelestialBodyActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.ERROR, state.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading || celestialBody == null) { LoadingUI(); return }

    val detailMenuItems = listOf(
        TopAppBarMenuItem(
            text = "Ubah Data",
            icon = Icons.Filled.Edit,
            route = null,
            onClick = {
                RouteHelper.to(
                    navController,
                    ConstHelper.RouteNames.CelestialBodyEdit.path
                        .replace("{celestialBodyId}", celestialBody!!.id)
                )
            }
        ),
        TopAppBarMenuItem(
            text = "Hapus Data",
            icon = Icons.Filled.Delete,
            route = null,
            onClick = { isConfirmDelete = true }
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = celestialBody!!.nama,
            showBackButton = true,
            customMenuItems = detailMenuItems
        )
        Box(modifier = Modifier.weight(1f)) {
            CelestialBodyDetailUI(celestialBody = celestialBody!!)
            BottomDialog(
                type = BottomDialogType.ERROR,
                show = isConfirmDelete,
                onDismiss = { isConfirmDelete = false },
                title = "Konfirmasi Hapus Data",
                message = "Apakah Anda yakin ingin menghapus data benda langit ini?",
                confirmText = "Ya, Hapus",
                onConfirm = { onDelete() },
                cancelText = "Batal",
                destructiveAction = true
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun CelestialBodyDetailUI(celestialBody: ResponseCelestialBodyData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            AsyncImage(
                model = ToolsHelper.getCelestialBodyImageUrl(celestialBody.id),
                contentDescription = celestialBody.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = celestialBody.nama,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        SectionCard(title = "Deskripsi", content = celestialBody.deskripsi)
        SectionCard(title = "Manfaat", content = celestialBody.manfaat)
        SectionCard(title = "Fakta Menarik", content = celestialBody.faktaMenarik)
    }
}

@Composable
private fun SectionCard(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewCelestialBodyDetailUI() {
    DelcomTheme { }
}
