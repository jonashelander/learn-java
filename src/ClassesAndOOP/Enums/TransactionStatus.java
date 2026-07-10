package ClassesAndOOP.Enums;

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

public enum TransactionStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED
}
