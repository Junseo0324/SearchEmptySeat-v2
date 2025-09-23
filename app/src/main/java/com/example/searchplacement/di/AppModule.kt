package com.example.searchplacement.di

import com.example.searchplacement.BuildConfig
import com.example.searchplacement.data.api.APIService
import com.example.searchplacement.data.api.AuthApiService
import com.example.searchplacement.data.api.FavoriteApiService
import com.example.searchplacement.data.api.MapApiService
import com.example.searchplacement.data.api.MenuApiService
import com.example.searchplacement.data.api.MenuSectionApiService
import com.example.searchplacement.util.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val BASE_URL = BuildConfig.BASE_URL

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, Any?>? {
            val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter { body ->
                if (body.contentLength() == 0L) null else delegate.convert(body)
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(nullOnEmptyConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthAPIService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFavoriteAPIService(retrofit: Retrofit): FavoriteApiService {
        return retrofit.create(FavoriteApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMapApiService(retrofit: Retrofit): MapApiService {
        return retrofit.create(MapApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMenuApiService(retrofit: Retrofit): MenuApiService {
        return retrofit.create(MenuApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMenuSectionApiService(retrofit: Retrofit): MenuSectionApiService {
        return retrofit.create(MenuSectionApiService::class.java)
    }

}
