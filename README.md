# 📚 Práctica Relacións + Swagger - Spring Boot

> **Autor:** Daniel Rodríguez Tato  
> **Curso:** 2º DAM - Acceso a Datos  
> **Tecnoloxías:** Spring Boot 3.4.2 + PostgreSQL + Swagger (OpenAPI)

---

## 📋 Índice

1. [Que é este proxecto?](#-que-é-este-proxecto)
2. [Estrutura do proxecto](#-estrutura-do-proxecto)
3. [Tecnoloxías utilizadas](#-tecnoloxías-utilizadas)
4. [Configuración da base de datos](#-configuración-da-base-de-datos)
5. [Os Modelos (Entidades)](#-os-modelos-entidades)
6. [Relacións entre entidades](#-relacións-entre-entidades)
7. [Arquitectura por capas](#-arquitectura-por-capas)
8. [Endpoints da API](#-endpoints-da-api)
9. [Como executar o proxecto](#-como-executar-o-proxecto)
10. [Probas con Swagger](#-probas-con-swagger)
11. [Exemplos de uso](#-exemplos-de-uso)

---

## 🎯 Que é este proxecto?

Este é un proxecto **API REST** que xestiona a relación entre **Titores** e **Alumnos**. 

- Un **Titor** pode ter moitos **Alumnos** (relación 1:N)
- Un **Alumno** só pertence a un **Titor**

O proxecto usa **Spring Boot** como framework e **PostgreSQL** como base de datos.

---

## 📁 Estrutura do proxecto

```
src/main/java/org/example/
├── Main.java                    # 🚀 Clase principal (arranca a aplicación)
├── config/
│   └── OpenApiConfig.java       # ⚙️ Configuración de Swagger
├── controller/
│   ├── AlumnoController.java    # 🎮 Endpoints de Alumnos
│   └── TitorController.java     # 🎮 Endpoints de Titores
├── model/
│   ├── Alumno.java              # 📦 Entidade Alumno
│   └── Titor.java               # 📦 Entidade Titor
├── repository/
│   ├── AlumnoRepository.java    # 💾 Acceso a datos Alumno
│   └── TitorRepository.java     # 💾 Acceso a datos Titor
└── service/
    ├── AlumnoService.java       # 🔧 Lóxica de negocio Alumno
    └── TitorService.java        # 🔧 Lóxica de negocio Titor
```

---

## 🛠 Tecnoloxías utilizadas

| Tecnoloxía | Versión | Para que serve? |
|------------|---------|-----------------|
| **Java** | 17 | Linguaxe de programación |
| **Spring Boot** | 3.4.2 | Framework para crear aplicacións web |
| **Spring Data JPA** | - | Facilita o acceso a base de datos |
| **PostgreSQL** | - | Base de datos relacional |
| **Hibernate** | - | ORM (mapea obxectos Java a táboas) |
| **Swagger/OpenAPI** | 2.5.0 | Documentación interactiva da API |
| **Maven** | - | Xestor de dependencias |

---

## 🗄 Configuración da base de datos

O arquivo `application.properties` contén a configuración:

```properties
# Conexión á base de datos PostgreSQL
spring.datasource.url=jdbc:postgresql://10.0.9.100:5432/probas
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de Hibernate
spring.jpa.hibernate.ddl-auto=update    # Crea/actualiza táboas automaticamente
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true                # Mostra as consultas SQL na consola

# Porto do servidor
server.port=8081
```

### 🔑 Opcións de `ddl-auto`:
| Valor | Que fai? |
|-------|----------|
| `create` | Borra e crea as táboas cada vez que arranca |
| `create-drop` | Crea ao arrancar, borra ao pechar |
| `update` | **Recomendado** - Actualiza sen borrar datos |
| `validate` | Só valida, non modifica nada |
| `none` | Non fai nada |

---

## 📦 Os Modelos (Entidades)

### Titor.java

```java
@Entity                          // Indica que é unha entidade de base de datos
@Table(name = "titor")           // Nome da táboa
public class Titor {

    @Id                                              // Clave primaria
    @GeneratedValue(strategy = GenerationType.AUTO)  // ID autoxerado
    private Long id_titor;

    @Column(name = "nome", nullable = false, length = 100)  // Columna obrigatoria
    private String nome;

    @Column(name = "apelidos", nullable = false, length = 150)
    private String apelidos;

    @OneToMany(mappedBy = "titor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alumno> alumnos;  // Un titor ten moitos alumnos
    
    // Getters e Setters...
}
```

### Alumno.java

```java
@Entity
@Table(name = "alumno")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_alumno;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "apelidos", nullable = false, length = 150)
    private String apelidos;

    @ManyToOne                      // Moitos alumnos pertencen a un titor
    @JoinColumn(name = "id_titor")  // Columna de clave foránea
    private Titor titor;
    
    // Getters e Setters...
}
```

---

## 🔗 Relacións entre entidades

```
┌─────────────┐         ┌─────────────┐
│   TITOR     │         │   ALUMNO    │
├─────────────┤         ├─────────────┤
│ id_titor PK │◄───────┐│ id_alumno PK│
│ nome        │    1:N ││ nome        │
│ apelidos    │        ││ apelidos    │
└─────────────┘        │└─id_titor FK─┘
                       └───────────────
```

### Anotacións importantes:

| Anotación | Significado |
|-----------|-------------|
| `@OneToMany` | Un titor ten MOITOS alumnos |
| `@ManyToOne` | Moitos alumnos teñen UN titor |
| `mappedBy = "titor"` | O campo "titor" en Alumno xestiona a relación |
| `cascade = CascadeType.ALL` | Se borras un titor, bórranse os seus alumnos |
| `orphanRemoval = true` | Se quitas un alumno da lista, bórrase da BD |
| `@JoinColumn` | Define a columna de clave foránea |

---

## 🏗 Arquitectura por capas

Spring Boot usa unha arquitectura de **3 capas**:

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE (Postman/Swagger)            │
└─────────────────────────┬───────────────────────────────┘
                          │ HTTP (JSON)
                          ▼
┌─────────────────────────────────────────────────────────┐
│  CONTROLLER (Capa de presentación)                      │
│  - Recibe peticións HTTP (GET, POST, PUT, DELETE)       │
│  - Valida datos de entrada                              │
│  - Devolve respostas HTTP                               │
└─────────────────────────┬───────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│  SERVICE (Capa de negocio)                              │
│  - Contén a lóxica da aplicación                        │
│  - Xestiona transaccións (@Transactional)               │
│  - Coordina operacións entre repositorios               │
└─────────────────────────┬───────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│  REPOSITORY (Capa de acceso a datos)                    │
│  - Comunícase coa base de datos                         │
│  - Usa JpaRepository (CRUD automático)                  │
└─────────────────────────┬───────────────────────────────┘
                          │ SQL
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    BASE DE DATOS (PostgreSQL)           │
└─────────────────────────────────────────────────────────┘
```

### Por que usar capas?

1. **Separación de responsabilidades** - Cada capa fai unha cousa
2. **Mantemento** - Podes cambiar unha capa sen afectar ás outras
3. **Testabilidade** - Podes probar cada capa por separado
4. **Reutilización** - Os servizos pódense usar en varios controladores

---

## 🌐 Endpoints da API

### Titores (`/titores`)

| Método | Endpoint | Descrición | Body |
|--------|----------|------------|------|
| `GET` | `/titores` | Obtén todos os titores | - |
| `GET` | `/titores/{id}` | Obtén un titor polo seu ID | - |
| `POST` | `/titores` | Crea un novo titor | JSON |
| `PUT` | `/titores/{id}` | Modifica un titor existente | JSON |
| `DELETE` | `/titores/{id}` | Elimina un titor | - |

### Alumnos (`/alumnos`)

| Método | Endpoint | Descrición | Body |
|--------|----------|------------|------|
| `GET` | `/alumnos/{id}` | Obtén un alumno polo seu ID | - |
| `POST` | `/alumnos` | Crea un novo alumno | JSON |
| `PUT` | `/alumnos/{id}` | Modifica un alumno existente | JSON |
| `DELETE` | `/alumnos/{id}` | Elimina un alumno | - |

---

## 🚀 Como executar o proxecto

### Requisitos previos:
- Java 17 instalado
- Maven instalado
- PostgreSQL correndo (ou cambiar a configuración)

### Pasos:

1. **Clonar ou abrir o proxecto**

2. **Configurar a base de datos** en `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/a_tua_bd
   spring.datasource.username=o_teu_usuario
   spring.datasource.password=o_teu_contrasinal
   ```

3. **Executar o proxecto**:
   ```bash
   # Usando Maven
   mvn spring-boot:run
   
   # Ou compilar e executar
   mvn clean package
   java -jar target/hibernateSwagger-1.0-SNAPSHOT.jar
   ```

4. **Acceder á API**:
   - API: `http://localhost:8081`
   - Swagger UI: `http://localhost:8081/swagger-ui.html`

---

## 📖 Probas con Swagger

Swagger é unha ferramenta que **documenta automaticamente** a túa API e permite probala dende o navegador.

### Como acceder:
1. Arranca a aplicación
2. Abre o navegador en: `http://localhost:8081/swagger-ui.html`

### Interface de Swagger:
```
┌────────────────────────────────────────────────────────────┐
│  🔵 titor-controller                                    ▼  │
├────────────────────────────────────────────────────────────┤
│  GET    /titores        Obtén todos os titores            │
│  POST   /titores        Crea un titor                     │
│  GET    /titores/{id}   Obtén un titor                    │
│  PUT    /titores/{id}   Modifica un titor                 │
│  DELETE /titores/{id}   Elimina un titor                  │
├────────────────────────────────────────────────────────────┤
│  🔵 alumno-controller                                   ▼  │
├────────────────────────────────────────────────────────────┤
│  POST   /alumnos        Crea un alumno                    │
│  GET    /alumnos/{id}   Obtén un alumno                   │
│  PUT    /alumnos/{id}   Modifica un alumno                │
│  DELETE /alumnos/{id}   Elimina un alumno                 │
└────────────────────────────────────────────────────────────┘
```

---

## 📝 Exemplos de uso

### 1️⃣ Crear un Titor

**POST** `http://localhost:8081/titores`

```json
{
    "nome": "María",
    "apelidos": "García López"
}
```

**Resposta:**
```json
{
    "id_titor": 1,
    "nome": "María",
    "apelidos": "García López",
    "alumnos": []
}
```

---

### 2️⃣ Crear un Alumno (asignándoo a un Titor)

**POST** `http://localhost:8081/alumnos`

```json
{
    "nome": "Pedro",
    "apelidos": "Fernández Pérez",
    "titor": {
        "id_titor": 1
    }
}
```

**Resposta:**
```json
{
    "id_alumno": 1,
    "nome": "Pedro",
    "apelidos": "Fernández Pérez",
    "titor": {
        "id_titor": 1,
        "nome": "María",
        "apelidos": "García López"
    }
}
```

---

### 3️⃣ Obter un Titor cos seus Alumnos

**GET** `http://localhost:8081/titores/1`

**Resposta:**
```json
{
    "id_titor": 1,
    "nome": "María",
    "apelidos": "García López",
    "alumnos": [
        {
            "id_alumno": 1,
            "nome": "Pedro",
            "apelidos": "Fernández Pérez"
        },
        {
            "id_alumno": 2,
            "nome": "Ana",
            "apelidos": "Martínez Ruiz"
        }
    ]
}
```

---

### 4️⃣ Modificar un Alumno

**PUT** `http://localhost:8081/alumnos/1`

```json
{
    "nome": "Pedro Pablo",
    "apelidos": "Fernández Pérez",
    "titor": {
        "id_titor": 1
    }
}
```

---

### 5️⃣ Eliminar un Alumno

**DELETE** `http://localhost:8081/alumnos/1`

**Resposta:** `204 No Content` (éxito, sen corpo)

---

## 🔍 Anotacións de Spring importantes

| Anotación | Onde se usa | Para que serve |
|-----------|-------------|----------------|
| `@SpringBootApplication` | Main.java | Arranca Spring Boot |
| `@RestController` | Controller | Indica que é un controlador REST |
| `@RequestMapping` | Controller | Define a ruta base |
| `@GetMapping` | Controller | Mapea peticións GET |
| `@PostMapping` | Controller | Mapea peticións POST |
| `@PutMapping` | Controller | Mapea peticións PUT |
| `@DeleteMapping` | Controller | Mapea peticións DELETE |
| `@PathVariable` | Controller | Obtén valores da URL |
| `@RequestBody` | Controller | Obtén o corpo da petición |
| `@Service` | Service | Indica que é un servizo |
| `@Transactional` | Service | Xestiona transaccións |
| `@Repository` | Repository | Indica que é un repositorio |
| `@Autowired` | Anywhere | Inxección de dependencias |
| `@Entity` | Model | Indica que é unha entidade JPA |
| `@Table` | Model | Nome da táboa |
| `@Id` | Model | Clave primaria |
| `@Column` | Model | Configuración da columna |

---

## ❓ Preguntas frecuentes

### Por que usar `ResponseEntity`?
Permite controlar o **código de resposta HTTP**:
- `200 OK` - Todo ben
- `201 Created` - Recurso creado
- `204 No Content` - Eliminado correctamente
- `404 Not Found` - Non se atopou
- `500 Internal Server Error` - Erro do servidor

### Por que usar `Optional`?
Evita `NullPointerException`. En vez de devolver `null`, devolve un obxecto que pode estar baleiro.

```java
Optional<Titor> titor = titorRepository.findById(1L);
if (titor.isPresent()) {
    // O titor existe
} else {
    // Non existe
}
```

### Que é `@Transactional`?
Garante que todas as operacións dunha función se executan **xuntas**. Se unha falla, revértense todas.

---

## 📚 Recursos para aprender máis

- [Documentación oficial de Spring Boot](https://spring.io/projects/spring-boot)
- [Guía de Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Tutorial de Swagger/OpenAPI](https://springdoc.org/)
- [Baeldung - Tutoriais de Spring](https://www.baeldung.com/spring-boot)

---

**Feito con ❤️ para 2º DAM**

