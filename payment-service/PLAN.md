# Payment Service — Plan

A payment routing service that receives payment requests, routes them to the correct provider (Stripe, Adyen), persists transactions to a database, and handles provider responses. Built topic by topic — each step adds a layer to the same running application.

---

## Current status

**Step 0 — Project setup — not started**

Update this line every session. Format: `Step N — Topic name — in progress / done`

---

## The app

A merchant sends a payment request to your service. Your service picks the right provider (Stripe or Adyen), calls their API, persists the result as a transaction, and returns the outcome. Each step below adds a piece of that flow.

---

## How the pieces fit together

Before you build anything, read this. It explains what every class in the system does and how they connect — so nothing you create in the steps below feels arbitrary.

---

### The flow of a payment request

```
HTTP request
    │
    ▼
PaymentController          ← receives the HTTP request, hands it off
    │
    ▼
PaymentService             ← orchestrates everything: find provider, call it, save result
    │           │
    │           ▼
    │     TransactionRepository   ← saves and reads Transaction records in the database
    │
    ▼
ProviderRegistry           ← knows which providers exist, finds the right one by name
    │
    ▼
PaymentProvider            ← interface: defines what every provider must be able to do
    │
AbstractPaymentProvider    ← implements PaymentProvider; shared fields and common code for all providers
    │
    ├── StripeProvider     ← extends AbstractPaymentProvider; calls Stripe's API
    └── AdyenProvider      ← extends AbstractPaymentProvider; calls Adyen's API
```

A request comes in at the top and flows downward. The response travels back up the same path.

---

### What each piece is

**`PaymentController`**
The entry point for HTTP. When a merchant sends `POST /payments`, this class receives it. It knows nothing about providers or databases — its only job is to accept the request and hand it to `PaymentService`, then return whatever `PaymentService` gives back.

**`PaymentService`**
The brain of the operation. It coordinates everything: asks `ProviderRegistry` for the right provider, calls `charge()` on it, and tells `TransactionRepository` to save the result. It is the only class that knows the full sequence of events for processing a payment.

**`ProviderRegistry`**
A directory of all available providers. When `PaymentService` says "I need Stripe", `ProviderRegistry` finds it. It holds a list of every `PaymentProvider` and can look one up by name.

**`PaymentProvider` (interface)**
A contract. It says: every provider — no matter how different their API — must implement `charge()` and `getName()`. `PaymentService` calls `charge()` without knowing or caring whether it's talking to Stripe or Adyen. This is what makes it easy to add a new provider later: implement the interface, register it, done.

**`AbstractPaymentProvider` (abstract class)**
Common ground shared by all providers. Things like building a log message or holding the API URL are the same regardless of provider — so they live here instead of being duplicated in every provider class. Provider-specific logic stays in the concrete class.

**`StripeProvider` / `AdyenProvider`**
The actual integrations. Each one knows how to talk to its specific provider: what URL to call, what format the request needs to be in, how to read the response. Everything unique to that provider lives here.

**`TransactionRepository`**
The connection to the database. Every payment attempt is saved as a `Transaction` record — with its status, amount, provider, and timestamp. `TransactionRepository` handles all reading and writing. `PaymentService` uses it but never writes SQL directly.

**`Transaction` (entity)**
A database row. Represents one payment attempt: who processed it, how much, which provider, what the outcome was, and when it happened. This is the permanent record of everything the system has done.

---

### The domain objects

These are not classes that *do* things — they are objects that *represent* things.

**`PaymentRequest`** — what comes in from the merchant: provider name, amount, payment method. This is the input.

**`Transaction`** — what gets saved to the database: the full record of what happened. This is the output and the audit trail.

**`TransactionStatus`** (enum) — the three states a transaction can be in: `PENDING` (created, not yet processed), `COMPLETED` (provider accepted it), `FAILED` (provider rejected it or something went wrong).

**`PaymentMethod`** (enum) — how the payment is being made: `CARD` or `BANK_TRANSFER`.

---

### Why this structure

