package org.delcom.pam_p4_ifs23024.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23024.network.celestialbodies.service.ICelestialBodyAppContainer
import org.delcom.pam_p4_ifs23024.network.celestialbodies.service.ICelestialBodyRepository
import org.delcom.pam_p4_ifs23024.network.celestialbodies.service.CelestialBodyAppContainer

@Module
@InstallIn(SingletonComponent::class)
object Celestialbodymodule {
    @Provides
    fun provideCelestialBodyContainer(): ICelestialBodyAppContainer {
        return CelestialBodyAppContainer()
    }

    @Provides
    fun provideCelestialBodyRepository(container: ICelestialBodyAppContainer): ICelestialBodyRepository {
        return container.celestialBodyRepository
    }
}
