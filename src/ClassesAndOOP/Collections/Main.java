package ClassesAndOOP.Collections;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    record Payment(int id, String provider, double amount, String status) {
    }

    public static void main(String[] args) {

        // 1. LIST — ordered, allows duplicates, access by index
        // Use a List when order matters or duplicates are allowed — e.g. a list of transactions
        // where the same provider can appear multiple times and you want them in the order they happened.
        // Create an ArrayList of payment providers (strings): "Stripe", "Adyen", "Klarna", "Stripe"
        // Print the list, then print the element at index 1.
        ArrayList<String> providers = new ArrayList<>();
        providers.add("Stripe");
        providers.add("Adyen");
        providers.add("Klarna");
        providers.add("Stripe");
        providers.forEach(System.out::println);
        String indexOne = providers.get(1);
        System.out.println(indexOne);


        // 2. LIST — add and remove
        // Lists are mutable — you update them as things change, e.g. a new provider is onboarded
        // or a decommissioned one is removed. This is everyday list manipulation.
        // Add "PayPal" to the list from exercise 1.
        // Remove "Klarna" from the list.
        // Print the updated list.
        providers.add("PayPal");
        providers.remove("Klarna");
        System.out.println(providers);


        // 3. SET — unordered, no duplicates
        // Use when you need uniqueness — e.g. all distinct statuses from a list of transactions,
        // or the unique providers used in a report. Order doesn't matter, duplicates are ignored.
        // Create a HashSet from the same provider names as exercise 1.
        // Print the set and observe that duplicates are removed and order is not guaranteed.
        HashSet<String> unique = new HashSet<>(providers);
        unique.add("Klarna");
        System.out.println(unique);


        // 4. MAP — key-value pairs
        // Use when you need fast lookups by a key — e.g. transaction limits per provider,
        // fee rates per currency, config values per environment. get() is O(1) — instant lookup.
        // Create a HashMap<String, Double> representing a transaction limit per provider:
        // Stripe -> 10000.0, Adyen -> 50000.0, Klarna -> 5000.0
        // Print the limit for "Adyen".
        // Add a new entry: PayPal -> 15000.0
        // Print the full map.
        HashMap<String, Double> limits = new HashMap<>();
        limits.put("Stripe", 10000.0);
        limits.put("Adyen", 50000.0);
        limits.put("Klarna", 5000.0);
        System.out.println(limits.get("Adyen"));
        limits.put("PayPal", 15000.0);
        System.out.println(limits);


        // 5. MAP — safe lookup
        // Use getOrDefault() instead of get() when a missing key is a normal case — e.g. looking up
        // a provider that hasn't been configured yet. get() returns null for missing keys, which
        // will cause a NullPointerException if you use the result without checking.
        // Using the map from exercise 4, look up the limit for "Revolut" (which doesn't exist).
        // Use getOrDefault() to return 0.0 if the key is not found.
        // Print the result.
        double defaultLimimt = limits.getOrDefault("Revolut", 00.0);
        System.out.println(defaultLimimt);


        // 6. CHOOSING THE RIGHT COLLECTION
        // This is the real pattern — you get raw data (a list of transactions from a DB or API)
        // and use streams to shape it: filter for a subset, deduplicate for a report, group for
        // aggregation. Knowing which collection fits the result is what makes the difference.
        // Create a List<Payment> with 4 payments — mix of statuses: "SUCCESS", "FAILED", "SUCCESS", "PENDING"
        // a) Use a stream to filter and collect only "SUCCESS" payments into a new List.
        // b) Use a stream to collect all unique statuses into a Set.
        // c) Use a stream to group payments by status into a Map<String, List<Payment>>.
        //    Print how many SUCCESS payments there are using the map.
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(1, "Adyen", 50.0, "SUCCESS"));
        payments.add(new Payment(1, "Adyen", 50.0, "PENDING"));
        payments.add(new Payment(3, "Stripe", 50.0, "FAILED"));
        payments.add(new Payment(4, "PayPal", 50.0, "SUCCESS"));

        //Only success payments
        List<Payment> successPayments = new ArrayList<>(payments.stream().filter(p -> p.status().equals("SUCCESS")).toList());
        System.out.println(successPayments);

        //Unique statuses
        Set<String> uniqueStatuses = new HashSet<>(payments.stream().map(Payment::status).toList());
        System.out.println(uniqueStatuses);

        //Group by status
        Map<String, List<Payment>> groupByStatus = payments.stream().collect(Collectors.groupingBy(Payment::status));
        groupByStatus.forEach((key, value) -> System.out.println(key + ", " + value));

    }
}