# Spring Boot Application

## Описание
Spring Boot приложение с JPA, базой данных и REST API для управления пользователями.

## Технологии
- Java 21
- Spring Boot 3.1.0
- Spring Data JPA
- H2 Database (разработка)
- PostgreSQL (продакшен)
- Gradle 8.14.3

## Запуск приложения

### Требования
- Java 21 или выше
- Gradle 8.7 или выше

### Команды для запуска

1. Сборка проекта:
```bash
gradle build
```

2. Запуск приложения в режиме разработки (H2):
```bash
gradle bootRun --args='--spring.profiles.active=dev'
```

Или запуск JAR файла:
```bash
java -jar build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

3. Запуск приложения в продакшене (PostgreSQL):
```bash
java -jar build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Конфигурация

### Режим разработки (dev)
- База данных: H2 в памяти
- URL: `jdbc:h2:mem:testdb`
- Консоль H2: http://localhost:8080/h2-console
- Пользователь: `sa`
- Пароль: `password`

### Режим продакшена (prod)
- База данных: PostgreSQL
- Переменные окружения:
  - `DATABASE_URL`: URL базы данных
  - `DATABASE_USERNAME`: имя пользователя
  - `DATABASE_PASSWORD`: пароль

## API Endpoints

### GET /welcome
Возвращает приветственное сообщение.

**Пример запроса:**
```bash
curl http://localhost:8080/welcome
```

**Ответ:**
```
Welcome to Spring
```

### Пользователи (User API)

#### GET /api/users
Получить всех пользователей.

#### GET /api/users/{id}
Получить пользователя по ID.

#### POST /api/users
Создать нового пользователя.

**Пример запроса:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe", 
    "email": "john@example.com",
    "password": "password123"
  }'
```

#### PUT /api/users/{id}
Обновить пользователя.

#### DELETE /api/users/{id}
Удалить пользователя.

## Модель данных

### User
- `id` - уникальный идентификатор (автогенерация)
- `firstName` - имя пользователя
- `lastName` - фамилия пользователя  
- `email` - адрес электронной почты (уникальный)
- `password` - пароль
- `createdAt` - дата создания
- `updatedAt` - дата обновления

## Структура проекта
```
app/
├── build.gradle              # Конфигурация Gradle
├── settings.gradle           # Настройки проекта
├── src/
│   ├── main/
│   │   ├── java/hexlet/code/app/
│   │   │   ├── AppApplication.java      # Главный класс
│   │   │   ├── WelcomeController.java   # Базовый контроллер
│   │   │   ├── controller/
│   │   │   │   └── UserController.java  # Контроллер пользователей
│   │   │   ├── model/
│   │   │   │   └── User.java            # Модель пользователя
│   │   │   └── repository/
│   │   │       └── UserRepository.java  # Репозиторий пользователей
│   │   └── resources/
│   │       ├── application.properties   # Основная конфигурация
│   │       ├── application-dev.properties # Конфигурация разработки
│   │       └── application-prod.properties # Конфигурация продакшена
│   └── test/
│       └── java/hexlet/code/app/
│           └── AppApplicationTests.java  # Тесты
└── README.md
```

## Параметры проекта
- **Имя проекта:** app
- **Группа:** hexlet.code
- **Версия:** 0.0.1-SNAPSHOT
- **Порт:** 8080

## Задеплоенное приложение
Приложение доступно по адресу: http://localhost:8080/welcome

### API Endpoints
- Пользователи: http://localhost:8080/api/users
- H2 Консоль: http://localhost:8080/h2-console (только в режиме разработки)
