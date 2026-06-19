package core.dtos.cliente

import com.google.gson.annotations.SerializedName

data class ClienteDTO(
    @SerializedName("clave") val clave: Int,
    @SerializedName("nombreCompleto") val nombreCompleto: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String
)
