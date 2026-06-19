package core.utils

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONObject

class SessionManager(context: Context) {

    // genera una clave maestra segura
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // crea un SharedPreferences cifrado
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // guarda el token de autenticación de forma cifrada
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    // recupera el token cifrado
    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // elimina el token y cierra la sesión
    fun clearSession() {
        sharedPreferences.edit().remove("auth_token").apply()
    }

    // decodifica el JWT y obtiene el rol del usuario
    fun getUserRole(): String? {
        val token = getToken() ?: return null
        return try {
            // divide el token en partes
            val parts = token.split(".")
            if (parts.size < 2) return null
            // decodifica la parte del payload
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)

            // intenta obtener "rol" o el claim estándar de .NET
            val rol = json.optString("rol", "")
            if (rol.isNotEmpty()) return rol

            // si no se encuentra "rol", intenta obtener "http://schemas.microsoft.com/ws/2008/06/identity/claims/role"
            val dotNetRole = json.optString("http://schemas.microsoft.com/ws/2008/06/identity/claims/role", "")
            if (dotNetRole.isNotEmpty()) dotNetRole else null
        } catch (e: Exception) {
            null
        }
    }
}
