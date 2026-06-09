# Java Learning Notes

---

## Contents

[Concepts & terminology](#concepts--terminology)

**Foundations** *(covered before fast track)*
[Variables & types](#variables--types) · [Access modifiers](#access-modifiers) · [static](#static) · [Classes & OOP](#classes--oop) · [Records](#records) · [Memory model](#memory-model)

**Java fundamentals**
[Streams](#streams) · [Lambdas & functional interfaces](#lambdas--functional-interfaces) · [Optional](#optional) · [Collections](#collections) · [Exception handling](#exception-handling) · [Inheritance & polymorphism](#inheritance--polymorphism) · [Interfaces & abstract classes](#interfaces--abstract-classes) · [Regular expressions](#regular-expressions)

**Spring Boot**
[Beans & the application context](#beans--the-application-context) · [Dependency injection](#dependency-injection) · [Core annotations](#core-annotations) · [Spring Data JPA](#spring-data-jpa) · [REST](#rest) · [Profiles & config](#profiles--config) · [Spring Retry & error handling](#spring-retry--error-handling)

**Testing**
[Unit testing](#unit-testing) · [Mocking](#mocking) · [Integration testing](#integration-testing) · [Mocked HTTP servers](#mocked-http-servers)

**Webhooks & async patterns**
[Callbacks & webhooks](#callbacks--webhooks) · [Request/response vs event-driven](#requestresponse-vs-event-driven)

**Cryptography basics**
[HMAC signatures](#hmac-signatures) · [AES encryption & RSA](#aes-encryption--rsa)

**Build & tooling**
[Git](#git) · [Maven](#maven) · [Docker](#docker) · [CI/CD](#cicd) · [Database migrations](#database-migrations)

**Observability**
[Logging](#logging)

---

## Concepts & terminology

Terms that come up constantly. Read this first so nothing in the notes catches you off guard.

---

**OOP — Object-Oriented Programming**
A way of structuring code around objects — things that have data (fields) and behaviour (methods). Instead of writing a list of instructions top to bottom, you model the real world: a `Payment` has an amount, a status, a provider. A `User` has a name and an email. You then write methods on those objects to do things with them. Java is built around OOP — almost everything you write will be a class.

---

**API**
The word gets used in multiple ways and that causes confusion:
- **A web API** — a URL you send HTTP requests to, e.g. `POST https://api.stripe.com/charge`. This is what most people mean in a payment/provider context.
- **A Java API** — the set of public methods on a class or library that you're allowed to call. When the docs say "use the Collections API", they mean the methods Java gives you on List, Map, Set etc.
- **Your own API** — the endpoints your Spring Boot app exposes so other systems can call you.

Same word, three different things. Context tells you which one.

---

**Interface (the concept)**
A contract. It says: "anything that implements me must have these methods." It doesn't say how — just what. A `PaymentProvider` interface might say every provider must have a `charge()` method. Stripe implements it one way, Adyen another. The rest of your code calls `provider.charge()` without caring which provider it is. This is how Spring wires things together — you depend on the interface, Spring decides which implementation to inject at runtime.

---

**Abstraction**
Hiding the details so you only see what matters. When you call `list.add("Stripe")` you don't know or care how ArrayList stores it in memory — you just know it works. That's abstraction. In your own code: a `PaymentService` abstracts the details of which provider to call and how — the controller just calls `paymentService.processPayment()` and gets a result back.

---

**Runtime vs compile time**
- **Compile time** — when Java turns your source code into bytecode. Errors here are caught before the program runs — wrong types, missing methods, syntax mistakes. The compiler refuses to build.
- **Runtime** — when the program is actually running. Errors here happen while the program executes — a NullPointerException, a provider returning 503, a missing config value. The program builds fine but crashes or misbehaves when used.

Rule of thumb: compile-time errors are easier to fix because the compiler tells you exactly where. Runtime errors require investigation.

---

**Framework vs library**
- **Library** — code you call. You're in control. You decide when to use it. Jackson is a library — you call `objectMapper.writeValueAsString(obj)` when you want to.
- **Framework** — code that calls you. The framework is in control. You write code that fits its structure and the framework runs it. Spring Boot is a framework — it starts the app, handles HTTP requests, manages beans, and calls your code at the right time.

---

**Dependency**
Something your code needs in order to work. A `PaymentService` depends on a `ProviderClient` to make API calls. In Spring Boot, dependencies are injected — Spring creates the objects and passes them in, so you don't have to wire them together manually.

---

**Boilerplate**
Code you have to write repeatedly that adds no real logic — just ceremony. Getters, setters, constructors that do nothing but assign fields. Records exist specifically to eliminate boilerplate for data classes.

---

## Foundations

*Topics covered before the fast track. Not in the fast track but useful reference.*

---

### Variables & types

**Production scenario:** A payment object has an `amount` field. You use `BigDecimal` not `double` because you're dealing with money. The `status` is a `boolean isPremium`. The transaction ID is a `long` because it comes from a database sequence that exceeds `int` range. These type choices matter — using the wrong one causes precision bugs or runtime crashes.

**Primitives** — stored directly on the stack, cannot be null, fast and lightweight:

| Type | Use when |
|---|---|
| `int` | Default for whole numbers — counters, IDs, indexes |
| `long` | Whole numbers too large for `int` — timestamps (milliseconds since epoch), large transaction IDs |
| `double` | Default for decimals — but never for money (use `BigDecimal` instead) |
| `float` | Rarely used — only when a library specifically requires it |
| `boolean` | True/false flags — `isActive`, `isPremium`, `hasError` |
| `char` | Single character — rarely used directly, mostly handled via `String` |
| `byte` / `short` | Almost never used directly — only in low-level or performance-critical code |

**Literal suffixes** — Java assumes whole numbers are `int` and decimals are `double`. Use suffixes to override:
- `L` → `long` (e.g. `10000000000L`) — required if the number is too large for `int`
- `F` → `float` (e.g. `3.14F`) — required because `3.14` defaults to `double`
- `0xFF` → hex literal, `0b1010` → binary literal

**Wrapper objects** — object versions of primitives: `Integer`, `Long`, `Double`, `Boolean`, `Character` etc.
- Live on the heap, the variable holds a reference
- Can be `null` — useful when absence of a value matters
- Have utility methods: `Integer.parseInt("42")`, `Integer.MAX_VALUE` etc.
- Required when Java expects an object — e.g. collections (`List<Integer>`, not `List<int>`)
- Use `int` by default, reach for `Integer` when you need null, collections, or object methods

**In Spring Boot** — use wrapper types (`Integer`, `Boolean`) for `@RequestParam` and similar, because parameters can be missing (null). `int` can't be null so Spring would throw an exception if the parameter is absent.

**BigDecimal — use for money, never double or float**
- `double` and `float` have floating point precision issues that show up during arithmetic: `0.1 + 0.2 = 0.30000000000000004`
- `BigDecimal` stores numbers exactly with no rounding surprises
- Always pass the value as a String to the constructor: `new BigDecimal("19.99")` — passing a double would reintroduce the precision issue

**BigInteger** — like `BigDecimal` but for whole numbers with no size limit. Use when `long` isn't big enough (e.g. cryptography, very large IDs). Rare in practice.

**Type casting** — converting a value from one type to another:
- **Widening** (smaller → larger, e.g. `int` → `long`) — happens automatically, no cast needed
- **Narrowing** (larger → smaller, e.g. `long` → `int`) — requires explicit cast, risk of losing data if value doesn't fit:
```java
long l = 100L;
int i = (int) l; // explicit cast required
```
- Works between all numeric types (`byte`, `short`, `int`, `long`, `float`, `double`)
- Can't cast unrelated types — to convert a `String` to `int` use `Integer.parseInt("42")` instead

**Operators and type rules**

- `+` with a `String` always produces a `String` — Java automatically converts the other operand. `String` is always the "master": `"charged: " + 50.0` → `"charged: 50.0"`.
- `-`, `*`, `/`, `%` are purely arithmetic — they never work with `String`. Using them with a String is a compile error.
- In arithmetic without Strings, the higher-precision type wins: `int + double` → `double`, `int + long` → `long`. This is called type promotion and happens automatically (widening).
- This is the only special operator behavior in Java. Unlike JavaScript, you can't do `"5" - 2` — Java is strict and the compiler rejects it.

**Autoboxing & unboxing** — Java automatically converts between primitive and wrapper. You don't need to think about it most of the time, but knowing it happens helps you understand unexpected `NullPointerException`s — unboxing a `null Integer` to `int` will crash:
- Autoboxing: `Integer x = 5;` (int → Integer, done automatically)
- Unboxing: `int y = x;` (Integer → int, done automatically)
- Watch out: `Integer x = null; int y = x;` — crashes at runtime because you can't unbox null

**`var`** — lets Java infer the type instead of writing it out explicitly:
- `var grouped = names.stream().collect(Collectors.groupingBy(...));` — Java figures out the type from the right side
- Useful when the full type is long or obvious from context
- Can reduce readability if the type isn't clear from the right side — a teammate has to know what the method returns to know what `var` holds
- Some teams avoid it for this reason; use it when the type is obvious, be explicit when it's not

---

### Access modifiers

Controls who can see a field, method, or class.

**Production scenario:** A `PaymentService` class has a `processPayment()` method that's `public` — it's called from a controller. Internally it calls `validateAmount()` which is `private` — no other class should call this directly. The `amount` field on the `Payment` object is `private` and only accessible via a getter. This keeps the internal logic hidden and lets you change it without breaking other parts of the codebase.

| Modifier | Same class | Same package | Subclass | Everywhere |
|---|---|---|---|---|
| `private` | yes | no | no | no |
| package-private (no keyword) | yes | yes | no | no |
| `protected` | yes | yes | yes | no |
| `public` | yes | yes | yes | yes |

- **`private`** — only the class itself can access it. Use for internal fields you want to hide from everything else. Most fields should be private (encapsulation).
- **package-private** — no keyword needed, just omit the modifier. Accessible within the same package but invisible outside it. Rarely used intentionally.
- **`protected`** — like package-private but also extends to subclasses. Use when a subclass needs direct access to a parent's field or method.
- **`public`** — accessible from anywhere. Use for methods and classes that form the public API of your code.

Example: `Animal`'s fields are `private`, so `Dog` cannot access `size`, `breed`, or `weight` directly even though it is a subclass — it can only set them via `super(...)`. Changing them to `protected` would allow `Dog` to access them directly.

---

### static

**Production scenario:** A `PaymentService` has a static constant `MAX_RETRY_ATTEMPTS = 3` — it's the same value for all payment attempts, no need for an instance. A utility class `CurrencyUtils` has a static method `convertToMinorUnits(amount)` — it doesn't need any object state, it just takes a number and returns one. You call it directly: `CurrencyUtils.convertToMinorUnits(19.99)`.

- A **non-static** field or method belongs to the instance — each object gets its own copy
- A **static** field or method belongs to the class itself — shared across all instances
- You can't call a non-static field or method directly on the class, you need to create an instance first
- You can call static from non-static code — just use the class name directly: `Dog.staticField` — this is fine and common
- Don't call static through an instance reference (`d.staticField`) — IntelliJ will warn you. It works, but it's misleading because the reader might think the field belongs to that specific instance, and that changing it only affects that instance. It doesn't — it affects all of them.
- Static methods can't use `this` — `this` refers to a specific instance, and static has no instance

---

### Classes & OOP

**Production scenario:** You have a `BasePaymentProvider` class with shared logic — logging, error handling, retry logic. `StripeProvider` and `AdyenProvider` both extend it and override the `processPayment()` method with their own provider-specific implementation. When a payment comes in, the code calls `provider.processPayment()` without caring whether it's Stripe or Adyen — polymorphism picks the right implementation at runtime.

- Inheritance: a subclass (`Dog extends Animal`) inherits fields and methods from the parent class
- `super(...)` calls the parent constructor to initialize inherited fields
- Method overriding: a subclass can replace a parent method with its own version
- `@Override` annotation tells the compiler you intend to override — catches typos in method names at compile time
- Polymorphism: declaring `Animal dog = new Dog()` means Java uses the actual object type (`Dog`) at runtime to decide which method to call, even though the declared type is `Animal`
- Declaring as `Animal` limits you to `Animal`'s methods — useful when you want to write code that works for any animal regardless of specific type

---

### Records

A `record` is a shorthand class for holding data. Java automatically generates the constructor, getters, `equals`, `hashCode`, and `toString` — you don't write any of it yourself.

```java
record User(int id, String name, String email, boolean isPremium) {}
```

Is equivalent to a full class with those four fields and all the boilerplate written out manually. You access fields as methods:

```java
user.name()
user.email()
```

You access fields as method calls with parentheses — not direct field access:

```java
user.name()    // correct — calling the generated getter method
user.name      // works but wrong — accessing the field directly, not the intended API
```

In a normal class the getter would be `getName()`. In a record it's just `name()`.

Used in exercises as a convenient data container. Not in the fast track — covered in detail in section 1 (Shaping data).

---

### Memory model

**Stack** — short-lived memory for method execution:
- Each method call gets a "frame" on the stack holding its local variables and primitives
- Fast to allocate and free — the frame is thrown away automatically when the method returns
- Primitives (`int`, `double`, `boolean`, etc.) live here directly

**Heap** — long-lived shared memory for objects:
- All objects (`String`, `ArrayList`, `Double`, etc.) live here
- Stick around as long as something holds a reference to them
- Cleaned up by the garbage collector when nothing points to them anymore

When you declare an object variable, the variable itself (the reference/pointer) lives on the stack, but the object it points to lives on the heap:

```java
int x = 5;          // x lives on the stack
String s = "hello"; // s (the reference) lives on the stack,
                    // but the String object lives on the heap
double d = 4.8;     // primitive — stack
Double d2 = 4.8;    // reference on stack → Double object on heap
```

---

## Java fundamentals

---

### Streams

A stream is a pipeline of operations on a collection — you don't modify the original list, you process it and get a result. All streams start with `.stream()` on a collection.

**Production scenario:** You fetch a list of transactions from the database. You need to return only the failed ones, with their amounts converted from pence to pounds, sorted newest first. Without streams you'd write multiple loops. With streams it's one readable pipeline: `filter` for failed → `map` to convert amount → `sorted` by date.

Streams never modify the original collection — they produce a new result. You can call `.stream()` on the same list multiple times safely.

**Stream operations quick reference:**

| Method | When to use |
|---|---|
| `filter` | Get a subset — only failed transactions, only premium users, only payments above a threshold |
| `map` | Transform data — extract emails from users, convert amounts, map objects to DTOs |
| `forEach` | Act on each element without returning anything — log, print, send notification |
| `sorted` | Order results — newest first, alphabetically, highest amount first |
| `count` | Get a number — how many transactions failed, how many users are premium |
| `anyMatch` | Yes/no across the collection — did any transaction fail? is there at least one admin? |
| `allMatch` | Yes/no across the collection — are all payments confirmed? |
| `noneMatch` | Yes/no across the collection — are there any duplicates? any blocked users? |
| `findFirst` | Get one result — first failed transaction to retry, first admin user |
| `reduce` | Collapse to one value — total amount, combined error message |
| `distinct` | Remove duplicates — unique providers from a list of transactions |
| `limit` | Pagination — take the first 20 results |
| `skip` | Pagination — skip the first page to get page 2 |
| `flatMap` | Flatten nested collections — all items across all orders in one stream |
| `mapToInt` | Numeric aggregation — total transaction amount, average order value, highest payment |
| `collect` | Materialise the stream into a List, Set, or Map — use after filter/map when you need to store the result |
| `toList` | Shorthand for collect into a List (Java 16+) |

**Notable details:**
- `findFirst()` — returns an `Optional` (might find nothing)
- `reduce((a, b) -> combine)` — returns an `Optional`
- `count()` — returns `long`, not `int`
- `forEach` — if just iterating with no other operations, call directly on the list without `.stream()`
- `collect(Collectors.toList())` — old way to get a List, replaced by `.toList()` in Java 16+
- `.toList()` returns an **unmodifiable** list — you can read it but not add or remove. If you need to modify the result, wrap it: `new ArrayList<>(stream.toList())`

**Method references** — shorthand for a lambda that just calls a single method:
- `n -> n.toUpperCase()` is the same as `String::toUpperCase`
- `n -> System.out.println(n)` is the same as `System.out::println`

**`count()` returns `long`** — declare as `long count = ...`, not `int`. If you need `int`, cast explicitly: `(int) count`

**Numeric stream operations** — use `mapToInt()` to convert a stream to an `IntStream`, which unlocks numeric aggregation methods:
- `mapToInt(n -> n.length())` — maps each element to an int (e.g. string length)
- `.sum()` — total of all values
- `.average()` — returns `OptionalDouble` (empty if stream is empty)
- `.min()` / `.max()` — return `OptionalInt` (empty if stream is empty)

```java
int total    = names.stream().mapToInt(n -> n.length()).sum();
int average  = (int) names.stream().mapToInt(n -> n.length()).average().orElse(0);
int shortest = names.stream().mapToInt(n -> n.length()).min().orElse(0);
int longest  = names.stream().mapToInt(n -> n.length()).max().orElse(0);
```

`average()` returns `OptionalDouble` — not `int` or `double` — because if the stream is empty there's no meaningful answer. `.orElse(0)` unwraps it to a plain `double`, and `(int)` casts that to an `int` (truncates, does not round).

**Collectors** — a utility class with specialized collectors for use with `.collect()`:
- `Collectors.joining(", ")` — use when building a formatted string from a collection, e.g. a comma-separated list of provider names for a log message or API response
- `Collectors.groupingBy(n -> key)` — use when you need to categorise data, e.g. group transactions by status, group payments by provider. Returns `Map<Key, List<Element>>`
- `Collectors.toMap(keyFn, valueFn)` — use when you need a lookup table from a collection, e.g. `Map<userId, email>` for quick lookups later
- `Collectors.toList()` — old way to collect into a List, replaced by `.toList()` in Java 16+

```java
// joining
String result = names.stream().collect(Collectors.joining(", "));

// groupingBy — returns Map<Character, List<String>>
var grouped = names.stream().collect(Collectors.groupingBy(n -> n.charAt(0)));
// e.g. {A=[Alice, Anna], B=[Bob], C=[Charlie], D=[David]}
```

A `Map` is a collection of key-value pairs — `map.get("A")` returns the value associated with key `"A"`.

---

### Lambdas & functional interfaces

A lambda is an anonymous function — same idea as an arrow function in JavaScript. Just a `parameter -> body` with no name.

**Production scenario:** You have a list of payments and need to find all failed ones above £1000. Instead of writing a separate named method for this one-off condition, you write it inline as a lambda passed to `filter()`: `payments.stream().filter(p -> p.status().equals("FAILED") && p.amount() > 1000)`.

```java
s -> s.length() > 3   // takes a String, returns boolean
s -> s.length()       // takes a String, returns Integer
s -> System.out.println(s)  // takes a String, returns nothing
```

The lambda itself is just the `->` expression. What it's allowed to return depends on which **functional interface** you assign it to.

**Functional interfaces** — each one defines a contract for what the lambda takes and returns:

| Interface | Takes | Returns | Use when |
|---|---|---|---|
| `Predicate<T>` | `T` | `boolean` | you need to filter a collection or check a condition — e.g. only premium users, only failed transactions |
| `Function<T, R>` | `T` | `R` | you need to transform data — extract a field, convert an object to a DTO, map amounts to a different currency |
| `Consumer<T>` | `T` | nothing | you need to act on each element without returning anything — log, print, send a notification |
| `Supplier<T>` | nothing | `T` | you need a lazy fallback value that should only be created if actually needed — e.g. `orElseGet`, `orElseThrow` |

The lambda body must match what the functional interface promises to return — if it doesn't, it won't compile.

**Predicate** — a condition that returns true or false:

```java
Predicate<String> isLongerThan3 = s -> s.length() > 3;
Predicate<String> startsWithA   = s -> s.startsWith("A");
Predicate<Integer> isEven       = n -> n % 2 == 0;
```

You could technically use `Function<String, Boolean>` instead of `Predicate<String>`, but `Predicate` is preferred — it's purpose-built and gives you `.and()`, `.or()`, `.negate()` for chaining conditions:

```java
Predicate<User> isAdult   = user -> user.age() >= 18;
Predicate<User> isPremium = user -> user.isPremium();

Predicate<User> premiumAdult  = isAdult.and(isPremium);  // both must be true
Predicate<User> adultOrPremium = isAdult.or(isPremium);  // either is enough
Predicate<User> isMinor       = isAdult.negate();        // flips the result

users.stream().filter(premiumAdult).toList();
```

A `Predicate` has a `.test()` method that runs the condition and returns true or false:

```java
Predicate<User> isAdult = user -> user.age() >= 18;
boolean result = isAdult.test(new User("Alice", 30)); // true
```

In practice you rarely call `.test()` directly like this — you pass the predicate into a method that calls it for you on each element:

```java
users.stream()
    .filter(isAdult)   // filter calls .test() on each user internally
    .toList();
```

**A lambda is a Predicate (or any functional interface) based on its shape, not its declaration.** If you write a lambda inline and the method expects a `Predicate<User>`, the compiler sees it takes a `User` and returns a `boolean` and wires it up automatically — you never have to write the word `Predicate`:

```java
// These two are identical — the inline lambda IS a Predicate, you just didn't name it
Predicate<User> isAdult = user -> user.age() >= 18;
users.stream().filter(isAdult)

users.stream().filter(user -> user.age() >= 18)
```

The reason to store it in a variable is **reuse** — if you need the same predicate in multiple places (e.g. for chaining), give it a name. If you only need it once, write it inline.

**Where each functional interface is used** — the pattern is always the same: if a method needs a condition it takes a Predicate, if it needs a transformation it takes a Function, and so on. You don't need to memorise this list — you'll start recognising the pattern as you read more code.

| Interface | Common methods that accept it |
|---|---|
| `Predicate<T>` | `stream.filter()`, `stream.anyMatch()`, `allMatch()`, `noneMatch()`, `list.removeIf()` |
| `Function<T,R>` | `stream.map()`, `stream.flatMap()`, `Optional.map()` |
| `Consumer<T>` | `stream.forEach()`, `list.forEach()`, `Optional.ifPresent()` |
| `Supplier<T>` | `Optional.orElseGet()`, `Optional.orElseThrow()` |

---

### Optional

A container that either holds a value or is empty — used instead of returning `null` to force explicit handling of the "no value" case and avoid NullPointerExceptions.

**Production scenario:** A payment service looks up a user by ID before processing a payment. The user might not exist. Without Optional you'd return `null` and risk a NullPointerException somewhere down the line. With Optional, `findById()` returns `Optional<User>` — the caller is forced to handle the empty case explicitly: either throw a meaningful error (`orElseThrow`) or provide a fallback (`orElseGet`).

- Some stream operations return `Optional` because they might find nothing: `findFirst()`, `reduce()`, `findAny()`
- Spring Data repository methods like `findById()` also return `Optional`

**Common ways to handle it:**
- `.orElse("default")` — return the value if present, otherwise return the default. The fallback is always evaluated, even if not needed.
- `.orElseGet(() -> fallback)` — same as orElse but lazy — the lambda only runs if the Optional is empty. Prefer this when the fallback is expensive (e.g. a database call).
- `.orElseThrow(() -> new SomeException())` — return the value if present, otherwise throw
- `.ifPresent(n -> action)` — only run the action if a value is present, does nothing if empty
- `.get()` — returns the value directly but throws `NoSuchElementException` if empty. Discouraged — use the options above instead.
- `.map(fn)` — transform the value inside the Optional if present, stays wrapped in Optional. Useful for extracting a field without unwrapping first:

```java
Optional<String> email = findById(1).map(user -> user.email());
System.out.println(email.orElse("no email found"));

// or chained into a String directly
String email = findById(1).map(user -> user.email()).orElse("no email found");
```

**In Spring Boot** — use `orElseThrow` when the entity must exist (e.g. updating a record), use `orElse` or `ifPresent` when absence is a normal case (e.g. optional profile data)

**Primitive Optionals** — `OptionalDouble`, `OptionalInt`, `OptionalLong` are specialized versions of `Optional` for primitive types. They work the same way but avoid boxing overhead:
- `Optional<Double>` wraps a `Double` object — two heap allocations (the Optional + the Double inside)
- `OptionalDouble` stores a primitive `double` directly inside — one heap allocation, no boxing
- Stream methods like `average()` return `OptionalDouble` rather than `Optional<Double>` for this reason

---

### Collections

#### Lists & queues

A `List` is an ordered collection that allows duplicates. Elements stay in the order you put them in and can be accessed by position.

**Production scenario:** You fetch all transactions for a merchant from the database and return them to the frontend in chronological order. Order matters — the merchant expects to see newest first after sorting. The same provider can appear many times, which is fine.

**Variants — which to use:**

| Implementation | Use when |
|---|---|
| `ArrayList` | Default choice — fast random access by index, good for most cases |
| `LinkedList` | You frequently add/remove from the middle or beginning — rare in practice |
| `ArrayDeque` | You need a queue (first-in first-out) or stack (last-in first-out) — e.g. processing jobs in order |

In practice you'll use `ArrayList` almost always. `LinkedList` and `ArrayDeque` are niche.

**Declare as the interface, create with the implementation** — this is standard Java convention you'll see everywhere in production code:

```java
List<String> providers = new ArrayList<>();   // not ArrayList<String>
Set<String> unique = new HashSet<>();          // not HashSet<String>
Map<String, Double> limits = new HashMap<>();  // not HashMap<String, Double>
```

This means if you ever need to swap the implementation (e.g. `ArrayList` → `LinkedList`), only the one line where you create it needs to change — nothing else in your code is affected.

**Real-world flow:**
A payment service fetches all transactions for a merchant from the database. They come back as a `List<Transaction>` in chronological order. You iterate over them, filter for failures, and return the results — order matters because you want to show the most recent first after sorting.

**Common methods:**
- `.add(value)` — append to the end
- `.add(index, value)` — insert at a specific position
- `.get(index)` — retrieve by position
- `.remove(value)` — remove by value
- `.remove(index)` — remove by position
- `.size()` — number of elements

**For-each loop — iterate over any collection**

The for-each loop goes through every element one at a time. Left side is the single item, right side is the collection:

```java
List<String> providers = List.of("Stripe", "Adyen", "Klarna");

for (String provider : providers) {
    System.out.println(provider);
}
```

Read it as: "for each String **provider** in **providers**". Works on any collection — List, Set, arrays. For Map you loop over `map.entrySet()` to get both key and value:

```java
Map<String, Double> limits = Map.of("Stripe", 10000.0, "Adyen", 50000.0);

for (Map.Entry<String, Double> entry : limits.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

In modern Java you'll use `.forEach()` on streams more often, but the for-each loop is common when you need a regular loop without stream operations.

---

#### Sets

A `Set` is a collection with no duplicates. Use when you need uniqueness and don't care about order.

**Production scenario:** You process a batch of transactions and need to know which providers were involved for a report. You don't want duplicates and you don't care about order — just the unique provider names. You stream the transactions, extract the provider from each, and collect into a `Set` — duplicates are discarded automatically.

**Variants — which to use:**

| Implementation | Order | Use when |
|---|---|---|
| `HashSet` | None | Default choice — fastest, order doesn't matter |
| `LinkedHashSet` | Insertion order | You need uniqueness but also want elements in the order they were added |
| `TreeSet` | Sorted (A-Z or 0-9) | You need uniqueness and want elements sorted automatically |

In practice you'll use `HashSet` almost always. Reach for `LinkedHashSet` when order matters, `TreeSet` when you need sorting.

**Real-world flow:**
You have a list of thousands of transactions. A report needs to show which providers were used, each listed once. You stream the transactions, extract the provider name from each, and collect into a `Set` — duplicates are discarded automatically:

```java
Set<String> usedProviders = transactions.stream()
    .map(t -> t.provider())
    .collect(Collectors.toSet()); // [Stripe, Adyen, Klarna] — no duplicates
```

**Common methods:**
- `.add(value)` — adds the value, ignores it if already present
- `.remove(value)` — removes the value
- `.contains(value)` — check if a value exists — faster than List for large collections
- `.size()` — number of elements

---

#### Maps

A `Map` stores key-value pairs — you look up a value by its key instantly, without looping or if/else chains.

**Real-world flow — provider transaction limits:**
1. App starts up — provider limits are loaded from the database once and stored in a `HashMap` in memory
2. Payment request comes in — `provider = "Adyen"`, `amount = 30000.0`
3. You look up the limit: `limits.get("Adyen")` → `50000.0` — instant, no database call needed
4. You compare: `30000.0 < 50000.0` → payment allowed

Without a Map you'd either hit the database on every request (slow) or write if/else chains that break every time a new provider is added:

```java
// without Map — breaks every time a provider is added or removed
if (provider.equals("Adyen")) limit = 50000.0;
else if (provider.equals("Stripe")) limit = 10000.0;

// with Map — one lookup regardless of how many providers there are
double limit = limits.getOrDefault(provider, 0.0);
```

**Variants — which to use:**

| Implementation | Order | Use when |
|---|---|---|
| `HashMap` | None | Default choice — fastest, order doesn't matter |
| `LinkedHashMap` | Insertion order | You need key-value lookup but also want entries in the order they were added |
| `TreeMap` | Sorted by key (A-Z or 0-9) | You need key-value lookup and want keys sorted automatically |

In practice you'll use `HashMap` almost always.

**Common methods:**
- `.put(key, value)` — add or update an entry
- `.get(key)` — returns the value, or `null` if the key doesn't exist
- `.getOrDefault(key, fallback)` — returns the value, or the fallback if the key doesn't exist. Prefer over `.get()` to avoid NullPointerExceptions when a missing key is a normal case.
- `.containsKey(key)` — check if a key exists
- `.forEach((key, value) -> action)` — iterate over all entries. Takes two parameters unlike List/Set forEach which takes one.

**When to use a Map vs fetching from the DB:**
- Fetch from DB each time — when the data is user-specific, changes frequently, or you only need it once (e.g. a user's profile, a specific transaction)
- Load into a Map in memory — when the data is shared across many requests and rarely changes (e.g. provider fee rates, config values). This is called caching. Spring Boot has built-in support for this (`@Cacheable`) which you'll cover later.

---

### Exception handling

**The full picture — how exceptions actually work in production:**

A payment request comes in. Your code calls an external provider API. The provider returns a 503. You can't just return null or an empty string — the caller has no idea something went wrong. Instead you throw an exception. Execution stops immediately, and the exception travels back up through every method that called this one until something catches it and decides what to do — log it, retry, return a fallback, or let it reach Spring Boot which turns it into a 500 response.

**Two flows — with and without try/catch:**

Most of the time you just throw and let it bubble — no try/catch needed:
1. Define the exception class
2. Throw it in a method when something goes wrong
3. Call the method — the exception travels up on its own to Spring Boot

Only write try/catch when you can recover from the failure — retry, fallback, log and continue:
1. Define the exception class
2. Throw it in a method when something goes wrong
3. Call that method inside a try/catch block and handle the failure

**The full try/catch flow, in order:**

**Step 1 — define the exception**

The structure is always the same. You just swap the name:

```java
class ProviderException extends RuntimeException {
    public ProviderException(String message) {
        super(message);
    }
}
```

`RuntimeException` is the base. You extend it to give your exception a meaningful name. That name is what shows up in logs — `ProviderException: Adyen returned 503` tells you immediately what went wrong without reading further. A plain `RuntimeException` loses that.

**Step 2 — throw it when something goes wrong**

```java
String chargeCard(String provider, double amount) {
    HttpResponse response = httpClient.post("https://api." + provider + ".com/charge", amount);

    if (response.statusCode() != 200) {
        throw new ProviderException(provider + " returned " + response.statusCode());
    }

    return response.body();
}
```

When `throw` executes, the method stops immediately. No return value. The exception travels up to whoever called `chargeCard`.

**Step 3 — catch it where you can do something useful**

```java
String processPayment(String provider, double amount) {
    try {
        return chargeCard(provider, amount);
    } catch (ProviderException e) {
        System.err.println("Provider call failed: " + e.getMessage());
        return "payment queued for retry";
    }
}
```

`processPayment` calls `chargeCard` inside a try block. If `chargeCard` throws a `ProviderException`, execution jumps immediately to the catch block. You write `ProviderException e` because you know that's what `chargeCard` throws — `e` gives you access to the message via `e.getMessage()`.

You catch it here because this is where you have enough context to do something useful — log it and queue for retry. Don't catch just to silence the error — an empty catch block hides bugs.

**Bubbling up — when nothing catches it**

If `processPayment` had no try/catch, the exception would keep traveling up to whatever called `processPayment`, and so on. In Spring Boot, if it reaches the top without being caught, Spring catches it and returns a 500 response. The app keeps running — only that one request fails.

**Checked vs unchecked — the two types:**

| Type | Extends | Must be declared/caught? | Use when |
|---|---|---|---|
| Checked | `Exception` | Yes — compiler forces you to handle it | External failures you can reasonably recover from — file not found, network timeout |
| Unchecked | `RuntimeException` | No — compiler doesn't force handling | Programming errors or unrecoverable failures — invalid input, null values, provider errors |

In modern Java and Spring Boot you'll almost always use unchecked exceptions (`RuntimeException`). Checked exceptions add boilerplate and are increasingly avoided.

**Custom exception — generic vs specific message**

If the exception always means the same thing, hardcode the message and take the relevant value as a parameter:

```java
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("User not found: " + id);  // always the same shape
    }
}
```

If the exception covers multiple scenarios that each need a different message, take the message as a parameter and let the caller decide:

```java
class InvalidPaymentRequestException extends RuntimeException {
    public InvalidPaymentRequestException(String message) {
        super(message);
    }
}

throw new InvalidPaymentRequestException("Card number is missing");
throw new InvalidPaymentRequestException("Amount must be greater than 0");
```

**Multiple catch blocks — handle different failures differently**

One try block can have multiple catch clauses — each catches a specific exception type:

```java
try {
    processPayment(userId, amount);
} catch (UserNotFoundException e) {
    System.out.println("User error: " + e.getMessage());  // handle 404
} catch (PaymentFailedException e) {
    System.out.println("Payment error: " + e.getMessage());  // handle 400
}
```

Catch blocks are checked top to bottom — the first one that matches runs, the rest are skipped. Only one exception can be thrown per try block — once an exception is thrown execution jumps to the catch immediately.

**finally — code that always runs**

A `finally` block runs after the try/catch regardless of whether an exception was thrown or not. Use for cleanup that must always happen — logging that a call was attempted, resetting state:

```java
String provider = "Revolut";
try {
    fetchFromProvider(provider);
} catch (RuntimeException e) {
    System.out.println("Failed: " + e.getMessage());
} finally {
    System.out.println("Provider call finished for: " + provider);  // always runs
}
```

In most real code you'd use try-with-resources instead of finally for closing resources. Use finally for non-closeable cleanup.

**In Spring Boot — @ControllerAdvice**

In Spring Boot you almost never write try/catch for business logic failures. You throw custom exceptions and let them bubble up to a `@ControllerAdvice` class that maps each exception type to an HTTP response — defined once, applies to every endpoint automatically:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(InvalidPaymentRequestException.class)
    public ResponseEntity<String> handleInvalidRequest(InvalidPaymentRequestException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
```

Your controller and service methods just return data or throw exceptions — they never touch `ResponseEntity` or HTTP status codes. Spring wraps every return value in a 200 response automatically. Only write try/catch yourself when you can genuinely recover — retry a provider call, return a fallback.

**try-with-resources — automatically close what you open**

Some resources need to be closed after use — file handles, database connections, HTTP clients. If you open one inside a regular try block and an exception is thrown, the close call never runs and the resource leaks. try-with-resources closes it automatically when the block ends, even if an exception is thrown:

```java
try (BufferedReader reader = new BufferedReader(new FileReader("payments.csv"))) {
    String line = reader.readLine();
} catch (Exception e) {
    System.out.println("Failed: " + e.getMessage());
} // reader.close() is called automatically here
```

The class inside the parentheses must implement `AutoCloseable` — that's what tells Java it can be closed. You'll understand this fully when you cover interfaces.

---

### Inheritance & polymorphism

**The full picture:**

You're building a payment platform that supports multiple providers — Stripe, Adyen, Klarna. Each provider needs to charge a card, refund a payment, and check a transaction status. The logic for logging, error handling, and retries is identical across all of them. Without inheritance you'd copy that shared logic into every provider class — a bug fix in one means fixing it in all of them. With inheritance, shared logic lives in one `BasePaymentProvider` class. Each provider extends it, inherits the shared logic for free, and only overrides the parts that are provider-specific.

**Inheritance — extending a class**

A subclass extends a parent class and inherits all its non-private fields and methods:

```java
class BasePaymentProvider {
    String name;

    BasePaymentProvider(String name) {
        this.name = name;
    }

    void logAttempt(double amount) {
        System.out.println(name + ": attempting charge of " + amount);
    }
}

class StripeProvider extends BasePaymentProvider {
    StripeProvider() {
        super("Stripe");  // calls the parent constructor
    }
}
```

`StripeProvider` inherits `logAttempt()` for free — you don't write it again. `super("Stripe")` calls the parent constructor to set the `name` field.

**Method overriding — replacing a parent method**

A subclass can replace a parent method with its own implementation:

```java
class BasePaymentProvider {
    String charge(double amount) {
        return "charged " + amount;
    }
}

class AdyenProvider extends BasePaymentProvider {
    @Override
    String charge(double amount) {
        return "Adyen charged " + amount;  // Adyen-specific implementation
    }
}
```

`@Override` tells the compiler you're intentionally replacing the parent method — if you typo the method name, the compiler catches it.

Calling `super.charge(amount)` inside the override runs the parent version first — useful when you want to keep the parent behaviour and add to it:

```java
class KlarnaProvider extends BasePaymentProvider {
    @Override
    String charge(double amount) {
        String result = super.charge(amount);       // runs parent: "charged 75.0"
        return result + " [Klarna fee added]";      // "charged 75.0 [Klarna fee added]"
    }
}
```

**Runtime polymorphism — the key concept**

You can declare a variable as the parent type but assign a child object to it:

```java
BasePaymentProvider provider = new AdyenProvider();
provider.charge(100.0);  // calls AdyenProvider's charge(), not BasePaymentProvider's
```

Java uses the actual object type (`AdyenProvider`) at runtime to decide which method to call, even though the declared type is `BasePaymentProvider`. This is runtime polymorphism.

The real power is when you have a list of providers:

```java
List<BasePaymentProvider> providers = List.of(
    new StripeProvider(),
    new AdyenProvider(),
    new KlarnaProvider()
);

for (BasePaymentProvider provider : providers) {
    provider.charge(100.0);  // each calls its own implementation
}
```

The loop doesn't know or care which provider it's dealing with — it just calls `charge()` and Java picks the right one. Adding a new provider means creating a new class — nothing else changes.

**Method overloading — same name, different parameters**

The same method name can exist multiple times in a class as long as the parameters differ. Java picks the right one at compile time based on what you pass:

```java
class PaymentService {
    void charge(double amount) { ... }
    void charge(double amount, String currency) { ... }
    void charge(double amount, String currency, String provider) { ... }
}

service.charge(100.0);                        // calls first
service.charge(100.0, "GBP");                 // calls second
service.charge(100.0, "GBP", "Adyen");        // calls third
```

Use overloading when a method does the same thing but with optional extra parameters — it's cleaner than different method names like `chargeWithCurrency` or `chargeWithProvider`.

---

### Interfaces & abstract classes

**The full picture:**

You're building a payment platform. Stripe, Adyen, Klarna — each charges a card differently, calls a different API, refunds differently. But your `PaymentService` doesn't want to know which provider it's dealing with. It just wants to call `charge()` and `refund()`. An interface defines *what* every provider must be able to do without saying *how*. Every provider implements the interface. Your service declares the type as `PaymentProvider` — Spring injects whichever implementation is active at runtime. This is the core pattern Spring Boot is built on.

An abstract class sits between an interface and a full class. Use it when multiple providers share real code — fields, constructors, concrete methods — but you still want to force each one to implement a specific part themselves.

**Production scenario:** `PaymentProvider` is an interface. `StripeProvider` and `AdyenProvider` implement it. `PaymentService` depends on `PaymentProvider` (the interface) — it never imports `StripeProvider` directly. In tests, you swap in a fake implementation. In production, Spring injects the real one. Nothing in `PaymentService` changes between environments.

---

**Interface — a contract**

An interface declares method signatures only — no method bodies, no fields (except constants). Any class that `implements` it must provide a body for every method. The compiler refuses to build if any are missing:

```java
interface PaymentProvider {
    String charge(double amount);   // implicitly public
    String refund(double amount);
}

class StripeProvider implements PaymentProvider {
    @Override
    public String charge(double amount) {
        return "Stripe charged " + amount;
    }

    @Override
    public String refund(double amount) {
        return "Stripe refunded " + amount;
    }
}
```

`implements` is used instead of `extends`. A class can implement multiple interfaces — separate them with commas: `class Foo implements A, B`. This is not possible with `extends` — a class can only extend one other class.

**Interface as type — the real power**

Declaring a variable as the interface type means your code works for any implementation:

```java
PaymentProvider provider = new StripeProvider();
provider.charge(100.0);  // calls StripeProvider's charge()

List<PaymentProvider> providers = List.of(new StripeProvider(), new AdyenProvider());

for (PaymentProvider p : providers) {
    p.charge(100.0);  // each calls its own implementation
}
```

This is exactly how Spring Boot dependency injection works — your service declares `PaymentProvider provider` and Spring decides which class to inject at runtime.

**Default methods — shared behaviour in an interface**

An interface can have a `default` method — a method with a body. All implementing classes get it for free, but can override it if they want. The `default` keyword is required:

```java
interface PaymentProvider {
    String charge(double amount);

    default void log(double amount) {
        System.out.println("Attempting charge of " + amount);
    }
}
```

Use default methods when you want shared behaviour in an interface without forcing every implementing class to write it.

**Abstract class — partial implementation**

An abstract class cannot be instantiated directly — you must extend it. It can have fields, constructors, concrete methods, and `abstract` methods. An `abstract` method has no body — the subclass must implement it:

```java
abstract class AbstractPaymentProvider {
    String name;

    AbstractPaymentProvider(String name) {
        this.name = name;
    }

    abstract String callApi(double amount);  // no body — subclass must implement

    String charge(double amount) {
        return "Charging via " + callApi(amount);  // calls the subclass version at runtime
    }
}

class KlarnaProvider extends AbstractPaymentProvider {
    KlarnaProvider() {
        super("Klarna");
    }

    @Override
    String callApi(double amount) {
        return "Klarna API: " + amount;
    }
}
```

`new AbstractPaymentProvider()` won't compile — you can never instantiate an abstract class directly.

**Interface vs abstract class — when to use each**

| | Interface | Abstract class |
|---|---|---|
| Fields | No (constants only) | Yes |
| Constructor | No | Yes |
| Multiple allowed | Yes — `implements A, B` | No — only one `extends` |
| Use when | Defining a contract — what something *must* do | Sharing code — what something *partly* does |

Rule of thumb: **start with an interface**. Add an abstract class only when multiple implementations share real code you want to write once.

**How Spring uses interfaces**

In Spring Boot you almost never depend on a concrete class directly. You declare the type as an interface:

```java
@Service
public class PaymentService {
    private final PaymentProvider provider;  // interface — not StripeProvider

    public PaymentService(PaymentProvider provider) {
        this.provider = provider;  // Spring injects the matching implementation
    }
}
```

Spring finds whichever class implements `PaymentProvider` and registered as a bean, and injects it automatically. You can swap implementations (Stripe vs Adyen, real vs mock) without touching `PaymentService`.

---

### Regular expressions

---

## Spring Boot

---

### Beans & the application context

---

### Dependency injection

---

### Core annotations

---

### Spring Data JPA

---

### REST

---

### Profiles & config

---

### Spring Retry & error handling

---

## Testing

---

### Unit testing

---

### Mocking

---

### Integration testing

---

### Mocked HTTP servers

---

## Webhooks & async patterns

---

### Callbacks & webhooks

---

### Request/response vs event-driven

---

## Cryptography basics

---

### HMAC signatures

---

### AES encryption & RSA

---

## Build & tooling

---

### Git

---

### Maven

---

### Docker

---

### CI/CD

---

### Database migrations

---

## Observability

---

### Logging
