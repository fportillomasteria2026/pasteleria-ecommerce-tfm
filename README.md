# Dulce Sabor - E-commerce y ERP para Pastelería Artesanal

## 1. Descripción General

**Dulce Sabor** es una plataforma web completa de gestión para una pastelería artesanal ficticia ubicada en Málaga, España. El proyecto integra un comercio electrónico público con un panel de administración completo (ERP) y herramientas de inteligencia artificial basadas en Google Gemini.

El sistema permite a los clientes explorar el catálogo de tartas, buscar por hashtags, contactar por WhatsApp y utilizar un asistente virtual con IA. El administrador puede gestionar tartas, recetas, materia prima, pedidos, y aprovechar funcionalidades de IA como generación de descripciones, análisis de fotos, hashtags automáticos y optimización de inventario.

---

## 2. Stack Tecnológico

### Frontend
| Tecnología | Versión | Uso |
|---|---|---|
| Angular | 22.1.0 | Framework SPA |
| TypeScript | - | Lenguaje tipado |
| TailwindCSS | 4 | Estilos |
| RxJS | - | Manejo reactivo de datos |
| Angular Signals | - | Estado reactivo |

### Backend
| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 3.4.1 | Framework REST API |
| Spring Security | - | Autenticación JWT |
| JPA / Hibernate | - | ORM para base de datos |
| Maven | 3.9.9 | Build tool |

### Base de Datos
| Tecnología | Uso |
|---|---|
| PostgreSQL (Supabase) | Base de datos relacional en la nube |
| Supabase REST API | Consultas alternativas |

### IA
| Tecnología | Uso |
|---|---|
| Google Gemini 2.5 Flash | Chat virtual, hashtags, descripciones, análisis de fotos, optimización de inventario |
| API REST directa (RestTemplate) | Comunicación con Gemini sin dependencias adicionales |

### Despliegue
| Servicio | Uso |
|---|---|
| Vercel | Hosting del frontend Angular (despliegue automático) |
| Render | Hosting del backend Spring Boot |
| GitHub | Repositorio de código fuente |

---

## 3. Instalación y Ejecución

### Prerrequisitos
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

# 2. Configurar variables de entorno (crear archivo .env en la raíz del proyecto)
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

# Compilar para producción
ng build
```

El frontend arranca en `http://localhost:4200`.

