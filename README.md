# Prompt Maestro: Sistema de Gestion y E-commerce con IA (TFM)

Proyecto de Trabajo de Fin de Master en Inteligencia Artificial. Una plantilla de **E-commerce y ERP** adaptable, configurada inicialmente para una **Pasteleria**, con un modulo central de **IA Multimodal** que genera hashtags automaticamente a partir de imagenes de productos.

---

## Stack Tecnologico

| Capa | Tecnologia |
|---|---|
| **Frontend** | Angular 22 con TailwindCSS 4 |
| **Backend** | Java 21+ con Spring Boot 3.4 |
| **Seguridad** | Spring Security + JWT |
| **Inteligencia Artificial** | Spring AI + Google Gemini Vision |
| **Base de Datos** | PostgreSQL (Supabase) |
| **ORM** | Spring Data JPA (Hibernate) |

---

## Instalacion y Ejecucion

### Requisitos

- Java 17 o superior (`JAVA_HOME` configurado)
- Node.js 20+ y npm
- Cuenta en [Supabase](https://supabase.com) (PostgreSQL)
- API Key de [Google Gemini](https://aistudio.google.com/apikey)

### Configurar Variables de Entorno

Copia el archivo de ejemplo y rellena tus credenciales:

```bash
cp .env.example .env
```

Edita `.env` con tus datos:

```env
GEMINI_API_KEY=tu_api_key_de_gemini
SUPABASE_DB_URL=jdbc:postgresql://aws-0-eu-west-1.pooler.supabase.com:5432/postgres
SUPABASE_DB_USERNAME=postgres.xxxxx
SUPABASE_DB_PASSWORD=tu_password_de_supabase
JWT_SECRET=una_clave_secreta_larga_y_segura
```

### Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

El backend se inicia en `http://localhost:8080`.

### Frontend (Angular)

```bash
cd frontend
npm install
npm start
```

El frontend se inicia en `http://localhost:4200`.

---

## Estructura del Proyecto

```
TFM_Workspace/
├── backend/                          # Spring Boot 3.x
│   ├── pom.xml
│   ├── mvnw                          # Maven Wrapper
│   └── src/main/java/com/promptmaestro/
│       ├── BackendApplication.java
│       ├── config/
│       │   ├── SecurityConfig.java       # Configuracion JWT + CORS
│       │   └── DataInitializer.java      # Usuario admin inicial
│       ├── security/
│       │   ├── JwtUtil.java              # Generacion y validacion JWT
│       │   ├── JwtAuthenticationFilter.java
│       │   └── CustomUserDetailsService.java
│       ├── service/
│       │   └── AiService.java            # Gemini Vision → hashtags
│       ├── controller/
│       │   ├── AuthController.java       # Login JWT
│       │   ├── ProductController.java    # CRUD + upload con IA
│       │   ├── InventoryController.java  # Ingredientes y recetas
│       │   └── OrderController.java      # Pedidos
│       ├── entity/                       # Entidades JPA
│       ├── repository/                   # Repositorios
│       ├── dto/                          # Data Transfer Objects
│       └── exception/
│           └── GlobalExceptionHandler.java
├── frontend/                         # Angular 22 + TailwindCSS 4
│   └── src/app/
│       ├── components/
│       │   ├── landing/                  # Pagina de inicio
│       │   ├── gallery/                  # Galeria con busqueda por hashtags
│       │   ├── whatsapp-widget/          # Boton flotante de contacto
│       │   └── admin/
│       │       ├── login/                # Login del administrador
│       │       ├── admin-dashboard/      # Panel principal
│       │       ├── product-upload/       # Subida + analisis IA
│       │       ├── inventory/            # Ingredientes y recetas
│       │       └── orders/               # Gestion de pedidos
│       ├── services/
│       │   ├── auth.ts                   # Autenticacion JWT
│       │   └── api.ts                    # Cliente HTTP REST
│       ├── interceptors/
│       │   └── jwt-interceptor.ts        # Inyeccion del token
│       └── guards/
│           ├── auth-guard.ts             # Proteccion de rutas
│           └── admin-guard.ts            # Solo rol ADMIN
├── .env.example                     # Plantilla de variables de entorno
└── README.md
```

---

## Funcionalidades Principales

### Portal Publico (Clientes)

- **Landing Page:** Presentacion del negocio con productos destacados
- **Galeria Inteligente:** Visualizacion de productos con busqueda por texto y por **#hashtags**
- **Widget de WhatsApp:** Boton flotante que redirige a WhatsApp con mensaje preconfigurado

### Panel de Administracion

- **Login Seguro:** Autenticacion con JWT (credenciales: `admin / admin`)
- **Subida con IA:** Sube una foto de producto → Gemini Vision genera automaticamente 5 hashtags descriptivos
- **Gestion de Inventario:** CRUD de ingredientes (nombre, cantidad, unidad)
- **Gestion de Recetas:** CRUD de recetas con instrucciones
- **Gestion de Pedidos:** CRUD con filtro por estado (Pendiente, En Proceso, Completado, Cancelado)

---

## Credenciales de Prueba

| Campo | Valor |
|---|---|
| **Usuario** | `admin` |
| **Contrasena** | `admin` |

---

## API Endpoints

### Publicos

| Metodo | Ruta | Descripcion |
|---|---|---|
| `POST` | `/api/auth/login` | Iniciar sesion (devuelve JWT) |
| `GET` | `/api/products` | Listar productos |
| `GET` | `/api/products/search?query=` | Buscar productos por texto |
| `GET` | `/api/products/by-hashtags?tags=#chocolate&tags=#boda` | Filtrar por hashtags |

### Admin (requiere JWT + rol ADMIN)

| Metodo | Ruta | Descripcion |
|---|---|---|
| `POST` | `/api/admin/products/upload` | Subir imagen + analisis IA |
| `DELETE` | `/api/admin/products/{id}` | Eliminar producto |
| `GET/POST/PUT/DELETE` | `/api/admin/ingredients` | CRUD ingredientes |
| `GET/POST/PUT/DELETE` | `/api/admin/recipes` | CRUD recetas |
| `GET/POST/PUT/DELETE` | `/api/admin/orders` | CRUD pedidos |

---

## Despliegue

- **Frontend (recomendado):** [Vercel](https://vercel.com) o [Netlify](https://netlify.com)
- **Backend (recomendado):** [Render](https://render.com) o [Fly.io](https://fly.io)
- **Base de Datos:** [Supabase](https://supabase.com)

---

## Enlaces del TFM

| Recurso | URL |
|---|---|
| Repositorio GitHub | [PENDIENTE] |
| Despliegue en produccion | [PENDIENTE] |
| Presentacion (Slides) | [PENDIENTE] |
| Video demostrativo | [PENDIENTE] |

---

## Licencia

Este proyecto forma parte de un Trabajo de Fin de Master en Inteligencia Artificial.
