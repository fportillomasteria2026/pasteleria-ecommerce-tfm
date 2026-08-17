# Prompt Maestro: Sistema de Gestión y E-commerce con IA (TFM)

## 1. Contexto y Objetivo del Proyecto
Actúa como un desarrollador Full-Stack Senior y Arquitecto de Software. El objetivo es desarrollar una aplicación web funcional que sirva como Trabajo de Fin de Máster (TFM) en Inteligencia Artificial.
El proyecto es una "Plantilla de E-commerce y ERP" adaptable, configurada inicialmente para una Pastelería. Debe incluir un portal público para clientes y un panel de administración privado para gestionar el negocio, destacando un módulo central de IA Multimodal.

## 2. Stack Tecnológico Estricto
Debes utilizar exclusivamente las siguientes tecnologías:
*   **Frontend:** Angular (versión 17 o superior) con TailwindCSS.
*   **Backend:** Java 17/21 con Spring Boot 3.x.
*   **Seguridad:** Spring Security con autenticación basada en JWT.
*   **Inteligencia Artificial:** Spring AI integrado con la API de Google Gemini (modelo Multimodal Vision).
*   **Base de Datos:** PostgreSQL (alojada en Supabase) usando Spring Data JPA.
*   **Almacenamiento de Archivos:** Supabase Storage (o almacenamiento local en su defecto).

## 3. Arquitectura y Roles del Sistema

### Nivel 1: Portal Público (Usuarios/Clientes)
*   **Landing Page:** Presentación del negocio y productos destacados.
*   **Galería Inteligente:** Un buscador visual donde se muestran imágenes de los productos. Debe incluir un buscador por *hashtags* (ej. `#chocolate`, `#boda`, `#fondant`).
*   **Widget de WhatsApp:** Un componente visual flotante que redirija a la API de WhatsApp (`wa.me`) con un mensaje preconfigurado.

### Nivel 2: Panel de Administración (Rol ADMIN)
*   **Login Privado:** Acceso protegido por usuario y contraseña (JWT). Las credenciales por defecto deben ser `admin / admin`.
*   **Gestión de Inventario y Recetas:** CRUD para controlar stock de ingredientes y fórmulas.
*   **Gestión de Pedidos y Facturas:** CRUD para administrar estados de pedidos.
*   **Módulo IA (El núcleo del TFM):** Un formulario para subir nuevas fotos de productos. Al subir la foto, el backend debe enviarla al modelo de IA (Gemini Vision) para que genere automáticamente una lista de hashtags descriptivos. Estos hashtags se guardan en la base de datos vinculados a la imagen para alimentar el buscador público.

## 4. Modelo de Datos Propuesto (JPA Entities)
Diseña las entidades basándote en esta estructura lógica:
*   `User`: id, username, password, role.
*   `ProductImage`: id, imageUrl, title, description, createdAt.
*   `Hashtag`: id, name.
*   `ProductImage_Hashtag`: Relación Many-to-Many.
*   `Ingredient` (Inventario): id, name, stockQuantity, unit.
*   `Recipe`: id, name, instructions.
*   `Order`: id, customerName, status, totalAmount.

## 5. Instrucciones de Ejecución Paso a Paso para el Agente
No generes todo el código de golpe. Pregúntame antes de avanzar a la siguiente fase y espera mi confirmación. Ejecuta el proyecto en estas fases:

### FASE 1: Configuración Base y Base de Datos
1.  Genera el archivo `pom.xml` de Spring Boot con las dependencias necesarias (Web, JPA, PostgreSQL, Security, JWT, Spring AI, Lombok).
2.  Crea la configuración del archivo `application.properties` preparada para conectar a Supabase e inyectar la variable `${GEMINI_API_KEY}`.
3.  Genera las entidades JPA y sus respectivos repositorios.

### FASE 2: Backend (IA y Seguridad)
1.  Implementa la configuración de Spring Security y el filtro JWT para proteger las rutas `/api/admin/**`.
2.  Desarrolla el servicio `AiService` utilizando Spring AI para enviar una imagen a Gemini y recibir un JSON con 5 hashtags.
3.  Crea los controladores REST (`ProductController`, `InventoryController`, `AiController`).

### FASE 3: Frontend (Angular)
1.  Genera la estructura de componentes: `landing`, `gallery`, `admin-dashboard`, `login`, `whatsapp-widget`.
2.  Implementa los servicios HTTP (`auth.service`, `api.service`) con un interceptor para añadir el token JWT.
3.  Crea la interfaz de subida de imágenes en el panel de admin, conectándola con el endpoint de IA para mostrar los hashtags generados automáticamente.
4.  Crea el buscador público de la galería filtrando por hashtags.

### FASE 4: Documentación TFM (README.md)
1.  Genera un archivo `README.md` exhaustivo que cumpla con los requisitos universitarios:
    *   Descripción detallada.
    *   Stack tecnológico.
    *   Instrucciones de compilación y ejecución.
    *   Estructura del proyecto.
    *   Credenciales de prueba (`admin / admin`).
    *   Espacios en blanco para que el usuario añada los links a Vercel, Render, Slides y Vídeo.

## 6. Reglas Estrictas de Código
*   Usa código limpio, modular y documentado.
*   Aplica buenas prácticas de inyección de dependencias en Spring y Angular.
*   Maneja las excepciones globalmente en el backend (`@ControllerAdvice`).
*   No utilices librerías externas innecesarias para el UI de Angular; utiliza TailwindCSS puro para los estilos.