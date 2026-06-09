package ClassesAndOOP.Inheritance;

// 3. SUPER — call the parent method from inside an override
// Use when you want to keep the parent behaviour and add to it —
// e.g. the base provider logs the attempt, and the subclass adds provider-specific logic on top.
//
// Create a class KlarnaProvider that extends BasePaymentProvider.
//   - constructor calls super("Klarna")
//   - overrides charge() — first calls super.charge(amount), then appends " [Klarna fee added]"
// In main, call charge(75.0) on a KlarnaProvider and print the result.
public class KlarnaProvider extends BasePaymentProvider {
    public KlarnaProvider() {
        super("Klarna");
    }

    @Override
    public String charge(double amount) {
        String s = super.charge(amount);

        return s + " [Klarna fee added]";
    }
}
