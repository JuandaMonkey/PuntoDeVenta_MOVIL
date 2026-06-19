# PV_MOVIL

Aplicación móvil Android de un sistema de **Punto de Venta (PV)** construida en **Kotlin**. Gestiona la interacción con el backend para la administración de **clientes** y **usuarios**, implementando autenticación segura mediante **JWT** y almacenamiento cifrado.

## Stack tecnológico

- **Kotlin** (Lenguaje principal)
- **Retrofit 2** & **OkHttp 3** para el consumo de APIs REST
- **Jetpack Security (EncryptedSharedPreferences)** para el almacenamiento seguro de tokens
- **Corrutinas de Kotlin** para peticiones asíncronas y manejo de hilos
- **Gson** para la serialización/deserialización de JSON
- **Material Design 3** para la interfaz de usuario

## Estructura del proyecto

El código está organizado siguiendo principios de arquitectura limpia y separación de responsabilidades:

```
PV_MOVIL/
├── app/
│   ├── src/main/java/com/example/pv_movil/    # Capa de Presentación
│   │   ├── Login.kt                           # Lógica de autenticación y navegación
│   │   └── MainActivity.kt                    # Dashboard principal
│   │
│   ├── src/main/java/core/                    # Capa Core (Lógica de Negocio)
│   │   ├── dtos/                              # Objetos de Transferencia de Datos
│   │   │   ├── auth/                          # LoginRequestDTO, LoginResponseDTO
│   │   │   └── cliente/                       # ClienteDTO, ClientesResponseDTO
│   │   │
│   │   ├── services/                          # Servicios Retrofit (Interfaces API)
│   │   │   ├── AuthService.kt
│   │   │   ├── ClienteService.kt
│   │   │   └── RetrofitClient.kt              # Configuración central de Retrofit
│   │   │
│   │   └── utils/                             # Utilidades y Seguridad
│   │       ├── SessionManager.kt              # Gestión de sesión y decodificación de JWT
│   │       └── AuthInterceptor.kt             # Interceptor para inyectar Token Bearer
│   │
│   └── src/main/res/                          # Recursos (Layouts XML, Strings, Temas)
```

### Descripción de cada módulo

- **Presentación:** Maneja la interacción con el usuario. El `Login.kt` gestiona el flujo de entrada, validando campos y procesando respuestas del servidor.
- **Core / DTOs:** Define los modelos de datos que coinciden exactamente con la API de .NET, asegurando una comunicación íntegra.
- **Core / Services:** `RetrofitClient` configura el cliente HTTP con interceptores de logging y seguridad. `ClienteService` provee métodos para obtener listados y búsquedas por clave.
- **Core / Utils:** Implementa el estándar **OWASP** para móviles mediante `SessionManager`, que cifra los tokens en el hardware del dispositivo.

## Seguridad y Autenticación

La aplicación está diseñada bajo el estándar **OWASP Mobile Top 10**:

1.  **M1: Almacenamiento Seguro:** Los tokens NO se guardan en texto plano. Se utiliza `EncryptedSharedPreferences` (AES-256).
2.  **M3: Comunicación Segura:** Implementa un `AuthInterceptor` que adjunta automáticamente el header `Authorization: Bearer <token>` a cada petición saliente.
3.  **M4: Autorización:** Incluye un decodificador de JWT local para identificar el **rol** del usuario (`admin`, `empleado`, `cliente`) y adaptar la interfaz de usuario en consecuencia.

## Configuración

Para conectar la aplicación con el servidor, la URL base está configurada en `core/services/RetrofitClient.kt`:

```kotlin
private const val BASE_URL = "https://puntodeventa-api.onrender.com/api/"
```

Asegúrate de que el dispositivo o emulador tenga acceso a internet, ya que la app requiere el permiso:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Cómo ejecutar el proyecto

1.  Clonar el repositorio.
2.  Abrir el proyecto en **Android Studio** (Ladybug o superior).
3.  Sincronizar el proyecto con Gradle para descargar las dependencias.
4.  Ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.

## Endpoints Consumidos

| Módulo | Endpoint | Descripción |
|--------|----------|-------------|
| **Auth** | `POST /Auth/login` | Autenticación y obtención de JWT |
| **Cliente** | `GET /Cliente/GetClientes` | Obtención de lista completa de clientes |
| **Cliente** | `GET /Cliente/GetClientePorClave` | Búsqueda de cliente específico por ID |

Consulte el archivo `SECURITY.md` para más detalles sobre las prácticas de seguridad implementadas.
