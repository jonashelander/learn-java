package ClassesAndOOP.Lambdas;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {

    record User(String name, String email, int age, boolean isPremium) {}

    // Simulates a database lookup — returns empty if the id doesn't exist
    static Optional<User> findById(int id) {
        List<User> db = List.of(
            new User("Alice", "alice@example.com", 30, true),
            new User("Bob",   "bob@example.com",   17, false)
        );
        return (id >= 0 && id < db.size()) ? Optional.of(db.get(id)) : Optional.empty();
    }

    public static void main(String[] args) {

        List<User> users = List.of(
            new User("Alice", "alice@example.com", 30, true),
            new User("Bob",   "bob@example.com",   17, false),
            new User("Anna",  "anna@example.com",  25, true),
            new User("Carl",  "carl@example.com",  15, false)
        );

        // 1. PREDICATE — a condition that returns true or false
        // Use when you need to filter or test elements — e.g. find all premium users, all failed transactions,
        // any payment above a threshold. Named it as a variable here so it can be reused in exercise 7.
        Predicate<User> isAdult = user -> user.age() >= 18;

        System.out.println("Adults:");
        users.stream()
             .filter(isAdult)
             .forEach(u -> System.out.println("  " + u.name()));


        // 2. FUNCTION — takes an input and returns an output
        // Use when you need to transform data — e.g. extract emails from users, map a domain object
        // to a DTO, convert amounts to a different currency.
        Function<User, String> toEmail = user -> user.email();

        System.out.println("Emails:");
        users.stream()
             .map(toEmail)
             .forEach(email -> System.out.println("  " + email));


        // 3. CONSUMER — takes an input and does something with it, returns nothing
        // Use when you need to act on each element without returning anything — e.g. log each
        // transaction, send a notification per user, print a report line.
        Consumer<User> printWelcome = user -> System.out.println("Welcome, " + user.name() + "!");

        System.out.println("Welcoming premium users:");
        users.stream()
             .filter(User::isPremium)
             .forEach(printWelcome);


        // 4. SUPPLIER — takes no input and returns a value
        // You almost never store a Supplier in a variable — you pass a lambda inline.
        // orElseThrow() and orElseGet() both take a Supplier: the lambda runs only if needed.
        User found = findById(0)
            .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Found: " + found.name());

        User fallback = findById(99)
            .orElseGet(() -> new User("Guest", "guest@example.com", 0, false));
        System.out.println("Fallback: " + fallback.name());


        // 5. METHOD REFERENCES — shorthand for lambdas that call a single method
        // Use when the lambda does nothing but call one existing method — cleaner and more readable.
        // Common in stream pipelines where you'd otherwise write boilerplate like n -> n.toString().
        // user -> user.email() and User::email are identical — the compiler treats them the same.
        Function<User, String> toEmailRef = User::email;

        System.out.println("Emails via method reference:");
        users.stream()
             .map(toEmailRef)
             .forEach(System.out::println);  // System.out::println is also a method reference


        // 6. FUNCTION CHAINING — combine multiple Functions with andThen()
        // Use when you have a multi-step transformation — each step stays small and focused.
        // In production: extract a field, then format it, then validate it — all in a pipeline.
        Function<User, String> toUpperEmail = toEmail.andThen(String::toUpperCase);
        System.out.println("Uppercased emails:");
        users.stream()
             .map(toUpperEmail)
             .forEach(email -> System.out.println("  " + email));


        // 7. PREDICATE CHAINING — combine predicates with and(), or(), negate()
        // Use instead of writing complex inline conditions — keeps each condition named and reusable.
        // In production: filter transactions that are both high-value AND from a specific provider.
        Predicate<User> isPremium = User::isPremium; // method reference for user -> user.isPremium()
        Predicate<User> premiumAdult  = isAdult.and(isPremium);   // must be both
        Predicate<User> adultOrPremium = isAdult.or(isPremium);   // either is enough
        Predicate<User> isMinor       = isAdult.negate();

        System.out.println("Premium adults:");
        users.stream().filter(premiumAdult).forEach(u -> System.out.println("  " + u.name()));

        System.out.println("Adult or premium:");
        users.stream().filter(adultOrPremium).forEach(u -> System.out.println("  " + u.name()));

        System.out.println("Minors:");
        users.stream().filter(isMinor).forEach(u -> System.out.println("  " + u.name()));
    }
}