You could put all of this in one class. It would work. But in a real codebase with multiple developers:

- `PaymentController` only changes when the HTTP API changes
- `PaymentService` only changes when the payment flow changes
- `StripeProvider` only changes when Stripe's API changes

Each class has one reason to change. When a new provider arrives, you add one class and register it — nothing else needs to touch. When Stripe updates their API, you edit `StripeProvider` and nothing else breaks. This is the structure you will be working inside of when you join a team.

---

## Step 0: Project setup

Not a learning topic — infrastructure. The app needs to exist and run before anything else.

**What gets created:**
- Maven project with Spring Boot
- `pom.xml` with starter dependencies
- `PaymentServiceApplication.java` — the entry point annotated with `@SpringBootApplication`
- `application.yml` — basic config
- Package structure under `com.example.paymentservice`

**Assignments:**
1. Create the project structure (done together)
2. Verify the app starts with `mvn spring-boot:run`

---

## Step 1: Beans & the application context

→ Read first: NOTES.md § Beans & the application context

Spring manages objects for you. A bean is an object Spring creates, owns, and makes available to the rest of the app. The application context is the container that holds all beans. You register a class as a bean by annotating it — Spring does the rest.

This is the foundation everything else builds on. You will also create the domain model here — the core objects the rest of the app will work with.

**What you'll build:** The domain model and first provider beans.

**Assignments:**
1. Create `TransactionStatus.java` enum — values: `PENDING`, `COMPLETED`, `FAILED`
   *Reuses: Enums*
2. Create `PaymentMethod.java` enum — values: `CARD`, `BANK_TRANSFER`
   *Reuses: Enums*
3. Create `PaymentProvider.java` interface — methods: `String getName()`, `String charge(BigDecimal amount, PaymentMethod method)`
   *Reuses: Interfaces & abstract classes*
4. Create `AbstractPaymentProvider.java` abstract class — implements `PaymentProvider`, add a `protected String apiUrl` field and a `protected String buildLogMessage(String action, BigDecimal amount)` method that returns a formatted log string
   *Reuses: Inheritance & abstract classes*
5. Create `StripeProvider.java` extending `AbstractPaymentProvider` — implement `getName()` and `charge()`, annotate the class with `@Component`
6. Create `AdyenProvider.java` extending `AbstractPaymentProvider` — same as above
7. Run the app — verify it starts without errors

---

## Step 2: Dependency injection

→ Read first: NOTES.md § Dependency injection

Beans depend on other beans. Instead of creating dependencies yourself with `new StripeProvider()`, Spring injects them for you. Constructor injection is the preferred way — it makes dependencies explicit and the class easy to test.

**What you'll build:** Wire the providers into a registry, and the registry into the service.

**Assignments:**
1. Create `ProviderRegistry.java` — holds a `List<PaymentProvider>`, annotate with `@Component`
2. Add a constructor that takes `List<PaymentProvider>` as a parameter — Spring will inject all registered providers automatically
3. Add a method `Optional<PaymentProvider> findByName(String name)` — use streams to search the list
   *Reuses: Streams, Optional*
4. Create `PaymentService.java` — annotate with `@Component`
5. Add a constructor that takes `ProviderRegistry` — this is constructor injection
6. Add a method `String processPayment(String providerName, BigDecimal amount, PaymentMethod method)` — use `ProviderRegistry` to find the provider and call `charge()`
   *Reuses: Optional, Exceptions — throw a meaningful exception if provider not found*

---

## Step 3: Core annotations

→ Read first: NOTES.md § Core annotations

`@Component` registers any class as a bean. But Spring also has semantic annotations that say *what role* the bean plays: `@Service` for business logic, `@Repository` for database access, `@RestController` for HTTP, `@Configuration` for config. Same behaviour under the hood — clearer intent for anyone reading the code.

**What you'll build:** Replace `@Component` with the correct annotation throughout the app. Nothing changes at runtime — this is about making the code readable and conventional.

