package ClassesAndOOP.InterfacesAndAbstractClasses;

// 1. INTERFACE — define a contract that all providers must follow
// Use when multiple classes need to share the same method signatures but implement them differently —
// e.g. every provider must be able to charge and refund, but each does it its own way.
//
// Create an interface PaymentProvider with two methods:
//   - String charge(double amount)
//   - String refund(double amount)
// Note: methods in an interface have no body — just the signature ending with a semicolon.
//       They are implicitly public, so no access modifier is needed.
//
// Create a class StripeProvider that implements PaymentProvider.
//   Note: "implements" is used instead of "extends" — it means this class promises
//         to provide a body for every method the interface declares.
//   - charge returns: "Stripe charged " + amount
//   - refund returns: "Stripe refunded " + amount
//
// Create a class AdyenProvider that implements PaymentProvider.
//   - charge returns: "Adyen charged " + amount
//   - refund returns: "Adyen refunded " + amount
//
// In main, create one of each, call charge(50.0) on both, and print the results.
public interface PaymentProvider {
    String charge(double amount);

    String refund(double amount);

    // 3. DEFAULT METHOD — add shared behaviour to an interface
    // Use when you want all implementing classes to share a method without them having to write it —
    // e.g. all providers log their attempts the same way, so define it once in the interface.
    //
    // Add a default method to PaymentProvider:
    //   - default void log(double amount) — prints: "Attempting charge of " + amount
    //   Note: "default" keyword is required. The method has a body inside the interface itself.
    //         All classes that implement PaymentProvider get this method for free.
    //         They can override it if they want, but don't have to.
    //         They can override it if they want, but don't have to.
    //
    // In main, create a StripeProvider and call log(75.0) on it — without writing anything in StripeProvider.
    default void log(double amount) {
        System.out.println("Attempting charge of " + amount);
    }

}
