package org.delcom.pam_p4_ifs23024.network.celestialbodies.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCelestialBodies(
    val celestialBodies: List<ResponseCelestialBodyData>
)

@Serializable
data class ResponseCelestialBody(
    val celestialBody: ResponseCelestialBodyData
)

@Serializable
data class ResponseCelestialBodyAdd(
    val celestialBodyId: String
)

@Serializable
data class ResponseCelestialBodyData(
    val id: String,
    val nama: String,
    val deskripsi: String,
    val manfaat: String,
    val faktaMenarik: String,
    val pathGambar: String,
    val createdAt: String,
    val updatedAt: String
)