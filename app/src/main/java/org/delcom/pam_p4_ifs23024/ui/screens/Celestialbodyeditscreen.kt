package org.delcom.pam_p4_ifs23024.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23024.R
import org.delcom.pam_p4_ifs23024.helper.AlertHelper
import org.delcom.pam_p4_ifs23024.helper.AlertState
import org.delcom.pam_p4_ifs23024.helper.AlertType
import org.delcom.pam_p4_ifs23024.helper.ConstHelper
import org.delcom.pam_p4_ifs23024.helper.RouteHelper
import org.delcom.pam_p4_ifs23024.helper.SuspendHelper
import org.delcom.pam_p4_ifs23024.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23024.helper.ToolsHelper
import org.delcom.pam_p4_ifs23024.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23024.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodyData
import org.delcom.pam_p4_ifs23024.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23024.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23024.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyActionUIState
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyUIState
import org.delcom.pam_p4_ifs23024.ui.viewmodels.CelestialBodyViewModel

@Composable
fun CelestialBodyEditScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    celestialBodyViewModel: CelestialBodyViewModel,
    celestialBodyId: String
) {
    val uiState by celestialBodyViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
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
            } else {
                RouteHelper.back(navController)
                isLoading = false
            }
        }
    }

    fun onSave(
        context: Context,
        nama: String,
        deskripsi: String,
        manfaat: String,
        faktaMenarik: String,
        file: Uri? = null
    ) {
        isLoading = true
        var filePart: MultipartBody.Part? = null
        if (file != null) filePart = uriToMultipart(context, file, "file")

        celestialBodyViewModel.putCelestialBody(
            celestialBodyId = celestialBodyId,
            nama = nama.toRequestBodyText(),
            deskripsi = deskripsi.toRequestBodyText(),
            manfaat = manfaat.toRequestBodyText(),
            faktaMenarik = faktaMenarik.toRequestBodyText(),
            file = filePart
        )
    }

    LaunchedEffect(uiState.celestialBodyAction) {
        when (val state = uiState.celestialBodyAction) {
            is CelestialBodyActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.SUCCESS, "Berhasil mengubah data")
                RouteHelper.to(
                    navController = navController,
                    destination = ConstHelper.RouteNames.CelestialBodyDetail.path
                        .replace("{celestialBodyId}", celestialBodyId),
                    popUpTo = ConstHelper.RouteNames.CelestialBodyDetail.path
                        .replace("{celestialBodyId}", celestialBodyId),
                    removeBackStack = true
                )
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Ubah Benda Langit", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            CelestialBodyEditUI(celestialBody = celestialBody!!, onSave = ::onSave)
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun CelestialBodyEditUI(
    celestialBody: ResponseCelestialBodyData,
    onSave: (Context, String, String, String, String, Uri?) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(celestialBody.nama) }
    var dataDeskripsi by remember { mutableStateOf(celestialBody.deskripsi) }
    var dataManfaat by remember { mutableStateOf(celestialBody.manfaat) }
    var dataFaktaMenarik by remember { mutableStateOf(celestialBody.faktaMenarik) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? -> dataFile = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = dataFile ?: ToolsHelper.getCelestialBodyImageUrl(celestialBody.id),
                    placeholder = painterResource(R.drawable.img_placeholder),
                    error = painterResource(R.drawable.img_placeholder),
                    contentDescription = "Pratinjau Gambar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Tap untuk mengganti gambar", style = MaterialTheme.typography.bodySmall)
        }

        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            cursorColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        OutlinedTextField(
            value = dataNama, onValueChange = { dataNama = it }, colors = textFieldColors,
            label = { Text("Nama", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        OutlinedTextField(
            value = dataDeskripsi, onValueChange = { dataDeskripsi = it }, colors = textFieldColors,
            label = { Text("Deskripsi", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 5, minLines = 3
        )

        OutlinedTextField(
            value = dataManfaat, onValueChange = { dataManfaat = it }, colors = textFieldColors,
            label = { Text("Manfaat", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 5, minLines = 3
        )

        OutlinedTextField(
            value = dataFaktaMenarik, onValueChange = { dataFaktaMenarik = it }, colors = textFieldColors,
            label = { Text("Fakta Menarik", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 5, minLines = 3
        )

        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                if (dataNama.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Nama tidak boleh kosong!"); return@FloatingActionButton }
                if (dataDeskripsi.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Deskripsi tidak boleh kosong!"); return@FloatingActionButton }
                if (dataManfaat.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Manfaat tidak boleh kosong!"); return@FloatingActionButton }
                if (dataFaktaMenarik.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Fakta Menarik tidak boleh kosong!"); return@FloatingActionButton }
                onSave(context, dataNama, dataDeskripsi, dataManfaat, dataFaktaMenarik, dataFile)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Simpan Data")
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(
            onDismissRequest = { AlertHelper.dismiss(alertState) },
            title = { Text(alertState.value.type.title) },
            text = { Text(alertState.value.message) },
            confirmButton = { TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") } }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCelestialBodyEditUI() { }
