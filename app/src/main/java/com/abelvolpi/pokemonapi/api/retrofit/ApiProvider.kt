package com.abelvolpi.pokemonapi.api.retrofit

import com.abelvolpi.pokemonapi.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider {

    private const val TIMEOUT = 3L
    val retrofit: Retrofit

    init {

        val logInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(logInterceptor)

        }.build()


        val kotlinMoshiAdapter = Moshi.Builder().apply {
            addLast(KotlinJsonAdapterFactory())
        }.build()

        retrofit = Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_URL)
            client(client)
            addConverterFactory(MoshiConverterFactory.create(kotlinMoshiAdapter))
        }.build()
    }

}