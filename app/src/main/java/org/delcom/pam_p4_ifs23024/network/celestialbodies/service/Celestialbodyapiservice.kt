package org.delcom.pam_p4_ifs23024.network.celestialbodies.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23024.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBody
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodyAdd
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodies
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseProfile
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CelestialBodyApiService {
    // Ambil profile developer
    @GET("profile")
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    // Ambil semua data benda langit
    @GET("celestial-bodies")
    suspend fun getAllCelestialBodies(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponseCelestialBodies?>

    // Tambah data benda langit
    @Multipart
    @POST("celestial-bodies")
    suspend fun postCelestialBody(
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("manfaat") manfaat: RequestBody,
        @Part("faktaMenarik") faktaMenarik: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseMessage<ResponseCelestialBodyAdd?>

    // Ambil data benda langit berdasarkan ID
    @GET("celestial-bodies/{id}")
    suspend fun getCelestialBodyById(
        @Path("id") celestialBodyId: String
    ): ResponseMessage<ResponseCelestialBody?>

    // Ubah data benda langit
    @Multipart
    @PUT("celestial-bodies/{id}")
    suspend fun putCelestialBody(
        @Path("id") celestialBodyId: String,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("manfaat") manfaat: RequestBody,
        @Part("faktaMenarik") faktaMenarik: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data benda langit
    @DELETE("celestial-bodies/{id}")
    suspend fun deleteCelestialBody(
        @Path("id") celestialBodyId: String
    ): ResponseMessage<String?>
}