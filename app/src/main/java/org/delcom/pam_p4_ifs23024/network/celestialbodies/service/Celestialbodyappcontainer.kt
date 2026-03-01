package org.delcom.pam_p4_ifs23024.network.celestialbodies.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.delcom.pam_p4_ifs23024.BuildConfig
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class CelestialBodyAppContainer : ICelestialBodyAppContainer {

    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    private val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, SecureRandom())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val hostInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .header("Host", "pam-2026-p4-ifs23024-be.fluxy.sbs")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(hostInterceptor)
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }
        sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        hostnameVerifier { _, _ -> true }
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_CELESTIAL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val retrofitService: CelestialBodyApiService by lazy {
        retrofit.create(CelestialBodyApiService::class.java)
    }

    override val celestialBodyRepository: ICelestialBodyRepository by lazy {
        CelestialBodyRepository(retrofitService)
    }
}