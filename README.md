# Dulce Sabor - E-commerce y ERP para Pasteleria Artesanal

## 1. Descripcion General

**Dulce Sabor** es una plataforma web completa de gestion para una pasteleria artesanal ficticia ubicada en Malaga, Espana. El proyecto integra un comercio electronico publico con un panel de administracion completo (ERP) y herramientas de inteligencia artificial basadas en Google Gemini.

El sistema permite a los clientes explorar el catalogo de tartas, buscar por hashtags, contactar por WhatsApp y utilizar un asistente virtual con IA. El administrador puede gestionar tartas, recetas, materia prima, pedidos, y aprovechar funcionalidades de IA como generacion de descripciones, analisis de fotos, hashtags automaticos y optimizacion de inventario.

**Direccion:** C/ Marques de Larios, 1, 29005 Malaga  
**Telefono:** 955 123 456  
**Horario:** Lunes a Sabado, 9:00 - 20:00

---

## 2. Stack Tecnologico

### Frontend
| Tecnologia | Version | Uso |
|---|---|---|
| Angular | 22.1.0 | Framework SPA |
| TypeScript | - | Lenguaje tipado |
| TailwindCSS | 4 | Estilos |
| RxJS | - | Manejo reactivo de datos |
| Angular Signals | - | Estado reactivo |

### Backend
| Tecnologia | Version | Uso |
|---|---|---|
| Java | 21 | Lenguaje de programacion |
| Spring Boot | 3.4.1 | Framework REST API |
| Spring Security | - | Autenticacion JWT |
| JPA / Hibernate | - | ORM para base de datos |
| Maven | 3.9.9 | Build tool |

### Base de Datos
| Tecnologia | Uso |
|---|---|
| PostgreSQL (Supabase) | Base de datos relacional en la nube |
| Supabase REST API | Consultas alternativas |

### IA
| Tecnologia | Uso |
|---|---|
| Google Gemini 2.5 Flash | Chat virtual, hashtags, descripciones, analisis de fotos, optimizacion inventario |
| API REST directa (RestTemplate) | Comunicacion con Gemini sin dependencias adicionales |

### Despliegue
| Servicio | Uso |
|---|---|
| Vercel | Hosting del frontend Angular (despliegue automatico) |
| Render | Hosting del backend Spring Boot |
| GitHub | Repositorio de codigo fuente |

---

## 3. Instalacion y Ejecucion

### Prerequisitos
- Java 21 (JDK)
- Node.js 18+ y npm
- Maven 3.9+ (o usar el wrapper `./mvnw`)
- Cuenta en Supabase (para la base de datos)
- API Key de Google Gemini (para funcionalidades de IA)

### Backend

```bash
# 1. Clonar el repositorio
git clone https://github.com/fportillomasteria2026/pasteleria-ecommerce-tfm.git
cd pasteleria-ecommerce-tfm/backend

# 2. Configurar variables de entorno (crear archivo .env en la raiz del proyecto)
# GEMINI_API_KEY=tu_api_key_de_gemini
# SUPABASE_DB_URL=jdbc:postgresql://db.tu-proyecto.supabase.co:6543/postgres
# SUPABASE_DB_USERNAME=postgres
# SUPABASE_DB_PASSWORD=tu_password
# JWT_SECRET=tu_clave_secreta_jwt_minimo_32_bytes

# 3. Compilar y ejecutar
./mvnw clean package -DskipTests
java -jar target/backend-1.0.0.jar

# O directamente:
./mvnw spring-boot:run
```

El backend arranca en `http://localhost:8080`.

### Frontend

```bash
cd pasteleria-ecommerce-tfm/frontend

# Instalar dependencias
npm install

# Ejecutar en desarrollo
ng serve

# Compilar para produccion
ng build
```

El frontend arranca en `http://localhost:4200`.

### Variables de Entorno

