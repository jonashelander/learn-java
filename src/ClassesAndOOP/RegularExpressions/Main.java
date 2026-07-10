package ClassesAndOOP.RegularExpressions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    // Domain: a payment platform receiving data from providers and users that needs validation and parsing.
    // All exercises build on each other — read the pattern syntax table in the notes before starting.

    public static void main(String[] args) {


        // 1. BASIC MATCH — check if a string matches a pattern exactly
        // Use when validating input format before processing it —
        // e.g. a card expiry must be exactly two digits, a slash, two digits.
        // If the format is wrong you reject it immediately rather than letting it cause errors later.
        //
        // Use String.matches(pattern) — returns true if the ENTIRE string matches the pattern.
        //   Note: in Java, every \ in a regex must be written as \\ in a String.
        //         So the regex \d (any digit) is written "\\d" in Java code.
        //
        // Check and print whether each of these is a valid card expiry in MM/YY format:
        //   - "09/28"   → valid (two digits, slash, two digits)
        //   - "9/28"    → invalid (month must be two digits)
        //   - "09/2028" → invalid (year must be two digits)
        // Pattern to use: "\\d{2}/\\d{2}"
        System.out.println("Ex 1");
        System.out.println("09/28".matches("\\d{2}/\\d{2}"));
        System.out.println("9/28".matches("\\d{2}/\\d{2}"));
        System.out.println("09/2028".matches("\\d{2}/\\d{2}"));


        // 2. FIND — locate a pattern inside a larger string
        // Use when you need to find something inside a string rather than validate the whole thing —
        // e.g. a provider sends a message like "Transaction ref: TXN-00291 processed" and you need
        // to extract the reference number from it.
        //
        // Use Pattern.compile(pattern) and pattern.matcher(input) to create a Matcher.
        // Then call matcher.find() — returns true if the pattern appears anywhere in the string.
        //   Note: unlike matches(), find() does not require the entire string to match.
        //
        // Given this string: "Payment ref: TXN-00482 was approved"
        // Find and print whether it contains a reference in the format TXN- followed by digits.
        // Pattern to use: "TXN-\\d+"
        System.out.println("Ex 2");
        Pattern pattern = Pattern.compile("TXN-\\d+");
        Matcher matcher = pattern.matcher("Payment ref: TXN-00482 was approved");
        boolean match = matcher.find();
        System.out.println(match);


        // 3. EXTRACT — pull out the part that matched
        // Use when you need the actual matched value, not just whether it matched —
        // e.g. after finding a transaction reference in a provider message, you need the reference
        // itself to look it up in the database.
        //
        // After calling matcher.find(), use matcher.group(0) to get the full matched text.
        //
        // Reuse the string from exercise 2.
        // If the pattern matches, print the matched reference (e.g. "TXN-00482").
        if(match){
            String a = matcher.group(0);
            System.out.println(a);
        }

        // 4. GROUPS — extract specific parts of a match
        // Use when the match contains multiple distinct parts you need separately —
        // e.g. a card expiry "09/28" contains a month and a year, and you need each one.
        //
        // Wrap parts of the pattern in () to capture them as groups.
        // After matcher.find() or matcher.matches(), use:
        //   matcher.group(1) — first captured group
        //   matcher.group(2) — second captured group
        //
        // Given this string: "Card expires 09/28"
        // Extract and print the month and year separately.
        // Pattern to use: "expires (\\d{2})/(\\d{2})"
        System.out.println("Ex 4");
        Pattern p = Pattern.compile("expires (\\d{2})/(\\d{2})");
        Matcher m = p.matcher("Card expires 09/28");
        boolean f = m.find();
        System.out.println(m.group(1));
        System.out.println(m.group(2));


        // 5. WEBHOOK EVENT ROUTING — match patterns against event names
        // Use when you need to route or filter events based on their name pattern —
        // e.g. a provider sends events like "payment.completed", "payment.failed", "refund.created".
        // Your platform needs to send all "payment.*" events to the payment handler
        // and all "refund.*" events to the refund handler.
        //
        // Note: in regex, a dot (.) means "any character". To match a literal dot,
        //       you must escape it: \. which in Java is written as "\\."
        //
        // Given these event names:
        //   "payment.completed", "payment.failed", "refund.created", "dispute.opened"
        //
        // Loop over them and print:
        //   "→ payment handler" if the event starts with "payment."
        //   "→ refund handler"  if the event starts with "refund."
        //   "→ unhandled"       otherwise
        // Use String.matches() with appropriate patterns for each check.
        System.out.println("Ex 5");
        List<String> events = List.of("payment.completed", "payment.failed", "refund.created", "dispute.opened");

        for (String event : events) {
            if(event.matches("payment\\..*")){
                System.out.println("-> payment handler");
            } else if (event.matches("refund\\..*")) {
                System.out.println("<- refund handler");
            } else System.out.println("-> unhandled");
        }

    }
}