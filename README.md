# Spring ORM — Spring Boot & PostgreSQL

A practical Spring Boot project designed to learn and implement **Spring ORM (Object Relational Mapping)** using **Hibernate/JPA** with **PostgreSQL**.

This project focuses on understanding how Java objects are mapped to relational database tables and how Spring Boot simplifies database operations using Spring Data JPA.

---

## 📌 Project Overview

This project demonstrates the complete flow:

```text
Client
  ↓
REST Controller
  ↓
Service Layer
  ↓
Repository Layer
  ↓
JPA / Hibernate
  ↓
PostgreSQL
```

The goal is not just to use JPA repositories, but to understand what actually happens between:

```text
Java Object → Entity → Hibernate → SQL → PostgreSQL
```

---

## 🛠️ Technologies Used

| Technology      | Purpose                      |
| --------------- | ---------------------------- |
| Java 21         | Programming Language         |
| Spring Boot     | Backend Framework            |
| Spring Web      | REST APIs                    |
| Spring Data JPA | Repository & ORM abstraction |
| Hibernate       | ORM implementation           |
| PostgreSQL      | Relational Database          |
| Lombok          | Boilerplate reduction        |
| Maven           | Dependency Management        |
| Postman         | API Testing                  |

---

# 📦 Main Dependencies

The core dependencies used in this project are:

```xml
<dependencies>

    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- PostgreSQL -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

</dependencies>
```

---

# 🗄️ Database Configuration

PostgreSQL is used as the database.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/life_os
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Important Properties

### `spring.jpa.hibernate.ddl-auto`

Controls how Hibernate handles database schema generation.

Common values:

```text
none
validate
update
create
create-drop
```

For development:

```properties
spring.jpa.hibernate.ddl-auto=update
```

For production, blindly using `update` is not recommended. Schema migrations should normally be handled using tools such as Flyway or Liquibase.

---

# 🧠 What is ORM?

ORM stands for:

> Object Relational Mapping

It allows us to work with database records using Java objects instead of manually writing SQL for every operation.

Without ORM:

```java
String sql =
    "SELECT * FROM users WHERE email = ?";
```

With JPA:

```java
User user = userRepository.findByEmail(email);
```

Hibernate internally generates the required SQL.

---

# 🔥 JPA vs Hibernate vs Spring Data JPA

These three are commonly confused.

## JPA

JPA is a **specification**.

It defines APIs and rules for ORM.

Examples:

```java
@Entity
@Id
@GeneratedValue
@OneToMany
@ManyToOne
```

---

## Hibernate

Hibernate is an **implementation of JPA**.

It actually performs ORM operations and communicates with the database.

```text
JPA
 ↓
Hibernate
 ↓
JDBC
 ↓
PostgreSQL
```

---

## Spring Data JPA

Spring Data JPA provides an abstraction over JPA repositories.

Instead of implementing CRUD manually:

```java
public interface UserRepository
        extends JpaRepository<User, Long> {
}
```

Spring generates the implementation for us.

---

# 🏗️ Project Architecture

The project follows a layered architecture:

```text
src/main/java
│
├── controller
│
├── service
│
├── repository
│
├── entity
│
├── dto
│
├── exception
│
└── config
```

### Controller

Responsible for handling HTTP requests.

```text
HTTP Request
     ↓
Controller
```

### Service

Contains business logic.

```text
Controller
    ↓
Service
```

### Repository

Communicates with the database.

```text
Service
   ↓
Repository
   ↓
JPA / Hibernate
```

### Entity

Represents a database table.

```java
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

}
```

---

# 🧩 ORM Concepts Covered

This project covers the major Spring ORM concepts.

## 1. Entity Mapping

```java
@Entity
@Table(name = "users")
public class User {
}
```

---

## 2. Primary Key Mapping

```java
@Id
@GeneratedValue
private Long id;
```

---

## 3. Sequence Generation

PostgreSQL sequences can be configured using:

```java
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "user_sequence"
)
@SequenceGenerator(
    name = "user_sequence",
    sequenceName = "user_sequence",
    allocationSize = 50
)
private Long id;
```

---

## 4. Column Mapping

```java
@Column(
    name = "email",
    nullable = false,
    unique = true
)
private String email;
```

---

## 5. Enum Mapping

Avoid storing enum values as ordinal numbers when possible.

Recommended:

```java
@Enumerated(EnumType.STRING)
private UserStatus status;
```

Instead of:

```java
@Enumerated(EnumType.ORDINAL)
```

`EnumType.STRING` is safer because changing enum order will not silently change existing database meaning.

---

# 🔗 Entity Relationships

The project also demonstrates relational mappings.

## One-to-One

```java
@OneToOne
private Profile profile;
```

Database relationship:

```text
User
 │
 └──── Profile
```

---

## One-to-Many

```java
@OneToMany
private List<Post> posts;
```

```text
User
 │
 ├── Post
 ├── Post
 └── Post
```