| Variable | Descripcion | Ejemplo |
|---|---|---|
| `GEMINI_API_KEY` | Clave API de Google Gemini | `AIzaSy...` |
| `SUPABASE_DB_URL` | URL de conexion JDBC a Supabase | `jdbc:postgresql://...` |
| `SUPABASE_DB_USERNAME` | Usuario de la BD | `postgres` |
| `SUPABASE_DB_PASSWORD` | Password de la BD | `tu_password` |
| `JWT_SECRET` | Clave secreta para tokens JWT (min 32 bytes) | `tu_clave_segura_aqui_32b` |

---

## 4. Estructura del Proyecto

```
pasteleria-ecommerce-tfm/
├── backend/                            # API REST - Spring Boot
│   ├── src/main/java/com/promptmaestro/
│   │   ├── BackendApplication.java     # Punto de entrada
│   │   ├── config/
│   │   │   └── SecurityConfig.java     # Seguridad JWT + CORS
│   │   ├── controller/
│   │   │   ├── AuthController.java     # Login/registro
│   │   │   ├── TartaController.java    # CRUD tartas (productos)
│   │   │   ├── OrderController.java    # CRUD pedidos
│   │   │   ├── MateriaPrimaController.java  # CRUD materia prima
│   │   │   ├── InventoryController.java     # Ingredientes y recetas
│   │   │   ├── ProductController.java  # Galeria de imagenes
│   │   │   ├── ChatController.java     # Chat virtual + pedidos
│   │   │   ├── AiHashtagsController.java    # IA: hashtags + descripciones + inventario
│   │   │   ├── AiTartaController.java  # IA: analisis de fotos
│   │   │   └── SetupController.java    # Setup inicial + seed datos
│   │   ├── entity/
│   │   │   ├── Tarta.java              # Entidad principal (productos)
│   │   │   ├── Order.java              # Pedidos
│   │   │   ├── MateriaPrima.java       # Materia prima del obrador
│   │   │   ├── Recipe.java             # Recetas de pasteleria
│   │   │   ├── Ingredient.java         # Ingredientes
│   │   │   ├── ProductImage.java       # Imagenes galeria
│   │   │   ├── User.java               # Usuarios
│   │   │   └── Hashtag.java            # Hashtags galeria
│   │   ├── repository/                 # Repositorios JPA
│   │   ├── service/
│   │   │   ├── ChatService.java        # Logica chat Gemini
│   │   │   ├── AiService.java          # Logica analisis fotos
│   │   │   └── SupabaseService.java    # Storage Supabase
│   │   └── security/                   # Filtros JWT
│   ├── src/main/resources/
│   │   ├── application.properties      # Configuracion principal
│   │   └── application-local.properties # Config local (H2)
│   └── Dockerfile                      # Para Render
│
├── frontend/                           # SPA - Angular
│   ├── src/app/
│   │   ├── components/
│   │   │   ├── landing/                # Pagina principal publica
│   │   │   ├── gallery/                # Galeria de tartas
│   │   │   ├── quienes-somos/          # Pagina "Quienes Somos"
│   │   │   ├── chat-widget/            # Widget chat virtual IA
│   │   │   ├── whatsapp-widget/        # Boton WhatsApp
│   │   │   └── admin/
│   │   │       ├── admin-dashboard/    # Dashboard con sidebar
│   │   │       ├── tartas/             # Gestion tartas + IA foto + hashtags
│   │   │       ├── orders/             # Gestion pedidos
│   │   │       ├── ingredients/        # Gestion materia prima
│   │   │       ├── recipes/            # Gestion recetas
│   │   │       ├── inventory-ai/       # IA optimizacion inventario
│   │   │       ├── product-upload/     # Subida imagenes galeria
│   │   │       └── login/              # Login administrador
│   │   ├── services/
│   │   │   └── api.ts                  # Servicio HTTP centralizado
│   │   ├── guards/
│   │   │   └── admin-guard.ts          # Guard de autenticacion
│   │   └── interceptors/               # Interceptor JWT
│   └── angular.json
│
├── .env                                # Variables de entorno
├── vercel.json                         # Configuracion Vercel (SPA routing)
└── README.md                           # Este fichero
```

---

## 5. Funcionalidades Principales

### 5.1 Area Publica

