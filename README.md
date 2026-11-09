# Travel Agency App

A JavaFX desktop application for a travel agency: customers search flights and buy tickets, and administrators manage flights, tickets and users from a dashboard. Built as a school project.

## Features

**Customer side**
- Login with user ID and password
- Search flights by departure, destination and date
- Buy tickets (duplicate purchases of the same flight are prevented)
- View your trips and profile information
- Leave reviews

**Admin dashboard**
- Add, update and delete flights, tickets and users
- Statistics: number of users and travellers, most popular departures and destinations, tickets per day (charts)

## Tech Stack

Java 21 · JavaFX (FXML, CSS) · MySQL (JDBC) · Maven · ControlsFX · BootstrapFX · Ikonli

## Getting Started

1. Create a MySQL database named `tirflayen` with the tables `users`, `flights` and `ticket`.
2. Check the connection settings in `src/main/java/org/example/demo/DataBaseConnection.java`.
3. Run:

```bash
./mvnw clean javafx:run         # Windows: mvnw.cmd clean javafx:run
```

## Structure

```
src/main/java/org/example/demo/        Controllers (flights, tickets, reviews, dashboard), models, DB connection
src/main/resources/org/example/demo/   FXML views and CSS styles
```
