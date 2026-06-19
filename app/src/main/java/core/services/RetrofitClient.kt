package core.services

import android.content.Context
import core.utils.AuthInterceptor
import core.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://puntodeventa-api.onrender.com/"
    private var retrofit: Retrofit? = null

    /**
     * obtiene la instancia de Retrofit. Se requiere un [Context] para inicializar
     * el [SessionManager] y el interceptor de autenticación.
     */
    private fun getInstance(context: Context): Retrofit {
        return retrofit ?: synchronized(this) {
            val instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit = instance
            instance
        }
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val sessionManager = SessionManager(context)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()
    }

    fun getAuthService(context: Context): AuthService {
        return getInstance(context).create(AuthService::class.java)
    }

    fun getClienteService(context: Context): ClienteService {
        return getInstance(context).create(ClienteService::class.java)
    }
}
