# Petclinic API Tests

Автотесты REST API для [Spring PetClinic REST](https://github.com/spring-petclinic/spring-petclinic-rest).

## Требования

- Java 17 или выше;
- Maven 3.9 или выше;
- Docker;
- Node.js и npm — для просмотра Allure-отчёта через локальный сервер.

Проект проверен на Java 21 и Maven 3.9.16.

## Запуск Spring PetClinic REST

Запуск через Docker:

```bash
docker pull springcommunity/spring-petclinic-rest
docker run --rm --name spring-petclinic-rest -p 9966:9966 springcommunity/spring-petclinic-rest
```

После запуска приложение доступно по адресам:

- Swagger UI: http://localhost:9966/petclinic/swagger-ui/index.html
- Health check: http://localhost:9966/petclinic/actuator/health

Остановка контейнера:

```bash
docker stop spring-petclinic-rest
```

## Запуск тестов

Рабочий каталог:

```text
petclinic-api-tests
```

Windows PowerShell:

```powershell
.\mvnw.cmd clean test -DbaseUrl=http://localhost:9966/petclinic
```

WSL2/Linux:

```bash
./mvnw clean test -DbaseUrl=http://localhost:9966/petclinic
```

Полный прогон завершается с `BUILD FAILURE`, поскольку тесты выявляют реальные дефекты в API. Соответствующие тесты остаются активными и отображаются в результатах прогона и Allure-отчёте.

## Allure

Формирование отчёта:

```bash
mvn allure:report
```

Windows PowerShell:

```powershell
npx.cmd --yes http-server ".\target\site\allure-maven-plugin" -p 8000
```

WSL2/Linux:

```bash
npx --yes http-server ./target/site/allure-maven-plugin -p 8000
```

Отчёт доступен по адресу:

```text
http://localhost:8000
```

Открытие `index.html` через `file://` может привести к блокировке загрузки данных браузером.

Результаты тестов сохраняются в:

```text
target/allure-results
```

## GitHub Actions

Workflow [`.github/workflows/api-tests.yml`](.github/workflows/api-tests.yml) запускается для `push` и `pull_request`.

В рамках workflow:

- запускается Spring PetClinic REST в Docker;
- выполняются API-тесты;
- формируются Surefire- и Allure-отчёты;
- отчёты сохраняются как артефакты GitHub Actions.

## Найденные проблемы API

| ID | Проблема | Тест |
|---|---|---|
| `PetBug-1` | Можно создать двух владельцев с одним номером телефона. API возвращает `201` вместо `400/409`. | [`OwnerDuplicateTests`](src/test/java/com/example/petclinicapitests/OwnerDuplicateTests.java) |
| `PetBug-2` | Для отрицательного `ownerId` API возвращает `500` вместо `400`. | [`OwnerIdValidationTests`](src/test/java/com/example/petclinicapitests/OwnerIdValidationTests.java) |
| `PetBug-3` | Невалидные параметры пагинации приводят к `500` вместо `400`. | [`OwnerPaginationTests`](src/test/java/com/example/petclinicapitests/OwnerPaginationTests.java) |
| `PetBug-4` | В Swagger для PUT/DELETE указан `200`, а фактически API возвращает `204`. | [`OwnerCrudTests`](src/test/java/com/example/petclinicapitests/OwnerCrudTests.java) |

Клиенты и DTO находятся в `src/main/java`, тесты — в `src/test/java`.
