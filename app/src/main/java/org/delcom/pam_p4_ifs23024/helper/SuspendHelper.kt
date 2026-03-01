package org.delcom.pam_p4_ifs23024.helper

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import org.delcom.pam_p4_ifs23024.network.data.ResponseMessage
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object SuspendHelper {
    enum class SnackBarType {
        SUCCESS, ERROR, INFO
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> ResponseMessage<T?>): ResponseMessage<T?> {
        return try {
            apiCall()
        } catch (e: UnknownHostException) {
            Log.e("SuspendHelper", "No internet connection: ${e.message}")
            ResponseMessage("error", "Koneksi internet tidak tersedia", null)
        } catch (e: SocketTimeoutException) {
            Log.e("SuspendHelper", "Connection timeout: ${e.message}")
            ResponseMessage("error", "Koneksi terputus (timeout)", null)
        } catch (e: Exception) {
            Log.e("SuspendHelper", "API call failed: ${e.message}", e)
            ResponseMessage("error", e.message ?: "Terjadi kesalahan tidak terduga", null)
        }
    }

    suspend fun showSnackBar(
        snackbarHostState: SnackbarHostState,
        type: SnackBarType,
        message: String
    ) {
        val prefix = when (type) {
            SnackBarType.SUCCESS -> "✅ "
            SnackBarType.ERROR -> "❌ "
            SnackBarType.INFO -> "ℹ️ "
        }
        snackbarHostState.showSnackbar(message = "$prefix$message")
    }
}
