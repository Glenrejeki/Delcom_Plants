package org.delcom.pam_p4_ifs23024.network.celestialbodies.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfile(
    val username: String,
    val nama: String,
    val tentang: String,
)