package ClassesAndOOP.Enums;

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
public enum PaymentMethod {
    CARD("Credit or debit card"),
    BANK_TRANSFER("Direct bank transfer"),
    WALLET("Digital wallet");

    String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}
