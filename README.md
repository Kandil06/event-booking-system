# Event Booking System (Spring Boot)

A server-rendered web app for browsing events and booking tickets. Each ticket gets a unique code that is emailed to the buyer, and admins manage events and check tickets in at the door.

## Features
- Register and log in (Spring Security, role-based access: `USER` / `ADMIN`)
- Browse upcoming events and book 1–10 tickets per booking
- One unique ticket code per ticket, emailed to the buyer (or printed to the console if mail isn't configured)
- No overselling: the event row is locked (`PESSIMISTIC_WRITE`) during booking and capacity is checked inside a transaction
- "My bookings" page for each user
- Admin dashboard: create, edit and delete events, see all bookings for an event, and mark tickets as used at check-in

## Stack
- Java, Spring Boot (Web MVC, Data JPA, Security, Validation, Mail)
- Thymeleaf templates + plain CSS
- H2 file database (stored in `./data`, ignored by git)
- Maven (wrapper included)

## Run it
```bash
# optional: set the first admin's password (defaults to "change-me")
export ADMIN_PASSWORD=your-admin-password

./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```
Open http://localhost:8080. The admin account `admin@booking.local` is created on first start.

To send real emails, fill in the `spring.mail.*` settings in `src/main/resources/application.properties`.

## Project layout
```
src/main/java/com/example/booking/
  config/      Security config, admin seeder
  model/       JPA entities: AppUser, Event, Booking, Ticket, Role
  repository/  Spring Data repositories
  service/     Booking, event, user, mail and ticket-code logic
  web/         MVC controllers and form objects
src/main/resources/
  templates/   Thymeleaf pages (public + admin)
  static/css/  Styles
```