---

## Many-to-One

```java
@ManyToOne
private User user;
```

This is commonly used together with `@OneToMany`.

---

## Many-to-Many

```java
@ManyToMany
private Set<Role> roles;
```

Usually represented through a join table.

```text
User
 │
 ├──── user_role ──── Role
```

---

# 🔄 Cascade Operations

Cascade determines which operations should propagate from one entity to another.

Example:

```java
@OneToMany(
    mappedBy = "user",
    cascade = CascadeType.ALL
)
private List<Post> posts;
```

Common cascade types:

```text
PERSIST
MERGE
REMOVE
REFRESH
DETACH
ALL
```

### Important

Do not blindly use:

```java
CascadeType.ALL
```

especially on relationships where deleting a parent must not delete unrelated/shared data.

---

# 🗑️ Orphan Removal

```java
@OneToMany(
    mappedBy = "user",
    orphanRemoval = true
)
```

When an entity is removed from the parent's collection, Hibernate can remove the corresponding child record.

This should only be used when the child truly belongs exclusively to the parent.

---

# 🔄 Fetch Strategies

Relationships can be fetched using:

```text
LAZY
EAGER
```

### LAZY

Data is loaded only when required.

```java
@ManyToOne(fetch = FetchType.LAZY)
```

### EAGER

Data is loaded immediately.

```java
@ManyToOne(fetch = FetchType.EAGER)
```

### Recommended

Prefer **LAZY loading** for most relationships and explicitly fetch what is required.

---

# 💾 Persistence Context

One of the most important Hibernate concepts.

The persistence context acts like a first-level cache managed by the `EntityManager`.

Entity states:

```text
Transient
   ↓
Managed
   ↓
Detached
   ↓
Removed
```

Understanding entity states is essential for understanding Hibernate behavior.

---

# 🧠 EntityManager

JPA provides:

```java
EntityManager
```

It provides operations such as:

```java
persist()
find()
merge()
remove()
detach()
flush()
```

Spring Data JPA hides much of this complexity behind repositories, but understanding `EntityManager` is important for advanced ORM development.

---

# 📚 Spring Data JPA Repository

Example:

```java
public interface UserRepository
        extends JpaRepository<User, Long> {

}
```

This provides operations such as:

```java
save()
findById()
findAll()
deleteById()
existsById()
count()
```

---

# 🔎 Derived Query Methods

Spring Data JPA can generate queries from method names.

Example:

```java
Optional<User> findByEmail(String email);
```

Another example:

```java
List<User> findByStatus(UserStatus status);
```

Multiple conditions:

```java
List<User> findByNameAndStatus(
    String name,
    UserStatus status
);
```

---

# 📝 JPQL

JPQL operates on **entities and their fields**, not database tables directly.

Example:

```java
@Query("""
    SELECT u
    FROM User u
    WHERE u.email = :email
""")
Optional<User> findUserByEmail(
    @Param("email") String email
);
```

Notice:

```text
User
```

is the entity name, not necessarily the table name.

---

# 🗃️ Native SQL

When database-specific SQL is required:

```java
@Query(
    value = """
        SELECT *
        FROM users
        WHERE email = :email
    """,
    nativeQuery = true
)
Optional<User> findUser(
    @Param("email") String email
);
```

Native queries should not be the default approach.

Use JPQL or derived queries when they are sufficient.

---

# 🔁 Transactions

Database operations should be executed inside appropriate transaction boundaries.

Example:

```java
@Transactional
public void createUser(User user) {

    userRepository.save(user);

}
```

Transactions provide:

```text
Atomicity
Consistency
Isolation
Durability
```

---

# ⚡ Dirty Checking

Hibernate automatically detects changes to managed entities.

Example:

```java
User user = userRepository.findById(id)
        .orElseThrow();

user.setName("Rajat");
```

If the entity is managed inside a transaction, Hibernate can detect the change and generate:

```sql
UPDATE users
SET name = ?
WHERE id = ?
```

No explicit `save()` is necessarily required for the update.

---

# 🚀 Persistence Operations

Hibernate provides different persistence operations.

```text
persist()
merge()
remove()
find()
flush()
```

Understanding the difference between `persist()` and `merge()` is particularly important when working directly with `EntityManager`.

---

# ⚠️ N+1 Query Problem

One of the most important Hibernate performance problems.

Example:

```text
SELECT * FROM users;

SELECT * FROM posts WHERE user_id = 1;
SELECT * FROM posts WHERE user_id = 2;
SELECT * FROM posts WHERE user_id = 3;
...
```

Instead of one query, Hibernate may execute many queries.

Possible solutions include:

```text
JOIN FETCH
EntityGraph
Batch Fetching
DTO Projections
Proper Fetch Strategy
```

---

# 🔍 Pagination and Sorting

Spring Data JPA supports pagination.

Example:

```java
Page<User> users =
    userRepository.findAll(
        PageRequest.of(0, 20)
    );
```

