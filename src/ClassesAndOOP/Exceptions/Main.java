package ClassesAndOOP.Exceptions;

public class Main {

    public static String lookupUser(int id) {
        if (id == 99) throw new UserNotFoundException(id);
        return "Jonas";
    }

    public static String callProviderApi(String provider) {
        if (provider.equals("Klarna")) {
            throw new RuntimeException("Provider unavailable: " + provider);
        }
        return "No exception";
    }

    public static void processPayment(int userId, double amount) {
        if (userId == 0) throw new UserNotFoundException(userId);
        if (amount <= 0) throw new PaymentFailedException(amount);
    }

    public static void fetchFromProvider(String provider) {
        if (provider.equals("Revolut")) {
            throw new RuntimeException("Provider error: " + provider);
        }
    }

    public static void main(String[] args) {

        // 1. THROWING AN EXCEPTION — signal that something went wrong
        // Use when your code encounters a state it cannot proceed from —
        // e.g. a required user was not found, a payment amount is negative,
        // a provider response is missing a required field.
        // The call stack unwinds from this point until something catches it.
        //
        // Write a method lookupUser(int id) that returns a String (the user's name).
        // If id < 0, throw a RuntimeException with the message "Invalid user id: " + id.
        // Call it from main with a valid id (prints the name) and an invalid id (throws).
        System.out.println(lookupUser(2));
        System.out.println(lookupUser(-1));


        // 2. CUSTOM EXCEPTION — give your errors a meaningful name
        // Use when different failures need to be told apart —
        // e.g. "user not found" is different from "payment declined", and callers
        // may want to handle each differently. A plain RuntimeException loses that distinction.
        //
        // Create a class UserNotFoundException that extends RuntimeException.
        // Its constructor takes an int id and calls super("User not found: " + id).
        // Rewrite lookupUser() from exercise 1 to throw UserNotFoundException instead of RuntimeException
        // when the user doesn't exist (use id == 99 to simulate a missing user).
        // Call it with id 99 and observe the exception message.
        //System.out.println(lookupUser(99));


        // 3. TRY/CATCH — intercept an exception and handle it
        // Use when you can do something useful with the failure —
        // e.g. log it, return a fallback response, retry the call.
        // Don't catch just to silence the error — an empty catch block hides bugs.
        //
        // Write a method callProviderApi(String provider) that throws a RuntimeException
        // with the message "Provider unavailable: " + provider if provider equals "Klarna".
        // In main, call it inside a try/catch block.
        // In the catch block, print "Caught error: " + e.getMessage().
        // Call it once with "Adyen" (no error) and once with "Klarna" (catches the error).
        try {
            callProviderApi("Adyen");
        } catch (Exception e) {
            System.out.println("Caught error: " + e.getMessage());
        }

        try {
            callProviderApi("Klarna");
        } catch (Exception e) {
            System.out.println("Caught error: " + e.getMessage());
        }


        // 4. MULTIPLE CATCH BLOCKS — handle different failures differently
        // Use when different exceptions require different responses —
        // e.g. a UserNotFoundException means return 404, a PaymentFailedException means return 400,
        // an unexpected RuntimeException means log and return 500.
        //
        // Create a second custom exception PaymentFailedException (same structure as UserNotFoundException).
        // Write a method processPayment(int userId, double amount) that:
        //   - throws UserNotFoundException if userId == 0
        //   - throws PaymentFailedException if amount <= 0
        // In main, call it with (0, 100.0) and (1, -50.0) — each in a try/catch with two catch blocks.
        // Print different messages depending on which exception was caught.
        try {
            processPayment(0, -100.0);
        } catch (UserNotFoundException u) {
            System.out.println("Caught error: " + u);
        } catch (PaymentFailedException p) {
            System.out.println("Caught error: " + p);
        }

        try {
            processPayment(1, -50.0);
        } catch (UserNotFoundException u) {
            System.out.println("Caught error: " + u.getMessage());
        } catch (PaymentFailedException p) {
            System.out.println("Caught error: " + p.getMessage());
        }


        // 5. FINALLY — code that always runs, even if an exception was thrown
        // Use when you need to clean up after an operation regardless of outcome —
        // e.g. closing a connection, logging that a call was attempted, resetting state.
        // In most real code you'd use try-with-resources instead (see exercise 6),
        // but finally is still useful for non-closeable cleanup.
        //
        // Write a method fetchFromProvider(String provider) that throws a RuntimeException
        // if provider equals "Revolut".
        // Wrap the call in a try/catch/finally block.
        // The finally block should always print "Provider call finished for: " + provider.
        // Call it once with "Adyen" and once with "Revolut" — observe finally runs both times.
        String provider1 = "Revolut";
        String provider2 = "Adyen";
        try {
            fetchFromProvider(provider1);
        } catch (RuntimeException r) {
            System.out.println(r.getMessage());
        } finally {
            System.out.println("Provider call finished for: " + provider1);
        }

        try {
            fetchFromProvider(provider2);
        } catch (RuntimeException r) {
            System.out.println(r.getMessage());
        } finally {
            System.out.println("Provider call finished for: " + provider2);
        }


        // 6. TRY-WITH-RESOURCES — automatically close a resource when done
        // Use whenever you open something that must be closed — file handles, DB connections,
        // HTTP clients. Without this, a crash inside the try block leaks the resource.
        // Java calls .close() automatically at the end of the block, even if an exception is thrown.
        //
        // Note: interfaces haven't been covered yet — for now just treat
        // "implements AutoCloseable" as a rule that makes the class work in try-with-resources.
        //
        // Write a class FakeHttpClient that implements AutoCloseable.
        // Its constructor prints "Client opened".
        // Its close() method prints "Client closed".
        // Its call() method returns the String "200 OK".
        // Use try-with-resources to open the client, call it, and print the response.
        // You will need a catch block for Exception — AutoCloseable requires it.
        // Observe that "Client closed" is printed automatically after the block.
        try (FakeHttpClient f = new FakeHttpClient()) {
            String response = f.call();
            System.out.println(response);
        } catch (Exception e){
            System.out.println(e);
        }

    }
}
