package ClassesAndOOP.Exceptions;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(double amount) {
        super("Invalid amount: " + amount);
    }
}
