# Seguridad - PV_MOVIL (Android OWASP Compliant)

Guía de seguridad de la aplicación móvil, implementada siguiendo los estándares de **OWASP Mobile Top 10**. Stack: Kotlin, Retrofit, OkHttp, Jetpack Security.

---

## Almacenamiento Seguro de Datos (M1: Insecure Data Storage)

### EncryptedSharedPreferences
- Los tokens de autenticación y datos sensibles **NO** se almacenan en texto plano.
- Se utiliza la librería **Jetpack Security (`androidx.security:security-crypto`)**.
- Los datos se cifran en reposo utilizando **AES-256** con llaves respaldadas por el **Android Keystore System** (hardware-backed TEE/StrongBox).

---

## Comunicación Segura (M3: Insecure Communication)

### Interceptor de Autenticación
- Implementación de `AuthInterceptor` para la inyección automática y centralizada del header `Authorization: Bearer`.
- Reduce la superficie de exposición del token en el código fuente.

### HTTPS y TLS
- Comunicación cifrada exclusiva mediante **HTTPS**.
- El logging de red (`HttpLoggingInterceptor`) está restringido a niveles de depuración.

---

## Autenticación y Autorización (M4: Insufficient Authentication/Authorization)

### Manejo de Sesión y Roles
- **Cierre de Sesión Seguro:** Al cerrar sesión, se eliminan físicamente las llaves y tokens del almacenamiento cifrado y se limpia el historial de actividades.
- **Validación de Roles:** Decodificación local de JWT para restringir visualmente funciones administrativas (ej. `admin`), delegando la validación final al servidor.

### Prevención de Enumeración y Ataques
- Mensajes de error genéricos en el Login para mitigar ataques de diccionario y enumeración de usuarios.
- Diálogos de confirmación obligatorios para acciones destructivas (Eliminación de registros).

---

## Integridad de Datos y Gestión de Secretos

### Validaciones de Entrada
- Sanitización y validación de longitud/formato en el cliente antes del envío de datos.
- Automatización del cálculo de edad mediante `DatePicker` para prevenir discrepancias de datos e integridad en el backend.

### Componentes y Dependencias
- Uso de componentes de arquitectura de Android (Lifecycle, Coroutines) para un manejo de memoria seguro.
- Mantener las librerías de terceros actualizadas a versiones estables para evitar vulnerabilidades conocidas.
