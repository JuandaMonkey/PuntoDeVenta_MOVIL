package core.dtos.auth

import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
    @SerializedName("exito") val exito: Boolean,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("token") val token: String? = null
)
