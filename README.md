# PaymentRestApi - A PortX Challenge

## Docker 

### Prerequisites

In order to run this project there are a few pre-requisites completed

#### Dependencies

You may want to use `Docker desktop`

- [Docker](https://www.docker.com/products/docker-desktop/)

Or using your trusted package manager install `docker` and `docker-compose`


#### Extra configurations

If you may consume kafka from outside docker container, you may want to
consider adding into `etc/hosts` the following line: `127.0.0.1 kafka`
DB Ports are forwarder in order to query it with a db manager if needed.

### Steps

- Move inside project root folder
- Run the following command: 
```shell
docker-compose up --build
```

Once containers are all up, you can start using the application!

#### For starting, you may want to look into `http://localhost:8081/swagger-ui/index.html`

## Local

#### Dependencies

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8.4](https://maven.apache.org/docs/3.8.4/release-notes.html)
- [Kafka](https://kafka.apache.org/downloads)
- [MariaDB 10.11](https://mariadb.org/download/?prod=mariadb&rel=10.11.2&old=&t=mariadb)

### Steps

- Make sure you're on Java 17.
- Start Kafka and MariaDB server
- Run `initializeDB.sql` script in your server
- Move inside the project root folder
- Run
```shell
mvn clean install
```
- Once done, run
```shell
mvn spring-boot:run
```

Or use your prefered IDE to run it. 

#### Again, for starting, you may want to look into `http://localhost:8081/swagger-ui/index.html`


### ENV vars

You may run it without configuring any env vars out of the box, but you can set up the following ones:
- `DB_HOST` Database host url (localhost by default)
- `DB_PORT` Database port (3306 by default)
- `DB_NAME` Database name (portx by default, if changed you should modify `initializeDB.sql`)
- `DB_USERNAME` Database username (root by default)
- `DB_PASSWORD` Database user password (root by default)
- `KAFKA_TOPIC` Kafka topic's url (localhost by default)

### Checking coverage

This is done by JaCoCo with the following command:

```shell
mvn clean test jacoco:report
```

# SupplierRestClient

Please refer to [its own repository documentation](https://github.com/daniellovero/portx-supplier)