**Assignments:**
1. Replace `@Component` on `StripeProvider` and `AdyenProvider` with the correct annotation
2. Replace `@Component` on `ProviderRegistry` with the correct annotation
3. Replace `@Component` on `PaymentService` with the correct annotation
4. Create `AppConfig.java` with `@Configuration`
5. Inside `AppConfig`, define a `@Bean` method that returns a `RestTemplate` — this is how you register objects that aren't your own classes as beans

---

## Step 4: Database setup

Not a learning topic — infrastructure. Sets up PostgreSQL and connects the app to it. Done before JPA so the database is ready when it is needed.

**What gets done:**
1. Install and start PostgreSQL (or verify it is running)
2. Create the `payment_service` database and a dedicated user
3. Add the PostgreSQL driver dependency to `pom.xml`
4. Configure the datasource in `application.yml` — URL, username, password
5. Set `spring.jpa.hibernate.ddl-auto=update` so Hibernate creates tables automatically for now
6. Verify the app starts and connects

---

## Step 5: Spring Data JPA

→ Read first: NOTES.md § Spring Data JPA

JPA maps Java objects to database tables. You annotate a class with `@Entity` and Spring + Hibernate handle storing it as a row and reading it back. Spring Data JPA gives you a repository interface with `save()`, `findById()`, `findAll()` — no SQL needed for standard operations.

**What you'll build:** Persist payment transactions to the database.

**Assignments:**
1. Create `Transaction.java` — annotate with `@Entity`, add fields: `id`, `providerName`, `amount`, `paymentMethod`, `status`, `createdAt`
2. Annotate the fields correctly: `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated(EnumType.STRING)`
3. Create `TransactionRepository.java` extending `JpaRepository<Transaction, Long>`
4. Add a custom query method: `List<Transaction> findByStatus(TransactionStatus status)` — no implementation needed, Spring generates it
5. Inject `TransactionRepository` into `PaymentService`
6. Update `processPayment()` in `PaymentService` to:
   - Save a `Transaction` with status `PENDING` before calling the provider
   - Update the status to `COMPLETED` or `FAILED` after the call
   - Return the saved `Transaction`
   *Reuses: Enums, Optional, Exceptions*

---

## Step 6: REST

→ Read first: NOTES.md § REST

REST endpoints let other systems call your app over HTTP. `@RestController` marks a class as an HTTP handler. `@PostMapping`, `@GetMapping` map HTTP verbs to your methods. `@RequestBody` deserializes the incoming JSON into a Java object. `@PathVariable` pulls a value from the URL path.

You also make outbound calls — calling the provider's API. `RestTemplate` is the standard way to do this in Spring Boot.

**What you'll build:** Expose payment endpoints and add outbound HTTP calls to providers.

**Assignments:**
1. Create `PaymentRequest.java` — a plain Java class with fields: `providerName`, `amount`, `paymentMethod`
2. Create `PaymentController.java` with `@RestController` and `@RequestMapping("/payments")`
3. Add `POST /payments` — accepts `@RequestBody PaymentRequest`, calls `PaymentService.processPayment()`, returns the saved `Transaction`
4. Add `GET /payments/{id}` — looks up a transaction by ID using `TransactionRepository`, returns it or 404 if not found
   *Reuses: Optional*
5. Add `GET /payments?status=COMPLETED` — returns all transactions with that status
   *Reuses: Streams, Enums*
6. Inject the `RestTemplate` bean into `StripeProvider` and `AdyenProvider`
7. Update `charge()` in both providers to make an outbound POST call using `RestTemplate` to a placeholder URL — map the response to a string for now

---

## Step 7: JSON & XML serialization

→ Read first: NOTES.md § JSON / XML serialization

Spring automatically converts your Java objects to JSON when you return them from a `@RestController` — Jackson handles this. But providers don't always return clean JSON. You need to control field names with `@JsonProperty`, ignore unexpected fields with `@JsonIgnoreProperties`, and sometimes parse XML (some providers, including older Adyen flows, use XML).

**What you'll build:** Control serialization of requests and responses, and parse realistic provider response formats.

