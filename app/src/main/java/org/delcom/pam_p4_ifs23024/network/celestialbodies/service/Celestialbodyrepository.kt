package org.delcom.pam_p4_ifs23024.network.celestialbodies.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23024.helper.SuspendHelper
import org.delcom.pam_p4_ifs23024.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBody
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodyAdd
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseCelestialBodies
import org.delcom.pam_p4_ifs23024.network.celestialbodies.data.ResponseProfile

class CelestialBodyRepository(
    private val celestialBodyApiService: CelestialBodyApiService
) : ICelestialBodyRepository {

    override suspend fun getProfile(): ResponseMessage<ResponseProfile?> {
        return SuspendHelper.safeApiCall {
            celestialBodyApiService.getProfile()
        }
    }

    override suspend fun getAllCelestialBodies(search: String?): ResponseMessage<ResponseCelestialBodies?> {
        return SuspendHelper.safeApiCall {
            celestialBodyApiService.getAllCelestialBodies(search)
        }
    }

    override suspend fun postCelestialBody(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        faktaMenarik: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseCelestialBodyAdd?> {
        return SuspendHelper.safeApiCall {
            celestialBodyApiService.postCelestialBody(
                nama = nama,
                deskripsi = deskripsi,
                manfaat = manfaat,
                faktaMenarik = faktaMenarik,
                file = file
            )
        }
    }

    override suspend fun getCelestialBodyById(celestialBodyId: String): ResponseMessage<ResponseCelestialBody?> {
        return SuspendHelper.safeApiCall {
            celestialBodyApiService.getCelestialBodyById(celestialBodyId)
        }
    }

    override suspend fun putCelestialBody(
        celestialBodyId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        faktaMenarik: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            celestialBodyApiService.putCelestialBody(
                celestialBodyId = celestialBodyId,
                nama = nama,
                deskripsi = deskripsi,
                manfaat = manfaat,
                faktaMenarik = faktaMenarik,
                file = file
            )
        }
    }

    override suspend fun deleteCelestialBody(celestialBodyId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            celestialBodyApiService.deleteCelestialBody(celestialBodyId)
        }
    }
}
