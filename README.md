 # Mendel - api-transactions
 
 Este proyecto es una **API REST de ejemplo** para gestionar transacciones en memoria usando Spring Boot, H2 y JPA.

 ## Tecnologías principales
 - Spring Boot 4
 - Spring Data JPA (Jakarta Persistence)
 - H2 Database (in-memory) con consola web
 - Spring Boot Actuator (/actuator/health)
 - Lombok

 ## Estructura básica
 * `com.mendel.rest.controllers` - controladores web
 * `com.mendel.rest.models` - entidades JPA
 * `com.mendel.rest.services` - lógica de negocio
 * `com.mendel.rest.repository` - repositorios Spring Data

 ## Endpoints disponibles

 | Método | Ruta | Descripción | Body / Parámetros |
 |--------|------|-------------|-------------------|
 | PUT | `/transactions/{transaction_id}` | Inserta o actualiza una transacción con el ID especificado. | JSON: `{amount,double,type,string,parent_id,long}` (sin `transaction_id`) |
 | GET | `/transactions/all` | Lista todas las transacciones. | Ninguno |
 | GET | `/transactions/types/{type}` | Lista transacciones de un tipo concreto. | Path variable `type` |
 | GET | `/health` | Endpoint de salud simple (OK). | Ninguno |
 | GET | `/actuator/health` | Endpoint Actuator con estado en JSON. | Ninguno |

 ### Ejemplos de uso

 **Insertar/actualizar transacción**
 ```bash
 curl -X PUT http://localhost:8080/transactions/1 \
	 -H "Content-Type: application/json" \
	 -d '{"parent_id":1,"type":"cart","amount":2000.00}'
 ```

 **Consultar todas**
 ```bash
 curl http://localhost:8080/transactions/all
 ```

 **Consultar por tipo**
 ```bash
 curl http://localhost:8080/transactions/types/cart
 ```

 **Health**
 ```bash
 curl http://localhost:8080/health
 curl http://localhost:8080/actuator/health
 ```

 ## Configuración de la base de datos
 Se utiliza H2 en memoria. La consola está disponible en `http://localhost:8080/h2-mendel` con usuario `sa` y sin contraseña. Los datos se recrean en cada arranque (DDL `create-drop`).

 ## Tests
 El proyecto contiene una suite de tests que carga el contexto y muestra ejemplos de requests.

 Ejecutar:
 ```bash
 mvn clean test
 ```

 ## Ejecución
 ```bash
 cd api-transactions
 mvn spring-boot:run
 ```

 La aplicación corre en `http://localhost:8080`.

 ## Notas
 - `transaction_id` se debe especificar en la URL del PUT.
 - Los DTOs usan Lombok (`@Data`, `@NoArgsConstructor`).

