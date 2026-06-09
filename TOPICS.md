# Java Topics — Fast Track for Provider Integration Development

## Java fundamentals worth knowing
- Streams — map, filter, reduce, collect — you'll use these constantly
- Lambdas & functional interfaces — needed to use streams properly
- Optional — very common in modern Spring Boot code
- Collections — which List/Map/Set to use and when, for-each loop
- Exception handling — checked vs unchecked, custom exceptions
- Inheritance & polymorphism — extending classes, method overriding, super(), runtime behaviour, method overloading
- Interfaces & abstract classes — contracts, when to use each, how Spring relies on them
- Generics — reading and writing basic generic type signatures, bounded types — needed to extend classes like AbstractCreditCardDeposit<MyConfig>
- Enums — reading and writing, used for transaction states and payment method types
- Regular expressions — used heavily in routing rules, fraud rules, and parsing provider responses

## Spring Boot
- Beans & the application context — what a bean is, how Spring manages them
- Dependency injection — @Autowired, constructor injection, why constructor injection is preferred
- Core annotations — @Component, @Service, @Repository, @RestController, @Configuration
- Spring Data JPA — repositories, entities, relationships
- REST — @GetMapping, @PostMapping, @RequestParam, @PathVariable, @RequestBody — and making outbound calls to provider APIs with RestTemplate / WebClient
- JSON / XML serialization — Jackson, @JsonProperty, @JsonIgnoreProperties, ObjectMapper — providers use both JSON and XML, Jackson handles both
- Profiles & config — application.yml, environment-specific config, environment variables
- Spring Retry & error handling patterns — handling flaky external APIs gracefully

## Testing
- Unit testing — JUnit 5, assertions, lifecycle
- Mocking — Mockito, stubs, verify, ArgumentCaptor
- Integration testing in Spring Boot — @SpringBootTest, @MockBean, TestRestTemplate
- Mocked HTTP servers — MockWebServer / WireMock, testing provider integrations without hitting real APIs

## Webhooks & async patterns
- Callbacks & webhooks — handling asynchronous payment notifications from providers
- Understanding the request/response vs event-driven flow difference

## Cryptography basics
- HMAC signatures — verifying provider callbacks
- AES encryption & RSA — used per-provider for security
- How to implement these in Java (javax.crypto, Bouncy Castle)

## Build & tooling
- Git — branching, merging, rebasing, pull requests, resolving conflicts, working in a team repo
- Maven — pom.xml, dependencies, build lifecycle, packaging to JAR
- Docker — containerizing a Spring Boot app, Dockerfile basics
- CI/CD — GitHub Actions, automated build/test/deploy pipelines
- Database migrations — Flyway or Liquibase, versioning schema changes

## Observability
- Logging — SLF4J, log levels, structured logging

## Lower priority for this role
- Spring Security / JWT — the team has its own auth layer
- Virtual threads / Project Loom — too new/niche for daily work
- JPMS modules — not used in the target codebase
- Spring Actuator / code coverage — useful but not a focus area
