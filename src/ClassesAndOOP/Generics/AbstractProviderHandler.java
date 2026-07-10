package ClassesAndOOP.Generics;

// Step 3 — Create an abstract class AbstractProviderHandler<C extends ProviderConfig>.
//           The <C extends ProviderConfig> means: C is a type placeholder, but it must always
//           be a ProviderConfig or a subclass of it. This lets the class hold any provider's
//           config while still being able to use config.apiKey (which all ProviderConfigs have).
//           Add:
//   - a field: C config
//   - a constructor that takes C config and assigns it
//   - an abstract method: String callApi(double amount)
//   - a concrete method: String charge(double amount) — returns "Charging via " + callApi(amount)
abstract public class AbstractProviderHandler<C extends ProviderConfig> {
    C config;

    public AbstractProviderHandler(C config) {
        this.config = config;
    }

    abstract String callApi(double amount);

    String charge(double amount) {
        return "Charging via " + callApi(amount);
    }
}
