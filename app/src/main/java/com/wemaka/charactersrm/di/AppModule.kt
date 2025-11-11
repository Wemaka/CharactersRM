package com.wemaka.charactersrm.di

import android.content.Context
import com.wemaka.charactersrm.core.util.Constants.BASE_URL
import com.wemaka.charactersrm.data.data_source.ConnectionNetworkProvider
import com.wemaka.charactersrm.data.data_source.NetworkChecker
import com.wemaka.charactersrm.data.data_source.RAMApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        println("Request URL: $url")
        return chain.proceed(request)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRAMApi(): RAMApi {
        val client = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RAMApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectionNetwork(@ApplicationContext context: Context): NetworkChecker {
        return ConnectionNetworkProvider(context)
    }
}