### Variables de Entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `GEMINI_API_KEY` | Clave API de Google Gemini | `AIzaSy...` |
| `SUPABASE_DB_URL` | URL de conexión JDBC a Supabase | `jdbc:postgresql://...` |
| `SUPABASE_DB_USERNAME` | Usuario de la BD | `postgres` |
| `SUPABASE_DB_PASSWORD` | Contraseña de la BD | `tu_password` |
| `JWT_SECRET` | Clave secreta para tokens JWT (mín. 32 bytes) | `tu_clave_segura_aquí_32b` |

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
│   │   │   ├── ProductController.java  # Galería de imágenes
│   │   │   ├── ChatController.java     # Chat virtual + pedidos
│   │   │   ├── AiHashtagsController.java    # IA: hashtags + descripciones + inventario
│   │   │   ├── AiTartaController.java  # IA: análisis de fotos
│   │   │   └── SetupController.java    # Setup inicial + seed datos
│   │   ├── entity/
│   │   │   ├── Tarta.java              # Entidad principal (productos)
│   │   │   ├── Order.java              # Pedidos
│   │   │   ├── MateriaPrima.java       # Materia prima del obrador
│   │   │   ├── Recipe.java             # Recetas de pastelería
│   │   │   ├── Ingredient.java         # Ingredientes
│   │   │   ├── ProductImage.java       # Imágenes galería
│   │   │   ├── User.java               # Usuarios
│   │   │   └── Hashtag.java            # Hashtags galería
│   │   ├── repository/                 # Repositorios JPA
│   │   ├── service/
│   │   │   ├── ChatService.java        # Lógica chat Gemini
│   │   │   ├── AiService.java          # Lógica análisis fotos
│   │   │   └── SupabaseService.java    # Storage Supabase
│   │   └── security/                   # Filtros JWT
│   ├── src/main/resources/
│   │   ├── application.properties      # Configuración principal
│   │   └── application-local.properties # Config local (H2)
│   └── Dockerfile                      # Para Render
│
├── frontend/                           # SPA - Angular
│   ├── src/app/
│   │   ├── components/
│   │   │   ├── landing/                # Página principal pública
│   │   │   ├── gallery/                # Galería de tartas
│   │   │   ├── quienes-somos/          # Página "Quiénes Somos"
│   │   │   ├── chat-widget/            # Widget chat virtual IA
│   │   │   ├── whatsapp-widget/        # Botón WhatsApp
│   │   │   └── admin/
│   │   │       ├── admin-dashboard/    # Dashboard con sidebar
│   │   │       ├── tartas/             # Gestión tartas + IA foto + hashtags
│   │   │       ├── orders/             # Gestión pedidos
│   │   │       ├── ingredients/        # Gestión materia prima
│   │   │       ├── recipes/            # Gestión recetas
│   │   │       ├── inventory-ai/       # IA optimización inventario
│   │   │       ├── product-upload/     # Subida imágenes galería
│   │   │       └── login/              # Login administrador
│   │   ├── services/
│   │   │   └── api.ts                  # Servicio HTTP centralizado
│   │   ├── guards/
│   │   │   └── admin-guard.ts          # Guard de autenticación
│   │   └── interceptors/               # Interceptor JWT
│   └── angular.json
│
├── .env                                # Variables de entorno
├── vercel.json                         # Configuración Vercel (SPA routing)
└── README.md                           # Este fichero
```

---

## 5. Funcionalidades Principales

### 5.1 Área Pública

#### Página Principal (Landing)
- Banner principal con imagen y texto de bienvenida
- Sección de tartas destacadas
- Widget de WhatsApp flotante para contacto directo
- Widget de chat virtual con asistente IA
- Diseño responsive para móvil

#### Galería de Tartas
- Catálogo visual de todas las tartas disponibles
- Tarjetas con imagen, nombre, descripción, tamaño y precio
- Búsqueda por título
- Diseño responsive con grid adaptativo

#### Quiénes Somos
- Información sobre la pastelería
- Valores y filosofía del negocio
- Diseño visual coherente con la marca

### 5.2 Chat Virtual con IA

El asistente virtual utiliza **Google Gemini 2.5 Flash** con un sistema híbrido:

- **Modo Real:** Si la API key de Gemini está configurada, envía el prompt a Gemini con contexto de productos de la base de datos y devuelve respuesta generada por IA
- **Modo Mock (Fallback):** Si Gemini no está disponible o falla, devuelve respuestas predefinidas coherentes

**Funcionalidades:**
- Responde preguntas sobre productos, precios, horarios y dirección
- Contexto automático con todas las tartas activas de la BD
- Botones de acción rápida: "Ver tartas", "Hacer pedido", "Horarios"
- Flujo de pedidos: formulario inline para seleccionar tarta, tamaño, personalización
- Genera resumen de pedido formateado para WhatsApp
- Botón "Enviar por WhatsApp" que abre wa.me con el mensaje

### 5.3 Panel de Administración

#### Login
- Autenticación JWT segura
- Protección de rutas con guard de Angular

#### Dashboard
- Panel lateral (sidebar) con navegación
- Tarjetas de acceso rápido a cada sección
- Responsive con hamburger menu en móvil

#### Gestión de Tartas (CRUD + IA)
- Crear, editar, eliminar tartas
- Campos: SKU, nombre, descripción, imagen, hashtags, tamaño, pisos, forma, dimensiones, bizcocho, crema, frutas, personalización, precio, coste, notas
- **IA - Generar hashtags:** Botón que envía campos a Gemini y genera 15 hashtags relevantes automáticamente
- **IA - Generar descripción:** Botón que genera descripción de marketing con Gemini
- **IA - Crear tarta desde foto:** Sube una imagen y Gemini analiza para crear la tarta automáticamente
- Selector de imágenes (local o servidor)

#### Gestión de Pedidos (CRUD)
- Crear, editar, eliminar pedidos
- Cambio rápido de estado: Pendiente → En Proceso → Completado
- Estados: PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
- Filtros por estado
- Búsqueda por cliente o tarta
- Campos: cliente, teléfono, tarta, tamaño, personalización, total, notas, estado

#### Gestión de Materia Prima (CRUD)
- Crear, editar, eliminar ingredientes del obrador
- Campos: SKU, nombre, marca, proveedor, coste, formato, peso, unidad, cantidad

#### Gestión de Recetas (CRUD)
- Crear, editar, eliminar recetas
- Campos: nombre, tarta asociada, categoría, porciones, tiempo prep./cocción, dificultad, ingredientes, instrucciones, notas
- Categorías: Bizcocho, Crema, Cobertura, Relleno, Decoración, General
- Dificultad: Fácil, Media, Difícil

#### IA - Optimización de Inventario
- Analiza todo el stock de materia prima con Gemini
- Devuelve sugerencias de reposición con prioridad (alta/media/baja)
- Cantidad sugerida y coste estimado
- Mock fallback si Gemini no está disponible

#### Galería de Imágenes (CRUD)
- Subida de imágenes de productos
- Asignación de hashtags por imagen
- Búsqueda por hashtags

### 5.4 Inteligencia Artificial (Google Gemini)

Todas las funcionalidades de IA siguen el patrón **Modo Híbrido**:

| Funcionalidad | Endpoint | Descripción |
|---|---|---|
| Chat virtual | `POST /api/chat` | Asistente conversacional con contexto de productos |
| Generar hashtags | `POST /api/admin/ai/hashtags` | 15 hashtags relevantes para una tarta |
| Generar descripción | `POST /api/admin/ai/generate-description` | Descripción de marketing automática |
| Analizar foto tarta | `POST /api/admin/ai/analyze-tarta` | Crea tarta desde imagen (visión) |
| Optimizar inventario | `POST /api/admin/ai/inventory-optimize` | Sugiere reposiciones según stock |

**Patrón de fallback:** Si `GEMINI_API_KEY` no está configurada o la llamada falla, todas las funcionalidades devuelven respuestas mock coherentes para que la aplicación nunca se caiga.

---

## 6. Usuario y Contraseña de Prueba

### Administrador
```
Usuario:  admin
Contraseña: admin
```

**URL de acceso:** `https://pasteleria-ecommerce-tfm.vercel.app/admin/login`

