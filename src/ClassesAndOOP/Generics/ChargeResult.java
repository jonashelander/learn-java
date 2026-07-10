package ClassesAndOOP.Generics;

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

public class ChargeResult {
    String transactionId;

    public ChargeResult(String transactionId) {
        this.transactionId = transactionId;
    }
}
