package ClassesAndOOP.InterfacesAndAbstractClasses;

// 5. MULTIPLE INTERFACES — a class can implement more than one interface
// Use when a class must satisfy multiple contracts —
// e.g. a provider both charges payments AND supports refunds as a separate capability.
//
// Create a second interface Refundable with one method:
//   - String refund(double amount)
//
// Create a class RevolutProvider that extends AbstractPaymentProvider
// AND implements Refundable.
//   Note: syntax is: class Foo extends Bar implements A, B
//         A class can only extend one class but can implement as many interfaces as needed.
//   - constructor calls super("Revolut")
//   - implements callApi to return: "Revolut API: " + amount
//   - implements refund to return: "Revolut refunded " + amount
//
// In main, declare a variable as Refundable, assign it a RevolutProvider.
// Call refund(50.0) on it and print the result.
// Then declare another variable as AbstractPaymentProvider, assign the same RevolutProvider.
// Call charge(50.0) on it — observe the same object works through both types.

public class RevolutProvider  extends AbstractPaymentProvider implements Refundable{

    public RevolutProvider() {
        super("Revolut");
    }

    @Override
    String callApi(double amount) {
        return "Revolut API: " + amount;
    }

    @Override
    public String refund(double amount) {
        return "Revolut refunded " + amount;
    }
}
