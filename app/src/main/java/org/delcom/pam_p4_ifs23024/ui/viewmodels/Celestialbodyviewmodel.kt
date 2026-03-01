package org.delcom.pam_p4_ifs23024.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodyData
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseProfile
import org.delcom.pam_p4_ifs23024.network.celestialbodies.service.ICelestialBodyRepository
import javax.inject.Inject

sealed interface ProfileUIState {
    data class Success(val data: ResponseProfile) : ProfileUIState
    data class Error(val message: String) : ProfileUIState
    object Loading : ProfileUIState
}

sealed interface CelestialBodiesUIState {
    data class Success(val data: List<ResponseCelestialBodyData>) : CelestialBodiesUIState
    data class Error(val message: String) : CelestialBodiesUIState
    object Loading : CelestialBodiesUIState
}

sealed interface CelestialBodyUIState {
    data class Success(val data: ResponseCelestialBodyData) : CelestialBodyUIState
    data class Error(val message: String) : CelestialBodyUIState
    object Loading : CelestialBodyUIState
}

sealed interface CelestialBodyActionUIState {
    data class Success(val message: String) : CelestialBodyActionUIState
    data class Error(val message: String) : CelestialBodyActionUIState
    object Loading : CelestialBodyActionUIState
}

data class UIStateCelestialBody(
    val profile: ProfileUIState = ProfileUIState.Loading,
    val celestialBodies: CelestialBodiesUIState = CelestialBodiesUIState.Loading,
    var celestialBody: CelestialBodyUIState = CelestialBodyUIState.Loading,
    var celestialBodyAction: CelestialBodyActionUIState = CelestialBodyActionUIState.Loading
)

@HiltViewModel
@Keep
class CelestialBodyViewModel @Inject constructor(
    private val repository: ICelestialBodyRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateCelestialBody())
    val uiState = _uiState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(profile = ProfileUIState.Loading) }
            val response = runCatching { repository.getProfile() }.getOrNull()
            _uiState.update { state ->
                val newState = if (response?.status == "success" && response.data != null) {
                    ProfileUIState.Success(response.data)
                } else {
                    ProfileUIState.Error(response?.message ?: "Gagal mengambil profil")
                }
                state.copy(profile = newState)
            }
        }
    }

    fun getAllCelestialBodies(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(celestialBodies = CelestialBodiesUIState.Loading) }
            val response = runCatching { repository.getAllCelestialBodies(search) }.getOrNull()
            _uiState.update { state ->
                val newState = if (response?.status == "success" && response.data != null) {
                    CelestialBodiesUIState.Success(response.data.celestialBodies)
                } else {
                    CelestialBodiesUIState.Error(response?.message ?: "Gagal mengambil data benda langit")
                }
                state.copy(celestialBodies = newState)
            }
        }
    }

    fun postCelestialBody(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        faktaMenarik: RequestBody,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(celestialBodyAction = CelestialBodyActionUIState.Loading) }
            val response = runCatching {
                repository.postCelestialBody(nama, deskripsi, manfaat, faktaMenarik, file)
            }.getOrNull()
            _uiState.update { state ->
                val newState = if (response?.status == "success" && response.data != null) {
                    CelestialBodyActionUIState.Success(response.data.celestialBodyId)
                } else {
                    CelestialBodyActionUIState.Error(response?.message ?: "Gagal menambah data")
                }
                state.copy(celestialBodyAction = newState)
            }
        }
    }

    fun getCelestialBodyById(celestialBodyId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(celestialBody = CelestialBodyUIState.Loading) }
            val response = runCatching { repository.getCelestialBodyById(celestialBodyId) }.getOrNull()
            _uiState.update { state ->
                val newState = if (response?.status == "success" && response.data != null) {
                    CelestialBodyUIState.Success(response.data.celestialBody)
                } else {
                    CelestialBodyUIState.Error(response?.message ?: "Gagal mengambil detail data")
                }
                state.copy(celestialBody = newState)
            }
        }
    }

    fun putCelestialBody(
        celestialBodyId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        faktaMenarik: RequestBody,
        file: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(celestialBodyAction = CelestialBodyActionUIState.Loading) }
            val response = runCatching {
                repository.putCelestialBody(celestialBodyId, nama, deskripsi, manfaat, faktaMenarik, file)
            }.getOrNull()
            _uiState.update { state ->
                val newState = if (response?.status == "success") {
                    CelestialBodyActionUIState.Success(response.message ?: "Berhasil memperbarui data")
                } else {
                    CelestialBodyActionUIState.Error(response?.message ?: "Gagal memperbarui data")
                }
                state.copy(celestialBodyAction = newState)
            }
        }
    }

    fun deleteCelestialBody(celestialBodyId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(celestialBodyAction = CelestialBodyActionUIState.Loading) }
            val response = runCatching { repository.deleteCelestialBody(celestialBodyId) }.getOrNull()
            _uiState.update { state ->
                val newState = if (response?.status == "success") {
                    CelestialBodyActionUIState.Success(response.message ?: "Berhasil menghapus data")
                } else {
                    CelestialBodyActionUIState.Error(response?.message ?: "Gagal menghapus data")
                }
                state.copy(celestialBodyAction = newState)
            }
        }
    }
}
