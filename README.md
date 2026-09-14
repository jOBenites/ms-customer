# ms-customer

Microservicio de gestión de clientes del sistema bancario. Expone CRUD completo para clientes personales y empresariales, y publica el evento `bank.customer.created` al registrar un nuevo cliente.

## Requisitos

- Java 17
- Maven 3.9+
- MongoDB (puerto 27017)
- Kafka (puerto 9092)
- Config Server corriendo en puerto 8888

## Variables de entorno

El servicio obtiene la configuración desde Config Server. Las variables críticas en `application.yml` local:

| Variable | Valor por defecto | Descripción |
|----------|-------------------|-------------|
| `server.port` | `8081` | Puerto del servicio |
| `spring.config.import` | `optional:configserver:http://localhost:8888` | URL del Config Server |
| `spring.data.mongodb.uri` | `mongodb://...customer_db` | URI de MongoDB (fallback si Config Server no está disponible) |
| `spring.kafka.bootstrap-servers` | `localhost:9092` | Bootstrap de Kafka |

## Levantar

```bash
mvn spring-boot:run
```

O generar el JAR:

```bash
mvn clean package -DskipTests
java -jar target/ms-customer-1.0.0.jar
```

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/customers/personal` | Crear cliente personal |
| POST | `/customers/business` | Crear cliente empresarial |
| GET | `/customers/{id}` | Buscar por ID |
| GET | `/customers` | Listar todos |
| PUT | `/customers/{id}` | Actualizar nombre/perfil |
| DELETE | `/customers/{id}` | Eliminar |

## Base de datos

- **Database:** `customer_db`
- **Colección:** `customer` (con discriminador `_class` para herencia)

## Eventos

| Topic | Trigger | Payload |
|-------|---------|---------|
| `bank.customer.created` | Al crear cualquier cliente | `customerId`, `customerType`, `profile`, `documentNumber`, `occurredAt` |

## Verificar

```bash
mvn verify
```

Ejecuta tests unitarios, Checkstyle y genera reporte JaCoCo.