**Assignments:**
1. Add `@JsonProperty` annotations to `PaymentRequest` fields to match a realistic API naming convention (e.g. `provider_name` instead of `providerName`)
2. Create `StripeChargeResponse.java` — model a realistic Stripe-like JSON response with fields like `id`, `status`, `amount` — annotate with `@JsonIgnoreProperties(ignoreUnknown = true)`
3. Update `StripeProvider.charge()` to deserialize the response body into `StripeChargeResponse` using `ObjectMapper`
4. Create `AdyenChargeResponse.java` — model a simple XML response from Adyen
5. Update `AdyenProvider.charge()` to parse an XML response using `ObjectMapper` configured for XML (Jackson has an XML module for this)

---

## Step 8: Profiles & config

→ Read first: NOTES.md § Profiles & config

You don't want the same config in development and production. Profiles let you have separate `application-dev.yml` and `application-prod.yml` files. Sensitive values like API keys and database passwords never go in config files — they come from environment variables. `@ConfigurationProperties` maps config values from yml into a typed Java object.

**What you'll build:** Separate dev and prod config, and move all provider settings into a config object.

**Assignments:**
1. Create `application-dev.yml` — dev database URL, placeholder provider API keys, `ddl-auto=update`
2. Create `application-prod.yml` — production database URL read from an environment variable, `ddl-auto=validate`
3. Create `ProviderProperties.java` with `@ConfigurationProperties(prefix = "providers")` — fields for Stripe and Adyen base URLs and API keys
4. Add the provider settings to `application-dev.yml` under the `providers` prefix
5. Inject `ProviderProperties` into `StripeProvider` and `AdyenProvider` — replace any hardcoded URL strings
6. Run the app with `--spring.profiles.active=dev` and verify it picks up the dev config

---

## Step 9: Spring Retry & error handling

→ Read first: NOTES.md § Spring Retry & error handling

External APIs fail. They return 503s, time out, or return unexpected responses. Spring Retry lets you automatically retry a failed call a set number of times before giving up. `@ControllerAdvice` lets you handle exceptions globally — one place to decide what HTTP response an exception produces, instead of repeating try/catch in every controller method.

**What you'll build:** Make provider calls resilient and add a global error handler.

**Assignments:**
1. Add `spring-retry` and `spring-aspects` to `pom.xml`
2. Add `@EnableRetry` to `PaymentServiceApplication.java`
3. Annotate `charge()` in `StripeProvider` with `@Retryable(retryFor = RuntimeException.class, maxAttempts = 3)`
4. Add a `@Recover` method to `StripeProvider` — called when all retries are exhausted, should return a failure string
5. Update `PaymentService` to set the transaction status to `FAILED` when a provider call fails
6. Create `GlobalExceptionHandler.java` with `@ControllerAdvice`
7. Add a handler method for `PaymentFailedException` — return HTTP 422 with a structured error body
8. Add a handler for when a transaction is not found — return HTTP 404
   *Reuses: Exceptions*

---

## Java fundamentals used in this project

A reference for which topics from the fundamentals show up and where.

| Fundamental | Where it appears |
|---|---|
| Enums | `TransactionStatus`, `PaymentMethod` — Step 1 |
| Interfaces | `PaymentProvider` interface — Step 1 |
| Abstract classes | `AbstractPaymentProvider` — Step 1 |
| Inheritance | `StripeProvider`, `AdyenProvider` extend abstract class — Step 1 |
| Generics | `JpaRepository<Transaction, Long>`, `Optional<PaymentProvider>` — Steps 2, 5 |
| Optional | `findByName()`, `findById()` — Steps 2, 6 |
| Streams & Lambdas | Filtering providers and transactions — Steps 2, 6 |
| Collections | `List<PaymentProvider>` in registry — Step 2 |
| Exception handling | `PaymentFailedException`, global handler — Steps 2, 9 |
| Regular expressions | Can be added to routing rules in `ProviderRegistry` — Step 2 extension |
