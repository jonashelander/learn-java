package ClassesAndOOP.InterfacesAndAbstractClasses;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // Domain: a payment platform supporting multiple providers.
    // All exercises build on each other — read them in order.

    public static void main(String[] args) {


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
        System.out.println("Ex 1");
        StripeProvider stripeProvider = new StripeProvider();
        AdyenProvider adyenProvider = new AdyenProvider();
        System.out.println(stripeProvider.charge(50.0));
        System.out.println(stripeProvider.refund(50.0));
        System.out.println(adyenProvider.charge(50.0));
        System.out.println(adyenProvider.refund(50.0));


        // 2. INTERFACE AS TYPE — write code that works for any provider
        // Use when you want to process multiple implementations without caring which one each is —
        // e.g. loop over all registered providers and charge each one. This is the pattern
        // Spring Boot uses everywhere: depend on the interface, not the concrete class.
        //
        // Declare two variables as type PaymentProvider (not StripeProvider or AdyenProvider).
        // Assign one a StripeProvider and the other an AdyenProvider.
        // Put them in a List<PaymentProvider>.
        // Loop over the list and call charge(100.0) on each — observe each calls its own implementation.
        System.out.println("Ex 2");
        PaymentProvider paymentProvider = new StripeProvider();
        PaymentProvider paymentProvider2 = new AdyenProvider();
        List<PaymentProvider> providers = new ArrayList<>();
        providers.add(paymentProvider);
        providers.add(paymentProvider2);
        providers.forEach(i -> System.out.println(i.charge(100)));


        // 3. DEFAULT METHOD — add shared behaviour to an interface
        // Use when you want all implementing classes to share a method without them having to write it —
        // e.g. all providers log their attempts the same way, so define it once in the interface.
        //
        // Add a default method to PaymentProvider:
        //   - default void log(double amount) — prints: "Attempting charge of " + amount
        //   Note: "default" keyword is required. The method has a body inside the interface itself.
        //         All classes that implement PaymentProvider get this method for free.
        //         They can override it if they want, but don't have to.
        //
        // In main, create a StripeProvider and call log(75.0) on it — without writing anything in StripeProvider.


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

    }
}
