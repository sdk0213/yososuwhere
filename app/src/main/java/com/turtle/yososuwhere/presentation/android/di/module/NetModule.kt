package com.turtle.yososuwhere.presentation.android.di.module

import com.google.gson.GsonBuilder
import com.turtle.yososuwhere.data.api.ApiClient
import com.turtle.yososuwhere.data.api.YososuAPIService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule {

    @Singleton
    @Provides
    fun provideYososuRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(ApiClient.YOSOSU_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideYososuAPIService(retrofit: Retrofit): YososuAPIService {
        return retrofit.create(YososuAPIService::class.java)
    }

    // okhttp 인스턴스 생성
    @Singleton
    @Provides
    fun provideOkHttpClient(htpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(htpLoggingInterceptor)
            .connectTimeout(40, TimeUnit.SECONDS)  // 커넥션 타임아웃
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    // 로그를 찍기 위한 로깅 인터셉터 설정
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.d("Http Logging message : $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

}