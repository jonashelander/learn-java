package ClassesAndOOP.Generics;

import java.util.List;

public class Main {

    // Domain: a payment platform making outbound calls to provider APIs.
    // All exercises build on each other — read them in order.

    //2
    // Add a static method to Main (or a helper class):
    //   static <T> T firstOrThrow(List<T> items, String errorMessage)
    //   Note: the <T> before the return type is what makes it a generic method.
    //         Java infers T from what you pass in — you never write it explicitly when calling.
    //   - throws RuntimeException with errorMessage if the list is empty
    //   - otherwise returns items.get(0)
    static <T> T firstOrThrow(List<T> items, String errorMessage) {
        if (items.isEmpty()) throw new RuntimeException(errorMessage);
        return items.get(0);
    }

    public static void main(String[] args) {


        // 1. GENERIC CLASS — one class that works for any response type
        // Use when you need a wrapper or container that holds different types depending on the caller —
        // e.g. every provider API call returns a status code and a body, but the body type differs
        // per call: ChargeResult, RefundResult, StatusResult. One class handles the envelope.
        //
        // Create a class ApiResponse<T> with three fields:
        //   - int statusCode
        //   - T body         (T is the type parameter — a placeholder the caller fills in)
        //   - String error
        //   Note: declare <T> after the class name: class ApiResponse<T> { ... }
        //         T can be used anywhere inside the class body just like a real type.
        //
        // Add a constructor that takes statusCode and body.
        // Add a method: boolean isSuccess() — returns true if statusCode is 200–299.
        //
        // Create a class ChargeResult with one field: String transactionId.
        // Constructor takes a String transactionId.
        //
        // In main:
        //   - Create an ApiResponse<ChargeResult> with statusCode 200 and a ChargeResult("txn-001")
        //   - Print isSuccess() and the transactionId from the body — no cast needed
        System.out.println("Ex 1");
        ApiResponse<ChargeResult> apiResponse = new ApiResponse(200, "txn-001");
        System.out.println(apiResponse.isSuccess());


        // 2. GENERIC METHOD — a utility that works for any type without a full generic class
        // Use when you want a reusable utility method that isn't tied to a specific type —
        // e.g. "give me the first item from any list, or throw if empty".
        // This is the same pattern used in Spring's utility classes.
        //
        // Add a static method to Main (or a helper class):
        //   static <T> T firstOrThrow(List<T> items, String errorMessage)
        //   Note: the <T> before the return type is what makes it a generic method.
        //         Java infers T from what you pass in — you never write it explicitly when calling.
        //   - throws RuntimeException with errorMessage if the list is empty
        //   - otherwise returns items.get(0)
        //
        // In main:
        //   - Call firstOrThrow on a List.of("txn-001", "txn-002") and print the result
        //   - Call firstOrThrow on an empty List.of() inside a try/catch and print the exception message
        System.out.println("Ex 2");
        System.out.println(firstOrThrow(List.of("txn-001", "txn-002"), "Nothing here"));
        System.out.println(firstOrThrow(List.of(11, 12, 14), "Nothing here"));


        // 3. BOUNDED TYPE PARAMETER — exercises 3 and 4 build one thing together.
        //
        // The goal: build a base class that handles the shared charge logic for all providers,
        // where each provider has its own config object (with its own fields like apiKey, merchantId etc).
        // The base class needs to hold that config and use it — but it shouldn't be locked to one
        // specific config type. That's what the bounded type parameter solves.
        //
        // Step 1 — Create an abstract class ProviderConfig with one field: String apiKey.
        //           Constructor takes a String apiKey and assigns it.
        //           This is the base config that all provider configs will extend.
        //
        // Step 2 — Create a class KlarnaConfig that extends ProviderConfig.
        //           Constructor takes a String apiKey and calls super(apiKey).
        //           This represents Klarna's specific config — in reality it might have more fields,
        //           but apiKey is enough for this exercise.
        //
        // Step 3 — Create an abstract class AbstractProviderHandler<C extends ProviderConfig>.
        //           The <C extends ProviderConfig> means: C is a type placeholder, but it must always
        //           be a ProviderConfig or a subclass of it. This lets the class hold any provider's
        //           config while still being able to use config.apiKey (which all ProviderConfigs have).
        //           Add:
        //   - a field: C config
        //   - a constructor that takes C config and assigns it
        //   - an abstract method: String callApi(double amount)
        //   - a concrete method: String charge(double amount) — returns "Charging via " + callApi(amount)
        //
        // No main code needed here — you use these classes in exercise 4.


        // 4. EXTENDING A GENERIC CLASS — this is what you will actually do on the job.
        //
        // The base class AbstractProviderHandler<C extends ProviderConfig> is already written (exercise 3).
        // Now you create a concrete provider by extending it and filling in C with your specific config type.
        // This is exactly the pattern used in real provider codebases — the company writes the base class,
        // you extend it for your provider.
        //
        // Create a class KlarnaHandler that extends AbstractProviderHandler<KlarnaConfig>.
        //   Note: by writing <KlarnaConfig> here, you are filling in C — so the inherited field
        //         "config" is now typed as KlarnaConfig, not just ProviderConfig.
        //   - constructor takes a KlarnaConfig and calls super(config)
        //   - implements callApi to return: "Klarna API [" + config.apiKey + "] charged " + amount
        //
        // In main:
        //   - Create a KlarnaConfig("klarna-key-abc")
        //   - Create a KlarnaHandler with that config
        //   - Call charge(150.0) and print the result
        //   - Print klarnaHandler.config.apiKey and observe it is typed as KlarnaConfig — no casting needed
        System.out.println("Ex 3 and 4");
        KlarnaConfig klarnaConfig = new KlarnaConfig("klarna-api-key");
        KlarnaHandler klarnaHandler = new KlarnaHandler(klarnaConfig);
        String amount = klarnaHandler.charge(150.0);
        System.out.println(amount);
        System.out.println(klarnaHandler.config.apikey);


    }
}