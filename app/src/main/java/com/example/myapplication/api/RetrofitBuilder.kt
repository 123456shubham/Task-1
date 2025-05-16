package com.example.myapplication.api


import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

class RetrofitBuilder(application: Application) {
    private val retrofit: Retrofit

    init {
        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()

                        .addHeader("Access-Control-Allow-Origin", "*")
                        .addHeader("Access-Control-Allow-Methods", "GET,POST,PUT, OPTIONS")
                        .addHeader("Access-Control-Allow-Headers", "Content-Type")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("X-Salt-Key", "T5G1YRJTIU4E")
                        .addHeader("X-Session-Token", "klbQPj6x")
                        .method(original.method, original.body)
                    val request: Request = requestBuilder.build()

                    val response: Response = chain.proceed(request)
                    return@Interceptor response
                })
                .addInterceptor(ChuckerInterceptor(application))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier(HostnameVerifier { hostname, session -> true })
                .build()

        val gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    val api: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    companion object {

        const val BASEURL = "http://dev.nonames.com/api/"

        @Volatile
        private var mInstance: RetrofitBuilder? = null

        @Synchronized
        fun getInstance(application: Application): RetrofitBuilder {
            return mInstance ?: synchronized(this) {
                mInstance ?: RetrofitBuilder(application).also { mInstance = it }
            }
        }
    }
}