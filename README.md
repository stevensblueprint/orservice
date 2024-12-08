# ORService
Spring Boot REST Service to provide a REST API replacement to current Sarapis OR Service.

## To run the database
Copy the environment variables from the `.env` file.
```bash
cp .env.example .env
```

Start the db container with the following command
```bash
docker-compose up
```
In your terminal you will find the logs of the database. If you see 
the following log the database has started gracefully.
```
LOG:  database system is ready to accept connections
```

To shut down the container database use
```bash
docker-compose down
```

## To run the service
Build the Java service. This command will generate the application `jar` inside
the `target` directory.
```bash
mvn clean install
```
If the build is successful, then run the service
```bash
java -jar target/orservice-0.0.1-SNAPSHOT.jar
```
You will start seeing logs from the service booting up. If you see the following log
```
Started OrserviceApplication in {} seconds (process running for {})
```
the service has started successfully.

## Troubleshooting
If the db has not started successfully do the following:
1. Check that the container is running
```bash
docker ps
```
You should see all the containers running in your machine. 

2. Try rebuilding the database
```bash
docker-compose down --volumes
rm -rf postgres_data
docker-compose up --build
```