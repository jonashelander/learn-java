package ClassesAndOOP.Inheritance;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // Domain: a payment platform supporting multiple providers.
    // All exercises build on each other — read them in order.

    public static void main(String[] args) {

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
        System.out.println("Ex 1");
        StripeProvider stripeProvider = new StripeProvider();
        stripeProvider.logAttempt(100.0);


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
        System.out.println("Ex 2");
        AdyenProvider adyenProvider = new AdyenProvider();
        StripeProvider stripeProvider2 = new StripeProvider();
        System.out.println(adyenProvider.charge(50.0));
        System.out.println(stripeProvider2.charge(50.0));


        // 3. SUPER — call the parent method from inside an override
        // Use when you want to keep the parent behaviour and add to it —
        // e.g. the base provider logs the attempt, and the subclass adds provider-specific logic on top.
        //
        // Create a class KlarnaProvider that extends BasePaymentProvider.
        //   - constructor calls super("Klarna")
        //   - overrides charge() — first calls super.charge(amount), then appends " [Klarna fee added]"
        // In main, call charge(75.0) on a KlarnaProvider and print the result.
        System.out.println("Ex 3");
        KlarnaProvider klarnaProvider = new KlarnaProvider();
        System.out.println(klarnaProvider.charge(75.0));


        // 4. RUNTIME POLYMORPHISM — write code that works for any provider
        // Use when you want to work with multiple implementations through a common type —
        // e.g. process a list of providers without knowing or caring which one each is.
        // This is the pattern Spring Boot uses everywhere.
        //
        // Declare three variables as type BasePaymentProvider, assigned to StripeProvider,
        // AdyenProvider, and KlarnaProvider instances.
        // Put them in a List<BasePaymentProvider>.
        // Loop over the list and call charge(100.0) on each — observe each calls its own implementation.
        System.out.println("EX 4");
        BasePaymentProvider basePaymentProvider = new StripeProvider();
        BasePaymentProvider basePaymentProvider2 = new AdyenProvider();
        BasePaymentProvider basePaymentProvider3 = new KlarnaProvider();

        List<BasePaymentProvider> listOfProviders = new ArrayList<>();
        listOfProviders.add(basePaymentProvider);
        listOfProviders.add(basePaymentProvider2);
        listOfProviders.add(basePaymentProvider3);

        listOfProviders.forEach(i -> System.out.println(i.charge(100.0)));



        // 5. METHOD OVERLOADING — same method name, different parameters
        // Use when a method does the same thing but with optional extra information —
        // e.g. charge with just an amount, or charge with an amount and a currency.
        //
        // Add two overloaded charge methods to StripeProvider:
        //   - charge(double amount) — override the parent method, return: "Stripe charged " + amount
        //   - charge(double amount, String currency) — returns: "Stripe charged " + amount + " " + currency
        // In main, call both versions and print the results.
        // Observe Java picks the right one based on what you pass.
        System.out.println("Ex 5");
        StripeProvider stripeProvider1 = new StripeProvider();
        System.out.println(stripeProvider1.charge(100.0));
        System.out.println(stripeProvider1.charge(100.0, "EUR"));

    }
}
