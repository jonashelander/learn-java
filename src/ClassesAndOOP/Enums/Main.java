package ClassesAndOOP.Enums;

import java.lang.reflect.Array;

public class Main {

    // Domain: a payment platform tracking transactions and payment methods.
    // All exercises build on each other — read them in order.

    public static void main(String[] args) {


        // 1. BASIC ENUM — a fixed set of named values
        // Use when a field can only be one of a known set of values —
        // e.g. a transaction can only be in one of a fixed number of states.
        // Using a String for this is dangerous — a typo like "COMPELTED" compiles fine but breaks at runtime.
        // An enum makes invalid values impossible — the compiler refuses to build if you use a value that doesn't exist.
        //
        // Create an enum TransactionStatus with these values:
        //   PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED
        //   Note: enum values are written in UPPER_SNAKE_CASE by convention.
        //         Declare the enum in its own file: TransactionStatus.java in this package.
        //
        // In main:
        //   - Declare a variable of type TransactionStatus and assign it TransactionStatus.COMPLETED
        //   - Print the variable
        //   - Print its name as a String using .name()
        //   - Print its position in the declaration order using .ordinal()
        System.out.println("Ex 1");
        TransactionStatus transactionStatus = TransactionStatus.COMPLETED;
        System.out.println(transactionStatus);
        System.out.println(transactionStatus.name());
        System.out.println(transactionStatus.ordinal());


        // 2. ENUM WITH FIELDS — attaching extra data to each value
        // Use when each value needs to carry additional information —
        // e.g. each payment method has a human-readable label that gets shown in the UI or API response.
        //
        // Create an enum PaymentMethod in its own file with these values and display names:
        //   CARD("Credit or debit card")
        //   BANK_TRANSFER("Direct bank transfer")
        //   WALLET("Digital wallet")
        //
        // The enum needs:
        //   - a String field: displayName
        //   - a constructor that takes a String displayName and assigns it
        //     Note: you never call this constructor yourself — Java calls it automatically
        //           for each value when the enum is loaded. The value + its arguments in
        //           parentheses IS the constructor call: CARD("Credit or debit card")
        //
        // In main:
        //   - Declare a PaymentMethod variable and assign it PaymentMethod.BANK_TRANSFER
        //   - Print the variable
        //   - Print its displayName field directly
        System.out.println("Ex 2");
        PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;
        System.out.println(paymentMethod);
        System.out.println(paymentMethod.displayName);


        // 3. LOOPING OVER ALL VALUES — iterating every value in an enum
        // Use when you need to process or display all possible values —
        // e.g. listing all available payment methods in an API response,
        // or checking which statuses are considered "final" (no further updates expected).
        //
        // Use TransactionStatus.values() — this returns an array of all values in declaration order.
        // Loop over it with a for-each loop and print each value's name.
        //   Note: a for-each loop over an array works the same as over a List:
        //         for (TransactionStatus status : TransactionStatus.values()) { ... }
        System.out.println("Ex 3");
        System.out.println(TransactionStatus.valueOf("COMPLETED"));
        //TransactionStatus[] transactionStatus1 = TransactionStatus.values();

        for (TransactionStatus status : TransactionStatus.values())
            System.out.println(status.name());


        // 4. SWITCH ON AN ENUM — taking different action based on the value
        // Use when different values require different behaviour —
        // e.g. a completed transaction gets logged differently from a failed one.
        //
        // Write a switch statement on a TransactionStatus variable.
        // Print a different message for each case:
        //   COMPLETED  → "Payment confirmed — notify the merchant"
        //   FAILED     → "Payment failed — queue for retry"
        //   REFUNDED   → "Payment refunded — update the ledger"
        //   default    → "Payment in progress"
        //   Note: each case needs a break; at the end, otherwise execution falls through to the next case.
        System.out.println("Ex 4");
        TransactionStatus status = TransactionStatus.PENDING;

        switch (status) {
            case COMPLETED:
                System.out.println("Payment confirmed - notify the merchant");
                break;
            case FAILED:
                System.out.println("Payment failed - queue for retry");
                break;
            case REFUNDED:
                System.out.println("Payment refunded - update the ledger");
                break;
            default:
                System.out.println("Payment in progress");
        }
    }
}