package core.dtos.cliente

import com.google.gson.annotations.SerializedName

data class ClienteResponseDTO(
    @SerializedName("exito") val exito: Boolean,
    @SerializedName("data") val data: ClienteDTO?,
    @SerializedName("mensaje") val mensaje: String
)
