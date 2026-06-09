package ClassesAndOOP.VariablesAndTypes;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        // 1. PRIMITIVES
        // Use for simple values where you don't need null and aren't using collections —
        // counters, flags, loop indexes. Faster and lighter than wrapper objects.
        // Declare one of each primitive type: byte, short, int, long, float, double, boolean, char
        // Use literal suffixes where needed (L for long, F for float)
        byte b = 100;
        short s = 10000;
        int i = 100000;
        long l = 100000L;
        float f = 3.14F;
        double d = 3.141592653589793;
        boolean bo = true;
        char c = 'A';

        // 2. WRAPPER OBJECTS (int vs Integer, double vs Double etc.)
        // Use when you need null (e.g. optional request params in Spring Boot — a missing param
        // comes in as null, which int can't hold), or when using collections (List<Integer> not List<int>).
        // Declare the same values but using their object wrapper types
        // Try: Integer, Long, Double, Boolean, Character
        Integer ig = 100000;
        Long lg = 100000L;
        Double db = 3.141592653589793;
        Boolean bl = true;
        Character ch = 'A';



        // 2b. BIGDECIMAL — use this for money, never double or float
        // double and float have floating point precision issues: 0.1 + 0.2 = 0.30000000000000004
        // BigDecimal stores numbers exactly — always pass the value as a String to avoid precision loss
        // Example: BigDecimal price = new BigDecimal("19.99");
        BigDecimal bd = new BigDecimal("19.99");
        System.out.println(bd); // prints 19.99 exactly

        // The precision issue shows up during arithmetic
        System.out.println(0.1 + 0.2); // prints 0.30000000000000004
        System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2"))); // prints 0.3 exactly


        // 3. AUTOBOXING & UNBOXING
        // Java converts between primitive and wrapper automatically — you don't have to think about it
        // most of the time, but knowing it happens helps when you see unexpected NullPointerExceptions
        // (e.g. unboxing a null Integer to int will crash).
        // Assign a primitive to a wrapper type and vice versa — Java does this automatically
        // Example: Integer x = 5; (autoboxing) and int y = x; (unboxing)


        // 4. LITERAL SUFFIXES
        // Required when the default type isn't what you need — L for large IDs or timestamps
        // that exceed int range, F when a method specifically requires float.
        // Show what happens without the suffix — e.g. assigning a plain 100 to a long
        // Show hex (0xFF) and binary (0b1010) literals


        // 5. TYPE CASTING
        // Widening happens automatically (safe — no data loss). Narrowing requires an explicit cast
        // because you might lose data — the compiler forces you to acknowledge that risk.
        // Widening: assign an int to a long — happens automatically
        // Narrowing: assign a double to an int — requires explicit cast (int)


        // 6. VAR
        // Use when the type is obvious from the right-hand side and writing it out adds noise —
        // e.g. Map<String, List<Payment>> is verbose; var makes it cleaner. Avoid when the type
        // isn't obvious from the assignment — a reader shouldn't have to guess.
        // Declare a few variables using var and let Java infer the type
        // Note: var only works for local variables, not fields


        // 7. STACK VS HEAP (conceptual — no code needed, just a comment explaining it)
        // Helps you understand why primitives are faster, why null only exists for objects,
        // and why passing an object to a method can affect the original while primitives cannot.
        // Primitives live on the stack. Objects live on the heap, the variable holds a reference.

    }
}
