# Prices Service - GFT Code Challenge

Servicio de listado de precios desarrollado con Spring Boot como parte del desafío técnico para el puesto de Tech Lead en GFT.

## 📋 Tabla de Contenidos

1. [Prerrequisitos](#-prerrequisitos)
2. [Arquitectura de Diseño](#-arquitectura-de-diseño)
3. [Gestión de Excepciones](#-gestión-de-excepciones)
4. [Esquema de Base de Datos](#-esquema-de-base-de-datos)
5. [Ejecutar la Aplicación y Acceso a Swagger](#-ejecutar-la-aplicación-y-acceso-a-swagger)
6. [Ejecución de Tests](#-ejecución-de-tests)
7. [Ejecución de Tests E2E](#-ejecución-de-tests-e2e)

## 🛠️ Prerrequisitos

Para ejecutar este proyecto necesitas tener instalado:

- **Java JDK 21** o superior
- **Git** (para clonar el repositorio)

> **Nota:** No es necesario instalar Maven ya que el proyecto incluye Maven Wrapper (`./mvnw`)

### Verificar Instalación

```bash
# Verificar Java
java -version

# Verificar que Maven Wrapper funciona
./mvnw -version
```

## 🏗️ Arquitectura de Diseño

El proyecto sigue los principios de **Arquitectura Hexagonal (Ports and Adapters)** y **Clean Architecture**, organizando el código en las siguientes capas:

### Estructura de Capas

```
src/main/java/com/inditex/code/prices/
├── PricesApplication.java              # Punto de entrada de Spring Boot
├── application/                        # Capa de Aplicación
│   └── service/                       # Servicios de aplicación y casos de uso
├── domain/                            # Capa de Dominio
│   ├── dto/                          # Data Transfer Objects
│   ├── port/                         # Puertos (interfaces)
│   └── exception/                    # Excepciones de dominio
├── infrastructure/                    # Capa de Infraestructura
│   ├── in/                          # Adaptadores de entrada (Controllers)
│   └── out/                         # Adaptadores de salida (Repositories)
└── configuration/                     # Configuración de Spring
```

### Dependencias Utilizadas

- **Spring Boot 3.5.4** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **H2 Database** - Base de datos en memoria para desarrollo y testing
- **Flyway** - Migración de base de datos
- **MapStruct 1.6.2** - Mapeo automático entre entidades y DTOs
- **Lombok** - Reducción de código boilerplate
- **SpringDoc OpenAPI** - Documentación en formato swagger
- **Karate 1.4.1** - Testing E2E


## ⚠️ Gestión de Excepciones

El proyecto implementa un sistema de gestión de excepciones a nivel de controladores REST utilizando el patrón **Global Exception Handler** de Spring Boot.

### Arquitectura de Manejo de Errores

```
Controller Request → Validation → Business Logic → Exception Handler → JSON Response
```

### Componentes Principales

#### 1. GlobalExceptionHandler (`@ControllerAdvice`)

Intercepta todas las excepciones lanzadas por los controladores y las transforma en respuestas HTTP estructuradas:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Manejo centralizado de excepciones
}
```

#### 2. ValidationException (Excepción de Dominio)

Excepción personalizada para errores de validación de reglas de negocio:

```java
public class ValidationException extends RuntimeException {
    private final List<String> errors;
    // Contiene lista de errores específicos
}
```

#### 3. PriceFilterValidator (Validador de Aplicación)

Validador que aplica reglas de negocio a los parámetros de entrada:

```java
@Component
public class PriceFilterValidator {
    public void validate(PriceFilterRequestDto request) {
        // Validaciones específicas de dominio
    }
}
```

## 🗃️ Esquema de Base de Datos

La base de datos utiliza **H2** (en memoria) con las siguientes tablas:

### Tabla BRANDS
```sql
CREATE TABLE BRANDS (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Tabla PRODUCTS
```sql
CREATE TABLE PRODUCTS (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(1000),
    BRAND_ID BIGINT,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BRAND_ID) REFERENCES BRANDS(ID)
);
```

### Tabla PRICES (Principal)
```sql
CREATE TABLE PRICES (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    BRAND_ID BIGINT NOT NULL,
    PRODUCT_ID BIGINT NOT NULL,
    PRICE_LIST INTEGER NOT NULL,
    START_DATE TIMESTAMP NOT NULL,
    END_DATE TIMESTAMP NOT NULL,
    PRIORITY INTEGER NOT NULL DEFAULT 0,
    PRICE DECIMAL(10,2) NOT NULL,
    CURR VARCHAR(3) NOT NULL DEFAULT 'EUR',
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BRAND_ID) REFERENCES BRANDS(ID),
    FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)
);
```

### Datos de Prueba

El sistema incluye los datos de prueba pre cargados para:
- **Brand**: ZARA (ID: 1)
- **Product**: First Product (ID: 35455)
- **4 listas de precios** con diferentes rangos de fechas y prioridades

## 🚀 Ejecutar la Aplicación y Acceso a Swagger

### 1. Clonar y Compilar

```bash
# Clonar el repositorio
git clone <repository-url>
cd gft-code-challenge

# Compilar el proyecto
./mvnw clean compile
```

### 2. Ejecutar la Aplicación

```bash
# Opción 1: Con Maven Wrapper
./mvnw spring-boot:run

# Opción 2: Con JAR compilado
./mvnw clean package
java -jar target/prices-service-0.0.1-SNAPSHOT.jar
```

### 3. Verificar que la Aplicación Está Ejecutándose

```bash
# Health Check
curl http://localhost:8083/v1/health
```

### 4. Acceso a Documentación y Herramientas

| Recurso | URL | Descripción |
|---------|-----|-------------|
| **Swagger UI** | http://localhost:8083/v1/swagger.html | Documentación interactiva de la API |
| **H2 Console** | http://localhost:8083/v1/h2-console | Consola de base de datos H2 |
| **Health Check** | http://localhost:8083/v1/health | Endpoint de salud de la aplicación |

#### Configuración H2 Console:
- **JDBC URL**: `jdbc:h2:mem:pricesdb`
- **User**: `sa`
- **Password**: *(vacío)*

### 5. Endpoints Principales

#### Consultar Precios
```bash
GET /v1/prices?activeDate={fecha}&productId={id}&brandId={id}

# Ejemplo:
curl "http://localhost:8083/v1/prices?activeDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

**Parámetros:**
- `activeDate`: Fecha y hora de consulta (formato ISO 8601)
- `productId`: ID del producto
- `brandId`: ID de la marca

## 🧪 Ejecución de Tests

### Tests Unitarios y de Integración

*Nota: asegurese que no está levantada la aplicación para que no fallen los tests funcionales

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar tests con reporte detallado
./mvnw test -Dtest="*Test"

# Ejecutar tests de una clase específica
./mvnw test -Dtest="PriceServiceTest"
```

### Tipos de Tests Incluidos

1. **Tests Unitarios**
   - `PriceServiceTest` - Lógica de negocio
   - `PriceFilterValidatorTest` - Validaciones
   - `HealthCheckServiceTest` - Servicio de salud

2. **Tests de Integración**
   - `PriceControllerIntegrationTest` - Tests del controlador
   - `PriceFilteringIntegrationTest` - Tests de filtrado

3. **Tests de Validación**
   - `PriceControllerValidationTest` - Validaciones de entrada
   - Tests de DTOs y mappers

## 🎯 Ejecución de Tests E2E

Los tests End-to-End utilizan **Karate Framework** para validar los casos de uso requeridos.

### Ejecutar Tests E2E

```bash
# Ejecutar solo tests E2E de Karate
./mvnw test -Dtest="KarateTests"

# Ejecutar tests E2E con la aplicación ejecutándose
./mvnw test -Dtest="*Karate*"
```

### Casos de Prueba E2E Implementados

Los siguientes escenarios están automatizados según los requerimientos:

| Test | Fecha/Hora | Producto | Brand | Precio Esperado | Lista de Precios |
|------|------------|----------|-------|-----------------|------------------|
| **Test 1** | 2020-06-14 10:00 | 35455 | 1 (ZARA) | 35.50 EUR | 1 |
| **Test 2** | 2020-06-14 16:00 | 35455 | 1 (ZARA) | 25.45 EUR | 2 |
| **Test 3** | 2020-06-14 21:00 | 35455 | 1 (ZARA) | 35.50 EUR | 1 |
| **Test 4** | 2020-06-15 10:00 | 35455 | 1 (ZARA) | 30.50 EUR | 3 |
| **Test 5** | 2020-06-16 21:00 | 35455 | 1 (ZARA) | 38.95 EUR | 4 |

### Archivos de Test E2E

```
src/test/java/com/inditex/code/prices/e2e/
├── KarateTests.java           # Runner de Karate
└── prices/
    └── prices.feature         # Escenarios de prueba
```

### Ver Reportes E2E

Los reportes de Karate se generan en:
```
target/karate-reports/
├── karate-summary.html        # Reporte principal
├── karate-timeline.html       # Timeline de ejecución  
└── *.html                     # Reportes detallados por feature
```

**Acceso al reporte**: `target/karate-reports/karate-summary.html`

### Validaciones de los Tests E2E

Cada test verifica:
- ✅ **Status Code 200** - Respuesta exitosa
- ✅ **Estructura de respuesta** - Formato JSON correcto
- ✅ **Precio aplicable** - Precio correcto según fecha y prioridad
- ✅ **Lista de precios** - Lista correcta aplicada
- ✅ **Metadatos** - Brand ID y Product ID correctos

---

## 📝 Notas Adicionales

### Configuración del Entorno

El proyecto está configurado para ejecutarse en:
- **Puerto**: 8083
- **Context Path**: `/v1`
- **Perfil**: desarrollo (base de datos en memoria)

