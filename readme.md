# aqa-2-3-task-1-app-card-delivery-(patterns)   [![Build status](https://ci.appveyor.com/api/projects/status/uxb3o6g58c5v4g1x?svg=true)](https://ci.appveyor.com/project/m-starilov/aqa-2-3-task-1-app-card-delivery-patterns)

### Инструкция для интеграции с Report Portal
Пример интеграции от самих ReportPortal на [Github](https://github.com/reportportal/agent-java-junit5) для JUnit5 на основе которого составлена дальнейшая инструкция.

#### Подготовка
1. В проекте по пути `src.test.resurses` создаём папку `META-INF.services`
2. В созданый каталог добавляем файл с наименованием `org.junit.jupiter.api.extension.Extension`
3. В файле указываем одну единственную ссылку на имплементацию `com.epam.reportportal.junit5.ReportPortalExtension`

#### Gradle
4. В `build.gradle` добавляем репозитории и зависимости 
```groovy
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testCompile 'com.epam.reportportal:agent-java-junit5:5.0.6'
    compile 'com.epam.reportportal:logger-java-log4j:5.0.3'
    compile 'com.epam.reportportal:agent-java-junit5:5.0.6'
    compile 'org.apache.logging.log4j:log4j-api:2.11.2'
    compile 'org.apache.logging.log4j:log4j-core:2.11.2'
    }

test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
    systemProperty 'junit.jupiter.extensions.autodetection.enabled', true
}
```
#### Logging
5. Добавляем файл `log4j2.xml` в папку `test.resources` с содержимым
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout
                    pattern="%d [%t] %-5level %logger{36} - %msg%n%throwable"/>
        </Console>
        <ReportPortalLog4j2Appender name="ReportPortalAppender">
            <PatternLayout
                    pattern="%d [%t] %-5level %logger{36} - %msg%n%throwable"/>
        </ReportPortalLog4j2Appender>
    </Appenders>
    <Loggers>
        <Root level="DEBUG">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="ReportPortalAppender"/>
        </Root>
    </Loggers>
</Configuration>
```
#### ReportPortal в Docker
6. [Скачиваем](https://github.com/reportportal/reportportal/blob/master/docker-compose.yml) актуальный Docker compose файл.
7. Добавляем в проект
8. В `docker-compose.yml` для сервисов `postgres` и `minio` комментим _volumes_ для _unix host_ и раскомментим _volumes_ для _windows host_. Например:
```yml
postgres:
  image: postgres:12-alpine
  shm_size: '512m'
environment:
  POSTGRES_USER: rpuser
  POSTGRES_PASSWORD: rppass
  POSTGRES_DB: reportportal
volumes:
    # For unix host
    # - ./data/postgres:/var/lib/postgresql/data
    # For windows host
    - postgres:/var/lib/postgresql/data
```
9. И раскомментируем ещё это (в конце .yml файла)
```yml
  # Docker volume for Windows host
volumes:
  postgres:
  minio:
```
10. Разворачиваем ReportPortal с помощью Docker командой в терминале `docker-compose up`
#### Работа в интерфейсе ReportPortal
11. Открываем ReportPortal по адресу http://localhost:8080 логин/пароль `superadmin\erebus`. 
_*Пожалуйста, измените пароль администратора для большей безопасности_
12. Создаём проект. 
_Administrate -> [Projects](http://localhost:8080/ui/#administrate/projects) -> Add New Project_
    ![image](https://user-images.githubusercontent.com/68705045/127614892-96b1a87a-6177-4336-a381-802581660310.png)
    ![image](https://user-images.githubusercontent.com/68705045/127614997-d2d9103b-c3a4-4a9b-bd11-06bf36c2e222.png)
13. Добавляем пользователей в проект. 
_Administrative -> [Users](http://localhost:8080/ui/#administrate/users) -> Add user_
    ![image](https://user-images.githubusercontent.com/68705045/127614150-119710bc-ce41-4216-9a9a-df39f784f9cb.png)


#### Связываем ReportPortal с нашими тестами
14. Добавляем файл `reportportal.properties` в `test.resources`. В него копируем информацию из профиля созданного пользователя
    ![image](https://user-images.githubusercontent.com/68705045/127615083-b05f9deb-5172-4de3-bc70-e3c7397c227b.png)
15. Запускаем тесты
16. В ReportPortal переходим в раздел Launches. Там должна отобразится информация о запусках тестов
    ![image](https://user-images.githubusercontent.com/68705045/127615395-cf6e48f9-6ed7-4e17-aa2c-20b62f3a4589.png)