#### Pagina Principal (Landing)
- Banner principal con imagen y texto de bienvenida
- Seccion de tartas destacadas
- Widget de WhatsApp flotante para contacto directo
- Widget de chat virtual con asistente IA
- Diseno responsive para movil

#### Galeria de Tartas
- Catalogo visual de todas las tartas disponibles
- Tarjetas con imagen, nombre, descripcion, tamano y precio
- Busqueda por titulo
- Diseno responsive con grid adaptativo

#### Quienes Somos
- Informacion sobre la pasteleria
- Valores y filosofia del negocio
- Diseno visual coherente con la marca

### 5.2 Chat Virtual con IA

El asistente virtual utiliza **Google Gemini 2.5 Flash** con un sistema hibrido:

- **Modo Real:** Si la API key de Gemini esta configurada, envia el prompt a Gemini con contexto de productos de la base de datos y devuelve respuesta generada por IA
- **Modo Mock (Fallback):** Si Gemini no esta disponible o falla, devuelve respuestas predefinidas coherentes

**Funcionalidades:**
- Responde preguntas sobre productos, precios, horarios y direccion
- Contexto automatico con todas las tartas activas de la BD
- Botones de accion rapida: "Ver tartas", "Hacer pedido", "Horarios"
- Flujo de pedidos: formulario inline para seleccionar tarta, tamano, personalizacion
- Genera resumen de pedido formateado para WhatsApp
- Boton "Enviar por WhatsApp" que abre wa.me con el mensaje

### 5.3 Panel de Administracion

#### Login
- Autenticacion JWT segura
- Proteccion de rutas con guard de Angular

#### Dashboard
- Panel lateral (sidebar) con navegacion
- Tarjetas de acceso rapido a cada seccion
- Responsive con hamburger menu en movil

#### Gestion de Tartas (CRUD + IA)
- Crear, editar, eliminar tartas
- Campos: SKU, nombre, descripcion, imagen, hashtags, tamano, pisos, forma, dimensiones, bizcocho, crema, frutas, personalizacion, precio, coste, notas
- **IA - Generar hashtags:** Boton que envia campos a Gemini y genera 15 hashtags relevantes automaticamente
- **IA - Generar descripcion:** Boton que genera descripcion de marketing con Gemini
- **IA - Crear tarta desde foto:** Sube una imagen y Gemini analiza para crear la tarta automaticamente
- Selector de imagenes (local o servidor)

#### Gestion de Pedidos (CRUD)
- Crear, editar, eliminar pedidos
- Cambio rapido de estado: Pendiente -> En Proceso -> Completado
- Estados: PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
- Filtros por estado
- Busqueda por cliente o tarta
- Campos: cliente, telefono, tarta, tamano, personalizacion, total, notas, estado

#### Gestion de Materia Prima (CRUD)
- Crear, editar, eliminar ingredientes del obrador
- Campos: SKU, nombre, marca, proveedor, coste, formato, peso, unidad, cantidad

#### Gestion de Recetas (CRUD)
- Crear, editar, eliminar recetas
- Campos: nombre, tarta asociada, categoria, porciones, tiempo prep/coccion, dificultad, ingredientes, instrucciones, notas
- Categorias: Bizcocho, Crema, Cobertura, Relleno, Decoracion, General
- Dificultad: Facil, Media, Dificil

#### IA - Optimizacion de Inventario
- Analiza todo el stock de materia prima con Gemini
- Devuelve sugerencias de reposicion con prioridad (alta/media/baja)
- Cantidad sugerida y coste estimado
- Mock fallback si Gemini no esta disponible

#### Galeria de Imagenes (CRUD)
- Subida de imagenes de productos
- Asignacion de hashtags por imagen
- Busqueda por hashtags

### 5.4 Inteligencia Artificial (Google Gemini)

Todas las funcionalidades de IA siguen el patron **Modo Hibrido**:

| Funcionalidad | Endpoint | Descripcion |
|---|---|---|
| Chat virtual | `POST /api/chat` | Asistente conversacional con contexto de productos |
| Generar hashtags | `POST /api/admin/ai/hashtags` | 15 hashtags relevantes para una tarta |
| Generar descripcion | `POST /api/admin/ai/generate-description` | Descripcion de marketing automatica |
| Analizar foto tarta | `POST /api/admin/ai/analyze-tarta` | Crea tarta desde imagen (vision) |
| Optimizar inventario | `POST /api/admin/ai/inventory-optimize` | Sugiere reposiciones segun stock |

