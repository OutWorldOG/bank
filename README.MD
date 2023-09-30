# Банк "Банк"

Restful приложение "Банк" предоставляют API для создания/удаления банковского счёта, банковских карт, стандартных манипуляций с банковскими картами, а также возможность для отслеживания транзакций.


### Фреймворки и библиотеки, использующиеся в проекте

*Java 17 <br>
*Spring Boot <br>
*Spring AOP <br>
*Spring Web <br>
*Spring Security <br>
*Spring Data JPA <br>
*Hibernate Validator <br>
*Lombok <br>
*H2 <br>
*Springfox <br>
*Springdoc <br>

## Запуск проекта

Проект запускается на порте 8080. <br>

```
   mvn spring-boot:run
   ```
## Архитектура проекта

Проект состоит из одного модуля. Модуль имеет стандартную архмитектуру для crud приложения: controller-service-repository.<br>
За обработку ошибок отвечает ControllerAdvice, за логгирование - Aspect. <br>
При запуске проекта отрабатывает schema.sql скрипт, который наполняет h2 3 таблицами: bank_user, user_account, transaction_history


## API

с API можно ознакомиться после поднятия проекта по адресу - http://localhost:8080/swagger-ui/index.html#/

