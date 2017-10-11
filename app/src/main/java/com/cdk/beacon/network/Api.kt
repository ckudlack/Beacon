package com.cdk.beacon.network

import com.cdk.beacon.network.adapter.BigDecimalAdapter
import com.cdk.beacon.network.adapter.LocalDateAdapter
import com.cdk.beacon.network.adapter.LocalDateTimeAdapter
import com.cdk.beacon.network.adapter.LocalTimeAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Api {

    private val BASE_URL = ""

    private var okHttpClient: OkHttpClient? = null
    private var moshi: Moshi? = null
    private var networkService: NetworkService? = null

    fun getNetworkService(): NetworkService? {
        if (networkService != null) {
            return networkService
        }

        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = createOkHttpClient().addInterceptor(logging).build()

        networkService = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(getMoshi()).asLenient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(NetworkService::class.java)

        return networkService
    }

    private fun createOkHttpClient(): OkHttpClient.Builder {

        return OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
    }

    private fun getMoshi(): Moshi? {
        if (moshi == null) {
            moshi = Moshi.Builder()
                    .add(BigDecimalAdapter())
                    .add(LocalDateAdapter())
                    .add(LocalTimeAdapter())
                    .add(LocalDateTimeAdapter())
                    .build()
        }

        return moshi
    }
}