This prevents loading thousands of database records into memory at once.

---

# 📊 DTO vs Entity

Entities should not automatically be exposed directly through APIs.

Prefer:

```text
Entity
 ↓
Service
 ↓
DTO
 ↓
Controller
 ↓
Client
```

Instead of:

```text
Entity
 ↓
Controller
 ↓
Client
```

DTOs provide better control over API contracts and help prevent accidental exposure of internal entity relationships.

---

# 🛡️ Common ORM Problems Covered

During development, the project also demonstrates common Hibernate/JPA errors such as:

### 405 Method Not Allowed

Usually caused by calling an endpoint using the wrong HTTP method.

Example:

```text
GET /auth/create-account
```

when the controller expects:

```text
POST /auth/create-account
```

---

### 415 Unsupported Media Type

Usually caused by sending the wrong request format.

For JSON APIs:

```http
Content-Type: application/json
```

Request:

```json
{
    "email": "test@example.com",
    "password": "password"
}
```

---

### Null Property / Transient Entity Errors

Example:

```text
PropertyValueException
```

These generally indicate that a required entity field or relationship was not properly populated before persistence.

---

### Enum Database Errors

Incorrect enum mapping can cause issues such as:

```text
invalid input syntax
```

Using:

```java
@Enumerated(EnumType.STRING)
```

is generally safer than ordinal storage.

---

# 🧪 Testing APIs

Postman can be used to test the REST endpoints.

Example:

```http
POST /auth/create-account
Content-Type: application/json
```

Body:

```json
{
    "name": "Rajat",
    "email": "rajat@example.com",
    "password": "password"
}
```

---

# 🔥 Important Hibernate Concepts to Master

The purpose of this project is not simply to memorize annotations.

You should understand:

```text
@Entity
@Table
@Id
@GeneratedValue
@SequenceGenerator
@Column

@OneToOne
@OneToMany
@ManyToOne
@ManyToMany

@JoinColumn
@JoinTable
mappedBy

CascadeType
orphanRemoval

FetchType
LAZY
EAGER

EntityManager
Persistence Context
Entity Lifecycle
Dirty Checking

@Transactional

JPQL
Native Query
Derived Query

Pagination
Sorting

N+1 Problem
JOIN FETCH
EntityGraph

DTO Projection
```

---

# 📖 Recommended Learning Order

Follow this sequence while studying the project:

```text
1. JDBC Basics
       ↓
2. ORM Fundamentals
       ↓
3. JPA
       ↓
4. Hibernate
       ↓
5. Entity Mapping
       ↓
6. Relationships
       ↓
7. Persistence Context
       ↓
8. Entity Lifecycle
       ↓
9. Transactions
       ↓
10. Spring Data JPA
       ↓
11. JPQL
       ↓
12. Native Queries
       ↓
13. Fetch Strategies
       ↓
14. N+1 Problem
       ↓
15. Pagination
       ↓
16. Performance Optimization
```

---

# ▶️ Running the Project

## 1. Clone the Repository

```bash
git clone <repository-url>
```

## 2. Configure PostgreSQL

Create the database:

```sql
CREATE DATABASE life_os;
```

## 3. Configure Credentials

Update:

```properties
spring.datasource.url
spring.datasource.username
spring.datasource.password
```

## 4. Build the Project

```bash
./mvnw clean install
```

Windows:

```bash
mvnw.cmd clean install
```

## 5. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

---

# 🎯 Learning Objective

After completing this project, you should be able to explain:

> "How a REST request travels from the Controller to the Service, Repository, Hibernate, JDBC and finally PostgreSQL, and how Hibernate converts Java entity operations into SQL."

You should also be comfortable explaining why a particular JPA annotation, relationship, transaction boundary, query strategy, or fetching strategy is being used instead of simply knowing the syntax.

---

# 🚧 Future Improvements

The project can be extended with:

* Flyway database migrations
* Redis caching
* Spring Security
* JWT authentication
* Optimistic locking
* Pessimistic locking
* Auditing
* Specifications
* Criteria API
* EntityGraph
* QueryDSL
* Hibernate Envers
* Testcontainers
* Integration testing
* Database indexing
* Query optimization
* Dockerized PostgreSQL

---

# 👨‍💻 Author

**Rajat Das**

Spring Boot / Flutter Developer

---

## ⭐ Key Takeaway

Spring ORM is not just about writing:

```java
@Entity
```

and:

```java
JpaRepository
```

The real objective is understanding the complete ORM lifecycle:

```text
Java Object
     ↓
JPA Entity
     ↓
Persistence Context
     ↓
Hibernate
     ↓
SQL Generation
     ↓
JDBC
     ↓
PostgreSQL
     ↓
Database Result
     ↓
Hibernate
     ↓
Java Object
```

Once this flow is clear, Spring Data JPA becomes much easier to understand and Hibernate-related production issues become significantly easier to debug.
