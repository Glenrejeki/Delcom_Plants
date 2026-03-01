package org.delcom.pam_p4_ifs23024.network.celestialbodies.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23024.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBody
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodyAdd
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodies
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseProfile

interface ICelestialBodyRepository {
    // Ambil profile developer
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    // Ambil semua data benda langit
    suspend fun getAllCelestialBodies(
        search: String? = null
    ): ResponseMessage<ResponseCelestialBodies?>

    // Tambah data benda langit
    suspend fun postCelestialBody(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        faktaMenarik: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseCelestialBodyAdd?>

    // Ambil data benda langit berdasarkan ID
    suspend fun getCelestialBodyById(
        celestialBodyId: String
    ): ResponseMessage<ResponseCelestialBody?>

    // Ubah data benda langit
    suspend fun putCelestialBody(
        celestialBodyId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        faktaMenarik: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data benda langit
    suspend fun deleteCelestialBody(
        celestialBodyId: String
    ): ResponseMessage<String?>
}
