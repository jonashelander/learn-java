package ClassesAndOOP.Optional;

import java.util.List;
import java.util.Optional;

public class Main {

    record User(int id, String name, String email, boolean isPremium) {}

    static List<User> db = List.of(
        new User(1, "Alice", "alice@example.com", true),
        new User(2, "Bob",   "bob@example.com",   false),
        new User(3, "Anna",  "anna@example.com",  true)
    );

    // Returns the user with the given id, or empty if not found
    static Optional<User> findById(int id) {
        // TODO: use db.stream(), filter by user id, and return the first match
/*         Optional<String> testEmail = db.stream().filter(user -> user.id() == id).findFirst().map(User::email);
        System.out.println(testEmail.orElse("nothing here"));*/
        return db.stream().filter(user -> user.id() == id).findFirst();
    }

    public static void main(String[] args) {


        // 1. orElseThrow — get a user that exists, throw if not found
        // Use when the value must exist — e.g. loading a user to update their profile.
        // If they don't exist something is genuinely wrong, so failing loudly is correct.
        // Call findById with an id that exists and assign the result to a User variable.
        // If not found, throw a RuntimeException with a meaningful message.
        User user = findById(2).orElseThrow(() -> new RuntimeException("User with id 2 not found"));
        System.out.println(user);


        // 2. orElse — get a user with a fallback
        // Use when absence is a normal case — e.g. no session exists so return a guest user.
        // The fallback is always created, even if the Optional has a value (unlike orElseGet).
        // Call findById with an id that does NOT exist.
        // If not found, fall back to a guest user: id=0, name="Guest", email="guest@example.com", isPremium=false
        User noUser = findById(4).orElse(new User(99, "ghost", "ghost@email.com", false));
        System.out.println(noUser);


        // 3. orElseGet — same result as orElse but lazy
        // Prefer over orElse when the fallback is expensive — e.g. a database call or object
        // that's costly to construct. The lambda only runs if the Optional is actually empty.
        // Rewrite exercise 2 using orElseGet instead of orElse.
        User notUserLamnda = findById(0).orElseGet(() -> new User(0, "Guest", "guest@example.com", false));
        System.out.println(notUserLamnda);


        // 4. ifPresent — act only if a value exists
        // Use when you want to do something with a value if it exists, but nothing if it doesn't —
        // avoids an explicit null check. Common for side effects like logging or sending a notification.
        // Call findById with an id that exists.
        // If found, print: "Welcome back, <name>!"
        // If not found, do nothing — ifPresent handles this automatically.
        System.out.println("4");
        findById(2).ifPresent(i -> System.out.println(user.name));
        findById(20).ifPresent(i -> System.out.println(user.name));


        // 5. map — transform the value inside the Optional
        // Use when you want to extract or transform the value without unwrapping first —
        // the empty case stays handled automatically. Common when you only need one field
        // from an object you looked up, e.g. get the email from a user if they exist.
        // Call findById with an id that exists.
        // Use map() to extract just the email address from the User.
        // Assign the result to an Optional<String> and print it with orElse("no email found").
        Optional<String> userEmail = findById(20).map(i -> i.email());
        System.out.println(userEmail.orElse("no email found"));


        // 6. CHAINING — combine map and orElse in a single expression
        // This is the most common pattern in real code — look something up, extract a field,
        // and provide a fallback, all in one line. No null checks, no intermediate variables.
        // Call findById with an id that does NOT exist.
        // Chain map() to extract the email, then orElse() to fall back to "unknown@example.com".
        // Assign directly to a String (not Optional<String>) and print it.
        String notExist = findById(10).map(i -> i.email()).orElse("unknown@example.com");
        System.out.println(notExist);
    }
}