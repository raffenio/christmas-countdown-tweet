# Christmas Countdown Tweet

Aplicación Spring Boot que envía un tweet diario con la cuenta regresiva de días faltantes para Navidad.

## Estructura del proyecto

```
christmas-countdown-tweet/
├── pom.xml
├── .env.example
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/raffenio/christmascountdown/
    │   │   ├── ChristmasCountdownApplication.java   ← entry point
    │   │   ├── config/
    │   │   │   ├── TwitterConfig.java               ← bean Twitter4J
    │   │   │   └── TwitterProperties.java           ← @ConfigurationProperties
    │   │   ├── service/
    │   │   │   ├── CountdownService.java            ← calcula días y arma el tweet
    │   │   │   └── TwitterService.java              ← publica via Twitter API
    │   │   └── scheduler/
    │   │       └── TweetScheduler.java              ← @Scheduled cron diario
    │   └── resources/application.properties
    └── test/
        └── CountdownServiceTest.java
```

## Requisitos

- Java 17+
- Maven 3.8+
- Cuenta de desarrollador en [developer.twitter.com](https://developer.twitter.com) con permisos de escritura (OAuth 1.0a)

## Configuración

### 1. Obtener credenciales de Twitter

1. Ingresa a [developer.twitter.com](https://developer.twitter.com) y crea una app.
2. En la sección **Keys and Tokens** obtén:
   - API Key
   - API Secret
   - Access Token
   - Access Token Secret
3. Asegúrate de que la app tenga permisos de **lectura y escritura** (Read and Write).

### 2. Configurar variables de entorno

Copia el archivo de ejemplo y completá con tus credenciales reales:

```bash
cp .env.example .env
```

Editá `.env`:

```env
TWITTER_API_KEY=tu_api_key_aqui
TWITTER_API_SECRET=tu_api_secret_aqui
TWITTER_ACCESS_TOKEN=tu_access_token_aqui
TWITTER_ACCESS_TOKEN_SECRET=tu_access_token_secret_aqui
```

> **Nunca subas el archivo `.env` al repositorio.** Ya está incluido en `.gitignore`.

### 3. Configurar horario (opcional)

En `src/main/resources/application.properties` podés ajustar el cron y la zona horaria:

```properties
# Todos los días a las 9:00 AM
tweet.schedule.cron=0 0 9 * * *
tweet.schedule.timezone=America/Argentina/Buenos_Aires
```

Ejemplos de cron:

| Expresión            | Descripción                        |
|----------------------|------------------------------------|
| `0 0 9 * * *`        | Todos los días a las 9:00 AM       |
| `0 30 8 * * *`       | Todos los días a las 8:30 AM       |
| `0 0 12 * * MON-FRI` | Lunes a viernes al mediodía        |

## Cómo ejecutarlo

### Exportar las variables de entorno y correr con Maven

```bash
export TWITTER_API_KEY=tu_api_key
export TWITTER_API_SECRET=tu_api_secret
export TWITTER_ACCESS_TOKEN=tu_access_token
export TWITTER_ACCESS_TOKEN_SECRET=tu_access_token_secret

mvn spring-boot:run
```

### Generar el JAR y ejecutarlo

```bash
mvn clean package

java -jar target/christmas-countdown-tweet-1.0.0.jar \
  --twitter.api-key=TU_API_KEY \
  --twitter.api-secret=TU_API_SECRET \
  --twitter.access-token=TU_ACCESS_TOKEN \
  --twitter.access-token-secret=TU_ACCESS_TOKEN_SECRET
```

### Correr los tests

```bash
mvn test
```

## Ejemplos de tweets generados

| Días restantes | Mensaje publicado                                                              |
|----------------|--------------------------------------------------------------------------------|
| > 100          | `❄️ ¡Faltan 201 días para Navidad! 🎅🎁 #CuentaRegresiva #Navidad #Christmas` |
| 8 – 100        | `🎄 ¡Faltan 45 días para Navidad! 🎅🎁 #CuentaRegresiva #Navidad #Christmas`  |
| 2 – 7          | `🎄✨ ¡Faltan 5 días para Navidad! 🎅🎁 #CuentaRegresiva #Navidad #Christmas` |
| 1              | `🎄 ¡Solo falta 1 día para Navidad! 🎅 ¡Mañana es el gran día! ✨🎁`          |
| 0              | `🎄🎅 ¡HOY ES NAVIDAD! ¡Feliz Navidad a todos! ✨🎁 #FelizNavidad`            |

## Tecnologías

| Tecnología       | Versión | Uso                              |
|------------------|---------|----------------------------------|
| Spring Boot      | 3.2.5   | Framework principal              |
| Twitter4J        | 4.1.2   | Cliente para la Twitter API v1.1 |
| Java             | 17      | Lenguaje                         |
| Maven            | 3.8+    | Build y dependencias             |