### Datos de Ejemplo en Base de Datos

#### Pedidos (8 registros)
| Cliente | Tarta | Estado | Total |
|---|---|---|---|
| María García | Chocolate L | Completado | 65 EUR |
| Juan López | Fresa Natural M | En Proceso | 38 EUR |
| Ana Martínez | Limón Merengada S | Pendiente | 42 EUR |
| Pedro Sánchez | Red Velvet XL | Pendiente | 95 EUR |
| Laura Fernández | Nuez y Caramelo M | Completado | 78 EUR |
| Carlos Ruiz | Vainilla Clásica L | En Proceso | 52 EUR |
| Elena Díaz | Chocolate S | Cancelado | 35 EUR |
| Roberto Moreno | Fresa Natural XL | Pendiente | 120 EUR |

#### Recetas (8 registros)
| Receta | Categoría | Dificultad |
|---|---|---|
| Bizcocho de Chocolate | Bizcocho | Media |
| Crema Chantilly | Crema | Fácil |
| Ganache de Chocolate | Cobertura | Fácil |
| Buttercream Americano | Cobertura | Fácil |
| Mousse de Fresa | Relleno | Media |
| Merengüe Italiano | Cobertura | Difícil |
| Pasta de Azúcar | Decoración | Difícil |
| Frangipan | Relleno | Fácil |

---

## 7. Endpoints API Principales

### Públicos (sin autenticación)
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/tartas` | Listar tartas activas |
| GET | `/api/tartas/{id}` | Detalle de tarta |
| GET | `/api/tartas/search?q=` | Buscar tartas |
| GET | `/api/products` | Galería de imágenes |
| POST | `/api/chat` | Chat virtual con IA |
| POST | `/api/chat/order` | Generar resumen de pedido |

### Protegidos (requieren JWT ADMIN)
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Login y obtener token |
| CRUD | `/api/admin/tartas` | Gestión de tartas |
| CRUD | `/api/admin/orders` | Gestión de pedidos |
| CRUD | `/api/admin/materia-prima` | Gestión materia prima |
| CRUD | `/api/admin/ingredients` | Gestión ingredientes |
| CRUD | `/api/admin/recipes` | Gestión recetas |
| POST | `/api/admin/ai/hashtags` | Generar hashtags con IA |
| POST | `/api/admin/ai/generate-description` | Generar descripción con IA |
| POST | `/api/admin/ai/analyze-tarta` | Analizar foto con IA |
| POST | `/api/admin/ai/inventory-optimize` | Optimizar inventario con IA |
| POST | `/api/setup/seed-orders` | Crear pedidos de ejemplo |
| POST | `/api/setup/seed-recipes` | Crear recetas de ejemplo |

---

## 8. Enlaces

| Recurso | URL |
|---|---|
| Frontend (Producción) | https://pasteleria-ecommerce-tfm.vercel.app |
| Backend API | https://belieta-backend.onrender.com |
| GitHub | https://github.com/fportillomasteria2026/pasteleria-ecommerce-tfm |
| Admin Login | https://pasteleria-ecommerce-tfm.vercel.app/admin/login |

---

## 9. Presentacion

**URL de acceso a la presentacion:** `https://pasteleria-ecommerce-tfm.vercel.app/presentacion/`

Presentacion interactiva con 14 diapositivas que incluye:
- Descripcion del proyecto y objetivos
- Stack tecnologico y arquitectura
- Funcionalidades publicas y de administracion
- Inteligencia artificial (5 funcionalidades con Gemini)
- Datos de ejemplo y demo de acceso
- Codigo fuente en GitHub

Tambien disponible en: `presentacion/index.html` (abrir en navegador local)

---

## 10. Licencia

Proyecto realizado como Trabajo de Fin de Máster (TFM).
