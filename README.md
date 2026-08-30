# Petclinic API Tests

Автотесты REST API для [Spring PetClinic REST](https://github.com/spring-petclinic/spring-petclinic-rest).

## Требования

- Java 17 или выше. В `pom.xml` задан `java.version=17`; проект проверен на Java 21.0.12.
- Maven 3.9 или выше. Проект проверен на Maven 3.9.16.
- Git.
- Docker Desktop с включённой интеграцией WSL2 или Docker Engine в WSL2 — если PetClinic запускается в контейнере.

Gradle в проекте не используется.

## Запуск Spring PetClinic REST

### Вариант 1. Docker

В Ubuntu/WSL2 выполните:

```bash
docker pull springcommunity/spring-petclinic-rest
docker run --rm --name spring-petclinic-rest -p 9966:9966 springcommunity/spring-petclinic-rest
```

Оставьте этот терминал запущенным. Приложение будет доступно по адресу:

- Swagger UI: http://localhost:9966/petclinic/swagger-ui/index.html
- OpenAPI: http://localhost:9966/petclinic/v3/api-docs
- Health check: http://localhost:9966/petclinic/actuator/health

Проверить доступность из WSL2 можно командой:

```bash
curl http://localhost:9966/petclinic/actuator/health
```

Остановить контейнер после завершения работы:

```bash
docker stop spring-petclinic-rest
```

### Вариант 2. Запуск из исходников PetClinic

```bash
git clone https://github.com/spring-petclinic/spring-petclinic-rest.git
cd spring-petclinic-rest
./mvnw spring-boot:run
```

Официальный проект использует встроенную H2-базу данных, поэтому отдельная база для стандартного запуска не требуется.

## Запуск автотестов

Перейдите в каталог этого проекта:

```bash
cd "/path/to/petclinic-api-tests"
```

Замените `/path/to` на фактический путь к проекту. Для WSL2 Windows-диск `D:` доступен через `/mnt/d`.

Например:

```bash
cd "/mnt/d/<ваша-папка>/petclinic-api-tests"
```

Запустите тесты:

```bash
mvn clean test -DbaseUrl=http://localhost:9966/petclinic
```

Параметр `baseUrl` можно не указывать: такое же значение задано по умолчанию в `src/test/resources/application.properties`.

Полный прогон завершается `BUILD FAILURE` ожидаемо: автотесты выявили реальные дефекты в API и показывают их в результатах прогона. Найденные расхождения описаны ниже как `PetBug-1..3`; соответствующие тесты намеренно оставлены активными.

Если Maven запускается из Windows PowerShell, используйте:

```powershell
.\mvnw.cmd clean test -DbaseUrl=http://localhost:9966/petclinic
```

Тесты создают временных владельцев со случайными данными и удаляют их после сценария. Перед запуском PetClinic должен быть доступен по `baseUrl`.

## Continuous Integration

Workflow [`.github/workflows/api-tests.yml`](.github/workflows/api-tests.yml) запускается для `push` и `pull_request`. Он устанавливает Java 17, поднимает Spring PetClinic REST в Docker, ждёт доступности health-check, запускает тесты и сохраняет Surefire и Allure-отчёты как артефакты GitHub Actions.

Поскольку тесты известных дефектов пока остаются активными, CI будет показывать failed job до исправления `PetBug-1..3` в тестируемом приложении.

## Allure-отчёт

После выполнения тестов сформируйте HTML-отчёт:

```bash
mvn allure:report
```

Не открывайте `index.html` напрямую через `file://`: Chrome и Opera могут заблокировать загрузку внутренних файлов отчёта.

Для Windows PowerShell запустите временный локальный сервер:

```powershell
npx.cmd --yes http-server ".\target\site\allure-maven-plugin" -p 8000
```

Для WSL2/Linux:

```bash
npx --yes http-server ./target/site/allure-maven-plugin -p 8000
```

После запуска откройте в браузере:

```text
http://localhost:8000
```

Также можно использовать встроенный сервер Maven:

```bash
mvn allure:serve
```

Результаты тестов сохраняются в `target/allure-results`. В отчёт также добавляются HTTP-запросы и ответы.

## Дополнительные настройки

- URL приложения переопределяется параметром `-DbaseUrl=...`.
- В `baseUrl` должен присутствовать контекст приложения `/petclinic`.
- Текущий проект ожидает, что PetClinic уже запущен отдельно и доступен по указанному `baseUrl`.
- Тесты используют REST-клиенты из `src/main/java`, DTO находятся в `dto`, а общие проверки — в `src/test/java/.../assertion`.

## Найденные расхождения API

Падающие тесты оставлены активными, чтобы они фиксировали дефекты, а не скрывали их. Для каждого сценария добавлены JUnit 5 `@Tag` и Allure `@Issue`.

| Идентификатор | Суть | Тест |
|---|---|---|
| `PetBug-1` | Второй владелец с уже занятым номером телефона создаётся с HTTP `201`. Ожидается отклонение `400` или `409`. | [`OwnerDuplicateTests.duplicateTelephoneShouldBeRejected()`](src/test/java/com/example/petclinicapitests/OwnerDuplicateTests.java#L26) |
| `PetBug-2` | Для отрицательного `ownerId` API возвращает HTTP `500`, хотя Swagger описывает `400`. | [`getOwnerWithNegativeIdShouldReturnBadRequest()`](src/test/java/com/example/petclinicapitests/OwnerIdValidationTests.java#L30), [`updateOwnerWithNegativeIdShouldReturnBadRequest()`](src/test/java/com/example/petclinicapitests/OwnerIdValidationTests.java#L46), [`deleteOwnerWithNegativeIdShouldReturnBadRequest()`](src/test/java/com/example/petclinicapitests/OwnerIdValidationTests.java#L68) |
| `PetBug-3` | Для невалидных параметров пагинации (`page=-1`, `size=0`, `size=101`) API возвращает HTTP `500`, хотя Swagger описывает `400`. | [`OwnerPaginationTests.listOwnersPageShouldRejectInvalidPagination()`](src/test/java/com/example/petclinicapitests/OwnerPaginationTests.java#L44) |
| `PetBug-4` | Swagger указывает `200` для `PUT`/`DELETE` владельца, а фактический API возвращает `204`. CRUD-тест учитывает фактический успешный ответ и проверяет состояние ресурса. | [`OwnerCrudTests.ownerCrudFlow()`](src/test/java/com/example/petclinicapitests/OwnerCrudTests.java#L24) |
