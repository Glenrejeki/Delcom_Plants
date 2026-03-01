package org.delcom.pam_p4_ifs23024.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23024.R
import org.delcom.pam_p4_ifs23024.helper.ConstHelper
import org.delcom.pam_p4_ifs23024.helper.RouteHelper
import org.delcom.pam_p4_ifs23024.helper.ToolsHelper
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodyData
import org.delcom.pam_p4_ifs23024.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23024.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23024.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodiesUIState
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyViewModel

@Composable
fun CelestialBodiesScreen(
    navController: NavHostController,
    celestialBodyViewModel: CelestialBodyViewModel
) {
    val uiState by celestialBodyViewModel.uiState.collectAsState()

    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var celestialBodies by remember { mutableStateOf<List<ResponseCelestialBodyData>>(emptyList()) }

    fun fetchData() {
        isLoading = true
        celestialBodyViewModel.getAllCelestialBodies(searchQuery.text)
    }

    LaunchedEffect(Unit) { fetchData() }

    LaunchedEffect(uiState.celestialBodies) {
        if (uiState.celestialBodies !is CelestialBodiesUIState.Loading) {
            isLoading = false
            celestialBodies = if (uiState.celestialBodies is CelestialBodiesUIState.Success)
                (uiState.celestialBodies as CelestialBodiesUIState.Success).data
            else emptyList()
        }
    }

    if (isLoading) { LoadingUI(); return }

    fun onOpen(celestialBodyId: String) {
        RouteHelper.to(
            navController = navController,
            destination = ConstHelper.RouteNames.CelestialBodyDetail.path
                .replace("{celestialBodyId}", celestialBodyId)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Benda Langit",
            showBackButton = false,
            withSearch = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchAction = { fetchData() }
        )
        Box(modifier = Modifier.weight(1f)) {
            CelestialBodiesListUI(celestialBodies = celestialBodies, onOpen = ::onOpen)

            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = {
                        RouteHelper.to(navController, ConstHelper.RouteNames.CelestialBodyAdd.path)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Benda Langit")
                }
            }
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun CelestialBodiesListUI(
    celestialBodies: List<ResponseCelestialBodyData>,
    onOpen: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(celestialBodies) { celestialBody ->
            CelestialBodyItemUI(celestialBody, onOpen)
        }
    }

    if (celestialBodies.isEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Tidak ada data benda langit!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun CelestialBodyItemUI(
    celestialBody: ResponseCelestialBodyData,
    onOpen: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onOpen(celestialBody.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = ToolsHelper.getCelestialBodyImageUrl(celestialBody.id),
                contentDescription = celestialBody.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .size(70.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = celestialBody.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = celestialBody.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewCelestialBodiesListUI() {
    // Preview placeholder
}
