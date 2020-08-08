package com.zavsmit.jokes.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


@Module
@InstallIn(ApplicationComponent::class)
class RetrofitModule {
    companion object {
        private const val BASE_URL = "https://api.icndb.com/"
        private const val TIMEOUT = 20
        private const val TIMEOUT_CONNECTION = 20
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val httpClientBuilder = OkHttpClient.Builder()
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(interceptor)
        setIgnoreSslError(httpClientBuilder)
        httpClientBuilder.interceptors().add(Interceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder()
            val request = requestBuilder.build()
            it.proceed(request)
        })
        httpClientBuilder.protocols(listOf(Protocol.HTTP_1_1))
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

    }

    private fun setIgnoreSslError(httpClient: OkHttpClient.Builder) {
        var sslSocketFactory: SSLSocketFactory? = null

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        })

        // Install the all-trusting trust manager
        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            sslSocketFactory = sslContext.socketFactory
        } catch (e: KeyManagementException) {
            Log.e("sslContext", "KeyManagementException", e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("sslContext", "NoSuchAlgorithmException", e)
        }

        if (sslSocketFactory != null) {
            httpClient.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            httpClient.hostnameVerifier(HostnameVerifier { _, _ -> true })
        }
    }
}