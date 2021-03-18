package com.alvayonara.shopapps.core.di

import com.alvayonara.shopapps.BuildConfig
import com.alvayonara.shopapps.core.data.source.remote.network.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpLogginInterceptor = HttpLoggingInterceptor()
        val interceptor = Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()
            builder.addHeader("Content-Type", "application/json")
            request = builder.build()
            chain.proceed(request)
        }

        httpLogginInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addNetworkInterceptor(httpLogginInterceptor)
        return okHttpClient.build()
    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}