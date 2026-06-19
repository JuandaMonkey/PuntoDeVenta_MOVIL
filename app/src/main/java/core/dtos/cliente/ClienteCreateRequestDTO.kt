package core.dtos.cliente

import com.google.gson.annotations.SerializedName

data class ClienteCreateRequestDTO(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String
)