**Patron de fallback:** Si `GEMINI_API_KEY` no esta configurada o la llamada falla, todas las funcionalidades devuelven respuestas mock coherentes para que la aplicacion nunca se caiga.

---

## 6. Usuario y Contrasena de Prueba

### Administrador
```
Usuario:  admin
Contrasena: admin
```

**URL de acceso:** `https://pasteleria-ecommerce-tfm.vercel.app/admin/login`

### Datos de Ejemplo en Base de Datos

#### Pedidos (8 registros)
| Cliente | Tarta | Estado | Total |
|---|---|---|---|
| Maria Garcia | Chocolate L | Completado | 65 EUR |
| Juan Lopez | Fresa Natural M | En Proceso | 38 EUR |
| Ana Martinez | Limon Merengada S | Pendiente | 42 EUR |
| Pedro Sanchez | Red Velvet XL | Pendiente | 95 EUR |
| Laura Fernandez | Nuez y Caramelo M | Completado | 78 EUR |
| Carlos Ruiz | Vainilla Clasica L | En Proceso | 52 EUR |
| Elena Diaz | Chocolate S | Cancelado | 35 EUR |
| Roberto Moreno | Fresa Natural XL | Pendiente | 120 EUR |

#### Recetas (8 registros)
| Receta | Categoria | Dificultad |
|---|---|---|
| Bizcocho de Chocolate | Bizcocho | Media |
| Crema Chantilly | Crema | Facil |
| Ganache de Chocolate | Cobertura | Facil |
| Buttercream Americano | Cobertura | Facil |
| Mousse de Fresa | Relleno | Media |
| Merengue Italiano | Cobertura | Dificil |
| Pasta de Azucar | Decoracion | Dificil |
| Frangipan | Relleno | Facil |

---

## 7. Endpoints API Principales

### Publicos (sin autenticacion)
| Metodo | Endpoint | Descripcion |
|---|---|---|
| GET | `/api/tartas` | Listar tartas activas |
| GET | `/api/tartas/{id}` | Detalle de tarta |
| GET | `/api/tartas/search?q=` | Buscar tartas |
| GET | `/api/products` | Galeria de imagenes |
| POST | `/api/chat` | Chat virtual con IA |
| POST | `/api/chat/order` | Generar resumen de pedido |

### Protegidos (requieren JWT ADMIN)
| Metodo | Endpoint | Descripcion |
|---|---|---|
| POST | `/api/auth/login` | Login y obtener token |
| CRUD | `/api/admin/tartas` | Gestion de tartas |
| CRUD | `/api/admin/orders` | Gestion de pedidos |
| CRUD | `/api/admin/materia-prima` | Gestion materia prima |
| CRUD | `/api/admin/ingredients` | Gestion ingredientes |
| CRUD | `/api/admin/recipes` | Gestion recetas |
| POST | `/api/admin/ai/hashtags` | Generar hashtags con IA |
| POST | `/api/admin/ai/generate-description` | Generar descripcion con IA |
| POST | `/api/admin/ai/analyze-tarta` | Analizar foto con IA |
| POST | `/api/admin/ai/inventory-optimize` | Optimizar inventario con IA |
| POST | `/api/setup/seed-orders` | Crear pedidos de ejemplo |
| POST | `/api/setup/seed-recipes` | Crear recetas de ejemplo |

---

## 8. Enlaces

| Recurso | URL |
|---|---|
| Frontend (Produccion) | https://pasteleria-ecommerce-tfm.vercel.app |
| Backend API | https://belieta-backend.onrender.com |
| GitHub | https://github.com/fportillomasteria2026/pasteleria-ecommerce-tfm |
| Admin Login | https://pasteleria-ecommerce-tfm.vercel.app/admin/login |

---

## 9. Licencia

Proyecto realizado como Trabajo de Fin de Master (TFM).
