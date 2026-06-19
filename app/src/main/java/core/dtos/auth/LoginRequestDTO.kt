package core.dtos.auth

data class LoginRequestDTO (
    val usuario: String,
    val contraseña: String
)