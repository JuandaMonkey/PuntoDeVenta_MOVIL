package core.services


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query
// dtos
import core.dtos.cliente.ClienteRequestDTO
import core.dtos.cliente.ClienteResponseDTO
import core.dtos.cliente.ClientesResponseDTO
import core.dtos.cliente.ClienteCreateRequestDTO

interface ClienteService {
    /**
     * obtiene los clientes
     */
    @GET("Cliente/GetClientes")
    suspend fun getClientes(): Response<ClientesResponseDTO>

    /**
     * obtiene al cliente por clave
     */
    @GET("Cliente/GetClientePorClave")
    suspend fun getClientePorClave(@Query("clave") clave: Int): Response<ClienteResponseDTO>

    /**
     * registra un nuevo cliente
     */
    @POST("Cliente/PostCliente")
    suspend fun postCliente(@Body request: ClienteCreateRequestDTO): Response<ClienteResponseDTO>

    /**
     * eliminar cliente
     */
    @HTTP(method = "DELETE", path = "Cliente/DeleteCliente", hasBody = true)
    suspend fun deleteCliente(@Body request: ClienteRequestDTO): Response<ClienteResponseDTO>
}
