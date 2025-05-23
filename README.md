# PivotHub

PivotHub es una aplicación de retos y desafíos grupales que permite a los usuarios crear y participar en retos con penalizaciones económicas.

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT para autenticación
- Maven
- Lombok

## Requisitos Previos

- Java 17 o superior
- Maven
- PostgreSQL

## Configuración

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/pivothub.git
cd pivothub
```

2. Configura la base de datos PostgreSQL:
   - Crea una base de datos llamada `pivothub_db`
   - Configura las credenciales en `application.properties`

3. Ejecuta la aplicación:
```bash
./mvnw spring-boot:run
```

## Endpoints Principales

### Autenticación
- `POST /api/auth/register` - Registro de usuarios
- `POST /api/auth/login` - Inicio de sesión

### Retos
- `GET /api/challenges` - Listar todos los retos
- `POST /api/challenges` - Crear un nuevo reto
- `POST /api/challenges/{id}/join` - Unirse a un reto

## Estructura del Proyecto

```
src/main/java/com/example/pivothub/
├── config/          # Configuraciones de Spring
├── controller/      # Controladores REST
├── dto/            # Objetos de transferencia de datos
├── model/          # Entidades JPA
├── repository/     # Repositorios JPA
└── service/        # Lógica de negocio
```

## Contribuir

1. Haz un Fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles. 