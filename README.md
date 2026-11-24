# MediPulse - Online Healthcare Management System (Java Web)

MediPulse helps patients book appointments and view their medical history. Doctors manage schedules and patient records, while administrators manage users, appointments and clinic settings. Patients can also find nearby hospitals.

Built with **Java 11, Servlets, JSP/JSTL, JDBC, MySQL and Maven** (runs on Apache Tomcat 9).

## Java concepts used (rubric mapping)
| Concept | Where |
|---|---|
| Inheritance and abstract class | `model/User` -> `Admin`, `Doctor`, `Patient`; `servlet/BaseServlet` -> all servlets |
| Polymorphism | `User.getDashboardPath()/getRoleLabel()` overridden per role; `UserFactory` |
| Interfaces and generics | `Dashboardable`, `GenericDAO<T>` implemented by every DAO |
| Exception handling | Custom hierarchy in `exception/` (`MediPulseException`, `ValidationException`, `AuthenticationException`, `BookingConflictException`, `DatabaseException`) |
| Collections | `LinkedHashMap`, `TreeMap`, `List`, streams in `AnalyticsService`, `UserDAO.countByRole()` |
| Multithreading | `NotificationService`: `ExecutorService` worker pool + `ConcurrentLinkedDeque` activity log |
| Synchronization | `AppointmentService.book()` is `synchronized` to prevent double booking |
| Enum | `AppointmentStatus` |
| JDBC | `DBConnection`, `PreparedStatement`, try-with-resources, batch insert (`SettingDAO`), joins |
| Servlets | `AuthServlet`, `AdminServlet`, `DoctorServlet`, `PatientServlet`, `ProfileServlet`, `AuthFilter` (Filter), `AppListener` (ServletContextListener), sessions, forward/redirect |

## Features
- Sign in, patient registration, forgot password, profile management
- **Admin:** user management, appointment management, hospital directory, system settings, analytics, activity log
- **Doctor:** schedule and appointment overview, confirm/complete/cancel, patient records, patient feedback and average rating
- **Patient:** book/cancel appointments, appointment history, medical history, rate doctors, nearby hospital search

## Setup
1. Install JDK 11+, Maven, MySQL 8, Apache Tomcat 9.
2. Run `sql/schema.sql` in MySQL.
3. Set the DB password: environment variable `MEDIPULSE_DB_PASS`, or edit `util/DBConnection.java`.
4. `mvn clean package`
5. Copy `target/medipulse.war` into Tomcat `webapps/` and start Tomcat.
6. Open `http://localhost:8080/medipulse/`

## Demo accounts (password: `careflow123`)
- Admin: `alex@careflow.health`
- Doctor: `amelia@careflow.health`
- Patient: `olivia@email.com`

## Structure
```
sql/schema.sql
src/main/java/com/medipulse/
  util/ exception/ model/ dao/ service/ servlet/
src/main/webapp/
  css/style.css
  WEB-INF/views/  (JSP pages)
```
