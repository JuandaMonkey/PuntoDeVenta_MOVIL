package core.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
// dtos
import core.dtos.auth.LoginRequestDTO
import core.dtos.auth.LoginResponseDTO

interface AuthService {
    /**
     * login
     */
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequestDTO): Response<LoginResponseDTO>
}