package core.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
// dtos
import core.dtos.cliente.ClienteResponseDTO
import core.dtos.cliente.ClientesResponseDTO

interface ClienteService {
    /**
     * obtiene los clientes
    **/
    @GET("Cliente/GetClientes")
    suspend fun getClientes(): Response<ClientesResponseDTO>

    /**
     * obtiene al cliente por clave
    **/
    @GET("Cliente/GetClientePorClave")
    suspend fun getClientePorClave(@Query("clave") clave: Int): Response<ClienteResponseDTO>
}
