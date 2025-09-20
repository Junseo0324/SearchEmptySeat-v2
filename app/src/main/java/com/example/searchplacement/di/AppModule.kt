package com.example.searchplacement.di

import com.example.searchplacement.BuildConfig
import com.example.searchplacement.data.api.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }
}

object CoroutineCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type,
                     annotations: Array<Annotation>,
                     retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Deferred::class.java) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException("Deferred return type must be parameterized as Deferred<Foo> or Deferred<? extends Foo>")
        }
        val responseType = getParameterUpperBound(0, returnType)
        return CoroutineCallAdapter<Any>(responseType)
    }
}

class CoroutineCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, Deferred<R>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: retrofit2.Call<R>): Deferred<R> {
        return CoroutineScope(Dispatchers.IO).async {
            call.execute().body() ?: throw Exception("Response body is null")
        }
    }
}