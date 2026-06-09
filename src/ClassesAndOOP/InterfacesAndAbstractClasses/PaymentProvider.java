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
}
