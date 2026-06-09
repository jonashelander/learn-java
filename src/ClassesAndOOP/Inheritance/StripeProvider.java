package ClassesAndOOP.Inheritance;

// 1. INHERITANCE — share logic across subclasses
// Use when multiple classes share the same fields and behaviour —
// e.g. all payment providers need a name, and all log their attempts the same way.
// Put shared logic in the parent class once — subclasses inherit it for free.
//
// Create a class BasePaymentProvider with:
//   - a String field: name
//   - a constructor that takes a String name and assigns it
//   - a method logAttempt(double amount) that prints: name + ": attempting charge of " + amount
// Create a class StripeProvider that extends BasePaymentProvider.
//   - its constructor takes no parameters and calls super("Stripe")
// In main, create a StripeProvider and call logAttempt(100.0).
// Observe that StripeProvider inherited the method without writing it.
public class StripeProvider extends BasePaymentProvider {
    StripeProvider() {
        super("Stripe");
    }


    // 5. METHOD OVERLOADING — same method name, different parameters
    // Use when a method does the same thing but with optional extra information —
    // e.g. charge with just an amount, or charge with an amount and a currency.
    //
    // Add two overloaded charge methods to StripeProvider:
    //   - charge(double amount) — override the parent method, return: "Stripe charged " + amount
    //   - charge(double amount, String currency) — returns: "Stripe charged " + amount + " " + currency
    // In main, call both versions and print the results.
    // Observe Java picks the right one based on what you pass.
    @Override
    public String charge(double amount) {
        return super.charge(amount);
    }

    public String charge(double amount, String currency) {
        return name + " charged " + amount + " " + currency;
    }
}