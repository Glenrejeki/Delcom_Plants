package org.delcom.pam_p4_ifs23024.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),
        CelestialBodies(path = "celestial-bodies"),
        CelestialBodyAdd(path = "celestial-bodies/add"),
        CelestialBodyDetail(path = "celestial-bodies/{celestialBodyId}"),
        CelestialBodyEdit(path = "celestial-bodies/{celestialBodyId}/edit"),
    }
}