# PV_MOVIL

Aplicación móvil Android de un sistema de **Punto de Venta (PV)** construida en **Kotlin**. Gestiona la interacción con el backend para la administración de **clientes** y **usuarios**, implementando una interfaz moderna bajo los estándares de **Material Design 3**.

## Stack tecnológico

- **Kotlin** (Lenguaje principal)
- **Retrofit 2** & **OkHttp 3** para el consumo de APIs REST.
- **Jetpack Security (EncryptedSharedPreferences)** para el almacenamiento seguro de tokens.
- **Corrutinas de Kotlin** para peticiones asíncronas.
- **Material Design 3** para una interfaz minimalista y responsiva.
- **Gson** para el mapeo de datos JSON.

## Características Principales

### Seguridad y Autenticación
- **OWASP Mobile Top 10:** Cumplimiento de los estándares M1 (Almacenamiento seguro), M3 (Comunicación segura) y M4 (Autorización).
- **Session Management:** Los tokens se guardan cifrados en el hardware del dispositivo.
- **AuthInterceptor:** Inyección automática del header `Authorization: Bearer` en todas las peticiones.
- **Roles:** Decodificación local de JWT para validar permisos (ej. `admin`).

### Gestión de Clientes
- **Catálogo Completo:** Visualización de clientes en una lista optimizada con diseño de tabla.
- **Operaciones CRUD:** 
  - **Creación:** Registro de nuevos clientes con validaciones de longitud y formato.
  - **Búsqueda:** Consulta detallada por clave única.
  - **Eliminación:** Borrado de registros con diálogo de confirmación de seguridad.
- **Automatización:** 
  - **DatePicker:** Selector de fecha nativo para evitar errores de formato.
  - **Cálculo de Edad:** La edad se calcula automáticamente al seleccionar la fecha de nacimiento y se bloquea para edición manual.
  - **Interfaz Dinámica:** Los campos se adaptan (muestran/ocultan) según se trate de un registro nuevo o una edición.

### Interfaz de Usuario (UI/UX)
- **Diseño Minimalista:** Uso de `MaterialCardView`, `TextInputLayout` (Outlined) y `MaterialButton`.
- **Iconografía:** Set de iconos vectoriales personalizados para una estética profesional.
- **Navegación Estable:** Implementación de `BottomNavigationView` con protección contra recargas innecesarias y cierres inesperados.

## Estructura del proyecto

```
PV_MOVIL/
├── app/
│   ├── src/main/java/com/example/pv_movil/    # Capa de Presentación
│   │   ├── Login.kt                           # Lógica de acceso
│   │   ├── MainActivity.kt                    # Host de fragmentos y NavBar
│   │   ├── AdministrarClientes.kt             # Fragmento de gestión CRUD
│   │   └── adapters/                          # Adaptadores de RecyclerView
│   │
│   ├── src/main/java/core/                    # Capa de Negocio
│   │   ├── dtos/                              # Modelos de datos (Auth/Cliente)
│   │   ├── services/                          # Interfaces Retrofit y Cliente API
│   │   └── utils/                             # Seguridad (SessionManager e Interceptor)
│   │
│   └── src/main/res/                          # Recursos (Layouts, Vectores, Colores)
```

## Endpoints Consumidos

| Módulo | Método | Endpoint | Descripción |
|--------|--------|----------|-------------|
| **Auth** | POST | `/api/Auth/login` | Login y obtención de JWT |
| **Cliente** | GET | `/Cliente/GetClientes` | Listado total de clientes |
| **Cliente** | GET | `/Cliente/GetClientePorClave` | Detalle por ID |
| **Cliente** | POST | `/Cliente/PostCliente` | Registro de nuevo cliente |
| **Cliente** | DELETE | `/Cliente/DeleteCliente` | Eliminación de registro |

## Cómo ejecutar el proyecto

1.  **Clonar** el repositorio.
2.  **Abrir** en Android Studio (Ladybug 2024.2.1 o superior).
3.  **Sincronizar** Gradle.
4.  **Ejecutar** en un emulador o dispositivo físico (API 24+).

Consulte el archivo `SECURITY.md` para más detalles técnicos sobre el cifrado y la protección de datos.
