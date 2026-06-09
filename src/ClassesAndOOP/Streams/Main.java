package ClassesAndOOP.Streams;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<String> names = List.of("Alice", "Bob", "Charlie", "Anna", "David");

        // 1. FILTER — keep only elements that match a condition
        // Use when you need a subset — e.g. only failed transactions, only premium users, only
        // payments above a threshold. Does not modify the original list.
        // Print only names that start with "A"
        //names.stream().filter(n -> n.startsWith("A")).forEach(System.out::println);


        // 2. MAP — transform each element into something else
        // Use when you need to convert data — e.g. extract emails from a list of users,
        // convert amounts from one currency to another, map domain objects to response DTOs.
        // Convert all names to uppercase and print them
        List<String> names2 = names.stream().map(i -> i.toUpperCase()).toList();
        System.out.println(names2);
        names.stream().map(String::toUpperCase).forEach(System.out::println);


        // 3. COLLECT — gather stream results back into a List
        // Streams are lazy pipelines — collect() is what materialises the result into an actual List.
        // Use after filter/map when you need to store or return the results.
        // Filter names longer than 3 characters and collect into a new List, then print it

        //Old way
        List<String> longNames = names.stream().filter(i -> i.length() > 3).collect(Collectors.toList());
        System.out.println(longNames);
        //New way
        List<String> longNames2 = names.stream().filter(i -> i.length() > 3).toList();
        System.out.println(longNames2);


        // 4. FOREACH — iterate over each element
        // Use when you just want to do something with each element and don't need a result back —
        // e.g. log each transaction, send a notification per user. If no other stream operations
        // are needed, call forEach directly on the list (no .stream() needed).
        // Print each name using forEach
        names.stream().forEach(System.out::println);
        names.forEach(System.out::println); // no need for stream if just iterating


        // 5. SORTED — sort elements
        // Use when the order of results matters — e.g. show transactions newest first,
        // sort users alphabetically, order amounts highest to lowest.
        // Print names sorted alphabetically
        List<String> sortedNames = names.stream().sorted().toList();
        sortedNames.forEach(System.out::println);

        // 6. COUNT — count matching elements
        // Use after filter() when you need a number — e.g. how many transactions failed today,
        // how many users are premium. Returns long, not int.
        // Count how many names start with "A" and print the result
        long count = names.stream().filter(i -> i.startsWith("A")).count();
        System.out.println(count);

        // 7. ANYMATCH / ALLMATCH / NONEMATCH — check conditions across the whole collection
        // Use when you need a yes/no answer about the whole collection — e.g. did any transaction fail?
        // are all payments confirmed? are there any duplicates? Stops as soon as the answer is known.
        // Check if any name is "Bob", if all names are longer than 2 chars, if none are empty
        boolean anyBob = names.stream().anyMatch(i -> i.equals("Bob"));
        boolean allLongerThan2 = names.stream().allMatch(i -> i.length() > 2);
        boolean noneEmpty = names.stream().noneMatch(String::isEmpty);
        System.out.println("Any Bob? " + anyBob);
        System.out.println("All longer than 2? " + allLongerThan2);
        System.out.println("None empty? " + noneEmpty);

        // 8. FINDFIRST — get the first match
        // Use when you only need one result — e.g. find the first failed transaction to retry,
        // get the first admin user. Returns Optional because there might be no match.
        // Find the first name that starts with "C" and print it
        System.out.println("findFirst: on A");
        String firstC = names.stream().filter(i -> i.startsWith("A")).findFirst().orElse("Not found");
        System.out.println(firstC);


        // 9. REDUCE — combine all elements into one value
        // Use when you need to collapse a collection into a single result — e.g. total amount
        // of all transactions, combined error message from multiple failures.
        // Join all names into a single comma-separated string
        System.out.println("reduce: ");
        String joined = names.stream().reduce((a, b) -> a + ", " + b).orElse("");
        System.out.println(joined);


        // 10. DISTINCT — remove duplicates
        // Use when your data source can return duplicates and you only want unique values —
        // e.g. a list of providers from multiple transaction records.
        // Add some duplicate names to a list and print only unique ones
        List<String> namesWithDuplicates = List.of("Alice", "Bob", "Alice", "Charlie", "Bob");
        List<String> uniqueNames = namesWithDuplicates.stream().distinct().toList();
        System.out.println(uniqueNames);


        // 11. LIMIT & SKIP — control how many elements to process
        // Use for pagination — e.g. show the first 20 results (limit), or skip the first page
        // to get page 2 (skip). Common when working with large datasets.
        // Print only the first 3 names (limit), then print names skipping the first 2 (skip)
        System.out.println("limit: ");
        names.stream().limit(3).forEach(System.out::println);
        System.out.println("skip: ");
        names.stream().skip(2).forEach(System.out::println);
        names.forEach(System.out::println);
        System.out.println("STOP");


        // 12. FLATMAP — flatten a list of lists into a single stream
        // Use when each element contains a collection and you want to work with all items together —
        // e.g. each order has a list of items, and you want all items across all orders in one stream.
        // Given a list of lists of names, flatten into one list and print all names
        List<List<String>> nestedNames = List.of(
                List.of("Alice", "Bob"),
                List.of("Charlie", "Anna"),
                List.of("David")
        );


        // 13. MAPTOINT / sum / average / min / max — numeric stream operations
        // Use when you need aggregation on numbers — e.g. total transaction amount, average order value,
        // highest single payment. mapToInt() converts to a numeric stream that unlocks these methods.
        // Get the total length of all names combined (sum), the average length, shortest and longest
        int totalLength = names.stream().mapToInt(i -> i.length()).sum();
        System.out.println(totalLength);
        int averageLength = (int) names.stream().mapToInt(i -> i.length()).average().orElse(0);
        System.out.println("Avg " + averageLength);
        int minLength = names.stream().mapToInt(String::length).min().orElse(0);// method reference instead of lambda
        System.out.println(minLength);
        int maxLength = names.stream().mapToInt(String::length).max().orElse(0);// method reference instead of lambda
        System.out.println(maxLength);

        // 14. COLLECTORS.JOINING — cleaner way to join strings than reduce
        // Use when building a formatted string from a collection — e.g. a comma-separated list
        // of provider names for a log message or API response.
        // Join all names into a comma-separated string using Collectors.joining()
        System.out.println("collectors joining: ");
        String joined2 = names.stream().collect(Collectors.joining(", "));
        System.out.println(joined2);


        // 15. COLLECTORS.GROUPINGBY — group elements into a Map by some property
        // Use when you need to categorise data — e.g. group transactions by status, group
        // payments by provider, group users by country. Returns Map<Key, List<Element>>.
        // Group names by their first letter and print the result
            var grouped = names.stream().collect(Collectors.groupingBy(i -> i.charAt(0)));
            System.out.println(grouped);


        // 16. COLLECTORS.TOMAP — collect into a Map
        // Use when you need a lookup table from a collection — e.g. Map<providerId, providerName>
        // from a list of providers, or Map<userId, email> for quick lookups later.
        // Collect names into a Map where the key is the name and the value is its length
        var nameLengthMap = names.stream().collect(Collectors.toMap(i -> i, String::length));
        System.out.println(nameLengthMap);

    }
}