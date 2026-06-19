# Seguridad - PV_MOVIL (Android OWASP Compliant)

Guía de seguridad de la aplicación móvil, implementada siguiendo los estándares de **OWASP Mobile Top 10**. Stack: Kotlin, Retrofit, OkHttp, Jetpack Security.

---

## Almacenamiento Seguro de Datos (M1: Insecure Data Storage)

### EncryptedSharedPreferences
- Los tokens de autenticación y datos sensibles **NO** se almacenan en texto plano en `SharedPreferences` estándar.
- Se utiliza la librería **Jetpack Security (`androidx.security:security-crypto`)**.
- Los datos se cifran en reposo utilizando:
  - **KeyScheme:** AES256_GCM para la llave maestra.
  - **EncryptionScheme:** AES256_SIV para las llaves y AES256_GCM para los valores.
- Las llaves maestras están respaldadas por el **Android Keystore System**, preferiblemente en hardware (TEE/StrongBox) si el dispositivo lo soporta.

---

## Comunicación Segura (M3: Insecure Communication)

### Interceptor de Autenticación
- Se implementó un `AuthInterceptor` que inyecta automáticamente el token JWT en el encabezado `Authorization: Bearer <token>` de forma centralizada.
- Esto evita el manejo manual de credenciales en cada llamada al servicio y reduce el riesgo de fugas de tokens en logs o código.

### HTTPS y TLS
- La aplicación se comunica exclusivamente mediante **HTTPS** con el backend (`https://puntodeventa-api.onrender.com/`).
- Se utiliza `HttpLoggingInterceptor` (nivel `BODY`) configurado únicamente para depuración, debiendo desactivarse o restringirse en compilaciones de producción.

---

## Autenticación y Autorización (M4: Insufficient Authentication/Authorization)

### Manejo de JWT (JSON Web Tokens)
- El token tiene un tiempo de expiración definido por el servidor.
- La aplicación realiza una decodificación local del JWT en el cliente (`SessionManager.getUserRole()`) para gestionar la interfaz de usuario (UI) basada en roles (ej. `admin`), sin embargo, la validación final siempre la realiza el servidor mediante el middleware de autorización.

### Prevención de Enumeración
- Siguiendo las mejores prácticas, la aplicación muestra mensajes de error genéricos ("Usuario o contraseña incorrectos") en lugar de especificar si el usuario existe o no, dificultando ataques de fuerza bruta.

---

## Gestión de Secretos y Configuración

### Git Hygiene
- El archivo `.gitignore` está configurado para excluir archivos sensibles como `local.properties`.
- Los secretos de sesión se generan dinámicamente y se guardan en el almacenamiento cifrado del dispositivo, nunca en el código fuente (Hardcoded).

### Componentes Actualizados
- Se mantienen las dependencias (Retrofit, OkHttp, Security Crypto) actualizadas a sus versiones estables para mitigar vulnerabilidades conocidas en librerías de terceros.
