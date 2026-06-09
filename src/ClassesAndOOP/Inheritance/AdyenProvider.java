package ClassesAndOOP.Inheritance;

// 2. METHOD OVERRIDING — replace a parent method with a subclass-specific version
// Use when subclasses share the same method signature but need different implementations —
// e.g. each provider charges a card differently, but the method is always called charge().
//
// Add a method charge(double amount) to BasePaymentProvider that returns: name + " charged " + amount
// Create a class AdyenProvider that extends BasePaymentProvider.
//   - constructor calls super("Adyen")
//   - overrides charge() to return: "Adyen API: charged " + amount
// In main, create both a StripeProvider and an AdyenProvider.
// Call charge(50.0) on each and print the results — observe different output.
// Use @Override on the AdyenProvider method.
public class AdyenProvider extends BasePaymentProvider{
    public AdyenProvider() {
        super("Adyen");
    }

    @Override
    public String charge(double amount) {
        return "Adyen API: charged " + amount;
    }
}
