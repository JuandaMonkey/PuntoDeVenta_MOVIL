package core.dtos.cliente

import com.google.gson.annotations.SerializedName

data class ClientesResponseDTO(
    @SerializedName("exito") val exito: Boolean,
    @SerializedName("data") val data: List<ClienteDTO>?,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("total") val total: Int? = null
)
