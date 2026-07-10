package ClassesAndOOP.InterfacesAndAbstractClasses;

// 4. ABSTRACT CLASS — force subclasses to implement specific parts
// Use when multiple providers share real code (fields, constructors, concrete methods)
// but each must implement a provider-specific step themselves —
// e.g. the retry logic is shared, but the actual API call differs per provider.
//
// Create an abstract class AbstractPaymentProvider with:
//   - a String field: name
//   - a constructor that takes a String name and assigns it
//   - an abstract method: String callApi(double amount)
//     Note: "abstract" before a method means no body — the subclass MUST implement it.
//           A class with any abstract method must itself be declared abstract.
//           You cannot do "new AbstractPaymentProvider()" — the compiler will refuse.
//   - a concrete method: String charge(double amount) — returns: "Charging via " + callApi(amount)
//     This calls callApi(), which will resolve to the subclass's implementation at runtime.
//
// Create a class KlarnaProvider that extends AbstractPaymentProvider.
//   - constructor calls super("Klarna")
//   - implements callApi to return: "Klarna API: " + amount
//
// In main, create a KlarnaProvider, call charge(200.0), and print the result.
// Observe that charge() is defined in the abstract class but calls KlarnaProvider's callApi().

abstract public class AbstractPaymentProvider {
    String name;

    public AbstractPaymentProvider(String name) {
        this.name = name;
    }

    abstract String callApi(double amount);

    String charge(double amount) {
        return "Charging via " + callApi(amount);
    }
}
